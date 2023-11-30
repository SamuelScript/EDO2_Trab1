package core;

import javax.swing.*;
import java.awt.*;

public class GChartSpaceVisualizer extends Visualizer {
    private final GChart chart;
    int drawMode;
    boolean animMode;
    int curves;
    double[] drawCurves;
    double drawEvery;
    int[] Xs;
    GChartSpaceVisualizer(NumericData data, double[] extraData, String name, int animMode) {
        super(data);
        drawMode = extraData==null?-1:(int) extraData[0];
        this.animMode = animMode == 0;
        switch (drawMode) {
            case -1: //Usuário não definiu nenhum parâmetro
                curves = 0;
                break;
            case 0: //Usuário especificou curvas específicas
                curves = extraData.length - 1;
                drawCurves = new double[curves];
                System.arraycopy(extraData, 1, drawCurves, 0, curves);
                break;
            case 1: //Usuário quer desenhar a cada %f metros
                curves = (int) (data.length_x / extraData[1]) + 1;
                drawCurves = new double[curves];
                drawEvery = extraData[1];
                for(int i = 0; i < curves; i++) drawCurves[i] = i * drawEvery;
                break;
            default: //Impossível cair aqui (O compilador reclama sem um caso default)
                curves = 1;
                System.out.println("Como é possível????");
                break;
        }

        chart = new GChart(name, curves);
        for(int i = 0; i < curves; i++) chart.setColor(i, Color.black);

        Xs = new int[curves];
        if(curves > 0) {
            int curve = 0;
            int curveidx = 0;
            for(double i = 0; i <= data.length_x; i += data.deltaX) {
                if(curveidx == curves) break;
                if(Math.abs(i - drawCurves[curveidx]) < 1.0e-5) {
                    chart.setLabel(curveidx, String.format("x = %.2f", i));
                    Xs[curveidx++] = curve;
                }
                curve++;
            }
        }
    }

    @Override
    void draw(double[] temperature, double time, boolean drawRequest) {
        if(drawMode == -1) return; //Este visualizador não possui comportamento padrão se não for passado parâmetros
        if(animMode) {
            if(drawRequest) {//Só cai aqui na animação, a cada 50ms.
                for(int i = 0; i < curves; i++) chart.addData(i, time, temperature[Xs[i]]);
            }
        } else { //Só cai aqui na computação, e desenha sempre.
            for(int i = 0; i < curves; i++) chart.addData(i, time, temperature[Xs[i]]);
        }
    }

    @Override
    JComponent getView() {
        return chart;
    }
}
