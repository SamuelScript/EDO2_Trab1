package Main;

import java.awt.*;
import java.io.IOException;
import static java.lang.Math.sin;
import static java.lang.Math.exp;

public class Trab1 {
    private final double length_x; //Lx : entre 1 e 20 [metros]
    private final double length_xf; //Uma fração de Lx
    private final double a; //Coeficiente de difusão : entre 0.001 a 0.1 [metros²/segundo]
    private final double t_max; // tempo máximo : entre 60 e 600 [segundos]
    private final double t_int; // tempo intermediário : % t_max
    private final double deltaX; //Comprimento da malha espacial
    private final double deltaT; //Intervalo da malha temporal
    private final double Ca; //Concentração inicial A
    private final double Cb; //Concentração inicial B
    private final double Cc; //Concentração inicial C
    private double[] Qs; //Valores no interior do domínio
    private double t_draw;
    private double dt_draw;
    private GChart chart;
    private GChart chartA;

    private double Algoritmo_Trabalho_1( int Xi ) {
        return Qs[Xi] + a*(deltaT/deltaX)*((Qs[Xi+1] - 2*Qs[Xi] + Qs[Xi-1])/deltaX);
    }

    Trab1(double length_x, double length_xf, double a, double t_max, double t_int, double Ca, double Cb, double Cc, int s_partition, int draw) { //Inicializa todos os valores para o tempo 0.
        this.length_x = length_x;
        this.length_xf = length_xf;
        this.a = a;
        this.t_max = t_max;
        this.t_int = t_int;
        this.deltaX = length_x/s_partition;
        this.deltaT = 0.8*(1/(2*a/(deltaX*deltaX)));
        this.Ca = Ca;
        this.Cb = Cb;
        this.Cc = Cc;

        dt_draw = t_max / draw; //Intervalo para desenhar no gráfico
        t_draw  = dt_draw;
        chart = new GChart("A barra de calor!!!", draw + 1);
        chartA = new GChart("Calor Analitico", draw + 1);

        Qs = new double[s_partition+1];

        int i;
        for(i = 0; i < Math.round((length_xf/length_x)*s_partition); i++) Qs[i] = Ca;
        for(i = i; i < s_partition; i++) Qs[i] = Cb;
        Qs[0] = 0;
        Qs[s_partition] = 0;
        for(int j = 1; j <= draw; j++) chart.setColor(j, Color.BLUE);
    }

    public void run() { //Roda quantas iterações forem necessárias até que T alcançe t_max.
        double t = 0; //Nosso tempo inicial.
        int c = 0; //Qual curva está sendo desenhada.
        double[] Qss = new double[Qs.length];
        final double dtdx = deltaT/deltaX;
        final int length = Qs.length;

        for(int i = 0; i < length; i++) chart.addData(0, i * deltaX, Qs[i]);
        chart.setLabel(0, "T = 0.0");
        for(int i = 0; i < length; i++) chartA.addData(0, i * deltaX, Qs[i]);
        chartA.setLabel(0, "T = 0.0");
        c++;

        Qss[0] = 0; //Precisamos inicializar o contorno em nosso array auxiliar também.

        for(t = deltaT; t <= t_int; t += deltaT){ //Iterar em 0 < t <= t_int.
            //System.out.println("T = "+t);
            for(int i = 1; i < length-1; i++) { //Iterar todos os volumes da malha (Exceto contornos!)
                Qss[i] = Algoritmo_Trabalho_1(i);
            }
            double[] tmp = Qss;
            Qss = Qs;
            Qs = tmp;

            if(t_draw <= t) {
                for(int i = 0; i < length; i++) chart.addData(c, i * deltaX, Qs[i]);
                chart.setLabel(c, "T = " + t);
                c++;
                t_draw += dt_draw;
            }
        }

        Qs[0] = Cc; //Após o tempo intermediário, c(0,t), t > t_int passa a ter valor Cc.
        Qss[0] = Cc; //Mesma coisa com nosso array auxiliar.

        for(t = t_int; t <= t_max; t += deltaT) { //Iterar em t_int < t <= t_max,
            //System.out.println("T = "+t);
            for(int i = 1; i < length-1; i++) { //Iterar todos os volumes da malha (Exceto contornos!)
                Qss[i] = Algoritmo_Trabalho_1(i);
            }
            double[] tmp = Qss;
            Qss = Qs;
            Qs = tmp;

            if(t_draw <= t) {
                for(int i = 0; i < length; i++) chart.addData(c, i * deltaX, Qs[i]);
                chart.setLabel(c, "T = " + t);
                c++;
                t_draw += dt_draw;
            }
        }

        for(int i = 0; i < length; i++) chart.addData(c, i * deltaX, Qs[i]);
        chart.setLabel(c, "T = " + t);

        for(int i = 1; i <= c ; i++){
            for(int j = 1; j <= c ; j++) chart.setVisible(j, false);
            chart.setVisible(i, true);
            try{ chart.savePNG("/home/gabrielm/Development/export/"+"A barra de calor!!!"+" "+i+".png", 900, 600); }
            catch(IOException e) { e.printStackTrace(); }
        }

        chartA.setLabel(c, "T = " + t);

        for(double x = 0; x < length_x; x+=deltaX) { //Computa a curva final da solução analítica.
            double acc = 0.0;
            int n = 1;
            while(true){
                double v = exp(-n*n*Math.PI*Math.PI*a*a*t/(length_x*length_x)) * sin((n*Math.PI*x)/length_x)/n;
                if(v < 0.0000001) break;
                acc += v;
                n += 2;
                if(n > 10000) break; //Condição anti-travamento.
            }
            acc *= 4*Ca/Math.PI;
            chartA.addData(1, x, acc);
        }
        try{ chartA.savePNG("/home/gabrielm/Development/export/"+"Calor Analitico"+".png", 900, 600); }
        catch(IOException e) { e.printStackTrace(); }
    }
}
