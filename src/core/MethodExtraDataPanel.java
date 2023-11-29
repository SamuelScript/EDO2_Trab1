package core;

import core.MethodFactory.METHODS;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MethodExtraDataPanel extends JPanel {
    JTextField[] fields;
    double[] extraData;

    public boolean validateFields() {
        int i = 0;
        for(JTextField field : fields)
            try { extraData[i++] = Double.parseDouble(field.getText()); }
            catch(NumberFormatException e) { return false; }
        return true;
    }

    public double[] getExtraData() {
        return extraData;
    }

    public MethodExtraDataPanel(METHODS method) {
        super(new GridBagLayout());
        setBorder(new LineBorder(Color.BLACK, 1));
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.weighty = 1;

        int length = method.method_extra_data.length;
        if(length == 0) add(new JLabel("Este método não requer parâmetros adicionais"), constraints);
        else add(new JLabel("Este método requer os seguintes parâmetros adicionais:"), constraints);
        fields = new JTextField[length];
        extraData = new double[length];

        Insets insetLeft = new Insets(10, 20, 10, 5);
        Insets insetRight = new Insets(10, 0, 10, 20);

        constraints.weighty = 0;
        constraints.ipadx = 110;
        constraints.ipady = 0;
        constraints.gridwidth = 1;
        for(int i = 1; i <= length; i++) {
            constraints.gridx = 0;
            constraints.gridy = i;
            constraints.weightx = 0;
            constraints.insets = insetLeft;
            constraints.anchor = GridBagConstraints.EAST;
            JLabel label = new JLabel(method.method_extra_data[i - 1] + ':');
            label.setHorizontalAlignment(JLabel.RIGHT);
            add(label , constraints);
            constraints.weightx = 1;
            constraints.insets = insetRight;
            constraints.anchor = GridBagConstraints.WEST;
            constraints.gridx = 1;
            fields[i - 1] = new JTextField();
            fields[i - 1].setHorizontalAlignment(JTextField.CENTER);
            fields[i - 1].setPreferredSize(new Dimension(50, 20));
            add(fields[i - 1], constraints);
        }

        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = length + 1;
        constraints.weighty = 1;
        add(new JPanel(null), constraints);
    }
}
