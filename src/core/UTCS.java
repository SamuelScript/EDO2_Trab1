package core;

public class UTCS extends NumericMethod{
    UTCS(NumericData data) {
        super(data);
        this.deltaT = 0.8*(1/(2*alpha/(deltaX*deltaX)));
    }
    @Override
    public void step(double t) {
        for(int i = 1; i < nodes-1; i++) {
            Tss[i] = Ts[i] + alpha*(deltaT/deltaX)*((Ts[i+1] - 2*Ts[i] + Ts[i-1])/deltaX);
        }
        double[] tmp = Tss;
        Tss = Ts;
        Ts = tmp;
    }
}
