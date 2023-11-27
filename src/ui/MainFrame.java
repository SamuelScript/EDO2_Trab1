package ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

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
    private JTextField textFieldTRight;
    private JLabel lblTIni;
    private JTextField textFieldTIni;
    private JLabel lblUnitLenghtX;
    private JLabel lblUnitAlpha;
    private JComboBox comboBoxUnitTemp1;
    private JComboBox comboBoxUnitTemp2;
    private JComboBox comboBoxUnitTemp3;
    private JLabel lblStatus;
    private JScrollPane scrollPaneMethodInfo;
    private int currentCard = 1;

    public MainFrame() {
        super("Metal Pipe DELUXE");
        setContentPane(contentPane);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel methodsModel = new DefaultTableModel(new Object[][] {
                {"Metodo 1", false}, {"Metodo 2", false}, {"Melhor metodo", false}},
                new String[] {"Metodo", "Selecionar"}) {
            final Class<?>[] columnTypes = new Class[] {String.class, Boolean.class};
            public Class<?> getColumnClass(int columnIndex) {return columnTypes[columnIndex];}
        };

        tableMethods.setModel(methodsModel);
        TableColumnModel methodsColumnModel = tableMethods.getColumnModel();
        methodsColumnModel.getColumn(0).setResizable(false);
        methodsColumnModel.getColumn(1).setResizable(false);
        methodsColumnModel.getColumn(1).setMaxWidth(100);
        tableMethods.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ListSelectionModel selectionModel = tableMethods.getSelectionModel();

        selectionModel.addListSelectionListener(new ListSelectionListener() {
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
                if(currentCard < 2) {
                    ((CardLayout) panelCards.getLayout()).next(panelCards);
                    currentCard++;
                    proximoButton.setEnabled(currentCard != 2);
                    anteriorButton.setEnabled(true);
                }
            }
        });
    }
}
