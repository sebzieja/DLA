import org.apache.commons.math3.distribution.NormalDistribution;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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
    private int howManyRectangles = 200;

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
        staticRectangles = new ArrayList<>();
        rectangles = new ArrayList<>();
        for (int i = 0; i < howManyRectangles; i++) {
            rectangles.add(createRectangle(width, height, canvasWidth, canvasHeight, Color.BLUE));
        }
        Rectangle stopped = new Rectangle((canvasWidth / 2) - 10, (canvasHeight / 2) - 10, 20, 20);
        stopped.stopped = true;
        rectangles.add(stopped);
        staticRectangles.add(stopped);
    }

    public Rectangle createRectangle(int widthRectangle, int heightRectangle, int canvasWidth, int canvasHeight, Color color) {
        return new Rectangle(widthRectangle, heightRectangle, canvasWidth, canvasHeight, color);
    }

    public void gameStart() {
        // Run the game logic in its own thread.
        Thread gameThread = new Thread(() -> {
            while (true) {

                for (int i = 0; i < 100; i++) {
                    gameUpdate();
                }
                repaint();
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();

                }
            }
        });
        gameThread.start();  // Invoke GameThread.run()
    }


    public void gameUpdate() {
        for (Rectangle rectangle : rectangles) {
            rectangle.moveOneStepCollision(box);
        }
    }


    class DrawCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);    // Paint background
            // Draw the box and the rectangle
            box.draw(graphics);
            for (Rectangle rectangle : rectangles) {
                rectangle.draw(graphics);
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
//        rectangle.calculateNextStepCooridanates();
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
//        float rectangleMinX = box.minX + 1;
//        float rectangleMinY = box.minY + 1;
//        float rectangleMaxX = box.maxX - rectangle.height - 1;
//        float rectangleMaxY = box.maxY - rectangle.width - 1;
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