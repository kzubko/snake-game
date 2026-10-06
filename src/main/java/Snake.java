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

    public void setDirection(Direction direction) {
        this.direction = direction;
    }


    public List<Point> getBody() {
        return body;
    }

    public void move(){
        Point head = body.getFirst();
        Point newHead = new Point(head.getX() + 1, head.getY());
        body.addFirst(newHead);
        body.removeLast();
    }

    public boolean hitWall(int gridSize){
        Point head = body.getFirst();

        return head.getX() < 0 || head.getX() >= gridSize || head.getY() < 0 || head.getY() >= gridSize;
    }

}
