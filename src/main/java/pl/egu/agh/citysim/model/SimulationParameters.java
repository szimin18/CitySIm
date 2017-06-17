package pl.egu.agh.citysim.model;

import com.google.common.collect.ImmutableSet;
import lombok.Value;

@Value
public class SimulationParameters {
    ImmutableSet<String> starts;
    ImmutableSet<String> ends;
    int requiredNumberOfCars;
}
