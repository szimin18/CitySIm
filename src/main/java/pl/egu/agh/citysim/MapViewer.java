package pl.egu.agh.citysim;

import com.google.common.collect.ImmutableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.RequiredArgsConstructor;
import lombok.val;
import pl.egu.agh.citysim.model.CarsState;
import pl.egu.agh.citysim.model.Coordinates;
import pl.egu.agh.citysim.model.Road;
import pl.egu.agh.citysim.model.RoadsMap;

import java.util.Optional;
import java.util.stream.IntStream;

import static java.lang.Math.*;
import static javafx.scene.paint.Color.BLACK;

@RequiredArgsConstructor
public class MapViewer {

    private final GraphicsContext graphicsContext;

    private final RoadsMap roadsMap;

    private static final double MAP_COORDINATES_RANGE = 1100.0;
    private static final double PIXELS_MAP_RANGE = 800.0;
    private static final double PIXELS_FOR_MARGINS = 100.0;

    private static final double PIXELS_PER_MAP_STEP = PIXELS_MAP_RANGE / MAP_COORDINATES_RANGE;

    public static final double CROSSING_SIZE = 30;
    public static final double CAR_SIZE = 10;
    private static final double HALF_CROSSING_SIZE = CROSSING_SIZE / 2;
    private static final double HALF_CAR_SIZE = CAR_SIZE / 2;

    private static final Color PIMP_PURPLE_COLOR = Color.valueOf("#803CA2");

    public static double requiredCanvasSize() {
        return PIXELS_FOR_MARGINS + PIXELS_MAP_RANGE + PIXELS_FOR_MARGINS;
    }

    private void drawMap() {
        graphicsContext.setFont(new Font("Arial", HALF_CROSSING_SIZE));

        roadsMap.getCrossings().forEach(crossing -> drawCrossing(crossing.getCoordinates(), crossing.getName()));

        roadsMap.getRoads().forEach(this::drawRoad);
    }

    private void drawRoad(final Road road) {
        final ImmutableList<Coordinates> bendingPoints = road.getBendingPoints();
        if (bendingPoints.isEmpty()) {
            val roadStart = movedPoint(scaleCoordinates(road.getStart().getCoordinates()), scaleCoordinates(road.getEnd().getCoordinates()), HALF_CROSSING_SIZE);
            val roadEnd = movedPoint(scaleCoordinates(road.getEnd().getCoordinates()), scaleCoordinates(road.getStart().getCoordinates()), HALF_CROSSING_SIZE);
            drawArrow(roadStart, roadEnd);
        } else {
            final Coordinates firstBendingPoint = bendingPoints.get(0);
            final Coordinates lastBendingPoint = bendingPoints.get(bendingPoints.size() - 1);
            val roadStart = movedPoint(scaleCoordinates(road.getStart().getCoordinates()), scaleCoordinates(firstBendingPoint), HALF_CROSSING_SIZE);
            val roadEnd = movedPoint(scaleCoordinates(road.getEnd().getCoordinates()), scaleCoordinates(lastBendingPoint), HALF_CROSSING_SIZE);
            drawLine(roadStart, scaleCoordinates(firstBendingPoint));
            drawArrow(scaleCoordinates(lastBendingPoint), roadEnd);

            IntStream.range(0, bendingPoints.size() - 1).forEach(part ->
                    drawLine(bendingPoints.get(part), bendingPoints.get(part + 1)));
        }
    }

    private Coordinates rotatedPoint(final Coordinates point, final Coordinates center, final double radiansToRight) {
        val sine = sin(radiansToRight);
        val cosine = cos(radiansToRight);

        final double x = (point.getX() - center.getX()) * cosine - (point.getY() - center.getY()) * sine + center.getX();
        final double y = (point.getX() - center.getX()) * sine + (point.getY() - center.getY()) * cosine + center.getY();
        return new Coordinates(x, y);
    }

    private Coordinates movedPoint(final Coordinates point, final Coordinates direction, final double distance) {
        final Coordinates vector = new Coordinates(direction.getX() - point.getX(), direction.getY() - point.getY());
        final double vectorLength = sqrt(pow(vector.getX(), 2) + pow(vector.getY(), 2));
        final Coordinates normalizedVector = new Coordinates(vector.getX() / vectorLength, vector.getY() / vectorLength);
        return new Coordinates(point.getX() + normalizedVector.getX() * distance, point.getY() + normalizedVector.getY() * distance);
    }

    private void drawCrossing(final Coordinates location, final String name) {
        val textX = scaleValue(location.getX()) + 1.5 * HALF_CROSSING_SIZE;
        val textY = scaleValue(location.getY()) - 1.0 * HALF_CROSSING_SIZE;

        drawCircle(scaleCoordinates(location), PIMP_PURPLE_COLOR, HALF_CROSSING_SIZE);
        graphicsContext.fillText(name, textX, textY);
    }

    public void drawCars(final CarsState carsState) {
        // FIXME ugly temporary fix
        graphicsContext.clearRect(0, 0, 1000, 1000);
        drawMap();
        carsState.getCars().forEach(car -> drawCar(car.getLocation(), car.getPreviousLocation(), car.getColor()));
    }

    private void drawCar(final Coordinates location, final Optional<Coordinates> previousLocation, final Color color) {
        final Coordinates scaledCoordinates = scaleCoordinates(location);
        if (!previousLocation.isPresent()) {
            drawRect(scaledCoordinates, color, HALF_CAR_SIZE);
        } else {
            final Coordinates scaledPreviousLocation = scaleCoordinates(previousLocation.get());
            final Coordinates movingDirection = scaledCoordinates.substract(scaledPreviousLocation);
            final double movingDirectionLength = sqrt(pow(movingDirection.getX(), 2) + pow(movingDirection.getY(), 2));
            final Coordinates unitMovingDirection = movingDirection.map(c -> c / movingDirectionLength * HALF_CAR_SIZE);
            final Coordinates rotatedMovingDirection = new Coordinates(-unitMovingDirection.getY(), unitMovingDirection.getX());
            final Coordinates movedLocation = scaledCoordinates.add(rotatedMovingDirection);
            drawRect(movedLocation, color, HALF_CAR_SIZE);
        }
    }

    private void drawCircle(final Coordinates middle, final Color color, final double radius) {
        final double x = middle.getX() - radius;
        final double y = middle.getY() - radius;
        final double size = radius * 2;

        graphicsContext.setFill(color);

        graphicsContext.fillOval(x, y, size, size);
        graphicsContext.strokeOval(x, y, size, size);

        graphicsContext.setFill(BLACK);
    }

    private void drawRect(final Coordinates middle, final Color color, final double halfRectSide) {
        final double rectX = middle.getX() - halfRectSide;
        final double rectY = middle.getY() - halfRectSide;
        final double rectSide = 2 * halfRectSide;

        graphicsContext.setFill(color);

        graphicsContext.fillRect(rectX, rectY, rectSide, rectSide);
        graphicsContext.strokeRect(rectX, rectY, rectSide, rectSide);

        graphicsContext.setFill(BLACK);
    }

    private void drawArrow(final Coordinates start, final Coordinates end) {
        drawLine(start, end);
        drawLine(movedPoint(end, rotatedPoint(start, end, +PI / 6), HALF_CROSSING_SIZE), end);
        drawLine(movedPoint(end, rotatedPoint(start, end, -PI / 6), HALF_CROSSING_SIZE), end);
    }

    private void drawLine(final Coordinates start, final Coordinates end) {
        graphicsContext.beginPath();
        graphicsContext.moveTo(start.getX(), start.getY());
        graphicsContext.lineTo(end.getX(), end.getY());
        graphicsContext.closePath();
        graphicsContext.stroke();
    }

    private Coordinates scaleCoordinates(final Coordinates coordinates) {
        return coordinates.map(this::scaleValue);
    }

    private double scaleValue(final double value) {
        return PIXELS_FOR_MARGINS + value * PIXELS_PER_MAP_STEP;
    }


}
