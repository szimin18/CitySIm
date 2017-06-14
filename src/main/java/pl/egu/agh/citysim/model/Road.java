package pl.egu.agh.citysim.model;

import com.google.common.collect.ImmutableList;
import lombok.Value;

@Value
public class Road {
    Crossing start;
    Crossing end;
    ImmutableList<Coordinates> bendingPoints;
}
