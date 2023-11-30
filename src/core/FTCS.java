package core;

public class FTCS extends NumericMethod{
    FTCS(NumericData data) {
        super(data);
        this.deltaT = 0.95*(1/(2*alpha/(deltaX*deltaX)));
    }
    @Override
    public void step(double t) {
        for(int i = 1; i < nodes-1; i++) Tss[i] = (Ts[i+1] + Ts[i-1]) / 2;
        double[] tmp = Tss;
        Tss = Ts;
        Ts = tmp;
    }
}
