package core;

import javax.swing.*;

abstract class Visualizer {
    protected NumericData data;
    Visualizer(NumericData data){
        this.data = data;
    }
    abstract void draw(double[] temperature, double time);
    abstract JComponent getView();
}
