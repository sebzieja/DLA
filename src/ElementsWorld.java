import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;

public class ElementsWorld extends JPanel {
    private static final int UPDATE_RATE = 30;  // Frames per second (fps)

    private Rectangle rectangle;         // A single bouncing Rectangle's instance
    private ContainerBox box;  // The container rectangular box

    private DrawCanvas canvas; // Custom canvas for drawing the box/rectangle
    private int canvasWidth;
    private int canvasHeight;
    private static int howManyRectangles = 50;
    private static Rectangle[] rectangles = new Rectangle[howManyRectangles];
    private static ArrayList<Rectangle> staticRectangles = new ArrayList<>();



    public ElementsWorld(int width, int height) {

        canvasWidth = width;
        canvasHeight = height;

        // Init the rectangle at a random location (inside the box) and moveAngle
        Random rand = new Random();
        int widthRectangle = 20;
        int heightRectangle = 20;
        int x = 0;
        int y = 0;

        int speed = 20;
        int angleInDegree = rand.nextInt(360);
        for (int i = 0; i < howManyRectangles; i++) {
            rectangles[i] = new Rectangle(widthRectangle, heightRectangle, canvasWidth, canvasHeight, speed, Color.BLUE);
        }
        staticRectangles.add(new Rectangle(canvasWidth / 2, canvasHeight / 2, 20, 20, 0, 0, Color.WHITE));

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
        gameStart();
    }


    public void gameStart() {
        // Run the game logic in its own thread.
        Thread gameThread = new Thread(() -> {
            while (true) {
                long beginTimeMillis, timeTakenMillis, timeLeftMillis;
                beginTimeMillis = System.currentTimeMillis();
                // Execute one time-step for the game
                gameUpdate();
                // Refresh the display
                repaint();
                // Provide the necessary delay to meet the target rate
                timeTakenMillis = System.currentTimeMillis() - beginTimeMillis;
                timeLeftMillis = 1000L / UPDATE_RATE - timeTakenMillis;
                if (timeLeftMillis < 5) timeLeftMillis = 5; // Set a minimum
                // Delay and give other thread a chance
                try {
                    Thread.sleep(timeLeftMillis);
                } catch (InterruptedException ex) {
                }
            }
        });
        gameThread.start();  // Invoke GaemThread.run()
    }


    public void gameUpdate() {
        for (int i = 0; i < howManyRectangles; i++) {
            rectangles[i].moveOneStepCollision(box);
        }
    }

    class DrawCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);    // Paint background
            // Draw the box and the rectangle
            box.draw(graphics);
            for (int i = 0; i < howManyRectangles; i++) {
                rectangles[i].draw(graphics);
            }
//            rectangle.draw(graphics);
//            // Display rectangle's information
//            graphics.setColor(Color.WHITE);
//            graphics.setFont(new Font("Courier New", Font.PLAIN, 12));
//            graphics.drawString("Rectangle " + rectangle.toString(), 20, 30);
        }

        @Override
        public Dimension getPreferredSize() {
            return (new Dimension(canvasWidth, canvasHeight));
        }
    }
}