package pl.egu.agh.citysim.burlap;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

public class CitySimRewardFunction implements RewardFunction {
    @Override
    public double reward(State state, Action action, State state1) {
        return 0;
    }
}