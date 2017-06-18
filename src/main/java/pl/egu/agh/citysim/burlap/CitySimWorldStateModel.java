package pl.egu.agh.citysim.burlap;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.SampleStateModel;

// consider FullStateModel with probability distribution for specific action, for now we're using simple solution
public class CitySimWorldStateModel implements SampleStateModel {

    @Override
    public State sample(final State state, final Action action) {
        final CitySimAction citySimAction = (CitySimAction) action;
        final CitySimState citySimState = (CitySimState) state;
        return citySimState.set(citySimAction.getRoadDefinition(), citySimState.get(citySimAction.getRoadDefinition()) + citySimAction.getLightDurationDelta().getDelta());
    }
}
