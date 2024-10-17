package disaster_visualizer.components;

import javax.swing.JFrame;

public class MainWindow extends JFrame {

    int width = 800;
    int height = 800;

    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setVisible(true);
        setLocationRelativeTo(null);
    }

}
