package pl.egu.agh.citysim;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.action.UniversalActionType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import javafx.util.Pair;
import pl.egu.agh.citysim.burlap.CitySimAction;
import pl.egu.agh.citysim.burlap.CitySimState;
import pl.egu.agh.citysim.model.CarsState;
import pl.egu.agh.citysim.model.Coordinates;
import pl.egu.agh.citysim.model.RoadsMap;
import pl.egu.agh.citysim.model.SimulationParameters;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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

        return new SimulationParameters(ImmutableSet.of("X", "Y", "Z"), ImmutableSet.of("W", "U", "T"), 100);
    }

    public List<ActionType> createAllActions() {
        return ImmutableList.of(
                new UniversalActionType(new CitySimAction(new Pair<>("A", "B"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("A", "B"), CitySimAction.LightDurationDelta.SHORTEN)),
                new UniversalActionType(new CitySimAction(new Pair<>("B", "A"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("B", "A"), CitySimAction.LightDurationDelta.SHORTEN)),
                new UniversalActionType(new CitySimAction(new Pair<>("B", "C"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("B", "C"), CitySimAction.LightDurationDelta.SHORTEN)),
                new UniversalActionType(new CitySimAction(new Pair<>("C", "B"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("C", "B"), CitySimAction.LightDurationDelta.SHORTEN)),
                new UniversalActionType(new CitySimAction(new Pair<>("D", "A"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("D", "A"), CitySimAction.LightDurationDelta.SHORTEN)),
                new UniversalActionType(new CitySimAction(new Pair<>("A", "D"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("A", "D"), CitySimAction.LightDurationDelta.SHORTEN)),
                new UniversalActionType(new CitySimAction(new Pair<>("E", "C"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("E", "C"), CitySimAction.LightDurationDelta.SHORTEN)),
                new UniversalActionType(new CitySimAction(new Pair<>("G", "B"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("G", "B"), CitySimAction.LightDurationDelta.SHORTEN)),
                new UniversalActionType(new CitySimAction(new Pair<>("F", "E"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("F", "E"), CitySimAction.LightDurationDelta.SHORTEN)),
                new UniversalActionType(new CitySimAction(new Pair<>("X", "A"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("X", "A"), CitySimAction.LightDurationDelta.SHORTEN)),
                new UniversalActionType(new CitySimAction(new Pair<>("A", "E"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("A", "E"), CitySimAction.LightDurationDelta.SHORTEN)),
                new UniversalActionType(new CitySimAction(new Pair<>("Z", "D"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("Z", "D"), CitySimAction.LightDurationDelta.SHORTEN)),
                new UniversalActionType(new CitySimAction(new Pair<>("C", "W"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("C", "W"), CitySimAction.LightDurationDelta.SHORTEN)),
                new UniversalActionType(new CitySimAction(new Pair<>("B", "U"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("B", "U"), CitySimAction.LightDurationDelta.SHORTEN)),
                new UniversalActionType(new CitySimAction(new Pair<>("D", "T"), CitySimAction.LightDurationDelta.PROLONG)),
                new UniversalActionType(new CitySimAction(new Pair<>("D", "T"), CitySimAction.LightDurationDelta.SHORTEN))
        );
    }

    public CitySimState createInitialState() {
        Map<Pair<String, String>, Double> initialState = Maps.newHashMap();
        Double initialLightsTime = 3000.;
        initialState.put(new Pair<>("A", "B"), initialLightsTime);
        initialState.put(new Pair<>("B", "A"), initialLightsTime);
        initialState.put(new Pair<>("B", "C"), initialLightsTime);
        initialState.put(new Pair<>("C", "B"), initialLightsTime);
        initialState.put(new Pair<>("D", "A"), initialLightsTime);
        initialState.put(new Pair<>("A", "D"), initialLightsTime);
        initialState.put(new Pair<>("E", "C"), initialLightsTime);
        initialState.put(new Pair<>("G", "B"), initialLightsTime);
        initialState.put(new Pair<>("F", "E"), initialLightsTime);
        initialState.put(new Pair<>("X", "A"), initialLightsTime);
        initialState.put(new Pair<>("A", "E"), initialLightsTime);
        initialState.put(new Pair<>("Z", "D"), initialLightsTime);
        initialState.put(new Pair<>("C", "W"), initialLightsTime);
        initialState.put(new Pair<>("B", "U"), initialLightsTime);
        initialState.put(new Pair<>("D", "T"), initialLightsTime);
        return new CitySimState(initialState);
    }

    public RoadsMap createInitalRoadMap() {
        return builder.build(createInitialState());
    }

    public double run(CitySimState state) {
        return run(state, c -> {
        });
    }

    public double run(Consumer<CarsState> carsStateConsumer) {
        return run(createInitialState(), carsStateConsumer);
    }

    private double run(CitySimState state, Consumer<CarsState> carsUpdateConsumer) {
        final RoadsMap roadsMap = builder.build(state);
        final Simulation simulation = new Simulation(roadsMap, 40, carsUpdateConsumer,
                simulationParameters.getStarts(), simulationParameters.getEnds(), simulationParameters.getRequiredNumberOfCars(), 100);
        simulation.run();
        return simulation.averageCarTime();
    }

}
