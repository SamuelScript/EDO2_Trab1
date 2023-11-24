package ui;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
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
    private int currentCard = 1;

    public MainFrame() {
        super("Metal Pipe DELUXE");
        setContentPane(contentPane);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel methodsModel = new DefaultTableModel(new Object[][] {{"Metodo 1", false}, {"Metodo 2", true}, {"Melhor metodo", false}}, new String[] {"Metodo", "Selecionar"}) {
            final Class<?>[] columnTypes = new Class[] {String.class, Boolean.class};
            public Class<?> getColumnClass(int columnIndex) {return columnTypes[columnIndex];}
        };

        tableMethods.setModel(methodsModel);
        TableColumnModel methodsColumnModel = tableMethods.getColumnModel();
        methodsColumnModel.getColumn(0).setResizable(false);
        methodsColumnModel.getColumn(1).setResizable(false);
        methodsColumnModel.getColumn(1).setMaxWidth(100);
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
