package core;

public class VisualizerFactory {
    public enum VISUALIZERS{
        TEXT("Saída de Texto"),
        TIME("Curvas de Tempo no Domínio"),
        SPACE("Curvas de Espaço no Tempo"),
        G3D("Superficie");
        public final String visualizer_name;
        VISUALIZERS(String visualizer_name){
            this.visualizer_name = visualizer_name;
        }
    }
    static Visualizer createVisualizer(NumericData data, double[] extraData, VISUALIZERS visualizer, int animMode) {
        switch(visualizer) {
            case TEXT:
                return new TextVisualizer(data, extraData, visualizer.visualizer_name, animMode);
            case TIME:
                return new GChartTimeVisualizer(data, extraData, visualizer.visualizer_name, animMode);
            case SPACE:
                return new GChartSpaceVisualizer(data, extraData, visualizer.visualizer_name, animMode);
            case G3D:
                return new GChart3DVisualizer(data, visualizer.visualizer_name, animMode);
        }
        return new TextVisualizer(data, extraData, visualizer.visualizer_name, animMode);
    }
}
