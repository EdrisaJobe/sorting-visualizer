package UserInterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    @FXML
    MenuBar menu;

    @FXML
    Menu speedMenu;
    @FXML
    Menu sizeMenu;

    @FXML
    MenuItem about;
    
    /**
     * Initializes the UI elements.
     * @param url url
     * @param resourceBundle srBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Add an event handler for the "About" menu item
        about.setOnAction(event -> {
            try {
                // Load the help view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("help-view.fxml"));
                Parent helpView = loader.load();

                // Create a new scene and show it
                Scene helpScene = new Scene(helpView);
                Stage helpStage = new Stage();
                helpStage.setTitle("Help Page");
                helpStage.setScene(helpScene);

                // set window size
                helpStage.setWidth(1120);
                helpStage.setHeight(750);

                helpStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Add the menu items to the context menu
        speedMenu.getItems().addAll(new MenuItem("1x"), new MenuItem("2x"),
                new MenuItem("3x"), new MenuItem("5x"), new MenuItem("10x"), new MenuItem("100x"));// Add the menu items to the context menu

        sizeMenu.getItems().addAll(new MenuItem("10"), new MenuItem("25"),
                new MenuItem("50"), new MenuItem("100"));


        // Add an action event handler to the context menu
        speedMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MenuItem menuItem = (MenuItem) event.getTarget();
                String menuItemText = menuItem.getText();
                if(speedMenu != event.getTarget()) {
                    visualizerController.speedDropdown.setValue(menuItemText);
                    visualizerController.SpeedDropdownHandler();
                }
            }
        });

        // Add an action event handler to the context menu
        sizeMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MenuItem menuItem = (MenuItem) event.getTarget();
                String menuItemText = menuItem.getText();
                if(sizeMenu != event.getTarget()) {
                    visualizerController.nDropdown.setValue(menuItemText);
                    visualizerController.nDropdownHandler();
                }
            }
        });


    }

    public void SetupVisualizer(){
        visualizerController.GenerateArray();
    }

    public void UpdateSpeed(){
        visualizerController.speedDropdown.setValue("");
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