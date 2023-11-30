package core;

import javax.swing.*;
import java.awt.*;

public class GChartTimeVisualizer extends Visualizer{
    private final GChart chart;
    int drawMode;
    boolean animMode;
    int curve = 0;
    double t_next = 0;
    double[] drawCurves;
    double drawEvery;
    double[] Xs;

    GChartTimeVisualizer(NumericData data, double[] extraData, String name, int animMode) {//extraData = [mode, extra], extra = (Se mode == 0) [curva 1, curva 2, curva 3, ...]; (Se mode == 1) [desenhe a cada %f segundos]
        super(data);
        drawMode = extraData==null?-1:(int) extraData[0];
        this.animMode = animMode == 0;
        int curves;
        switch (drawMode) {
            case -1: //Usuário não definiu nenhum parâmetro
                curves = 2;
                break;
            case 0: //Usuário especificou curvas específicas
                curves = extraData.length - 1;
                drawCurves = new double[curves];
                System.arraycopy(extraData, 1, drawCurves, 0, curves);
                t_next = drawCurves[0];
                break;
            case 1: //Usuário quer desenhar a cada %f segundos
                curves = (int) (data.time_max / extraData[1]) + 1;
                drawCurves = new double[curves];
                drawEvery = extraData[1];
                for(int i = 0; i < curves; i++) drawCurves[i] = i * drawEvery;
                break;
            default: //Impossível cair aqui
                curves = 1;
                System.out.println("Como é possível????");
                break;
        }
        if(this.animMode) curves = 1; //No modo de animação, a entrada do usuário não influencia o desenho.
        Xs = new double[(int) (data.length_x / data.deltaX) + 1];
        for(int i = 0; i < Xs.length; i++) Xs[i] = i * data.deltaX;

        chart = new GChart(name, curves);
        for(int i = 0; i < curves; i++) chart.setColor(i, Color.black);
    }

    @Override
    void draw(double[] temperature, double time, boolean drawRequest) {
        //Essa função ficou muito confusa!
        //Animação E drawRequest - Desenhar temperaturas em t = time
        //Animação E not drawRequest - Temperaturas ainda não estão prontas e NÃO devem ser desenhadas
        //not Animação E mode == -1 - Desenhar APENAS se t = 0 ou t = t_max
        //not Animação E mode != -1 - Desenhar se for hora de desenhar
        if(drawRequest && animMode) { //Animação do gráfico - > FUNCIONANDO
            chart.setLabel(0, String.format("t = %.2f", time));
            chart.clear(0);
            chart.addData(0, Xs, temperature);
        } else if(drawRequest && drawMode == -1) {//COMPUTACAO DEFAULT - > FUNCIONANDO
            if(time == 0) {
                chart.setLabel(0, "t = 0.00");
                chart.addData(0, Xs, temperature);
            } else if(time >= data.time_max) {
                chart.setLabel(1, String.format("t = %.2f", time));
                chart.addData(1, Xs, temperature);
            }
        } else if(t_next - time <= 0 && drawMode != -1) { //PLOT DE CADA CURVA - RUIM
            chart.setLabel(curve, String.format("t = %.2f", time));
            chart.addData(curve++, Xs, temperature);
            if(curve >= drawCurves.length) {
                t_next = Double.POSITIVE_INFINITY;
                return;
            }
            if(drawMode == 0) t_next = drawCurves[curve];
            else t_next += drawEvery;
        }
    }

    @Override
    JComponent getView() {
        return chart;
    }
}
