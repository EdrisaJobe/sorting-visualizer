package UserInterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        int width = 1370;
        int height = 800;
        Scene scene = new Scene(fxmlLoader.load(), width, -200);
        MainController controller = fxmlLoader.getController();
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        // Generate a new array once the scene is fully loaded.
        stage.setOnShown((event) -> {
            controller.SetupVisualizer();
        });        

        // set the title, width and height
        stage.setTitle("Algorithm Visualizer");
        stage.setMinWidth(width);
        stage.setMinHeight(height);

        // Set the icon for the main GUI
        Image icon = new Image(getClass().getResourceAsStream("images/main.png"));
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}