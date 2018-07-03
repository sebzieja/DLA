import java.awt.*;

class ContainerBox {
    int minX, maxX, minY, maxY;
    private final Color colorFilled;
    private final Color colorBorder;


    public ContainerBox(int x, int y, int width, int height, Color colorFilled, Color colorBorder) {
        minX = x;
        minY = y;
        maxX = x + width;
        maxY = y + height;
        this.colorFilled = colorFilled;
        this.colorBorder = colorBorder;
    }

    public void setter(int x, int y, int width, int height) {
        minX = x;
        minY = y;
        maxX = x + width - 1;
        maxY = y + height - 1;
    }

    public void draw(Graphics graphics) {
        graphics.setColor(colorFilled);
        graphics.fillRect(minX, minY, maxX - minX, maxY - minY);
        graphics.setColor(colorBorder);
        graphics.drawRect(minX, minY, maxX - minX, maxY - minY);
    }
}


