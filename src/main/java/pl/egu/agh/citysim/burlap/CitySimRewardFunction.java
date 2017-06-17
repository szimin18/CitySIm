package pl.egu.agh.citysim.burlap;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import pl.egu.agh.citysim.SimulationRunner;

public class CitySimRewardFunction implements RewardFunction {
    private final SimulationRunner simulationRunner;

    public CitySimRewardFunction(SimulationRunner simulationRunner) {
        this.simulationRunner = simulationRunner;
    }

    @Override
    public double reward(State state, Action action, State state1) {
        return simulationRunner.run((CitySimState)state1);
    }
}
