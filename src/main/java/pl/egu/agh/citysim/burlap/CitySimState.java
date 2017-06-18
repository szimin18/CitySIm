package pl.egu.agh.citysim.burlap;

import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.State;
import com.google.common.collect.Lists;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class CitySimState implements MutableState {

    private final static int MAX_NUM_OF_EPOCHS = 100;
    private final Map<Pair<String, String>, Double> crossingsLightDuration;
    private int epochNumber;

    public CitySimState(final Map<Pair<String, String>, Double> crossingsLightDuration) {
        this(crossingsLightDuration, 0);
    }

    private CitySimState(final Map<Pair<String, String>, Double> crossingsLightDuration, final int epochNumber) {
        this.crossingsLightDuration = crossingsLightDuration;
        this.epochNumber = epochNumber;
    }

    @Override
    public MutableState set(final Object key, final Object value) {
        crossingsLightDuration.put((Pair<String, String>) key, (Double) value);
        epochNumber++;
        return this;
    }

    @Override
    public List<Object> variableKeys() {
        return Lists.newArrayList(crossingsLightDuration.keySet());
    }

    @Override
    public Double get(final Object roadDefinition) {
        return crossingsLightDuration.get(roadDefinition);
    }

    @Override
    public State copy() {
        return new CitySimState(newHashMap(crossingsLightDuration), epochNumber);
    }

    public boolean isTerminal() {
        return epochNumber == MAX_NUM_OF_EPOCHS;
    }
}
