package pl.egu.agh.citysim.model;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

@Value
@RequiredArgsConstructor
public class Crossing {
    Coordinates coordinates;
    String name;
    List<Road> inRoads = newArrayList();
    Map<Crossing, Road> outRoads = newHashMap();
}
