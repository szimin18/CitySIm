package pl.egu.agh.citysim.model;

import javafx.scene.paint.Color;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class Car {
    private final Color color;
    private final List<Crossing> nextCrossings;

    private Road road;
    private Coordinates location;
    private Optional<Coordinates> previousLocation;
    private boolean moved = false;

    public void moveTo(final Coordinates newLocation) {
        previousLocation = Optional.ofNullable(location);
        location = newLocation;
        moved = true;
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
}
