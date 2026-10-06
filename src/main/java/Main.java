import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Main extends Application {

    private static final int GRID_SIZE = 25;
    private static final int CELL_SIZE = 30;
    private static final int SIZE = GRID_SIZE * CELL_SIZE;

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Snake Game");

        Canvas canvas = new Canvas(SIZE, SIZE);

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);

        stage.setScene(scene);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, SIZE, SIZE);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(0.2);

        for (int i = 0; i <= SIZE; i += CELL_SIZE) {
            gc.strokeLine(i, 0, i, SIZE);
            gc.strokeLine(0, i, SIZE, i);
        }

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}