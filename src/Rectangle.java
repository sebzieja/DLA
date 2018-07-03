import java.awt.*;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.math3.distribution.*;


import static org.apache.commons.math3.util.FastMath.abs;
import static org.apache.commons.math3.util.FastMath.ceil;


public class Rectangle {
    private static NormalDistribution normalDistribution = new NormalDistribution(0, 1);
    private float x;
    private float y;
    private float x2;
    private float y2;
    private float width;
    private float height;
    private float rectangleMinX;
    private float rectangleMinY;
    private float rectangleMaxX;
    private float rectangleMaxY;
    private Color color = Color.RED;
    private static final SplittableRandom splittableRandom = new SplittableRandom();

    private Rectangle() {
    }

    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.x2 = x + width;
        this.y2 = y + height;
    }

    // Constructor that create Rectangle on random point
    public Rectangle(float width, float height, float canvasWidth, float canvasHeight, Color color) {
        this.x = (float) (ThreadLocalRandom.current().nextDouble(20, canvasWidth - 20));
        this.y = (float) (ThreadLocalRandom.current().nextDouble(20, canvasHeight - 20));
        this.width = width;
        this.height = height;
        this.color = color;
    }

    //static constructor that create Rectangle on border
    public static Rectangle createRectangleOnBorder(float width, float height, float canvasWidth, float canvasHeight, Color color) {
        Rectangle rectangle = new Rectangle();
        float p = (float) (ThreadLocalRandom.current().nextDouble(0, canvasWidth * 2 + canvasHeight * 2));
        if (p < (canvasHeight + canvasWidth)) {
            if (p < canvasWidth) {
                rectangle.x = p;
                rectangle.y = 0;
            } else {
                rectangle.x = canvasWidth;
                rectangle.y = p - canvasWidth;
            }
        } else {
            p -= (canvasWidth + canvasHeight);
            if (p < canvasWidth) {
                rectangle.x = canvasWidth - p;
                rectangle.y = canvasHeight;
                rectangle.x2 = canvasWidth - p + width;
                rectangle.y2 = canvasWidth + height;
            } else {
                rectangle.x = 0;
                rectangle.y = canvasHeight - (p - canvasWidth);
            }
        }
        rectangle.width = width;
        rectangle.height = height;
        rectangle.color = color;
        return rectangle;
    }

    public static void setNormalDistribution(double temperature) {
        Rectangle.normalDistribution = new NormalDistribution(0, temperature);
    }

    private boolean intersect(Rectangle rectangle) {
        return this.x < (rectangle.x + rectangle.width) && (this.x + this.width) > rectangle.x && this.y < (rectangle.y + rectangle.height) && (this.y + this.height) > rectangle.y;
    }

    private void calculateNextStepCoordinates() {
        x += (splittableRandom.nextDouble(1.0) - 0.5) * abs(ceil(normalDistribution.sample()));
        y += (splittableRandom.nextDouble(1.0) - 0.5) * abs(ceil(normalDistribution.sample()));
    }

    //Move one step and check for collision.
    //Teleport Rectangle on other side if they go through border
    //Return true if collision detected, false if not
    public boolean moveOneStepCollision(ContainerBox box) {
            calculateNextStepCoordinates();
            for (Rectangle rect : ElementsWorld.staticRectangles) {
                if (intersect(rect)) {
                    color = Color.RED;
                    return true;
                }
            }

        rectangleMinX = box.minX + 1;
        rectangleMinY = box.minY + 1;
        rectangleMaxX = box.maxX - height - 1;
        rectangleMaxY = box.maxY - width - 1;

            if (x < rectangleMinX){
                x = rectangleMaxX - (rectangleMinX - x);
            } else if (x > rectangleMaxX){
                x = rectangleMinX + (x - rectangleMaxX);
            }
            if (y < rectangleMinY) {
                y = rectangleMaxY - (rectangleMinY - y);
            } else if (y > rectangleMaxY) {
                y = rectangleMinY + (y - rectangleMaxY);
            }
        return false;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(color);
        graphics.fillRect((int) x, (int) y, (int) width, (int) height);
    }
    
    @Override
    public boolean equals(Object other){
        if (this == other) return true;
        if (!(other instanceof Rectangle)) return false;
        final Rectangle that = (Rectangle) other;
        return (this.x == that.x && this.x2 == that.x2 && this.y == that.y && this.y2 == that.y2);
    }
    @Override
    public int hashCode(){
        return (int)(this.width * 31 + this.height);
    }
}

