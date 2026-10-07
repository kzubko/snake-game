import java.util.List;
import java.util.Random;

public class Food {

    private Point position;

    public Food() {}

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void generatePosition(int gridSize, List<Point> body) {

        Random random = new Random();

        while (true) {
            int x = random.nextInt(gridSize);
            int y = random.nextInt(gridSize);

            boolean occupied = false;

            for (Point p : body) {
                if (p.getX() == x && p.getY() == y) {
                    occupied = true;
                    break;
                }
            }

            if (!occupied) {
                position = new Point(x, y);
                break;
            }
        }
    }
}
