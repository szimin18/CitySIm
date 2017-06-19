package pl.egu.agh.citysim;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import pl.egu.agh.citysim.model.*;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.String.format;
import static java.util.function.Predicate.isEqual;
import static java.util.stream.Stream.empty;
import static java.util.stream.Stream.of;
import static javafx.scene.paint.Color.*;
import static pl.egu.agh.citysim.MapViewer.CAR_SIZE;

@AllArgsConstructor
public class Simulation {
    private static final Random RANDOM = new Random();
    private static final ImmutableList<Color> COLORS_LIST = ImmutableList.of(RED, GREEN, BLUE, WHITE);
    private static int counter = 0;

    private final RoadsMap roadsMap;
    private final long intervalMiliseconds;
    private final Consumer<CarsState> carsUpdateConsumer;
    private final ImmutableSet<String> starts;
    private final ImmutableSet<String> ends;
    private final int requiredNumberOfCars;
    private final int simulationSteps;
    private final List<Long> carTimes = newArrayList();
    private final Lock carTimesLock = new ReentrantLock();

    public double averageCarTime() {
        return carTimes.stream().mapToDouble(t -> t).average().orElse(0);
    }

    public void run() {
        if (intervalMiliseconds != 0) {
            final AnimationTimer timer = new AnimationTimer() {
                private CarsState carsState = new CarsState(ImmutableSet.of(), roadsMap);
                private final long intervalNanoseconds = intervalMiliseconds * 1000000;
                private long lastNanoseconds = 0;
                private long nanosecondsPassed = 0;
                private long stepsPassed = 0;

                @Override
                public void handle(final long now) {
                    if (lastNanoseconds != 0) {
                        nanosecondsPassed += now - lastNanoseconds;
                        if (nanosecondsPassed > intervalNanoseconds) {
                            nanosecondsPassed -= intervalNanoseconds;
                            carsState = calculateFrame(carsState, stepsPassed);
                            carsUpdateConsumer.accept(carsState);
                            stepsPassed++;
                            System.out.println("Frame " + stepsPassed + " done");
                            if (stepsPassed >= simulationSteps) {
                                stop();
                            }
                        }
                    }
                    lastNanoseconds = now;
                }
            };
            timer.start();
        } else {
            CarsState carsState = new CarsState(ImmutableSet.of(), roadsMap);
            for (int i = 0; i < simulationSteps; i++) {
                carsState = calculateFrame(carsState, i);
            }
            counter++;
            if (counter % 10 == 0) {
                System.out.println("Finished out of timer simulation: " + counter + " with result " + averageCarTime());
            }
        }
    }

    private CarsState calculateFrame(final CarsState carsState, final long step) {
        try {
            carsState.getRoadsMap().getCrossings().forEach(crossing -> crossing.passed(40));

            final ImmutableSet<Car> cars = carsState.getCars();
            final ImmutableSet<Car> newCars = spawnCars(requiredNumberOfCars - cars.size(), cars, step);
            final ImmutableSet<CarShadow> previousCarsShadows = cars.stream().map(Car::shadow).collect(toImmutableSet());

            final ImmutableSet<Car> movedCars = cars.parallelStream().flatMap(car -> calculateFrameForCar(car, previousCarsShadows, step)).collect(toImmutableSet());
            return new CarsState(ImmutableSet.<Car>builder().addAll(newCars).addAll(movedCars).build(), roadsMap);
        } catch (final Throwable e) {
            e.printStackTrace();
            return carsState;
        }
    }

    private Stream<Car> calculateFrameForCar(final Car car, final ImmutableSet<CarShadow> previousCarsShadows, final long step) {
        final double currentCarDistancePassedOnRoad = car.getDistancePassedOnRoad();
        final Road currentCarRoad = car.getRoad();
        final double distancePassedPerFrame = 2 * CAR_SIZE;
        final double maxDistanceForCurrentCar = previousCarsShadows.stream()
                .filter(carShadow -> carShadow.getRoad().equals(currentCarRoad))
                .filter(carShadow -> carShadow.getDistancePassedOnRoad() > currentCarDistancePassedOnRoad)
                .mapToDouble(carShadow -> {
                    final double distancePassedOnRoad = carShadow.getDistancePassedOnRoad();
                    final boolean isMoved = carShadow.isMoved();
                    final double maxDistance = max(0, distancePassedOnRoad - currentCarDistancePassedOnRoad - CAR_SIZE * (isMoved ? 2 : 7 / 5));
                    return min(maxDistance, distancePassedPerFrame);
                }).min().orElse(distancePassedPerFrame);

        if (maxDistanceForCurrentCar == 0) {
            car.stay();
            return of(car);
        } else if (maxDistanceForCurrentCar > currentCarRoad.getLength() - currentCarDistancePassedOnRoad) { // reaches end of road
            if (currentCarRoad.getEnd().isGreen(currentCarRoad)) { // has green light
                car.markVisited(currentCarRoad.getEnd());
                if (!car.nextCrossing().isPresent()) { // end of road
                    carTimesLock.lock();
                    try {
                        carTimes.add(step - car.getCreationTime());
                    } finally {
                        carTimesLock.unlock();
                    }
                    return empty();
                } else {
                    car.moveTo(currentCarRoad.getEnd().getOutRoads().get(car.nextCrossing().get()), maxDistanceForCurrentCar - (currentCarRoad.getLength() - currentCarDistancePassedOnRoad));
                    return of(car);
                    // TODO
                }
            } else {
                if (currentCarRoad.getLength() > currentCarDistancePassedOnRoad) {
                    car.moveTo(currentCarRoad, currentCarRoad.getLength() - currentCarDistancePassedOnRoad);
                    return of(car);
                } else {
                    car.stay();
                    return of(car);
                }
            }
        } else {
            car.moveTo(currentCarRoad, maxDistanceForCurrentCar);
            return of(car);
        }
    }

    private ImmutableSet<Car> spawnCars(final int count, final ImmutableSet<Car> allCars, final long step) {
        final Set<String> remainingStarts = newHashSet(starts);
        final Set<Car> newCars = newHashSet();

        IntStream.range(0, min(count, starts.size()))
                .forEach(i -> {
                    final Crossing start = randomCrossing(remainingStarts);
                    remainingStarts.remove(start.getName());
                    spawnCar(start, allCars, step).ifPresent(newCars::add);
                });

        return ImmutableSet.copyOf(newCars);
    }

    private Optional<Car> spawnCar(final Crossing crossing, final ImmutableSet<Car> allCars, final long step) {
        if (allCars.stream().map(Car::getLocation).anyMatch(isEqual(crossing.getCoordinates()))) {
            return Optional.empty();
        }

        final Crossing end = randomCrossing(ends);

        final List<Crossing> nextCrossings = bfsPath(crossing, end);
        final Car car = new Car(randomColor(), nextCrossings, step);
        car.moveTo(crossing.getOutRoads().get(nextCrossings.get(0)), 0);

        return Optional.of(car);
    }

    private List<Crossing> bfsPath(final Crossing crossing, final Crossing end) {
        final Map<Crossing, List<Crossing>> shortestPaths = newHashMap();
        final Queue<Crossing> crossingsQueue = newLinkedList();
        shortestPaths.put(crossing, newArrayList());
        crossingsQueue.add(crossing);

        while (!crossingsQueue.isEmpty()) {
            final Crossing currentCrossing = crossingsQueue.poll();
            final List<Crossing> pathToCurrent = shortestPaths.get(currentCrossing);
            for (final Crossing c : currentCrossing.getOutRoads().keySet()) {
                if (!shortestPaths.containsKey(c)) {
                    final List<Crossing> path = newArrayList(pathToCurrent);
                    path.add(c);
                    if (c.equals(end)) {
                        return path;
                    }
                    shortestPaths.put(c, path);
                    crossingsQueue.offer(c);
                }
            }
        }

        throw new IllegalArgumentException(format("Cannot find road from %s to %s", crossing.getName(), end.getName()));
    }

    private Crossing randomCrossing(final Set<String> crossingNames) {
        final ImmutableList<Crossing> crossings = roadsMap.getCrossings()
                .stream()
                .filter(crossing -> crossingNames.contains(crossing.getName()))
                .collect(toImmutableList());

        return randomFromList(crossings);
    }

    private Color randomColor() {
        return randomFromList(COLORS_LIST);
    }

    private <T> T randomFromList(final ImmutableList<T> items) {
        return items.get(RANDOM.nextInt(items.size()));
    }
}
