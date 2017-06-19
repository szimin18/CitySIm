package pl.egu.agh.citysim;

import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.action.UniversalActionType;
import com.google.common.collect.ImmutableSet;
import javafx.util.Pair;
import pl.egu.agh.citysim.burlap.CitySimAction;
import pl.egu.agh.citysim.burlap.CitySimState;
import pl.egu.agh.citysim.model.*;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.stream.Collectors.toMap;
import static pl.egu.agh.citysim.util.Consumers.empty;

public class SimulationRunner {

    private final RoadsMap.Builder builder;
    private final SimulationParameters simulationParameters;

    public SimulationRunner() {
        builder = RoadsMap.builder();
        simulationParameters = createMap1(builder);
    }

    private static SimulationParameters createMap1(final RoadsMap.Builder builder) {
        builder.addCrossing("A", new Coordinates(200, 200));
        builder.addCrossing("B", new Coordinates(600, 200));
        builder.addCrossing("G", new Coordinates(800, 200));
        builder.addCrossing("C", new Coordinates(600, 600));
        builder.addCrossing("D", new Coordinates(200, 600));
        builder.addCrossing("E", new Coordinates(1000, 600));
        builder.addCrossing("F", new Coordinates(1080, 600));
        builder.addCrossing("X", new Coordinates(200, 0));
        builder.addCrossing("Y", new Coordinates(1000, 800));
        builder.addCrossing("Z", new Coordinates(0, 600));
        builder.addCrossing("W", new Coordinates(400, 600));
        builder.addCrossing("U", new Coordinates(600, 0));
        builder.addCrossing("T", new Coordinates(200, 800));

        builder.addRoad("A", "B");
        builder.addRoad("B", "A");
        builder.addRoad("B", "C");
        builder.addRoad("C", "B");
        builder.addRoad("D", "A");
        builder.addRoad("A", "D");
        builder.addRoad("E", "C");
        builder.addRoad("G", "B");
        builder.addRoad("F", "E");
        builder.addRoad("X", "A");
        builder.addRoad("Y", "E");
        builder.addRoad("Z", "D");
        builder.addRoad("C", "W");
        builder.addRoad("B", "U");
        builder.addRoad("D", "T");

        return new SimulationParameters(ImmutableSet.of("X", "Y", "Z"), ImmutableSet.of("W", "U", "T"), 500);
    }

    public List<ActionType> createAllActions() {
        return builder.getRoads().stream().flatMap(road -> {
            if (road.getEnd().getInRoads().size() > 1) {
                final Pair<String, String> roadName = new Pair<>(road.getStart().getName(), road.getEnd().getName());
                return Stream.<ActionType>of(
                        new UniversalActionType(new CitySimAction(roadName, CitySimAction.LightDurationDelta.PROLONG)),
                        new UniversalActionType(new CitySimAction(roadName, CitySimAction.LightDurationDelta.SHORTEN)));
            } else {
                return Stream.empty();
            }
        }).collect(toImmutableList());
    }

    public CitySimState createInitialState() {
        final Set<Road> roads = builder.getRoads();
        final Double initialLightsTime = 3000.;
        return new CitySimState(roads.stream().collect(toMap(
                road -> new Pair<>(road.getStart().getName(), road.getEnd().getName()),
                road -> initialLightsTime)));
    }

    public RoadsMap createInitialRoadMap() {
        return builder.build(createInitialState());
    }

    public double run(final CitySimState state) {
        return run(state, empty());
    }

    public double run(final Consumer<CarsState> carsStateConsumer) {
        return run(createInitialState(), carsStateConsumer);
    }

    private double run(final CitySimState state, final Consumer<CarsState> carsUpdateConsumer) {
        final RoadsMap roadsMap = builder.build(state);
        final Simulation simulation = new Simulation(roadsMap, 40, carsUpdateConsumer,
                simulationParameters.getStarts(), simulationParameters.getEnds(), simulationParameters.getRequiredNumberOfCars(), 10000);
        simulation.run(true);
        return simulation.averageCarTime();
    }

}
