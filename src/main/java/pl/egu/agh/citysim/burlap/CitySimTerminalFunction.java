package pl.egu.agh.citysim.burlap;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public class CitySimTerminalFunction implements TerminalFunction{
    @Override
    public boolean isTerminal(State state) {
        return ((CitySimState)state).isTerminal();
    }
}
