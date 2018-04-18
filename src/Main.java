import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        // Run UI in the Event Dispatcher Thread (EDT), instead of Main thread
        GUI gui = new GUI();
        javax.swing.SwingUtilities.invokeLater(gui::guiStart);
    }
}