package UserInterface;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private VisualizerController visualizerController;

    /**
     * Initializes the UI elements.
     * @param url url
     * @param resourceBundle srBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void SetupVisualizer(){
        visualizerController.GenerateRandomArray();
    }



}