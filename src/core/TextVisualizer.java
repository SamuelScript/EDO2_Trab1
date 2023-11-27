package core;

import javax.swing.*;

public class TextVisualizer extends Visualizer {
    private JScrollPane view;
    private JTextArea text;
    TextVisualizer(NumericData data) {
        super(data);
        text = new JTextArea();
        view = new JScrollPane(text);
        view.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        view.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    }

    @Override
    void draw(double[] temperature, double time) {
        text.append(String.format("t = %.4f, ", time));
        for(int i = 0; i < temperature.length; i++) text.append(String.format("%.4f, ", temperature[i]));
        text.append("\n");
    }

    @Override
    JComponent getView() {
        return view;
    }
}
