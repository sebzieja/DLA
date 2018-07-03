import javax.swing.*;
import java.awt.*;


class GUI {
    static final ElementsWorld elementsWorld = new ElementsWorld(800, 800);
    static JFrame frame;
    private static JPanel container;
    private static JPanel controlPanel;

    public void guiStart() {

        frame = new JFrame("Diffusion-Limited Aggregation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        frame.setContentPane(container);
        container.add(elementsWorld);
        GUICardLayout guiCardLayout = new GUICardLayout();
        controlPanel = guiCardLayout.panel1;
        CardLayout cardLayout = (CardLayout)controlPanel.getLayout();
        container.add(controlPanel);
        cardLayout.show(controlPanel, "Card2");
        frame.pack();
        frame.setVisible(true);
    }

}
