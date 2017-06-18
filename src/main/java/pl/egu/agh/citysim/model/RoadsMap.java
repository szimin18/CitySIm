package pl.egu.agh.citysim.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import javafx.util.Pair;
import lombok.*;
import pl.egu.agh.citysim.burlap.CitySimState;

import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class RoadsMap {
    private final ImmutableSet<Crossing> crossings;
    private final ImmutableSet<Road> roads;

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = PRIVATE)
    public static final class Builder {
        private final Map<String, Crossing> crossings = newHashMap();
        @Getter
        private final Set<Road> roads = newHashSet();

        public Builder addCrossing(final String name, final Coordinates coordinates) {
            crossings.put(name, new Crossing(name, coordinates));
            return this;
        }

        public Builder addRoad(final String start, final String end, final Coordinates... bendingPoints) {
            return addRoad(start, end, ImmutableList.copyOf(bendingPoints));
        }

        public Builder addRoad(final String start, final String end, final ImmutableList<Coordinates> bendingPoints) {
            final Crossing startCrossing = crossings.get(start);
            final Crossing endCrossing = crossings.get(end);
            final Road road = new Road(startCrossing, endCrossing, bendingPoints);
            startCrossing.getOutRoads().put(endCrossing, road);
            endCrossing.getInRoads().add(road);
            roads.add(road);
            return this;
        }

        public RoadsMap build(final CitySimState state) {
            crossings.values().forEach(Crossing::initialize);
            state.variableKeys().stream()
                    .map(o -> (Pair<String, String>) o)
                    .forEach(roadDefinition -> {
                        final Crossing crossing = crossings.get(roadDefinition.getValue());
                        final Road road = crossing.getInRoads().stream()
                                .filter(r -> r.getStart().getName().equals(roadDefinition.getKey()))
                                .findFirst()
                                .get();
                        final int ind = crossing.getInRoads().indexOf(road);
                        crossing.getLightsTimes().remove(ind);
                        crossing.getLightsTimes().add(ind, state.get(roadDefinition));
                    });
            return new RoadsMap(ImmutableSet.copyOf(crossings.values()), ImmutableSet.copyOf(roads));
        }
    }
}
