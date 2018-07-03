import java.awt.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.math3.distribution.*;


import static org.apache.commons.math3.util.FastMath.abs;
import static org.apache.commons.math3.util.FastMath.ceil;


public class Rectangle {
    private int row1, row2, column1, column2, firstCorner, secondCorner;
    public static NormalDistribution normalDistribution = new NormalDistribution(0, 1);
    public double x, y, x2, y2, width, height, rectangleMinX, rectangleMinY, rectangleMaxX, rectangleMaxY;
    public boolean stopped;
    public Color color = Color.RED;
    public LinkedHashSet<Integer> matrixIndex = new LinkedHashSet<>();
    private static SplittableRandom splittableRandom = new SplittableRandom();
    private ListIterator<Rectangle> staticRectangleIterator;

    /*
     * Contructor
     *
     * */
    private Rectangle() {
    }

    //    public createStaticRectangle();
    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.x2 = x + width;
        this.y2 = y + height;
    }


    /*Constructor with random placement on the borders*/
    public Rectangle(double width, double height, double canvasWidth, double canvasHeight, Color color) {
//        double p = (double) (ThreadLocalRandom.current().nextDouble(0, canvasWidth * 2 + canvasHeight * 2));
        this.x = (double) (ThreadLocalRandom.current().nextDouble(20, canvasWidth - 20));
        this.y = (double) (ThreadLocalRandom.current().nextDouble(20, canvasHeight - 20));
        this.x2 = this.x + width;
        this.y2 = this.y + height;
        this.width = width;
        this.height = height;
        this.color = color;
        this.calculateMatrixIndex(canvasWidth, canvasHeight);
    }
    
    public void calculateMatrixIndex(double canvasWidth, double canvasHeight){
        matrixIndex.clear();
        row1 = (int)(x/(canvasWidth/(double)ElementsWorld.howManyPiecesInRow));
        column1 = (int)(y/(canvasHeight/(double)ElementsWorld.howManyPiecesInRow));
        row2 = (int)(x2/(canvasWidth/(double)ElementsWorld.howManyPiecesInRow));
        column2 = (int)(y2/(canvasHeight/(double)ElementsWorld.howManyPiecesInRow));
        firstCorner = row1 * ElementsWorld.howManyPiecesInRow + column1;
        secondCorner = row2 * ElementsWorld.howManyPiecesInRow + column2;
        if(secondCorner>=81 || firstCorner>=81){
            System.out.println("why not?");
        }
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
    public static Rectangle createRectangleOnBorder(double width, double height, double canvasWidth, double canvasHeight, Color color) {
        Rectangle rectangle = new Rectangle();
        double p = (double) (ThreadLocalRandom.current().nextDouble(0, canvasWidth * 2 + canvasHeight * 2));
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

    public void calculateNextStepCoordinates(int canvasWidth, int canvasHeight) {
        x += (splittableRandom.nextDouble(1.0) - 0.5) * abs(ceil(normalDistribution.sample()));
        y += (splittableRandom.nextDouble(1.0) - 0.5) * abs(ceil(normalDistribution.sample()));
        x2 = x + width;
        y2 = y + height;
    }

    //TODO Collision Detection
    /*Move one move, check for collision and react if collision occurs*/
    /*Return true if collision detected*/
    public boolean moveOneStepCollision(ContainerBox box, ArrayList<LinkedList<Rectangle>> listOfMatrix, int canvasWidth, int canvasHeight) {
        // Get the rectangle's bounds, offset by the radius of the rectangle

        // Calculate the rectangle's new position
        calculateNextStepCoordinates(canvasWidth, canvasHeight);
        //Calculate intersection with every staticRectangle
//        for (Rectangle rect : ElementsWorld.staticRectangles) {
//            if (intersect(rect)) {
//                color = Color.RED;
//                stopped = true;
//                return true;
//            }
//        }
        //Calculate intersection with staticRectangle in the same matrix

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
//        return false;
//    }

        // Check if the rectangle moves over the bounds. If so, bounce from bound
        if (x < rectangleMinX) {
            x = rectangleMinX+1;     // Re-position the rectangle at the edge
            x2 = x+width;
        } else if (x >= rectangleMaxX) {
            x = rectangleMaxX-1;
            x2 = x+width;
        }
        // May cross both x and y bounds
        if (y < rectangleMinY) {
            y = rectangleMinY+1;
            y2 = y + height;
        } else if (y > rectangleMaxY) {
            y = rectangleMaxY-1;
            y2 = y + height;
        }
        calculateMatrixIndex(canvasWidth, canvasHeight);

        for(int i=0; i<matrixIndex.size(); ++i){
            staticRectangleIterator = listOfMatrix.get((Integer) matrixIndex.toArray()[i]).listIterator();
            while(staticRectangleIterator.hasNext()){
                if(intersect(staticRectangleIterator.next())){
                    color = Color.RED;
                    stopped = true;
                    return true;
                }
            }
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

