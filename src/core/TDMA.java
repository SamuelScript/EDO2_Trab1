package core;

public class TDMA extends NumericMethod {
    private final double beta;
    private final double hesse;
    private final double aj;
    private final double bj;
    private final double cj;
    private final double[] Pj;
    private final double[] Qj;

    TDMA(NumericData data, double beta, double hesse) {
        super(data);
        this.beta = beta;
        this.hesse = hesse;
        Pj = new double[nodes];
        Qj = new double[nodes];

        deltaT = hesse*deltaX*deltaX/alpha;
        aj = 1 + 2*hesse*(1 - beta);
        bj = -hesse*(beta - 1);
        cj = bj;

        Pj[0] = 0;
        Qj[0] = tce;
        Pj[nodes - 1] = 0;
        Qj[nodes - 1] = tcd;
    }

    @Override
    public void step(double t) {
        for (int i = 1; i < nodes - 1; i++) {
            Tss[i] = hesse*beta*(Ts[i - 1] - 2*Ts[i] + Ts[i + 1]) + Ts[i]; //Coloquei hesse*beta em evidência para acelerar um pouco matemática de FPU
            Pj[i] = bj/(aj - cj*Pj[i - 1]);
            Qj[i] = (Tss[i] + cj*Qj[i - 1])/(aj - cj*Pj[i - 1]);
        }

        for(int i = nodes - 2; i > 0; i--) Tss[i] = Tss[i + 1]*Pj[i] + Qj[i];

        //Empurrar next para posição atual. Next agora é inválido.
        double[] tmp = Ts;
        Ts = Tss;
        Tss = tmp;
    }
}
