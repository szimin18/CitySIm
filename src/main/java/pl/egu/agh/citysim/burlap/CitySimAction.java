package pl.egu.agh.citysim.burlap;

import burlap.mdp.core.action.Action;
import javafx.util.Pair;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class CitySimAction implements Action {

    private final Pair<String, String> roadDefinition;
    private final LightDurationDelta lightDurationDelta;

    public CitySimAction(Pair<String, String> roadDefinition, LightDurationDelta lightDurationDelta) {
        this.roadDefinition = roadDefinition;
        this.lightDurationDelta = lightDurationDelta;
    }

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
        private double delta;

        private LightDurationDelta(double delta) {
            this.delta = delta;
        }
    }
}
