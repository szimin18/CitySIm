package pl.egu.agh.citysim.model;

import javafx.scene.paint.Color;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class Car {
    private final Color color;
    private final List<Crossing> nextCrossings;

    private final long creationTime;
    private Road road;
    private double distancePassedOnRoad;
    private Coordinates location;
    private Optional<Coordinates> previousLocation;
    private boolean moved = false;

    public void moveTo(final Road road, final double distancePassedOnRoad) {
        moved = true;
        if (this.road == null || !this.road.equals(road)) {
            this.distancePassedOnRoad = 0;
            this.road = road;
        }
        this.distancePassedOnRoad += distancePassedOnRoad;

        previousLocation = Optional.ofNullable(location);
        location = this.road.locationAfterPassing(this.distancePassedOnRoad);
    }

    public void stay() {
        moved = false;
    }

    public void markVisited(final Crossing crossing) {
        if (!nextCrossings.get(0).equals(crossing)) {
            throw new IllegalArgumentException("It's not the next crossing!");
        }

        nextCrossings.remove(0);
    }

    public Optional<Crossing> nextCrossing() {
        if (nextCrossings.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(nextCrossings.get(0));
        }
    }

    public CarShadow shadow() {
        return new CarShadow(road, distancePassedOnRoad, moved);
    }
}
