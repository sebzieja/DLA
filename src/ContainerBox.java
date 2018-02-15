import java.awt.*;

public class ContainerBox {
    int minX, maxX, minY, maxY;
    private Color colorFilled, colorBorder;
    private static final Color DEFAULT_COLOR_FILL = Color.RED;
    private static final Color DEFAULT_COLOR_BORDER = Color.YELLOW;


    /* Contructor */
    public ContainerBox(int x, int y, int width, int height, Color colorFilled, Color colorBorder) {
        minX = x;
        minY = y;
        maxX = x + width - 1;
        maxY = y + height - 1;
        this.colorFilled = colorFilled;
        this.colorBorder = colorBorder;
    }

    /* Constructor with default color */
    public ContainerBox(int x, int y, int width, int height) {
        this(x, y, width, height, DEFAULT_COLOR_FILL, DEFAULT_COLOR_BORDER);
    }

    public void setter(int x, int y, int width, int height) {
        minX = x;
        minY = y;
        maxX = x + width - 1;
        maxY = y + height - 1;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(colorFilled);
        graphics.fillRect(minX, minY, maxX - minX - 1, maxY - minY - 1);
        graphics.setColor(colorBorder);
        graphics.drawRect(minX, minY, maxX - minX - 1, maxY - minY - 1);
    }
}


