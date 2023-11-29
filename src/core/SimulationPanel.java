package core;

import core.MethodFactory.METHODS;
import core.VisualizerFactory.VISUALIZERS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;

public class SimulationPanel extends JPanel {
    enum STATES {
        READY("Pronto"), RUNNING("Rodando"), PAUSED("Pausado"), FINISHED("Terminou"), ABORTED("Abortado"), ERRORED("Falhou");
        final String label;
        STATES(String label) {
            this.label = label;
        }
    }
    private final JButton playpause;
    private final JButton stop;
    private STATES status = STATES.READY;
    JLabel lblStatus;
    private final NumericMethod simulation;
    private final ExecutorService threadPool;
    private final SimulationFrame frame;
    private final int mode;
    private final NumericData data;
    private double t;
    private double t_next;
    private Future<Boolean> task;

    public void doPlaypauseClick() { playpause.doClick(); }

    public void doStopClick() {
        stop.doClick();
    }

    public boolean isRunning() {
        return status == STATES.RUNNING;
    }

    public void tick() {
        //System.out.println("tick tack " + simulation.getTitle());
    }

    private boolean compute() {
        double deltaT = simulation.getDeltaT();
        for(; t <= t_next; t += deltaT) {
            simulation.step(t);
            frame.draw_all(simulation.getTs(), t);
        }
        return true;
    }

    public SimulationPanel(int mode, ExecutorService threadPool, NumericData data, METHODS method, VISUALIZERS[] visualizer, double[] extraMethodData, double[][] extraVisualizerData) {
        super(new GridBagLayout());
        this.threadPool = threadPool;
        this.mode = mode;
        this.data = data;
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.ipady = 20;
        constraints.anchor = GridBagConstraints.WEST;
        add(new JLabel(method.method_name), constraints);

        constraints.gridx = 1;
        constraints.weightx = 0;
        constraints.ipadx = 70;
        constraints.anchor = GridBagConstraints.CENTER;
        lblStatus = new JLabel(STATES.READY.label);
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

        simulation = MethodFactory.createMethod(data, extraMethodData, method);
        frame = new SimulationFrame(data, method.method_name, visualizer, extraVisualizerData);

        frame.draw_all(simulation.getTs(), 0);

        if(mode == 1) {
            status = STATES.RUNNING;
            t_next = data.time_max;
            playpause.setText("Pausar");
            playpause.setEnabled(false);
            stop.setEnabled(true);
        } else {
            frame.setVisible(true);
            t_next = 0;
        }

        t = simulation.getDeltaT();
        task = threadPool.submit(this::compute);

        ActionListener playpauseListener = new ActionListener() {
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
                lblStatus.setText(status.label);
            }
        };

        ActionListener stopListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (status) {
                    case RUNNING:
                    case PAUSED:
                        playpause.setEnabled(false);
                        stop.setEnabled(false);
                        status = STATES.ABORTED;
                        task.cancel(true);
                        frame.dispose();
                        break;
                }
                lblStatus.setText(status.label);
            }
        };

        playpause.addActionListener(playpauseListener);
        stop.addActionListener(stopListener);
    }
}
