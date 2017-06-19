package pl.egu.agh.citysim.burlap;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import lombok.Value;
import pl.egu.agh.citysim.SimulationRunner;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Value
public class CitySimRewardFunction implements RewardFunction {
    Map<CitySimState, Double> cache = newHashMap();
    SimulationRunner simulationRunner;

    @Override
    public double reward(final State state, final Action action, final State state1) {
        final CitySimState citySimState = (CitySimState) state1;
        cache.computeIfAbsent(citySimState, simulationRunner::run);
        return -cache.get(citySimState);
    }
}
