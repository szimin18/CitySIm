package pl.egu.agh.citysim;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final SimulationRunner simulationRunner = new SimulationRunner();

        final GraphicsContext graphicsContext = createAndShowStage(primaryStage).getGraphicsContext2D();
        final MapViewer mapViewer = new MapViewer(graphicsContext, simulationRunner.createInitialRoadMap());

        simulationRunner.run(mapViewer::drawCars, 40);
    }

    private static Canvas createAndShowStage(final Stage stage) {
        final Canvas canvas = new Canvas(MapViewer.requiredCanvasSize(), MapViewer.requiredCanvasSize());
        final Pane pane = new Pane(canvas);
        final Scene scene = new Scene(pane);

        stage.setScene(scene);
        stage.show();

        return canvas;
    }
}
