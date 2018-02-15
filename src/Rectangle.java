import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Rectangle {
    private float x, y, width, height, speedX, speedY;
    private Color color;
    private static final Color DEFAULT_COLOT = Color.RED;

    /*
     * Contructor
     *
     * */
    public Rectangle(float x, float y, float width, float height, float speed, float angle, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        //Convert speed and angle to x, y speed
        this.speedX = (float) (speed * Math.cos(Math.toRadians((angle))));
        this.speedY = (float) (-speed * Math.sin(Math.toRadians((angle))));
        this.color = color;
    }

    /*Constructor with default color*/
    public Rectangle(float x, float y, float width, float height, float speed, float angle) {
        this(x, y, width, height, speed, angle, DEFAULT_COLOT);
    }

    public void draw(Graphics graphics) {
        graphics.setColor(color);
        graphics.fillRect((int) x, (int) y, (int) width, (int) height);
    }

    /*Move one move, check for collision and react if collision occurs*/
    public void moveOneStepCollision(ContainerBox box) {
        // Get the rectangle's bounds, offset by the radius of the rectangle
        float rectangleMinX = box.minX + 1;
        float rectangleMinY = box.minY + 1;
        float rectangleMaxX = box.maxX - height + 1;
        float rectangleMaxY = box.maxY - width + 1;

        // Calculate the rectangle's new position
        x += (ThreadLocalRandom.current().nextFloat() - 0.5) * 20;
        y += (ThreadLocalRandom.current().nextFloat() - 0.5) * 20;
        // Check if the rectangle moves over the bounds. If so, adjust the position and speed.
        if (x < rectangleMinX) {
            speedX = -speedX; // Reflect along normal
            x = rectangleMinX;     // Re-position the rectangle at the edge
        } else if (x > rectangleMaxX) {
            speedX = -speedX;
            x = rectangleMaxX;
        }
        // May cross both x and y bounds
        if (y < rectangleMinY) {
            speedY = -speedY;
            y = rectangleMinY;
        } else if (y > rectangleMaxY) {
            speedY = -speedY;
            y = rectangleMaxY;
        }
    }
}
