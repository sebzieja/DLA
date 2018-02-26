import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Rectangle {
    public float x, y, x2, y2, width, height, speed, speedX, speedY;
    public Color color;
    public boolean stopped;

    {
        stopped = false;
    }

    private static final Color DEFAULT_COLOT = Color.RED;

    /*
     * Contructor
     *
     * */
    public Rectangle(float x, float y, float width, float height, float speed, float angle, Color color) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.x2 = x + width;
        this.y2 = y + height;
        //Convert speed and angle to x, y speed
        this.speedX = (float) (speed * Math.cos(Math.toRadians((angle))));
        this.speedY = (float) (-speed * Math.sin(Math.toRadians((angle))));
        this.color = color;
    }


    /*Constructor with random placement on the borders*/
    public Rectangle(float width, float height, float canvasWidth, float canvasHeight, float speed, Color color) {
//        float p = (float) (ThreadLocalRandom.current().nextDouble(0, canvasWidth * 2 + canvasHeight * 2));
        this.x = (float) (ThreadLocalRandom.current().nextDouble(0, canvasWidth));
        this.y = (float) (ThreadLocalRandom.current().nextDouble(0, canvasHeight));
        this.width = width;
        this.height = height;
        this.color = color;
        this.speed = speed;
        this.speedX = 0;
        this.speedY = 0;

    }
//    public Rectangle(float width, float height, float canvasWidth, float canvasHeight, float speed, Color color) {
//        float p = (float) (ThreadLocalRandom.current().nextDouble(0, canvasWidth * 2 + canvasHeight * 2));
//        if (p < (canvasHeight + canvasWidth)) {
//            if (p < canvasWidth) {
//                this.x = p;
//                this.y = 0;
//            } else {
//                this.x = canvasWidth;
//                this.y = p - canvasWidth;
//            }
//
//        } else {
//            p -= (canvasWidth + canvasHeight);
//            if (p < canvasWidth) {
//                this.x = canvasWidth - p;
//                this.y = canvasHeight;
//                this.x2 = canvasWidth - p + width;
//                this.y2 = canvasWidth + height;
//            } else {
//                this.x = 0;
//                this.y = canvasHeight - (p - canvasWidth);
//            }
//        }
//        this.width = width;
//        this.height = height;
//        this.color = color;
//        this.speed = speed;
//        this.speedX = (float) (speed * Math.cos(Math.toRadians((15))));
//        this.speedY = (float) (-speed * Math.sin(Math.toRadians((15))));
//
//    }


    public boolean intersect(Rectangle rectangle) {
        if (this.x < (rectangle.x + rectangle.width) && (this.x + this.width) > rectangle.x && this.y < (rectangle.y + rectangle.height) && (this.y + this.height) > rectangle.y) {
            return true;
        }
        return false;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(color);
        graphics.fillRect((int) x, (int) y, (int) width, (int) height);
    }


    //TODO Collision Detection
    /*Move one move, check for collision and react if collision occurs*/

}

