package pl.egu.agh.citysim.model;

import com.google.common.collect.ImmutableSet;
import lombok.Value;

@Value
public class CarsState {
    ImmutableSet<Car> cars;
    RoadsMap roadsMap;
}
