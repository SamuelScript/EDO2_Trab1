package core;

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

public class GChart extends ChartPanel {
    private XYSeriesCollection dataset;
    private XYSeries series[];
    XYPlot plot;

    public void setLabel(int serie, String label) {
        series[serie].setKey(label);
    }

    public void setVisible(int serie, boolean visible) { plot.getRenderer().setSeriesVisible(serie, visible); }

    public void setColor(int serie, Paint paint) { plot.getRenderer().setSeriesPaint(serie, paint); }

    public void addData(int serie, double[] x, double[] y) { for(int i=0; i<x.length; i++) series[serie].add(x[i], y[i]); }

    public void addData(int serie, double x, double y) { series[serie].add(x, y); }
    public  void clear(int serie) { series[serie].clear(); }


    public GChart(String title, int series) {
        super(ChartFactory.createXYLineChart(title, "", "", new XYSeriesCollection(), PlotOrientation.VERTICAL, true, true, false));
        plot = (XYPlot) getChart().getPlot();
        dataset = (XYSeriesCollection) plot.getDataset();
        this.series = new XYSeries[series];
        for(int i=0; i<series; i++) {
            this.series[i] = new XYSeries(""+i);
            dataset.addSeries(this.series[i]);
        }

        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeZeroBaselineVisible(true);
        plot.setDomainZeroBaselineVisible(true);
        plot.setRangePannable(true);
        plot.setDomainPannable(true);
        setMouseWheelEnabled(true);
        setMouseZoomable(true);
    }
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
}
