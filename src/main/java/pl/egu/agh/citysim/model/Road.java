package pl.egu.agh.citysim.model;

import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import java.util.List;
import java.util.stream.IntStream;

import static com.google.common.collect.Lists.newLinkedList;

@ToString(of = {"start", "end"})
@EqualsAndHashCode(of = {"start", "end"})
@Value
public class Road {
    Crossing start;
    Crossing end;
    ImmutableList<Coordinates> bendingPoints;
    @Getter(lazy = true)
    ImmutableList<Coordinates> allCoordinates = calculateAllCoordinates();
    @Getter(lazy = true)
    double length = calculateLength();

    private double calculateLength() {
        final ImmutableList<Coordinates> allCoordinates = getAllCoordinates();

        final double total = 0;

        return IntStream.range(0, allCoordinates.size() - 1)
                .mapToDouble(i -> allCoordinates.get(i + 1).substract(allCoordinates.get(i)).length()).sum();
    }

    private ImmutableList<Coordinates> calculateAllCoordinates() {
        return ImmutableList.<Coordinates>builder()
                .add(start.getCoordinates())
                .addAll(bendingPoints)
                .add(end.getCoordinates()).build();
    }

    public Coordinates locationAfterPassing(final double distancePassedOnRoad) {
        double distanceLeft = distancePassedOnRoad;

        final List<Coordinates> allCoordinates = newLinkedList(getAllCoordinates());
        while (allCoordinates.size() > 1 && distanceLeft >= allCoordinates.get(1).substract(allCoordinates.get(0)).length()) {
            distanceLeft -= allCoordinates.get(1).substract(allCoordinates.get(0)).length();
            allCoordinates.remove(0);
        }

        if (distanceLeft == 0) {
            return allCoordinates.get(0);
        }

        return allCoordinates.get(1).substract(allCoordinates.get(0)).normalize().muliply(distanceLeft).add(allCoordinates.get(0));
    }
}
