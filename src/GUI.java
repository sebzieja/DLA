import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.Hashtable;


public class GUI {
    public void guiStart() {

        JFrame frame = new JFrame("A World of Balls");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        frame.setContentPane(container);
        ElementsWorld elementsWorld = new ElementsWorld(800, 800);
        container.add(elementsWorld);
        JPanel controlls = new JPanel();
        controlls.setLayout(new BoxLayout(controlls, BoxLayout.Y_AXIS));

        JButton startButton = new JButton("Start");
        startButton.addActionListener(event -> {
            elementsWorld.gameStart();
        });
        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(event -> {
            elementsWorld.createRectangles(10, 10);
            elementsWorld.repaint();
        });
        JSlider temperatureSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, 1);
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10000, 1);
        Hashtable labelTableTemperature = new Hashtable();
        labelTableTemperature.put(1, new JLabel("Low"));
        labelTableTemperature.put(1000, new JLabel("High"));
        temperatureSlider.setLabelTable(labelTableTemperature);
        temperatureSlider.setPaintLabels(true);
        Hashtable labelTableSpeed = new Hashtable();
        labelTableSpeed.put(1, new JLabel("Fast"));
        labelTableSpeed.put(10000, new JLabel("Slow"));
        speedSlider.setLabelTable(labelTableSpeed);
        speedSlider.setPaintLabels(true);
        temperatureSlider.addChangeListener(event -> {
            Rectangle.setNormalDistribution((double) temperatureSlider.getValue() / 10);
        });

        speedSlider.addChangeListener(event -> {
            ElementsWorld.speed = speedSlider.getValue() / 100;
        });

        JSpinner numberOfRectangles = new JSpinner(new SpinnerNumberModel());
        numberOfRectangles.setValue(200);
        numberOfRectangles.addChangeListener(event -> {
            try {
                numberOfRectangles.commitEdit();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            elementsWorld.setHowManyRectangles((Integer) numberOfRectangles.getValue());
        });

        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        controlls.add(numberOfRectangles);
        controlls.add(new JLabel("Temperature"));
        controlls.add(temperatureSlider);
        controlls.add(new JLabel("Speed"));
        controlls.add(speedSlider);
        controlls.add(startButton);
        controlls.add(generateButton);
        container.add(controlls);
//        frame.getContentPane().add(BorderLayout.CENTER, new ElementsWorld(640, 480)); // BallWorld is a JPanel
//
//        frame.getContentPane().add(BorderLayout.SOUTH, slider1);
//        frame.getContentPane().add(BorderLayout.SOUTH, slider2);
//        frame.getContentPane().add(BorderLayout.SOUTH, button1);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();            // Preferred size of BallWorld
        frame.setVisible(true);  // Show it
    }

}
