package core;

import javax.swing.*;
import java.awt.*;
import core.MethodFactory.*;
import core.VisualizerFactory.*;


public class SimulationFrame extends JFrame {
    private boolean running = false;
    public SimulationFrame(NumericData data, METHODS method, VISUALIZERS[] visualizer, double[] extraMethodData, double[][] extraVisualizerData) {
        super(method.method_name);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTabbedPane contentPane = new JTabbedPane();
        setContentPane(contentPane);
        NumericMethod simulation = MethodFactory.createMethod(data, extraMethodData, method);
        Visualizer[] visualizers = new Visualizer[visualizer.length];
        for(int i = 0; i < visualizer.length; i++) {
            visualizers[i] = VisualizerFactory.createVisualizer(data, extraVisualizerData[i], visualizer[i]);
            contentPane.add(visualizers[i].getView());
            contentPane.setTitleAt(i, visualizer[i].visualizer_name);
        }

        assert simulation != null;
        double deltaT = simulation.getDeltaT();
        for(double t = deltaT; t < data.time_max; t += deltaT) {
            simulation.step(t);
            for(int j = 0; j < visualizers.length; j++) visualizers[j].draw(simulation.getTs(), t);
        }
    }
}
