package core;

public class MethodFactory {
    public enum METHODS{
        EXACT("Solução Analítica"),
        UTCS("Upwind no Tempo e Centrada no Espaço"),
        TDMA_IMPLICIT("Método de Thomas Implícito"),
        TDMA_EXPLICIT("Método de Thomas Explícito"),
        TDMA_CRANK_NICOLSON("Método de Crank e Nicolson"),
        TDMA_CUSTOM("Método de Thomas com Beta e Hesse");
        public String method_name;
        METHODS(String method_name) {
            this.method_name = method_name;
        }
    }
    public static NumericMethod createMethod(NumericData data, double[] extraData, METHODS method) {
        switch(method) {
            case EXACT:
                return new ExactSolution(data);
            case UTCS:
                return new UTCS(data);
            case TDMA_IMPLICIT:
                return new TDMA(data, 0.0, extraData[0]);
            case TDMA_EXPLICIT:
                return new TDMA(data, 1.0, extraData[0]);
            case TDMA_CRANK_NICOLSON:
                return new TDMA(data, 0.5, extraData[0]);
            case TDMA_CUSTOM:
                return new TDMA(data, extraData[0], extraData[1]);
        }
        return null;
    }
}
