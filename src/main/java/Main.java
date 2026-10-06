import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Main extends Application {

    private static final int GRID_SIZE = 25;
    private static final int CELL_SIZE = 30;
    private static final int SIZE = GRID_SIZE * CELL_SIZE;
    private static final int SNAKE_PADDING = 3;

    private Snake snake;
    private Canvas canvas;
    private GraphicsContext gc;
    private Timeline timeline;

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Snake Game");

        snake = new Snake();

        canvas = new Canvas(SIZE, SIZE);
        gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);

        stage.setScene(scene);

        drawGame();


        timeline = new Timeline(
                new KeyFrame(Duration.millis(200), event -> {
                    snake.move();

                    if (snake.hitWall(GRID_SIZE)){
                        timeline.stop();
                        gameOver();
                    } else {
                        drawGame();
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        stage.show();
    }

    public void drawGame () {

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, SIZE, SIZE);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(0.2);

        for (int i = 0; i <= SIZE; i += CELL_SIZE) {
            gc.strokeLine(i, 0, i, SIZE);
            gc.strokeLine(0, i, SIZE, i);
        }

        for (int i = 0; i < snake.getBody().size(); i++) {
            Point point = snake.getBody().get(i);

            int x = point.getX() * CELL_SIZE;
            int y = point.getY() * CELL_SIZE;

            if ( i == 0 ) {
                gc.setFill(Color.DARKRED);
                gc.fillOval(x + 3, y + 3, CELL_SIZE - 6, CELL_SIZE - 6);
            } else {
                gc.setFill(Color.RED);
                gc.fillRoundRect(x + SNAKE_PADDING,
                        y + SNAKE_PADDING,
                        CELL_SIZE - SNAKE_PADDING * 2,
                        CELL_SIZE - SNAKE_PADDING * 2,
                        8,
                        8);
            }

        }

    }

    public void gameOver() {

        double boxWidth = 400;
        double boxHeight = 180;

        double x = (SIZE - boxWidth) / 2.0;
        double y = (SIZE - boxHeight) / 2.0;

        gc.setGlobalAlpha(0.85);
        gc.setFill(Color.DARKGRAY);

        gc.fillRoundRect(x, y, boxWidth, boxHeight, 30, 30);

        gc.setGlobalAlpha(1.0);

        gc.setTextAlign(TextAlignment.CENTER);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.setFill(Color.RED);
        gc.fillText("Game Over", SIZE / 2.0, SIZE / 2.0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}