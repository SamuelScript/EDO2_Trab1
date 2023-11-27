package core;

public class MethodFactory {
    enum METHODS{ EXACT, UTCS, TDMA_IMPLICIT, TDMA_EXPLICIT, TDMA_CRANK_NICOLSON, TDMA_CUSTOM }
    private static NumericMethod createMethod(NumericData data, double[] extraData, METHODS method) {
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
