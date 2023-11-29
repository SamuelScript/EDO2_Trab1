package Main;

import core.MethodFactory;
import core.NumericData;
import core.VisualizerFactory;
import ui.MainFrame;

import javax.swing.*;

public class Main {
    //static double length_x = 50;
    //static double a = 1;
    //static double t_max = 70;
    //static double ti = 20;
    //static double tce = 0;
    //static double tcd = 0;
    //static final int s_partition = 1000;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            (new MainFrame()).setVisible(true);
        } catch (Exception e) {
            System.out.println("Exceção em Main: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}