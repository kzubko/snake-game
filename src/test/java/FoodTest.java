import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FoodTest {

    @Test
    void foodShouldBeGeneratedInsideGrid() {
        Snake snake = new Snake();
        Food food = new Food();

        int gridSize = 18;

        food.generatePosition(gridSize, snake.getBody());

        Point position = food.getPosition();

        assertTrue(position.getX() >= 0);
        assertTrue(position.getX() < gridSize);

        assertTrue(position.getY() >= 0);
        assertTrue(position.getY() < gridSize);
    }

    @Test
    void foodShouldNotBeGeneratedOnSnake() {
        Snake snake = new Snake();
        Food food = new Food();

        int gridSize = 18;

        for (int i = 0; i < 100; i++) {
            food.generatePosition(gridSize, snake.getBody());

            Point foodPosition = food.getPosition();

            boolean foodOnSnake = false;

            for (Point bodyPart : snake.getBody()) {
                if (bodyPart.getX() == foodPosition.getX()
                        && bodyPart.getY() == foodPosition.getY()) {

                    foodOnSnake = true;
                    break;
                }
            }

            assertFalse(foodOnSnake);
        }
    }

    @Test
    void foodPositionShouldNotBeNullAfterGeneration() {
        Snake snake = new Snake();
        Food food = new Food();

        food.generatePosition(18, snake.getBody());

        assertNotNull(food.getPosition());
    }
}