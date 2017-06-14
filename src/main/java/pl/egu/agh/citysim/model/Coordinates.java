package pl.egu.agh.citysim.model;

import lombok.Value;

import java.util.function.DoubleUnaryOperator;

@Value
public class Coordinates {
    double x;
    double y;

    public Coordinates map(DoubleUnaryOperator mapper) {
        return new Coordinates(mapper.applyAsDouble(x), mapper.applyAsDouble(y));
    }

    public Coordinates add(Coordinates other) {
        return new Coordinates(x + other.x, y + other.y);
    }

    public Coordinates substract(Coordinates other) {
        return new Coordinates(x - other.x, y - other.y);
    }
}
