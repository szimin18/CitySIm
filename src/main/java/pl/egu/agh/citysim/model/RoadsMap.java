package pl.egu.agh.citysim.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.*;

import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
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
        private final Set<Road> roads = newHashSet();

        public Builder addCrossing(final String name, final Coordinates coordinates) {
            crossings.put(name, new Crossing(coordinates, name, newArrayList(), newArrayList()));
            return this;
        }

        public Builder addRoad(final String start, final String end, final Coordinates... bendingPoints) {
            return addRoad(start, end, ImmutableList.copyOf(bendingPoints));
        }

        public Builder addRoad(final String start, final String end, final ImmutableList<Coordinates> bendingPoints) {
            roads.add(new Road(crossings.get(start), crossings.get(end), bendingPoints));
            return this;
        }

        public RoadsMap build() {
            return new RoadsMap(ImmutableSet.copyOf(crossings.values()), ImmutableSet.copyOf(roads));
        }
    }
}
