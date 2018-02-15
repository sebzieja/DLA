import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class ElementsWorld extends JPanel {
    private static final int UPDATE_RATE = 30;  // Frames per second (fps)

    private Rectangle rectangle;         // A single bouncing Rectangle's instance
    private ContainerBox box;  // The container rectangular box

    private DrawCanvas canvas; // Custom canvas for drawing the box/rectangle
    private int canvasWidth;
    private int canvasHeight;

    public ElementsWorld(int width, int height) {

        canvasWidth = width;
        canvasHeight = height;

        // Init the rectangle at a random location (inside the box) and moveAngle
        Random rand = new Random();
        int widthRectangle = 100;
        int heightRectangle = 100;
        int x = 0;
        int y = 0;
//        int x = rand.nextInt(canvasWidth - radius * 2 - 20) + radius + 10;
//        int y = rand.nextInt(canvasHeight - radius * 2 - 20) + radius + 10;
        int speed = 5;
        int angleInDegree = rand.nextInt(360);
        rectangle = new Rectangle(x, y, widthRectangle, heightRectangle, speed, angleInDegree, Color.BLUE);

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

    /**
     * Start the rectangle bouncing.
     */
    public void gameStart() {
        // Run the game logic in its own thread.
        Thread gameThread = new Thread(() -> {
            while (true) {
                // Execute one time-step for the game
                gameUpdate();
                // Refresh the display
                repaint();
                // Delay and give other thread a chance
                try {
                    Thread.sleep(1000 / UPDATE_RATE);
                } catch (InterruptedException ex) {
                }
            }
        });
        gameThread.start();  // Invoke GaemThread.run()
    }


    public void gameUpdate() {
        rectangle.moveOneStepCollision(box);
    }

    /**
     * The custom drawing panel for the bouncing rectangle (inner class).
     */
    class DrawCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);    // Paint background
            // Draw the box and the rectangle
            box.draw(g);
            rectangle.draw(g);
//            // Display rectangle's information
//            g.setColor(Color.WHITE);
//            g.setFont(new Font("Courier New", Font.PLAIN, 12));
//            g.drawString("Rectangle " + rectangle.toString(), 20, 30);
        }

        @Override
        public Dimension getPreferredSize() {
            return (new Dimension(canvasWidth, canvasHeight));
        }
    }
}