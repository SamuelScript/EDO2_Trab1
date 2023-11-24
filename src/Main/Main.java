package Main;

import ui.MainFrame;

import javax.swing.*;

public class Main {
    //static double length_x = 50;
    //static double length_xf = length_x*0.5;
    //static double a = 1;
    //static double t_max = 70;
    //static double t_int = t_max*0.5;
    //static double Ca = 20;
    //static double Cb = 20;
    //static double Cc = 0;
    //static final int s_partition = 1000;
    //static final int draw = 1;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            (new MainFrame()).setVisible(true);
        } catch (Exception e) {
            System.out.println("Exceção em Main: " + e.getLocalizedMessage());
        }
    }
}