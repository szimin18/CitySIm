package pl.egu.agh.citysim.burlap;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import pl.egu.agh.citysim.SimulationRunner;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class CitySimRewardFunction implements RewardFunction {
    private final SimulationRunner simulationRunner;

    private final Map<State, Double> cache = newHashMap();

    public CitySimRewardFunction(final SimulationRunner simulationRunner) {
        this.simulationRunner = simulationRunner;
    }

    @Override
    public double reward(final State state, final Action action, final State state1) {
        cache.computeIfAbsent(state1, s -> simulationRunner.run((CitySimState) s));
        return cache.get(state1);
    }
}
