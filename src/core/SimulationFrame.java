package core;

import javax.swing.*;
import java.awt.*;
import core.VisualizerFactory.*;

class SimulationFrame extends JFrame {
    private final Visualizer[] visualizers;
    SimulationFrame(NumericData data, String method, VISUALIZERS[] visualizer, double[][] extraVisualizerData) {
        super(method);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTabbedPane contentPane = new JTabbedPane();
        setContentPane(contentPane);
        visualizers = new Visualizer[visualizer.length];
        for(int i = 0; i < visualizer.length; i++) {
            visualizers[i] = VisualizerFactory.createVisualizer(data, extraVisualizerData[i], visualizer[i]);
            contentPane.add(visualizers[i].getView());
            contentPane.setTitleAt(i, visualizer[i].visualizer_name);
        }
    }
    void draw_all(double[] Ts,double t) {
        for(Visualizer visualizer : visualizers) visualizer.draw(Ts, t);
    }
}
