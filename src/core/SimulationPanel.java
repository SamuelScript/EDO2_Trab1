package core;

import core.MethodFactory.METHODS;
import core.VisualizerFactory.VISUALIZERS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationPanel extends JPanel {
    enum STATES {
        READY("Pronto"), RUNNING("Rodando"), PAUSED("Pausado"), FINISHED("Terminou"), ABORTED("Abortado"), ERRORED("Falhou");
        String label;
        STATES(String label) {
            this.label = label;
        }
    }
    private JButton playpause, stop;
    private JLabel lblStatus = new JLabel(STATES.READY.label);
    private STATES status = STATES.READY;
    private SimulationFrame simulation;
    private ActionListener playpauseListener, stopListener;

    public ActionListener getPlaypauseListener() {
        return playpauseListener;
    }

    public ActionListener getStopListener() {
        return stopListener;
    }

    public boolean isRunning() {
        return status == STATES.RUNNING;
    }

    public void tick() {
        //System.out.println("tick tack " + simulation.getTitle());
    }

    public SimulationPanel(NumericData data, METHODS method, VISUALIZERS[] visualizer, double[] extraMethodData, double[] extraVisualizerData) {
        super(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.ipady = 20;
        add(new JLabel(method.method_name), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0;
        constraints.ipadx = 70;
        add(lblStatus, constraints);

        playpause = new JButton("Iniciar");
        stop = new JButton("Parar");
        stop.setEnabled(false);

        constraints.insets = new Insets(10, 20, 10, 10);
        constraints.gridx = 2;
        add(playpause,constraints);

        constraints.insets = new Insets(10, 10, 10, 20);
        constraints.gridx = 3;
        add(stop,constraints);

        simulation = new SimulationFrame(data, method, visualizer, extraMethodData, extraVisualizerData);

        playpauseListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (status) {
                    case READY:
                        playpause.setText("Pausar");
                        stop.setEnabled(true);

                        status = STATES.RUNNING;
                        break;
                    case RUNNING:
                        playpause.setText("Continuar");

                        status = STATES.PAUSED;
                        break;
                    case PAUSED:
                        playpause.setText("Pausar");

                        status = STATES.RUNNING;
                        break;
                }
            }
        };

        stopListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (status) {
                    case RUNNING:
                    case PAUSED:
                        playpause.setEnabled(false);
                        stop.setEnabled(false);
                        status = STATES.ABORTED;
                        break;
                }
            }
        };

        playpause.addActionListener(playpauseListener);
        stop.addActionListener(stopListener);
    }
}
