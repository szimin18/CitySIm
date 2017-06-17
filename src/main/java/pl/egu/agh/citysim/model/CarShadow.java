package pl.egu.agh.citysim.model;

import lombok.Value;

@Value
public class CarShadow {
    Road road;
    double distancePassedOnRoad;
    boolean moved;
}
