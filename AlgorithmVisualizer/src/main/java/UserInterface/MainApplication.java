package UserInterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1180, 800);
        MainController controller = fxmlLoader.getController();
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

//        Generate a new array once the scene is fully loaded.
        stage.setOnShown((event) -> {
            controller.SetupVisualizer();
        });

        stage.setTitle("Algorithm Visualizer");
        stage.setMinWidth(900);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}