import java.awt.*;
import java.util.LinkedHashSet;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.math3.distribution.*;


import static org.apache.commons.math3.util.FastMath.abs;
import static org.apache.commons.math3.util.FastMath.ceil;


public class Rectangle {
    private int row1, row2, column1, column2, firstCorner, secondCorner;
    public static NormalDistribution normalDistribution = new NormalDistribution(0, 1);
    public float x, y, x2, y2, width, height, rectangleMinX, rectangleMinY, rectangleMaxX, rectangleMaxY;
    public boolean stopped;
    public Color color = Color.RED;
    public LinkedHashSet<Integer> matrixIndex = new LinkedHashSet<>();
    private static SplittableRandom splittableRandom = new SplittableRandom();

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
        this.calculateMatrixIndex(canvasWidth, canvasHeight);
    }
    
    public void calculateMatrixIndex(float canvasWidth, float canvasHeight){
        int rowx1, rowx2, columny1, columny2, firstCorner, secondCorner;
        matrixIndex.clear();
        rowx1 = (int)(canvasWidth/ElementsWorld.howManyPiecesInRow/x);
        columny1 = (int)(canvasWidth/ElementsWorld.howManyPiecesInRow/y);
        rowx2 = (int)(canvasWidth/ElementsWorld.howManyPiecesInRow/x2);
        columny2 = (int)(canvasWidth/ElementsWorld.howManyPiecesInRow/y2);
        firstCorner = rowx1 * ElementsWorld.howManyPiecesInRow + columny1;
        secondCorner = rowx2 * ElementsWorld.howManyPiecesInRow + columny2;
        if(secondCorner-firstCorner > firstCorner + ElementsWorld.howManyPiecesInRow){
            matrixIndex.add(firstCorner);
            matrixIndex.add(firstCorner+1);
            matrixIndex.add(secondCorner);
            matrixIndex.add(secondCorner-1);
        }
        else{
            matrixIndex.add(firstCorner);
            matrixIndex.add(secondCorner);
        }
    }
    public void checkIfInTheSameMatrixIndex(double canvasWidth, double canvasHeight){
        //TODO
    }

    public static void setNormalDistribution(double temperature) {
        Rectangle.normalDistribution = new NormalDistribution(0, temperature);
    }
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
        x += (splittableRandom.nextDouble(1.0) - 0.5) * abs(ceil(normalDistribution.sample()));
        y += (splittableRandom.nextDouble(1.0) - 0.5) * abs(ceil(normalDistribution.sample()));
    }

    //TODO Collision Detection
    /*Move one move, check for collision and react if collision occurs*/
    /*Return true if collision detected*/
    public boolean moveOneStepCollision(ContainerBox box) {
        // Get the rectangle's bounds, offset by the radius of the rectangle

            // Calculate the rectangle's new position
            calculateNextStepCooridanates();
            for (Rectangle rect : ElementsWorld.staticRectangles) {
                if (intersect(rect)) {
                    color = Color.RED;
                    stopped = true;
//                        ElementsWorld.staticRectangles.add(this);
                    return true;
                }
            }

        rectangleMinX = box.minX + 1;
        rectangleMinY = box.minY + 1;
        rectangleMaxX = box.maxX - height - 1;
        rectangleMaxY = box.maxY - width - 1;

//            //Check if the rectangle moves over bounds. If so, teleport to other side.
//            if (x < rectangleMinX){
//                x = rectangleMaxX;
//            } else if (x > rectangleMaxX){
//                x = rectangleMinX;
//            }
//            if (y < rectangleMinY) {
//                y = rectangleMaxY;
//            } else if (y > rectangleMaxY) {
//                y = rectangleMinY;
//            }
            //Check if the rectangle moves over bounds. If so, teleport to other side.
            if (x < rectangleMinX){
                x = rectangleMaxX;
            } else if (x > rectangleMaxX){
                x = rectangleMinX;
            }
            if (y < rectangleMinY) {
                y = rectangleMaxY;
            } else if (y > rectangleMaxY) {
                y = rectangleMinY;
            }
        return false;
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

