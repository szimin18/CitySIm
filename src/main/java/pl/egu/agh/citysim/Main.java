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
        return SimulationRunner.createMap1(builder);
    }
}
