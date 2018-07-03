import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;

class GUICardLayout {
    public JPanel panel1;
    private JSpinner numberOfRectangles;
    private JSlider temperatureSlider;
    private JSlider speedSlider;
    public JButton stopButton;
    private JButton generateMoreButton;
    public JPanel controlPanel;
    private JButton startButton;
    private JButton generateButton;
    public JPanel controlPanelRun;
    private JSlider speedSliderControlPanel;
    private JSlider temperatureSliderControlPanel;
    private JSpinner heightSpinnerControlPanel;
    private JSpinner widthSpinnerControlPanel;
    private JSpinner heightSpinnerControlRunPanel;
    private JSpinner widthSpinnerControlRunPanel;
    private JSpinner numberOfRectanglesControlRunPanel;
    private JButton createNewButton;
    private final CardLayout cardLayout = (CardLayout)panel1.getLayout();


    public GUICardLayout() {
        startButton.addActionListener(event -> {
            GUI.elementsWorld.gameStart();
            cardLayout.show(panel1, "Card3");
            GUI.elementsWorld.setHowManyRectangles((Integer) numberOfRectanglesControlRunPanel.getValue());
            GUI.frame.revalidate(); // to invoke the layout manager
            GUI.frame.repaint();
        });
        generateButton.addActionListener(event -> {
            GUI.elementsWorld.createRectangles((Integer) widthSpinnerControlPanel.getValue(), (Integer) heightSpinnerControlPanel.getValue());
            GUI.frame.revalidate();
            GUI.elementsWorld.repaint();
            GUI.frame.repaint();
        });
        createNewButton.addActionListener(event ->{
            GUI.elementsWorld.gameThread.interrupt();
            cardLayout.show(panel1, "Card2");
//            GUI.elementsWorld = new ElementsWorld(GUI.elementsWorld.getCanvasWidth(), GUI.elementsWorld.getCanvasHeight());
            ElementsWorld.setStaticRectangles(new ArrayList<>());
            ElementsWorld.setRectangles(new ArrayList<>());
            GUI.elementsWorld.setHowManyRectangles((Integer) numberOfRectangles.getValue());
            GUI.frame.revalidate();
            GUI.frame.repaint();
            GUI.elementsWorld.repaint();
        });
        stopButton.addActionListener(event -> { 
            if(!GUI.elementsWorld.gameThread.isInterrupted()) GUI.elementsWorld.gameStart();
            else GUI.elementsWorld.gameThread.stop();
        });
        generateMoreButton.addActionListener(event -> {
            if(GUI.elementsWorld.gameThread.isInterrupted()){
                GUI.elementsWorld.createRectangleOnBorder((Integer) widthSpinnerControlRunPanel.getValue(), (Integer) heightSpinnerControlRunPanel.getValue());
                GUI.frame.repaint();
            }
            else{
                GUI.elementsWorld.gameThread.stop();
                GUI.elementsWorld.createRectangleOnBorder((Integer) widthSpinnerControlRunPanel.getValue(), (Integer) heightSpinnerControlRunPanel.getValue());
                GUI.elementsWorld.gameStart();
            }
        });
        heightSpinnerControlPanel.setValue(10);
        heightSpinnerControlPanel.addChangeListener(event -> {
            try {
                heightSpinnerControlPanel.commitEdit();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        widthSpinnerControlPanel.setValue(10);
        widthSpinnerControlPanel.addChangeListener(event -> {
            try {
                widthSpinnerControlPanel.commitEdit();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        heightSpinnerControlRunPanel.setValue(10);
        heightSpinnerControlRunPanel.addChangeListener(event -> {
            try {
                heightSpinnerControlRunPanel.commitEdit();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        widthSpinnerControlRunPanel.setValue(10);
        widthSpinnerControlRunPanel.addChangeListener(event -> {
            try {
                widthSpinnerControlRunPanel.commitEdit();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        Hashtable labelTableTemperature = new Hashtable();
        labelTableTemperature.put(1, new JLabel("Low"));
        labelTableTemperature.put(20, new JLabel("High"));
        temperatureSlider.setLabelTable(labelTableTemperature);
        temperatureSlider.setPaintLabels(true);
        temperatureSliderControlPanel.setLabelTable(labelTableTemperature);
        temperatureSliderControlPanel.setPaintLabels(true);
        
        Hashtable labelTableSpeed = new Hashtable();
        labelTableSpeed.put(1, new JLabel("Fast"));
        labelTableSpeed.put(10000, new JLabel("Slow"));
        speedSlider.setLabelTable(labelTableSpeed);
        speedSlider.setPaintLabels(true);
        speedSliderControlPanel.setLabelTable(labelTableSpeed);
        speedSliderControlPanel.setPaintLabels(true);
        temperatureSlider.addChangeListener(event -> Rectangle.setNormalDistribution((double) temperatureSlider.getValue() / 10));

        speedSlider.addChangeListener(event -> ElementsWorld.speed = speedSlider.getValue() / 100);
        temperatureSliderControlPanel.addChangeListener(event -> {
            Rectangle.setNormalDistribution((double) temperatureSlider.getValue() / 10);
            temperatureSlider.setValue(temperatureSliderControlPanel.getValue());
        });

        speedSliderControlPanel.addChangeListener(event -> ElementsWorld.speed = speedSlider.getValue() / 100);
        numberOfRectangles.setValue(200);
        numberOfRectangles.addChangeListener(event -> {
            try {
                numberOfRectangles.commitEdit();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            GUI.elementsWorld.setHowManyRectangles((Integer) numberOfRectangles.getValue());
        });
        numberOfRectanglesControlRunPanel.setValue(10);
        numberOfRectanglesControlRunPanel.addChangeListener(event -> {
            try {
                numberOfRectanglesControlRunPanel.commitEdit();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            GUI.elementsWorld.setHowManyRectangles((Integer) numberOfRectanglesControlRunPanel.getValue());
        });
    }
    
}
