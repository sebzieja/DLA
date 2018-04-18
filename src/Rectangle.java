import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.math3.distribution.*;

import java.lang.Math.*;


import static org.apache.commons.math3.util.FastMath.abs;
import static org.apache.commons.math3.util.FastMath.ceil;


public class Rectangle {
    public static NormalDistribution normalDistribution = new NormalDistribution(0, 1);
    public float x, y, x2, y2, width, height;
    public boolean stopped;
    public Color color = Color.RED;

    /*
     * Contructor
     *
     * */
    private Rectangle() {
    }

    //    public createStaticRectangle();
    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.x2 = x + width;
        this.y2 = y + height;
    }


    /*Constructor with random placement on the borders*/
    public Rectangle(float width, float height, float canvasWidth, float canvasHeight, Color color) {
//        float p = (float) (ThreadLocalRandom.current().nextDouble(0, canvasWidth * 2 + canvasHeight * 2));
        this.x = (float) (ThreadLocalRandom.current().nextDouble(20, canvasWidth - 20));
        this.y = (float) (ThreadLocalRandom.current().nextDouble(20, canvasHeight - 20));
        this.width = width;
        this.height = height;
        this.color = color;

    }

    public static void setNormalDistribution(double temperature) {
        Rectangle.normalDistribution = new NormalDistribution(0, temperature);
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

    public void calculateNextStepCooridanates() {
        x += (ThreadLocalRandom.current().nextFloat() - 0.5) * abs(ceil(normalDistribution.sample()));
        y += (ThreadLocalRandom.current().nextFloat() - 0.5) * abs(ceil(normalDistribution.sample()));
    }

    //TODO Collision Detection
    /*Move one move, check for collision and react if collision occurs*/
    public void moveOneStepCollision(ContainerBox box) {
        // Get the rectangle's bounds, offset by the radius of the rectangle

        if (!stopped) {
            // Calculate the rectangle's new position
            calculateNextStepCooridanates();
            for (Rectangle rect : ElementsWorld.staticRectangles) {
                if (rect.stopped) {
                    if (intersect(rect)) {
                        color = Color.RED;
                        stopped = true;
                        ElementsWorld.staticRectangles.add(this);
                        return;
                    }
                }
            }

            float rectangleMinX = box.minX + 1;
            float rectangleMinY = box.minY + 1;
            float rectangleMaxX = box.maxX - height - 1;
            float rectangleMaxY = box.maxY - width - 1;

            // Check if the rectangle moves over the bounds. If so, adjust the position and speed.
            if (x < rectangleMinX) {
                x = rectangleMinX;     // Re-position the rectangle at the edge
            } else if (x > rectangleMaxX) {
                x = rectangleMaxX;
            }
            // May cross both x and y bounds
            if (y < rectangleMinY) {
                y = rectangleMinY;
            } else if (y > rectangleMaxY) {
                y = rectangleMaxY;
            }
        }
    }
}

