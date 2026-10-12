import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SnakeTest {

    @Test
    void snakeShouldStartWithThreeBodyParts() {
        Snake snake = new Snake();

        assertEquals(3, snake.getBody().size());
    }

    @Test
    void snakeShouldStartMovingRight() {
        Snake snake = new Snake();

        assertEquals(Direction.RIGHT, snake.getDirection());
    }

    @Test
    void snakeShouldMoveRight() {
        Snake snake = new Snake();

        Point oldHead = snake.getBody().getFirst();

        snake.move(false);

        Point newHead = snake.getBody().getFirst();

        assertEquals(oldHead.getX() + 1, newHead.getX());
        assertEquals(oldHead.getY(), newHead.getY());
    }

    @Test
    void snakeShouldMoveUp() {
        Snake snake = new Snake();

        Point oldHead = snake.getBody().getFirst();

        snake.setDirection(Direction.UP);
        snake.move(false);

        Point newHead = snake.getBody().getFirst();

        assertEquals(oldHead.getX(), newHead.getX());
        assertEquals(oldHead.getY() - 1, newHead.getY());
    }

    @Test
    void snakeShouldMoveDown() {
        Snake snake = new Snake();

        Point oldHead = snake.getBody().getFirst();

        snake.setDirection(Direction.DOWN);
        snake.move(false);

        Point newHead = snake.getBody().getFirst();

        assertEquals(oldHead.getX(), newHead.getX());
        assertEquals(oldHead.getY() + 1, newHead.getY());
    }

    @Test
    void snakeShouldMoveLeft() {
        Snake snake = new Snake();

        /*
         * The snake initially moves RIGHT, so LEFT cannot be selected
         * immediately. First make one valid turn and move.
         */
        snake.setDirection(Direction.UP);
        snake.move(false);

        Point oldHead = snake.getBody().getFirst();

        snake.setDirection(Direction.LEFT);
        snake.move(false);

        Point newHead = snake.getBody().getFirst();

        assertEquals(oldHead.getX() - 1, newHead.getX());
        assertEquals(oldHead.getY(), newHead.getY());
    }

    @Test
    void snakeShouldKeepSameLengthWhenNotGrowing() {
        Snake snake = new Snake();

        int oldSize = snake.getBody().size();

        snake.move(false);

        assertEquals(oldSize, snake.getBody().size());
    }

    @Test
    void snakeShouldGrowWhenEatingFood() {
        Snake snake = new Snake();

        int oldSize = snake.getBody().size();

        snake.move(true);

        assertEquals(oldSize + 1, snake.getBody().size());
    }

    @Test
    void snakeShouldNotReverseFromRightToLeft() {
        Snake snake = new Snake();

        Point oldHead = snake.getBody().getFirst();

        snake.setDirection(Direction.LEFT);
        snake.move(false);

        Point newHead = snake.getBody().getFirst();

        // LEFT should be ignored, so the snake still moves RIGHT
        assertEquals(oldHead.getX() + 1, newHead.getX());
        assertEquals(oldHead.getY(), newHead.getY());
    }

    @Test
    void snakeShouldNotReverseFromUpToDown() {
        Snake snake = new Snake();

        snake.setDirection(Direction.UP);
        snake.move(false);

        Point oldHead = snake.getBody().getFirst();

        snake.setDirection(Direction.DOWN);
        snake.move(false);

        Point newHead = snake.getBody().getFirst();

        // DOWN should be ignored
        assertEquals(oldHead.getX(), newHead.getX());
        assertEquals(oldHead.getY() - 1, newHead.getY());
    }

    @Test
    void snakeShouldAcceptOnlyOneDirectionChangePerTick() {
        Snake snake = new Snake();

        Point oldHead = snake.getBody().getFirst();

        snake.setDirection(Direction.UP);
        snake.setDirection(Direction.LEFT);

        snake.move(false);

        Point newHead = snake.getBody().getFirst();

        /*
         * The first change (UP) should be accepted.
         * The second change (LEFT) should be ignored until move().
         */
        assertEquals(oldHead.getX(), newHead.getX());
        assertEquals(oldHead.getY() - 1, newHead.getY());
    }

    @Test
    void directionChangeShouldBeAllowedAgainAfterMove() {
        Snake snake = new Snake();

        snake.setDirection(Direction.UP);
        snake.move(false);

        Point oldHead = snake.getBody().getFirst();

        // move() resets directionChanged, so LEFT can now be selected
        snake.setDirection(Direction.LEFT);
        snake.move(false);

        Point newHead = snake.getBody().getFirst();

        assertEquals(oldHead.getX() - 1, newHead.getX());
        assertEquals(oldHead.getY(), newHead.getY());
    }

    @Test
    void snakeShouldDetectWallCollision() {
        Snake snake = new Snake();

        int gridSize = 18;

        while (!snake.hitWall(gridSize)) {
            snake.move(false);
        }

        assertTrue(snake.hitWall(gridSize));
    }

    @Test
    void snakeShouldNotHitWallAtStart() {
        Snake snake = new Snake();

        assertFalse(snake.hitWall(18));
    }

    @Test
    void snakeShouldDetectSelfCollision() {
        Snake snake = new Snake();

        // Make the snake longer
        snake.move(true);
        snake.move(true);
        snake.move(true);

        // Turn down
        snake.setDirection(Direction.DOWN);
        snake.move(false);

        // Turn left
        snake.setDirection(Direction.LEFT);
        snake.move(false);

        // Turn up into its own body
        snake.setDirection(Direction.UP);
        snake.move(false);

        assertTrue(snake.hitItself());
    }

    @Test
    void snakeShouldNotHitItselfDuringNormalMovement() {
        Snake snake = new Snake();

        snake.move(false);
        snake.move(false);

        assertFalse(snake.hitItself());
    }

    @Test
    void snakeShouldNotReverseFromLeftToRight() {
        Snake snake = new Snake();

        snake.setDirection(Direction.UP);
        snake.move(false);

        snake.setDirection(Direction.LEFT);
        snake.move(false);

        Point oldHead = snake.getBody().getFirst();

        snake.setDirection(Direction.RIGHT);
        snake.move(false);

        Point newHead = snake.getBody().getFirst();

        assertEquals(oldHead.getX() - 1, newHead.getX());
        assertEquals(oldHead.getY(), newHead.getY());
    }

    @Test
    void snakeShouldNotReverseFromDownToUp() {
        Snake snake = new Snake();

        snake.setDirection(Direction.DOWN);
        snake.move(false);

        Point oldHead = snake.getBody().getFirst();

        snake.setDirection(Direction.UP);
        snake.move(false);

        Point newHead = snake.getBody().getFirst();

        assertEquals(oldHead.getX(), newHead.getX());
        assertEquals(oldHead.getY() + 1, newHead.getY());
    }

    @Test
    void snakeShouldNotDetectWallCollisionInsideGrid() {
        Snake snake = new Snake();

        assertFalse(snake.hitWall(18));
    }
}