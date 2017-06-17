package pl.egu.agh.citysim.model;

import lombok.Value;

import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.sqrt;

@Value
public class Coordinates {
    double x;
    double y;

    public double length() {
        return sqrt(x * x + y * y);
    }

    public Coordinates map(final DoubleUnaryOperator mapper) {
        return new Coordinates(mapper.applyAsDouble(x), mapper.applyAsDouble(y));
    }

    public Coordinates add(final Coordinates other) {
        return new Coordinates(x + other.x, y + other.y);
    }

    public Coordinates substract(final Coordinates other) {
        return new Coordinates(x - other.x, y - other.y);
    }

    public Coordinates muliply(final double value) {
        return new Coordinates(x * value, y * value);
    }

    public Coordinates divide(final double value) {
        return new Coordinates(x / value, y / value);
    }
}
