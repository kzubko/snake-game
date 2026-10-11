import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Main extends Application {

    private static final int GRID_SIZE = 18;
    private static final int CELL_SIZE = 33;
    private static final int SIZE = GRID_SIZE * CELL_SIZE;
    private static final int SNAKE_PADDING = 3;

    private Snake snake;
    private Stage stage;
    private Canvas canvas;
    private GraphicsContext gc;
    private Timeline timeline;
    private Food food;
    private int score;
    private Label scoreLabel;

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;

        stage.setTitle("Snake Game");

        snake = new Snake();

        canvas = new Canvas(SIZE, SIZE);
        gc = canvas.getGraphicsContext2D();
        food = new Food();
        food.generatePosition(GRID_SIZE, snake.getBody());


        Scene scene = getScene();

        stage.setScene(scene);

        drawGame();


        timeline = new Timeline(
                new KeyFrame(Duration.millis(200), event -> {

                    Point nextHead = snake.getNextHead();
                    Point foodPos = food.getPosition();

                    boolean eatsFood = nextHead.getX() == foodPos.getX() && nextHead.getY() == foodPos.getY();

                    snake.move(eatsFood);

                    if (eatsFood) {
                        food.generatePosition(GRID_SIZE, snake.getBody());
                        score += 10;
                        scoreLabel.setText("Score: " + score);
                    }

                    if (snake.hitWall(GRID_SIZE) || snake.hitItself()){
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

    private Scene getScene() {

        scoreLabel = new Label("Score: " + score);

        Button newGameButton = new Button("New Game");
        Button exitButton = new Button("Exit");

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();

        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        HBox topBar = new HBox(newGameButton,
                leftSpacer,
                scoreLabel,
                rightSpacer,
                exitButton
                );

        topBar.setStyle(
                "-fx-background-color: black;"
        );

        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(8));

        VBox root = new VBox(topBar, canvas);
        Scene scene = new Scene(root);

        scoreLabel.setStyle(
                        "-fx-text-fill: white;" +
                        "-fx-font-family: Inter;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        String buttonStyle =
                "-fx-background-color: #2b2b2b;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8px 18px;" +
                        "-fx-font-family: Inter;";

        newGameButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        newGameButton.setOnAction(e -> {
            restartGame();
        });

        exitButton.setOnAction(e -> {
            stage.close();
        });



        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            switch (event.getCode()) {
                case UP, W:
                    snake.setDirection(Direction.UP);
                    break;
                case DOWN, S:
                    snake.setDirection(Direction.DOWN);
                    break;
                case LEFT, A:
                    snake.setDirection(Direction.LEFT);
                    break;
                case RIGHT,D:
                    snake.setDirection(Direction.RIGHT);
                    break;
            }
        });

        return scene;
    }

    public void drawGame () {

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, SIZE, SIZE);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(0.5);

        gc.strokeLine(0, 0, SIZE, 0);

//        for (int i = 0; i <= SIZE; i += CELL_SIZE) {
//            gc.strokeLine(i, 0, i, SIZE);
//            gc.strokeLine(0, i, SIZE, i);
//        }

        for (int i = 0; i < snake.getBody().size(); i++) {
            Point point = snake.getBody().get(i);

            int x = point.getX() * CELL_SIZE;
            int y = point.getY() * CELL_SIZE;

            if ( i == 0 ) {
                gc.setFill(Color.web("#00D99A"));
                gc.fillOval(x + 3, y + 3, CELL_SIZE - 6, CELL_SIZE - 6);
            } else {
                gc.setFill(Color.web("#00966F"));
                gc.fillRoundRect(x + SNAKE_PADDING,
                        y + SNAKE_PADDING,
                        CELL_SIZE - SNAKE_PADDING * 2,
                        CELL_SIZE - SNAKE_PADDING * 2,
                        8,
                        8);
            }

        }

        Point foodPos = food.getPosition();
        gc.setFill(Color.web("#D414FF"));

        int x = foodPos.getX() * CELL_SIZE;
        int y = foodPos.getY() * CELL_SIZE;

        gc.fillOval(x + SNAKE_PADDING,
                y + SNAKE_PADDING,
                CELL_SIZE - SNAKE_PADDING * 2,
                CELL_SIZE - SNAKE_PADDING * 2);

    }

    public void gameOver() {

        double boxWidth = 400;
        double boxHeight = 180;

        double x = (SIZE - boxWidth) / 2.0;
        double y = (SIZE - boxHeight) / 2.0;

        //gc.setGlobalAlpha(0.85);
        //gc.setFill(Color.web("#CBF2E6"));

        //gc.fillRoundRect(x, y, boxWidth, boxHeight, 30, 30);

        gc.setGlobalAlpha(1.0);

        gc.setTextAlign(TextAlignment.CENTER);

        gc.setFont(Font.font("Inter", FontWeight.BOLD, 48));
        gc.setFill(Color.web("#B5093B"));
        gc.fillText("Game Over", SIZE / 2.0, SIZE / 2.0);
    }

    private void restartGame() {
        timeline.stop();

        snake = new Snake();

        score = 0;
        scoreLabel.setText("Score: " + score);

        food.generatePosition(GRID_SIZE, snake.getBody());

        drawGame();

        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}