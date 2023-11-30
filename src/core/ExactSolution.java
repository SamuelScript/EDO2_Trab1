package core;

import static java.lang.Math.exp;
import static java.lang.Math.sin;

public class ExactSolution extends NumericMethod{
    ExactSolution(NumericData data) {
        super(data);
        deltaT = 0.1;
    }
    @Override
    public void step(double t) {
        int i = 1;
        for(double x = deltaX; x < length_x; x+=deltaX, i++) { //Computa a curva final da solução analítica.
            double acc = 0.0;
            for(int n = 1; n < 20; n+=2) {
                acc += exp(-n*n*Math.PI*Math.PI*alpha*alpha*t/(length_x*length_x)) * sin((n*Math.PI*x)/length_x)/n;
            }
            acc *= 4*ti/Math.PI;
            Ts[i] = acc;
        }
    }
}