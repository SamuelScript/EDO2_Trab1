package core;


import com.orsoncharts.Chart3D;
import com.orsoncharts.Chart3DFactory;
import com.orsoncharts.Chart3DPanel;
import com.orsoncharts.Colors;
import com.orsoncharts.data.xyz.XYZSeries;
import com.orsoncharts.data.xyz.XYZSeriesCollection;
import com.orsoncharts.graphics3d.Dimension3D;
import com.orsoncharts.graphics3d.ViewPoint3D;
import com.orsoncharts.label.StandardXYZLabelGenerator;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.renderer.xyz.ScatterXYZRenderer;

import javax.swing.*;

public class GChart3DVisualizer extends Visualizer {
    private final JPanel chartPanel;
    private final XYZSeries<String> series;
    private int drawCount = 0;
    private int drawMax;
    private final boolean animMode;
    private boolean checkDelta = false;

    GChart3DVisualizer(NumericData data, String name, int animMode) {
        super(data);
        this.animMode = animMode == 0;
        XYZSeriesCollection<String> dataset = new XYZSeriesCollection<>();
        XYZSeries<String> series = new XYZSeries<>("Loena nós merecemos 10!");
        dataset.add(series);
        this.series = series;

        Chart3D chart = Chart3DFactory.createScatterChart(name, "Criar este plot 3d foi muito difícil", dataset, "X", "Temperatura", "t");
        XYZPlot plot = (XYZPlot) chart.getPlot();

        plot.setDimensions(new Dimension3D(20.0, 10.0, 20.0));
        plot.setLegendLabelGenerator(new StandardXYZLabelGenerator(StandardXYZLabelGenerator.KEY_ONLY_TEMPLATE));
        ScatterXYZRenderer renderer = (ScatterXYZRenderer) plot.getRenderer();
        renderer.setSize(0.05);

        renderer.setColors(Colors.createIntenseColors());
        chartPanel = new Chart3DPanel(chart);

        chart.setViewPoint(new ViewPoint3D(0.6146828755786389, 0.8287338719285909, 70, -Math.PI/3));
    }

    @Override
    void draw(double[] temperature, double time, boolean drawRequest) {
        if(checkDelta) {
            drawMax = (int) ((data.time_max / time) / 100);
            checkDelta = false;
        }
        if(time == 0) {
            checkDelta = true;
            for(int i = 0; i < temperature.length; i++) series.add(i * data.deltaX, temperature[i], time);
        }
        if(animMode) {
            if(drawRequest) {
                if(drawCount == 20) {
                    for(int i = 0; i < temperature.length; i++) series.add(i * data.deltaX, temperature[i], time);
                    drawCount = 0;
                } else drawCount++;
            }
        } else {
            if(!drawRequest) {
                if(drawCount == drawMax) {
                    for(int i = 0; i < temperature.length; i++) series.add(i * data.deltaX, temperature[i], time);
                    drawCount = 0;
                } else drawCount++;
            }
        }
    }

    @Override
    JComponent getView() {
        return chartPanel;
    }
}
