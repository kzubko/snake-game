import java.util.ArrayList;
import java.util.List;

public class Snake {

    private final List<Point> body;
    private Direction direction;

    public Snake() {
        body = new ArrayList<Point>();

        body.add(new Point(12, 12));
        body.add(new Point(11, 12));
        body.add(new Point(10, 12));

        direction = Direction.RIGHT;

    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction newDirection) {

        if ( direction == Direction.RIGHT && newDirection == Direction.LEFT) {
            return;
        }

        if ( direction == Direction.LEFT && newDirection == Direction.RIGHT) {
            return;
        }

        if ( direction == Direction.UP && newDirection == Direction.DOWN) {
            return;
        }

        if ( direction == Direction.DOWN && newDirection == Direction.UP) {
            return;
        }
        direction = newDirection;
    }


    public List<Point> getBody() {
        return body;
    }

    public void move(boolean grow){

        Point newHead = getNextHead();
        body.addFirst(newHead);

        if (!grow) {
            body.removeLast();
        }
    }

    public Point getNextHead() {
        Point head = body.getFirst();

        int newX = head.getX();
        int newY = head.getY();

        switch(direction){
            case RIGHT:
                newX++;
                break;
            case LEFT:
                newX--;
                break;
            case DOWN:
                newY++;
                break;
            case UP:
                newY--;
                break;
        }

        return new Point(newX, newY);
    }

    public boolean hitWall(int gridSize){
        Point head = body.getFirst();

        return head.getX() < 0 || head.getX() >= gridSize || head.getY() < 0 || head.getY() >= gridSize;
    }

}
