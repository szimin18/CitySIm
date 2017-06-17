package pl.egu.agh.citysim.burlap;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.SampleStateModel;

public class CitySimWorldStateModel implements SampleStateModel {
//    following will be needed for FullStateModel
//    @Override
//    public List<StateTransitionProb> stateTransitions(State state, Action action) {
//        return null;
//    }

    @Override
    public State sample(State state, Action action) {
        return null;
    }
}
