package ui;

import core.MethodExtraDataPanel;
import core.MethodFactory.METHODS;
import core.NumericData;
import core.SimulationPanel;
import core.VisualizerExtraDataPanel;
import core.VisualizerFactory.VISUALIZERS;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainFrame extends JFrame {
    private JPanel contentPane, panelCards, panelButtons, panelProps, panelMethods, panelHeader, panelSimStatus, panelSimList, panelMethodInfo, panelVisualizerInfo, panelVisualizer;
    private JButton anteriorButton, proximoButton, buttonGlobalPlayPause, buttonGlobalStop;
    private JLabel lblXLength, lblAlpha, lblTMax, lblCLeft, lblCRight, lblTIni, lblUnitLenghtX, lblUnitAlpha, lblStatus, lblSimType, lblUnitDeltaX, lblTempUnit, lblTempUnit1, lblTempUnit2, lblTempUnit3;
    private JScrollPane scrollPaneMethods,scrollPaneVisualizer,scrollPaneSimLIst;
    private JTable tableMethods,tableVisualizer;
    private JTextField textFieldLengthX, textFieldAlpha, textFieldTMax, textFieldCLeft, textFieldCRight, textFieldTIni, textFieldDeltaX;
    private final JTextField[] textFields = new JTextField[] { textFieldLengthX, textFieldAlpha, textFieldTMax, textFieldCLeft, textFieldCRight, textFieldTIni, textFieldDeltaX};
    private JComboBox<String> comboBoxDeltaX,comboBoxSimType,comboBoxTempUnit;
    private int currentCard = 1;
    private NumericData data;
    private int simu_mode;
    private int partition_type;
    private int temperature_type;
    private JTextField[] textFields = new JTextField[] { textFieldLengthX, textFieldAlpha, textFieldTMax, textFieldCLeft, textFieldCRight, textFieldTIni, textFieldDeltaX};
    private final String[] textNames = new String[] { "Comprimento do Domínio" , "Condutividade Térmica", "Tempo de Simulação", "Temperatura no Contorno Esquerdo", "Temperatura no Contorno Direito", "Temperatura inicial da barra", "Delta X/Numero de partições"};
    private final boolean[] selected_methods = new boolean[METHODS.values().length];
    private final boolean[] selected_visualizers = new boolean[VISUALIZERS.values().length];
    private SimulationPanel[] simulations;
    private final MethodExtraDataPanel[] extraDataPanels;
    private final VisualizerExtraDataPanel[] extraViewPanels;
    private final double[][] extraDataMethods = new double[METHODS.values().length][];
    private final double[][] extraDataVisualizers = new double[VISUALIZERS.values().length][];
    private int simulations_running = 0;
    private ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public MainFrame() {
        super("Metal Pipe DELUXE");
        setContentPane(contentPane);
        setMinimumSize(new Dimension(900, 600));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ((JLabel)comboBoxDeltaX.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
        panelSimList.setLayout(new GridLayout(0, 1, 0, 10));

        // PAGINA 2 (SELECAO DOS METODOS) !------!
        Object[][] methods = new Object[METHODS.values().length][2];
        for(int i = 0; i < methods.length; i++) methods[i] = new Object[] { METHODS.values()[i].method_name, false };

        DefaultTableModel methodsModel = new DefaultTableModel(methods, new String[] {"Metodo", "Selecionar"}) {
            final Class<?>[] columnTypes = new Class[] {String.class, Boolean.class};
            public Class<?> getColumnClass(int columnIndex) {return columnTypes[columnIndex];}

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };

        tableMethods.setModel(methodsModel);
        TableColumnModel methodsColumnModel = tableMethods.getColumnModel();
        methodsColumnModel.getColumn(0).setResizable(false);
        methodsColumnModel.getColumn(1).setResizable(false);
        methodsColumnModel.getColumn(1).setMaxWidth(100);
        tableMethods.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ListSelectionModel methodSelectionModel = tableMethods.getSelectionModel();

        extraDataPanels = new MethodExtraDataPanel[METHODS.values().length];
        for(int i = 0; i < extraDataPanels.length; i++) {
            extraDataPanels[i] = new MethodExtraDataPanel(METHODS.values()[i]);
            panelMethodInfo.add(extraDataPanels[i], String.valueOf(i));
        }

        // PAGINA 3 (VISUALIZADORES) !------!
        Object[][] visualizers = new Object[VISUALIZERS.values().length][2];
        for(int i = 0; i < visualizers.length; i++) visualizers[i] = new Object[] { VISUALIZERS.values()[i].visualizer_name, false };

        DefaultTableModel visualizerModel = new DefaultTableModel(visualizers, new String[] {"Metodo", "Selecionar"}) {
            final Class<?>[] columnTypes = new Class[] {String.class, Boolean.class};
            public Class<?> getColumnClass(int columnIndex) {return columnTypes[columnIndex];}

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };

        tableVisualizer.setModel(visualizerModel);
        TableColumnModel visualizerColumnModel = tableVisualizer.getColumnModel();
        visualizerColumnModel.getColumn(0).setResizable(false);
        visualizerColumnModel.getColumn(1).setResizable(false);
        visualizerColumnModel.getColumn(1).setMaxWidth(100);
        tableVisualizer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ListSelectionModel VisualizerSelectionModel = tableVisualizer.getSelectionModel();

        extraViewPanels = new VisualizerExtraDataPanel[VISUALIZERS.values().length];
        for(int i = 0; i < extraViewPanels.length; i++) {
            extraViewPanels[i] = new VisualizerExtraDataPanel();
            panelVisualizerInfo.add(extraViewPanels[i], String.valueOf(i));
        }

        //LISTENERS
        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(SimulationPanel simulation : simulations) simulation.tick();
            }
        });
        comboBoxDeltaX.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (comboBoxDeltaX.getSelectedIndex() == 0) lblUnitDeltaX.setText("Metros");
                else lblUnitDeltaX.setText("Partições");
            }
        });

        comboBoxTempUnit.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                lblTempUnit1.setText((String)comboBoxTempUnit.getSelectedItem());
                lblTempUnit2.setText((String)comboBoxTempUnit.getSelectedItem());
                lblTempUnit3.setText((String)comboBoxTempUnit.getSelectedItem());
            }
        });
        methodSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ((CardLayout)panelMethodInfo.getLayout()).show(panelMethodInfo, String.valueOf(tableMethods.getSelectedRow()));
            }
        });

        VisualizerSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ((CardLayout)panelVisualizerInfo.getLayout()).show(panelVisualizerInfo, String.valueOf(tableVisualizer.getSelectedRow()));
            }
        });


        // BUTTONS
        anteriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(currentCard) {
                    case 2:
                        ((CardLayout) panelCards.getLayout()).previous(panelCards);
                        currentCard--;
                        anteriorButton.setEnabled(false);
                        break;
                    case 3:
                        ((CardLayout) panelCards.getLayout()).previous(panelCards);
                        currentCard--;
                        break;
                    case 4:
                        simulations_running = 0;
                        for(SimulationPanel sim : simulations) if(sim.isRunning()) simulations_running++;
                        if(simulations_running > 0) {
                            int result = JOptionPane.showConfirmDialog(null, "Um ou mais simulações estão rodando.\n" +
                                    "Para voltar ao menu anterior, é necessário abortá-las.\n" +
                                    "Deseja abortar todas as simulações em andamento?", "Pergunta Rápida", JOptionPane.YES_NO_OPTION);
                            if(result == JOptionPane.CANCEL_OPTION || result == JOptionPane.NO_OPTION) return;
                        }
                        ((CardLayout) panelCards.getLayout()).previous(panelCards);
                        currentCard--;
                        proximoButton.setEnabled(true);

                        for(SimulationPanel simulation : simulations) {
                            simulation.doStopClick();
                            panelSimList.remove(simulation);
                        }
                        timer.stop();
                        simulations = null;
                        break;
                }
                lblStatus.setText("");
            }
        });

        proximoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(currentCard) {
                    case 1:
                        double[] temp = new double[textFields.length];
                        for(int i = 0; i < textFields.length; i++) {
                            if(textFields[i].getText().trim().isEmpty()) {
                                lblStatus.setText(String.format("Campo %s não pode ficar vazio", textNames[i]));
                                return;
                            }
                            try{ temp[i] = Double.parseDouble(textFields[i].getText()); }
                            catch(NumberFormatException ex) {
                                lblStatus.setText(String.format("Campo %s não é um número", textNames[i]));
                                return;
                            }
                        }

                        data = new NumericData(temp[0],temp[1],temp[2],temp[6],temp[3],temp[4],temp[5]);
                        if(comboBoxDeltaX.getSelectedIndex() == 1) data.deltaX = data.length_x / data.deltaX;

                        if(comboBoxTempUnit.getSelectedIndex() == 1) { //Celsius -> Kelvin
                            data.ti += 273.15;
                            data.tcd += 273.15;
                            data.tce += 273.15;
                        } else if(comboBoxTempUnit.getSelectedIndex() == 2){ //Fahrenheit -> Kelvin
                            data.ti = (data.ti - 32) * (5.0 / 9.0) + 273.15;
                            data.tcd = (data.tcd - 32) * (5.0 / 9.0) + 273.15;
                            data.tce = (data.tce - 32) * (5.0 / 9.0) + 273.15;
                        }

                        simu_mode = comboBoxSimType.getSelectedIndex();
                        currentCard++;
                        ((CardLayout) panelCards.getLayout()).next(panelCards);
                        anteriorButton.setEnabled(true);
                        lblStatus.setText("");
                        break;
                    case 2:
                        boolean has_any_method = false;
                        for(int i = 0; i < tableMethods.getRowCount(); i++) {
                            selected_methods[i] = (boolean) tableMethods.getValueAt(i, 1);
                            has_any_method |= selected_methods[i];
                        }
                        if(!has_any_method) {
                            lblStatus.setText("Selecione ao menos um método.");
                            return;
                        } else lblStatus.setText("");
                        currentCard++;
                        ((CardLayout) panelCards.getLayout()).next(panelCards);
                        break;
                    case 3:
                        boolean has_any_visualizer = false;
                        for(int i = 0; i < tableVisualizer.getRowCount(); i++) {
                            selected_visualizers[i] = (boolean) tableVisualizer.getValueAt(i, 1);
                            has_any_visualizer |= selected_visualizers[i];
                        }
                        if(!has_any_visualizer) {
                            lblStatus.setText("Selecione ao menos um visualizador.");
                            return;
                        } else lblStatus.setText("");
                        currentCard++;
                        ((CardLayout) panelCards.getLayout()).next(panelCards);
                        proximoButton.setEnabled(false);

                        int methodCount = 0;
                        int visualizerCount = 0;
                        METHODS[] methods = new METHODS[METHODS.values().length]; //Candidato a refatoração
                        VISUALIZERS[] visualizers = new VISUALIZERS[VISUALIZERS.values().length];
                        for(int i = 0; i < selected_methods.length; i++) if(selected_methods[i]) methods[methodCount++] = METHODS.values()[i];
                        for(int i = 0; i < selected_visualizers.length; i++) if(selected_visualizers[i]) visualizers[visualizerCount++] = VISUALIZERS.values()[i];
                        simulations = new SimulationPanel[methodCount];
                        GridBagConstraints constraints = new GridBagConstraints();
                        constraints.gridx = 0;
                        constraints.weightx = 1;
                        constraints.weighty = 0;
                        constraints.ipady = 10;
                        int panelLength = scrollPaneSimLIst.getSize().width;
                        for(int i = 0; i < methodCount; i++) {
                            constraints.gridy = i;
                            simulations[i] = new SimulationPanel(simu_mode,threadPool,data, methods[i], visualizers, extraDataMethods[i], extraDataVisualizers);
                            simulations[i].setPreferredSize(new Dimension(panelLength-15,40));
                            panelSimList.add(simulations[i], constraints);
                        }
                        timer.start();
                        break;
                }
                lblStatus.setText("");
            }
        });

        buttonGlobalPlayPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(SimulationPanel sim : simulations) sim.doPlaypauseClick();
            }
        });

        buttonGlobalStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(SimulationPanel sim : simulations) sim.doStopClick();
            }
        });
    }
}
