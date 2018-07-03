import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class ElementsWorld extends JPanel {
    public static long speed = 1 / 100;
    public Thread gameThread;

    private final ContainerBox box;
    private final DrawCanvas canvas;
    private int canvasWidth;
    private int canvasHeight;
    public static ArrayList<Rectangle> staticRectangles = new ArrayList<>();
    private static ArrayList<Rectangle> rectangles = new ArrayList<>();
    private int howManyRectangles = 200;
    private ListIterator<Rectangle> iter;
    private Rectangle item;
    private long startMillis;
    private long endMillis;
    private int numberOfStaticRectangles = 1;
    private final ArrayList<Long> averageTime = new ArrayList<>();


    public ElementsWorld(int width, int height) {

        canvasWidth = width;
        canvasHeight = height;
        box = new ContainerBox(0, 0, canvasWidth, canvasHeight, Color.BLACK, Color.WHITE);
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
    }

    public void setHowManyRectangles(int howManyRectangles) {
        this.howManyRectangles = howManyRectangles;
    }

    public void createRectangles(int width, int height) {
        for (int i = 0; i < howManyRectangles; i++) {
            rectangles.add(createRectangle(width, height, canvasWidth, canvasHeight));
        }
        Rectangle stopped = new Rectangle((canvasWidth / 2) - 10, (canvasHeight / 2) - 10, 20, 20);
        staticRectangles.add(stopped);
    }
    
    public void createRectangleOnBorder(int width, int height){
        for (int i = 0; i < howManyRectangles; i++) {
            rectangles.add(Rectangle.createRectangleOnBorder(width, height, canvasWidth, canvasHeight, Color.BLUE));
        }
    }

    private Rectangle createRectangle(int widthRectangle, int heightRectangle, int canvasWidth, int canvasHeight) {
        return new Rectangle(widthRectangle, heightRectangle, canvasWidth, canvasHeight, Color.BLUE);
    }

    public void gameStart() {
        gameThread = new Thread(() -> {
            while (!gameThread.isInterrupted()) {
                if(rectangles.size() == 0) {
                    return;
                }
                gameUpdate();
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

    private void gameUpdate() {

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
            super.paintComponent(graphics);
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
}