package pl.egu.agh.citysim.model;

import lombok.Value;

import java.util.List;

@Value
public class Crossing {
    Coordinates coordinates;
    String name;
    List<Road> in;
    List<Road> out;
}
