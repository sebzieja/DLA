import org.apache.commons.math3.distribution.NormalDistribution;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class ElementsWorld extends JPanel {
    public static int temperature = 1;
    public static long speed = 1 / 100;
    private static final int UPDATE_RATE = 30;  // Frames per second (fps)

    private ContainerBox box;  // The container rectangular box
    private DrawCanvas canvas; // Custom canvas for drawing the box/rectangle

    private int canvasWidth;
    private int canvasHeight;
    public static ArrayList<Rectangle> staticRectangles = new ArrayList<>();
    public static ArrayList<Rectangle> rectangles = new ArrayList<>();
    public static ArrayList<LinkedList<Rectangle>> listOfMatrix;
    public ArrayList<LinkedList<Rectangle>> listOfMatrixStaticRectangles;
    static int howManyPiecesInRow = 9;
    private int howManyRectangles = 200;
    Thread gameThread;
    ListIterator<Rectangle> iter, paintIterator, matrixIterator;
    Rectangle item;
    long startMillis;
    long endMillis;
    int numberOfStaticRectangles = 1;
    ArrayList<Long> averageTime = new ArrayList<>();
    
    public void createMatrix(int howManyPiecesInRow){
        this.howManyPiecesInRow = howManyPiecesInRow;
        listOfMatrix = new ArrayList<>();
        for(int i=0; i<howManyPiecesInRow*howManyPiecesInRow; ++i){
            listOfMatrix.add(new LinkedList<Rectangle>());
        }
    }
    public void addRectangleToMatrix(Rectangle rectangle){
        
    }

    public ElementsWorld(int width, int height) {

        canvasWidth = width;
        canvasHeight = height;

        // Init the rectangle at a random location (inside the box) and moveAngle


        // Init the Container Box to fill the screen
        box = new ContainerBox(0, 0, canvasWidth, canvasHeight, Color.BLACK, Color.WHITE);

        // Init the custom drawing panel for drawing the game
        canvas = new DrawCanvas();
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);

        // Handling window resize.
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Component c = (Component) e.getSource();
                Dimension dim = c.getSize();
                canvasWidth = dim.width;
                canvasHeight = dim.height;
                // Adjust the bounds of the container to fill the window
                box.setter(0, 0, canvasWidth, canvasHeight);
            }
        });

        // Start the rectangle bouncing
//        gameStart();
    }

    public void setHowManyRectangles(int howManyRectangles) {
        this.howManyRectangles = howManyRectangles;
    }

    public void createRectangles(int width, int height) {
        createMatrix(howManyPiecesInRow);
        for (int i = 0; i < howManyRectangles; i++) {
            rectangles.add(createRectangle(width, height, canvasWidth, canvasHeight, Color.BLUE));
        }
        Rectangle stopped = new Rectangle((canvasWidth / 2) - 10, (canvasHeight / 2) - 10, 20, 20);
        stopped.calculateMatrixIndex(canvasWidth, canvasHeight);
        stopped.stopped = true;
//        rectangles.add(stopped);
        staticRectangles.add(stopped);
    }
    
    public void createRectangleOnBorder(int width, int height){
        for (int i = 0; i < howManyRectangles; i++) {
            rectangles.add(Rectangle.createRectangleOnBorder(width, height, canvasWidth, canvasHeight, Color.BLUE));
        }
    }

    public Rectangle createRectangle(int widthRectangle, int heightRectangle, int canvasWidth, int canvasHeight, Color color) {
        return new Rectangle(widthRectangle, heightRectangle, canvasWidth, canvasHeight, color);
    }

    public void gameStart() {
        // Run the game logic in its own thread.
        gameThread = new Thread(() -> {
            while (!gameThread.isInterrupted()) {
                if(rectangles.size() == 0) {
                    return;
                }
                for (int i = 0; i < 10; i++) {
                startMillis = System.nanoTime();

                    gameUpdate();

                endMillis = System.nanoTime();
                if ((staticRectangles.size() == numberOfStaticRectangles)) {
                    averageTime.add(endMillis - startMillis);
                } else {
                    System.out.println(numberOfStaticRectangles + "," + averageTime.stream().mapToDouble(val -> val).average().orElse(0.0));
                    numberOfStaticRectangles=staticRectangles.size();
                    averageTime.clear();
                    averageTime.add(endMillis - startMillis);
                }
                }
                repaint();
                if(speed != 1/100) {
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
        gameThread.start();
    }

// iterate every rectangle, check matrixIndex and iterate with static rectangles in that matrix[i]

    public void gameUpdate() {

        iter = rectangles.listIterator();
        while(iter.hasNext()){
            try{
                item = iter.next();
            } catch (Exception e){
                gameThread.interrupt();
            }
            if(item.moveOneStepCollision(box)){
                staticRectangles.add(item);
                iter.remove();
            }
        }
//        for (Rectangle rectangle : rectangles) {
//            rectangle.moveOneStepCollision(box);
//        }
//        iter = rectangles.listIterator();
//        while (iter.hasNext()) {
//            item = iter.next();
//            if (item.moveOneStepCollision(box, listOfMatrix)) {
//                staticRectangles.add(item);
//                iter.remove();
//            }
//            
//        }
    }
    public int getCanvasWidth() {
        return canvasWidth;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }
    public static void setStaticRectangles(ArrayList<Rectangle> staticRectangles) {
        ElementsWorld.staticRectangles = staticRectangles;
    }

    public static void setRectangles(ArrayList<Rectangle> rectangles) {
        ElementsWorld.rectangles = rectangles;
    }


    class DrawCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);    // Paint background
            // Draw the box and the rectangle
            box.draw(graphics);
            for(int i=0;i<rectangles.size();++i){
                rectangles.get(i).draw(graphics);
            }
            for(int i=0;i<staticRectangles.size();++i){
                staticRectangles.get(i).draw(graphics);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return (new Dimension(canvasWidth, canvasHeight));
        }
    }


//    public void moveOneStepCollision(ContainerBox box, Rectangle rectangle) {
//        // Get the rectangle's bounds, offset by the radius of the rectangle
//
//
//        // Calculate the rectangle's new position
//        rectangle.calculateNextStepCoordinates();
//        for (Rectangle rect : staticRectangles) {
//            if (!rectangle.stopped && rect.stopped && (rect != rectangle)) {
//                if (rectangle.intersect(rect)) {
//                    rectangle.speed = 0;
//                    rectangle.color = Color.RED;
//                    rectangle.stopped = true;
//                    staticRectangles.add(rectangle);
//                    return;
//                }
//            }
//        }
//
//        double rectangleMinX = box.minX + 1;
//        double rectangleMinY = box.minY + 1;
//        double rectangleMaxX = box.maxX - rectangle.height - 1;
//        double rectangleMaxY = box.maxY - rectangle.width - 1;
//
//        // Check if the rectangle moves over the bounds. If so, adjust the position and speed.
//        if (rectangle.x < rectangleMinX) {
//            rectangle.x = rectangleMinX;     // Re-position the rectangle at the edge
//        } else if (rectangle.x > rectangleMaxX) {
//            rectangle.x = rectangleMaxX;
//        }
//        // May cross both x and y bounds
//        if (rectangle.y < rectangleMinY) {
//            rectangle.y = rectangleMinY;
//        } else if (rectangle.y > rectangleMaxY) {
//            rectangle.y = rectangleMaxY;
//        }
//    }
}