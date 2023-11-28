package ui;

import core.MethodFactory.METHODS;
import core.NumericData;
import core.SimulationPanel;
import core.VisualizerFactory.VISUALIZERS;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JPanel contentPane;
    private JPanel panelCards;
    private JPanel panelButtons;
    private JButton anteriorButton;
    private JButton proximoButton;
    private JPanel panelProps;
    private JPanel panelMethods;
    private JScrollPane scrollPaneMethods;
    private JTable tableMethods;
    private JPanel panelHeader;
    private JLabel lblXLength;
    private JTextField textFieldLengthX;
    private JTextField textFieldAlpha;
    private JLabel lblAlpha;
    private JLabel lblTMax;
    private JTextField textFieldTMax;
    private JLabel lblCLeft;
    private JTextField textFieldCLeft;
    private JLabel lblCRight;
    private JTextField textFieldCRight;
    private JLabel lblTIni;
    private JTextField textFieldTIni;
    private JLabel lblUnitLenghtX;
    private JLabel lblUnitAlpha;
    private JLabel lblStatus;
    private JScrollPane scrollPaneMethodInfo;
    private JPanel panelVisualizer;
    private JScrollPane scrollPaneVisualizer;
    private JScrollPane scrollPaneVisualizerInfo;
    private JTable tableVisualizer;
    private JLabel lblSimType;
    private JPanel panelSimStatus;
    private JComboBox<String> comboBoxDeltaX;
    private JTextField textFieldDeltaX;
    private JLabel lblUnitDeltaX;
    private JComboBox<String> comboBoxSimType;
    private JLabel lblTempUnit;
    private JComboBox<String> comboBoxTempUnit;
    private JLabel lblTempUnit1;
    private JLabel lblTempUnit2;
    private JLabel lblTempUnit3;
    private JScrollPane scrollPaneSimLIst;
    private JPanel panelSimList;
    private int currentCard = 1;
    private NumericData data;
    private int simu_mode;
    private int partition_type;
    private int temperature_type;
    private JTextField[] textFields = new JTextField[] { textFieldLengthX, textFieldAlpha, textFieldTMax, textFieldCLeft, textFieldCRight, textFieldTIni, textFieldDeltaX};
    private final String[] textNames = new String[] { "Comprimento do Domínio" , "Condutividade Térmica", "Tempo de Simulação", "Temperatura no Contorno Esquerdo", "Temperatura no Contorno Direito", "Temperatura inicial da barra", "Delta X/Numero de partições"};
    private boolean[] selected_methods = new boolean[METHODS.values().length];
    private boolean[] selected_visualizers = new boolean[VISUALIZERS.values().length];
    public MainFrame() {
        super("Metal Pipe DELUXE");
        setContentPane(contentPane);
        setMinimumSize(new Dimension(900, 600));
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
                lblStatus.setText(String.valueOf(tableMethods.getSelectedRow()));
            }
        });
        anteriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentCard > 1) {
                    ((CardLayout) panelCards.getLayout()).previous(panelCards);
                    currentCard--;
                    anteriorButton.setEnabled(currentCard != 1);
                    proximoButton.setEnabled(true);
                }
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
                        for(int i = 0; i < methodCount; i++) {
                            simulations[i] = new SimulationPanel(data, methods[i], visualizers, new double[1], new double[1]);
                            panelSimList.add(simulations[i]);
                        }
                        timer.start();
                        break;
                }
            }
        });
    }
}
