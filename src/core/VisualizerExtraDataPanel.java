package core;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class VisualizerExtraDataPanel extends JPanel {
    JTextField field;
    JComboBox<String> comboBox;
    double[] extraData;

    public boolean validateFields() {
        if(field.getText().trim().isEmpty()) {
            extraData = null;
            return true;
        }
        try {
            if(comboBox.getSelectedIndex() == 0) {
                String[] numbers = field.getText().split(";");
                extraData = new double[numbers.length + 1];
                for(int i = 1; i <= numbers.length; i++) extraData[i] = Double.parseDouble(numbers[i - 1]);
            } else {
                extraData = new double[2];
                extraData[1] = Double.parseDouble(field.getText());
            }
        } catch(NumberFormatException e) { return false; }
        finally { extraData[0] = comboBox.getSelectedIndex(); }
        return true;
    }

    public double[] getExtraData() {
        return extraData;
    }

    public VisualizerExtraDataPanel() {
        super(new GridBagLayout());
        setBorder(new LineBorder(Color.BLACK, 1));
        GridBagConstraints constraints = new GridBagConstraints();

        field = new JTextField();
        comboBox = new JComboBox<>(new String[] { "Valores Específicos:", "A Cada:"});

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(10, 20, 10, 5);
        ((JLabel)comboBox.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
        add(comboBox, constraints);

        constraints.gridx = 1;
        constraints.ipadx = 110;
        constraints.anchor = GridBagConstraints.WEST;
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setPreferredSize(new Dimension(110, 20));
        constraints.insets = new Insets(10, 0, 10, 20);
        add(field, constraints);
    }
}
