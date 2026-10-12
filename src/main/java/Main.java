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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Main extends Application {

    private static final int GRID_SIZE = 18;
    private static final int CELL_SIZE = 33;
    private static final int SIZE = GRID_SIZE * CELL_SIZE;
    private static final int SNAKE_PADDING = 3;
    private static final String HIGH_SCORE_FILE = System.getProperty("user.home") + "/.snake-game/highscore.txt";

    private Snake snake;
    private Stage stage;
    private Canvas canvas;
    private GraphicsContext gc;
    private Timeline timeline;
    private Food food;
    private int score;
    private Label scoreLabel;
    private int level = 1;
    private Label levelLabel;
    private double delay = 200;
    private Label bestScore;
    private int highScore;


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
        startGame(200);

        stage.show();
    }

    private Scene getScene() {

        loadHighScore();

        HBox topBar = createTopBar();
        HBox bottomBar = createBottomBar();

        VBox root = new VBox(topBar, bottomBar, canvas);

        Scene scene = new Scene(root);

        loadStyles(scene);
        setupKeyboard(scene);

        return scene;
    }

    public void drawGame () {
        drawBackground();
        drawSnake();
        drawFood();
    }

    private HBox createTopBar() {
        levelLabel = new Label("Level: " + level);

        Button newGameButton = new Button("New Game");
        Button exitButton = new Button("Exit");

        newGameButton.setPrefWidth(120);
        exitButton.setPrefWidth(120);

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();

        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        HBox topBar = new HBox(newGameButton,
                leftSpacer,
                levelLabel,
                rightSpacer,
                exitButton
        );

        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(8));

        levelLabel.getStyleClass().add("level-label-style");

        newGameButton.getStyleClass().add("game-button-style");
        exitButton.getStyleClass().add("game-button-style");

        topBar.getStyleClass().add("top-bar-style");

        newGameButton.setOnAction(e -> {
            restartGame();
        });

        exitButton.setOnAction(e -> {
            stage.close();
        });

        return topBar;
    }

    private HBox createBottomBar() {
        scoreLabel = new Label("Score: " + score);
        bestScore = new Label("Best Score: " + highScore);

        Button resetButton = new Button("reset");

        Region middleSpacer = new Region();

        VBox bestBox = new VBox(bestScore, resetButton);
        bestBox.setAlignment(Pos.TOP_RIGHT);

        VBox scoreBox = new VBox(scoreLabel);
        scoreBox.setAlignment(Pos.TOP_LEFT);

        HBox.setHgrow(middleSpacer, Priority.ALWAYS);

        HBox bottomBar = new HBox(
                scoreBox,
                middleSpacer,
                bestBox
        );

        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(8));

        scoreLabel.getStyleClass().add("bottom-style");
        bestScore.getStyleClass().add("bottom-style");
        resetButton.getStyleClass().add("reset-button-style");

        bottomBar.getStyleClass().add("bottom-bar-style");

        resetButton.setOnAction(e -> {
            confirmReset();
        });

        return bottomBar;
    }

    private void confirmReset() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Reset Best Score");
        alert.setHeaderText("Reset Best Score?");
        alert.setContentText("Are you sure you want to reset the best score?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                highScore = 0;
                bestScore.setText("Best Score: " + highScore);
                saveHighScore();
            }
        });
    }

    private void loadStyles (Scene scene) {
        scene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/style.css")
                ).toExternalForm()
        );
    }

    private void setupKeyboard(Scene scene) {
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
    }

    private void drawBackground() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, SIZE, SIZE);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(0.5);

        gc.strokeLine(0, 0, SIZE, 0);
    }

    private void drawSnake() {
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
    }

    private void drawFood() {
        Point foodPos = food.getPosition();
        gc.setFill(Color.web("#D414FF"));

        int x = foodPos.getX() * CELL_SIZE;
        int y = foodPos.getY() * CELL_SIZE;

        gc.fillOval(x + SNAKE_PADDING,
                y + SNAKE_PADDING,
                CELL_SIZE - SNAKE_PADDING * 2,
                CELL_SIZE - SNAKE_PADDING * 2
        );
    }

    public void gameOver() {

        double boxWidth = 350;
        double boxHeight = 100;

        double x = (SIZE - boxWidth) / 2.0;
        double y = (SIZE - boxHeight) / 2.0;

        //gc.setGlobalAlpha(0.85);
        gc.setFill(Color.BLACK);

        gc.fillRect(x, y, boxWidth, boxHeight);

        //gc.setGlobalAlpha(1.0);

        gc.setTextAlign(TextAlignment.CENTER);

        gc.setFont(Font.font("Inter", FontWeight.BOLD, 48));
        gc.setFill(Color.web("#B5093B"));
        gc.fillText("Game Over", SIZE / 2.0, SIZE / 2.0);
    }

    private void startGame(double delay) {

        if ( timeline != null ) {
            timeline.stop();
        }

        timeline = new Timeline(
                new KeyFrame(Duration.millis(delay), event -> {
                    gameTick();
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

    private void gameTick() {
        Point nextHead = snake.getNextHead();
        Point foodPos = food.getPosition();

        boolean eatsFood =
                nextHead.getX() == foodPos.getX()
                        && nextHead.getY() == foodPos.getY();

        snake.move(eatsFood);

        if (eatsFood && handleFood()) {
            return;
        }

        checkGameOver();
    }

    private boolean handleFood() {
        food.generatePosition(GRID_SIZE, snake.getBody());

        score += 10;
        scoreLabel.setText("Score: " + score);

        updateHighScore();

        return updateLevel();
    }

    private void updateHighScore() {
        if (score > highScore) {
            highScore = score;
            bestScore.setText("Best Score: " + highScore);
            saveHighScore();
        }
    }

    private boolean updateLevel() {
        if (score % 50 != 0) {
            return false;
        }

        if (delay <= 40) {
            drawGame();
            timeline.stop();
            winGame();
            return true;
        }

        level++;
        levelLabel.setText("Level: " + level);

        delay -= 20;
        startGame(delay);

        return false;
    }

    private void checkGameOver() {
        if (snake.hitWall(GRID_SIZE) || snake.hitItself()) {
            timeline.stop();
            gameOver();
        } else {
            drawGame();
        }
    }

    private void restartGame() {
        timeline.stop();

        snake = new Snake();

        score = 0;
        scoreLabel.setText("Score: " + score);

        level = 1;
        levelLabel.setText("Level: " + level);

        delay = 200;

        food.generatePosition(GRID_SIZE, snake.getBody());

        drawGame();

        startGame(delay);
    }

    public void winGame() {

        gc.setTextAlign(TextAlignment.CENTER);

        gc.setFont(Font.font("Inter", FontWeight.BOLD, 48));
        gc.setFill(Color.web("#E8F3FF"));
        gc.fillText("You Won !", SIZE / 2.0, SIZE / 2.0);
    }

    private void loadHighScore() {
        Path path = Paths.get(HIGH_SCORE_FILE);

        try {
            if (Files.exists(path)) {
                String text = Files.readString(path);
                highScore = Integer.parseInt(text.trim());
            } else {
                highScore = 0;
            }
        } catch (IOException | NumberFormatException e) {
            highScore = 0;
        }
    }

    private void saveHighScore() {
        Path path = Paths.get(HIGH_SCORE_FILE);

        try {
            Files.createDirectories(path.getParent());
            String text = String.valueOf(highScore);
            Files.writeString(path, text);
        } catch (IOException e) {
            System.out.println("Could not save high score.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}