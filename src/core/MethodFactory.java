package core;

public class MethodFactory {
    public enum METHODS{
        EXACT("Solução Analítica", new String[] {}),
        UTCS("Upwind no Tempo e Centrada no Espaço", new String[] {}),
        TDMA_IMPLICIT("Método de Thomas Implícito", new String[] { "Hesse" }),
        TDMA_EXPLICIT("Método de Thomas Explícito", new String[] { "Hesse" }),
        TDMA_CRANK_NICOLSON("Método de Crank e Nicolson", new String[] { "Hesse" }),
        TDMA_CUSTOM("Método de Thomas Customizado", new String[] { "Beta", "Hesse" });
        public String method_name;
        public String[] method_extra_data;
        METHODS(String method_name, String[] method_extra_data) {
            this.method_name = method_name;
            this.method_extra_data = method_extra_data;
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
        return new UTCS(data);
    }
}
