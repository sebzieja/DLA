import javax.smartcardio.Card;
import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.Hashtable;


public class GUI {
    static ElementsWorld elementsWorld = new ElementsWorld(800, 800);
    static JFrame frame;
    static JPanel container;
    static JPanel controlPanel;
    static JPanel controlPanelRunning;
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
        guiCardLayout.stopButton.addActionListener(event -> {
            
        });
        container.add(controlPanel);
        cardLayout.show(controlPanel, "Card2");

//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setVisible(true);
    }

}
