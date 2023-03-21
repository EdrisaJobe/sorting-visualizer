module com.example.AlgorithmVisualizer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens UserInterface to javafx.fxml;
    exports UserInterface;
}