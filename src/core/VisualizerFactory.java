package core;

public class VisualizerFactory {
    public enum VISUALIZERS{
        TEXT("Saída de Texto");
        public String visualizer_name;
        VISUALIZERS(String visualizer_name){
            this.visualizer_name = visualizer_name;
        }
    }
    public static Visualizer createVisualizer(NumericData data, double[] extraData, VISUALIZERS visualizer) {
        switch(visualizer) {
            case TEXT:
                return new TextVisualizer(data);
        }
        return null;
    }
}
