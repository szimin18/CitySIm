package pl.egu.agh.citysim.model;

import javafx.scene.paint.Color;
import lombok.Value;

import java.util.Optional;

@Value
public class Car {
    String id;
    Coordinates location;
    Color color;
    Optional<Coordinates> previousLocation;
}
