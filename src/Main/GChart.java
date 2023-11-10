package Main;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class GChart extends JFrame {
    private XYSeriesCollection dataset;
    private XYSeries series[];
    private ChartPanel chartPanel;
    private JFreeChart chart;

    public void setLabel(int serie, String label) {
        series[serie].setKey(label);
    }

    public void setVisible(int serie, boolean visible) { chart.getXYPlot().getRenderer().setSeriesVisible(serie, visible); }

    public void setColor(int serie, Paint paint) { chart.getXYPlot().getRenderer().setSeriesPaint(serie, paint); }

    public void savePNG(String path, int width, int height) throws IOException { ChartUtilities.saveChartAsPNG(new File(path), chart, width, height); }

    public void setData(int serie, Number[] x, Number[] y) { for(int i=0; i<x.length; i++) series[serie].add(x[i], y[i]); }

    public void addData(int serie, Number x, Number y) { series[serie].add(x, y); }


    public GChart(String title, int series) {
        super(title);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        dataset = new XYSeriesCollection();
        this.series = new XYSeries[series];
        for(int i=0; i<series; i++) {
            this.series[i] = new XYSeries(""+i);
            dataset.addSeries(this.series[i]);
        }
        chart = ChartFactory.createXYLineChart(title, "", "", dataset, PlotOrientation.VERTICAL, true, true, false);
        chartPanel = new ChartPanel(chart) {
            @Override
            public void mousePressed(MouseEvent e) {
                int mods = e.getModifiers();
                int panMask = MouseEvent.BUTTON1_MASK;
                if (mods == panMask+MouseEvent.SHIFT_MASK) panMask = 255;
                try {
                    Field mask = ChartPanel.class.getDeclaredField("panMask");
                    mask.setAccessible(true);
                    mask.set(this, panMask);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                super.mousePressed(e);
            }
        };
        setContentPane(chartPanel);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeZeroBaselineVisible(true);
        plot.setDomainZeroBaselineVisible(true);
        plot.setRangePannable(true);
        plot.setDomainPannable(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setMouseZoomable(true);
    }
}
