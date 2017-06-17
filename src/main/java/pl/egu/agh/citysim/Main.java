package pl.egu.agh.citysim;

import com.google.common.collect.ImmutableSet;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.egu.agh.citysim.model.Coordinates;
import pl.egu.agh.citysim.model.RoadsMap;
import pl.egu.agh.citysim.model.SimulationParameters;

public class Main extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        final RoadsMap.Builder builder = RoadsMap.builder();
        final SimulationParameters simulationParameters = createMap1(builder);
        final RoadsMap roadsMap = builder.build();

        final GraphicsContext graphicsContext = createAndShowStage(primaryStage).getGraphicsContext2D();
        final MapViewer mapViewer = new MapViewer(graphicsContext, roadsMap);

        final Simulation simulation = new Simulation(roadsMap, 40, mapViewer::drawCars,
                simulationParameters.getStarts(), simulationParameters.getEnds(), simulationParameters.getRequiredNumberOfCars(), 100);
        simulation.run();
    }

    private static Canvas createAndShowStage(final Stage stage) {
        final Canvas canvas = new Canvas(MapViewer.requiredCanvasSize(), MapViewer.requiredCanvasSize());
        final Pane pane = new Pane(canvas);
        final Scene scene = new Scene(pane);

        stage.setScene(scene);
        stage.show();

        return canvas;
    }

    private static SimulationParameters createMap1(final RoadsMap.Builder builder) {
        builder.addCrossing("A", new Coordinates(200, 200));
        builder.addCrossing("B", new Coordinates(600, 200));
        builder.addCrossing("G", new Coordinates(800, 200));
        builder.addCrossing("C", new Coordinates(600, 600));
        builder.addCrossing("D", new Coordinates(200, 600));
        builder.addCrossing("E", new Coordinates(1000, 600));
        builder.addCrossing("F", new Coordinates(1080, 600));
        builder.addCrossing("X", new Coordinates(200, 0));
        builder.addCrossing("Y", new Coordinates(1000, 800));
        builder.addCrossing("Z", new Coordinates(0, 600));
        builder.addCrossing("W", new Coordinates(400, 600));
        builder.addCrossing("U", new Coordinates(600, 0));
        builder.addCrossing("T", new Coordinates(200, 800));

        builder.addRoad("A", "B");
        builder.addRoad("B", "A");
        builder.addRoad("B", "C");
        builder.addRoad("C", "B");
        builder.addRoad("D", "A");
        builder.addRoad("A", "D");
        builder.addRoad("E", "C");
        builder.addRoad("G", "B");
        builder.addRoad("F", "E");
        builder.addRoad("X", "A");
        builder.addRoad("Y", "E");
        builder.addRoad("Z", "D");
        builder.addRoad("C", "W");
        builder.addRoad("B", "U");
        builder.addRoad("D", "T");

        return new SimulationParameters(ImmutableSet.of("X", "Y", "Z"), ImmutableSet.of("W", "U", "T"), 100);
    }
}
