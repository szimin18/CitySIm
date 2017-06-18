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
    private final int epochNumber;

    public CitySimState(Map<Pair<String, String>, Double> crossingsLightDuration) {
        this(crossingsLightDuration, 0);
    }

    private CitySimState(Map<Pair<String, String>, Double> crossingsLightDuration, int epochNumber) {
        this.crossingsLightDuration = crossingsLightDuration;
        this.epochNumber = epochNumber;
    }

    @Override
    public MutableState set(Object key, Object value) {
        crossingsLightDuration.put((Pair<String,String>)key, (Double)value);
        return this;
    }

    @Override
    public List<Object> variableKeys() {
        return Lists.newArrayList(crossingsLightDuration.keySet());
    }

    @Override
    public Double get(Object roadDefinition) {
        return crossingsLightDuration.get(roadDefinition);
    }

    @Override
    public State copy() {
        return new CitySimState(newHashMap(crossingsLightDuration), epochNumber + 1);
    }

    public boolean isTerminal() {
        return epochNumber == MAX_NUM_OF_EPOCHS;
    }
}
