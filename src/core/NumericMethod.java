package core;

abstract class NumericMethod {
    protected final double length_x; //Comprimento do domínio
    protected final double alpha; //Condutividade térmica?
    protected final double time_max; //Tempo máximo
    protected final double deltaX; //Comprimento da malha espacial
    protected final double ti; // Temperatura inicial
    protected final double tce; // Temperatura no contorno esquerdo
    protected final double tcd; // Temperatura no contorno direito
    protected final int nodes;
    protected double deltaT; //Intervalo da malha temporal. Não é final pois cada método tem sua condição de estabilidade.
    protected double[] Ts; //Array de temperatura efetivo.
    protected double[] Tss; //Array temporário de temperaturas para operações out-of-place.

    public NumericMethod(NumericData data) {
        this.length_x = data.length_x;
        this.alpha = data.alpha;
        this.time_max = data.time_max;
        this.deltaX = data.deltaX;
        this.ti = data.ti;
        this.tce = data.tce;
        this.tcd = data.tcd;

        this.deltaT = -1; //Os métodos precisam implementar seus próprios deltaT.
        nodes = (int) Math.round(length_x / deltaX) + 1;
        Ts = new double[nodes];
        Tss = new double[nodes];

        for(int i = 0; i < nodes; i++) Ts[i] = ti;
        Ts[0] = tce;
        Ts[nodes - 1] = tcd;
        Tss[0] = tce;
        Tss[nodes - 1] = tcd;
    }
    abstract public void step(double t);
    public double[] getTs() {
        return Ts;
    }

    public double getDeltaT() {
        return deltaT;
    }
}
