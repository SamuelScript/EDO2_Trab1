package core;

import ui.MainFrame;

import javax.swing.*;

public class Main {
    //length_x = 50;
    //a = 1;
    //t_max = 70;
    //ti = 20;
    //tce = 0;
    //tcd = 0;
    //int s_partition = 1000;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            (new MainFrame()).setVisible(true);
        } catch (Exception e) {
            System.out.println("Exceção em Main: " + e.getLocalizedMessage());
        }
    }
}