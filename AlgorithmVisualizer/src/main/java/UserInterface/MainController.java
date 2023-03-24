package UserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private VisualizerController visualizerController;
    @FXML
    private StackPane tutorialPane;

    @FXML
    private TextArea welcomeText,genArrayBtnText,animationBtnText,leftMenuText,tabText, pseudoText,finalText;

    @FXML
    private Button doneBtn, btnNext, btnSkip;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private int tutorialStage=0;


    /**
     * Initializes the UI elements.
     * @param url url
     * @param resourceBundle srBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void SetupVisualizer(){
        visualizerController.GenerateArray();
    }

    public void hideTutorial(ActionEvent actionEvent) throws IOException {
        tutorialPane.setVisible(false);
    }

    public void nextWelcome(ActionEvent actionEvent) throws IOException {

        switch (tutorialStage){
            case 0:
                welcomeText.setVisible(false);
                genArrayBtnText.setVisible(true);
                tutorialStage++;
                break;
            case 1:
                genArrayBtnText.setVisible(false);
                animationBtnText.setVisible(true);
                tutorialStage++;
                break;
            case 2:
                animationBtnText.setVisible(false);
                leftMenuText.setVisible(true);
                tutorialStage++;
                break;
            case 3:
                leftMenuText.setVisible(false);
                pseudoText.setVisible(true);
                tutorialStage++;
                break;
            case 4:
                pseudoText.setVisible(false);
                tabText.setVisible(true);
                tutorialStage++;
                break;

            case 5:
                tabText.setVisible(false);
                finalText.setVisible(true);
                doneBtn.setVisible(true);
                btnNext.setVisible(false);
                btnSkip.setVisible(false);
                tutorialStage++;
                break;
        }

    }
}