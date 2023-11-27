package core;

public class NumericData {
    public double length_x; //Comprimento do domínio
    public double alpha; //Condutividade térmica?
    public double time_max; //Tempo máximo
    public double deltaX; //Comprimento da malha espacial
    public double ti; // Temperatura inicial
    public double tce; // Temperatura no contorno esquerdo
    public double tcd; // Temperatura no contorno direito

    public NumericData() { };
    public NumericData(double length_x, double alpha, double time_max, double deltaX, double ti, double tce, double tcd) {
        this.length_x = length_x;
        this.alpha = alpha;
        this.time_max = time_max;
        this.deltaX = deltaX;
        this.ti = ti;
        this.tce = tce;
        this.tcd = tcd;
    }
}
