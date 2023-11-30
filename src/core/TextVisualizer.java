package core;

import javax.swing.*;

public class TextVisualizer extends Visualizer {
    private final JScrollPane view;
    private final JTextArea text;
    int drawMode;
    int curve = 1;
    boolean animMode;
    double t_next;
    double[] drawCurves;
    double drawEvery;
    TextVisualizer(NumericData data, double[] extraData, String name, int animMode) {
        super(data);
        this.animMode = animMode == 0;
        drawMode = extraData==null?-1:(int) extraData[0];
        switch (drawMode) {
            case -1: //Usuário não definiu nenhum parâmetro
                break;
            case 0: //Usuário especificou curvas específicas
                int curves = extraData.length - 1;
                drawCurves = new double[curves];
                System.arraycopy(extraData, 1, drawCurves, 0, curves);
                t_next = drawCurves[0];
                break;
            case 1: //Usuário quer desenhar a cada %f segundos
                this.drawEvery = extraData[1];
                break;
            default: //Impossível cair aqui
                System.out.println("Como é possível????");
                break;
        }

        text = new JTextArea();
        view = new JScrollPane(text);
        view.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        view.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    @Override
    void draw(double[] temperature, double time, boolean drawRequest) {
        if(animMode) {
            if(drawRequest) {
                text.append(String.format("t = %.4f, ", time));
                for(double v : temperature) text.append(String.format("%.4f, ", v));
                text.append("\n");
            }
        } else {
            switch(drawMode) {
                case -1:
                    break;
                case 0:
                    if(t_next - time <= 1.0e-5){
                        if(curve > drawCurves.length) {
                            t_next = Double.POSITIVE_INFINITY;
                            return;
                        } else if(curve < drawCurves.length) t_next = drawCurves[curve];
                        curve++;
                    } else return;
                    break;
                case 1:
                    if(t_next - time <= 1.0e-5) t_next += drawEvery;
                    else return;
                    break;
            }
            text.append(String.format("t = %.4f, ", time));
            for(double v : temperature) text.append(String.format("%.4f, ", v));
            text.append("\n");
        }
    }

    @Override
    JComponent getView() {
        return view;
    }
}
