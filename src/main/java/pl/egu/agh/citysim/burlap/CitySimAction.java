package pl.egu.agh.citysim.burlap;

import burlap.mdp.core.action.Action;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Value;

@Value
public class CitySimAction implements Action {
    Pair<String, String> roadDefinition;
    LightDurationDelta lightDurationDelta;

    @Override
    public String actionName() {
        return toString();
    }

    @Override
    public Action copy() {
        return new CitySimAction(new Pair<>(roadDefinition.getKey(), roadDefinition.getValue()), lightDurationDelta);
    }

    public enum LightDurationDelta {
        SHORTEN(-100.),
        PROLONG(100.);

        @Getter
        private final double delta;

        LightDurationDelta(final double delta) {
            this.delta = delta;
        }
    }
}
