package pl.egu.agh.citysim;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import pl.egu.agh.citysim.model.Car;
import pl.egu.agh.citysim.model.CarsState;
import pl.egu.agh.citysim.model.Crossing;
import pl.egu.agh.citysim.model.RoadsMap;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.function.Predicate.isEqual;
import static javafx.scene.paint.Color.*;

@AllArgsConstructor
public class Simulation {
    private static final Random RANDOM = new Random();
    private static final ImmutableList<Color> COLORS_LIST = ImmutableList.of(RED, GREEN, BLUE, WHITE);

    private final RoadsMap roadsMap;
    private final int intervalMiliseconds;
    private final Consumer<CarsState> carsUpdateConsumer;
    private final ImmutableSet<String> starts;
    private final ImmutableSet<String> ends;
    private final int requiredNumberOfCars;

    public void run() {
        final AtomicReference<CarsState> carsState = new AtomicReference<>(new CarsState(ImmutableMap.of(), roadsMap));
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> carsUpdateConsumer.accept(carsState.updateAndGet(this::calculateFrame)),
                        intervalMiliseconds, intervalMiliseconds, MILLISECONDS);
    }

    public CarsState calculateFrame(final CarsState carsState) {
        final ImmutableSet<Car> cars = carsState.getCars();
        final ImmutableSet<Car> newCars = spawnCars(requiredNumberOfCars - cars.size(), cars);

        return null;
    }

    private ImmutableSet<Car> spawnCars(final int count, final ImmutableSet<Car> allCars) {
        final Set<String> remainingStarts = newHashSet(starts);
        final Set<Car> newCars = newHashSet();

        IntStream.range(0, Math.min(count, starts.size()))
                .forEach(i -> {
                    final Crossing start = randomCrossing(remainingStarts);
                    remainingStarts.remove(start.getName());
                    spawnCar(start, allCars).ifPresent(newCars::add);
                });

        return ImmutableSet.copyOf(newCars);
    }

    private Optional<Car> spawnCar(final Crossing crossing, final ImmutableSet<Car> allCars) {
        if (allCars.stream().map(Car::getLocation).anyMatch(isEqual(crossing.getCoordinates()))) {
            return Optional.empty();
        }

        final Crossing end = randomCrossing(ends);
        final List<Crossing> path = bfsPath(crossing, end);

        return Optional.of(new Car(randomColor(), path));
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
