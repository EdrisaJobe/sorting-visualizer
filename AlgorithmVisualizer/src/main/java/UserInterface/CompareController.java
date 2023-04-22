package UserInterface;

import Algorithms.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class CompareController implements Initializable {

    @FXML
    ComboBox<String> dropDown;

    @FXML
    VBox compareContainer;


    /**
     * Initializes the UI elements.
     * @param url url
     * @param resourceBundle srBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dropDown.getItems().setAll("Bubble Sort", "Insertion Sort", "Quick Sort",
                "Selection Sort", "Merge Sort", "Bucket Sort", "Heap Sort","Tree Sort");
        dropDown.setValue("Bubble Sort");
//        dropDownHandler();
    }

    /**
     * Prepares the transitions for the given algorithm on the given array
     */
    @FXML
    public void dropDownHandler(){
        String algorithmName = dropDown.getValue();
        AbstractAlgorithm algo = null;
        switch(algorithmName)
        {
            case "Bubble Sort":
                algo = new BubbleSort();
                break;
            case "Insertion Sort":
                algo = new InsertionSort();
                break;
            case "Quick Sort":
                algo = new QuickSort();
                break;
            case "Selection Sort":
                algo = new SelectionSort();
                break;
            case "Merge Sort":
                algo = new MergeSort();
                break;
            case "Bucket Sort":
//                algo = new BucketSort();
//                UpdateTimes(algo.bestTime,algo.averageTime,algo.worstTime);
                break;
            case "Heap Sort":
                algo = new HeapSort();
                break;
            case "Tree Sort":
//                algo = new BubbleSort();
//                UpdateTimes(algo.bestTime,algo.averageTime,algo.worstTime);
                break;
            case "Linear Search":
                algo = new LinearSearch();
                break;
        }
        if(algo != null) {
            UpdateTimes(algo.bestTime, algo.averageTime, algo.worstTime, algo.spaceComplexity);
        }
    }

    /**
     * Updates the UI elements with algo information
     * @param best
     * @param avg
     * @param worst
     * @param space
     */
    public void UpdateTimes(String best, String avg, String worst, String space){

        //Get ids (string within the parenthesis)
        String bestId = best.substring(best.indexOf("(")+1, best.indexOf(")")).replace(" ", "");
        String avgId = avg.substring(avg.indexOf("(")+1, avg.indexOf(")")).replace(" ", "");
        String worstId = worst.substring(worst.indexOf("(")+1, worst.indexOf(")")).replace(" ", "");
        String spaceId = space.substring(space.indexOf("(")+1, space.indexOf(")")).replace(" ", "");

        ((Group)compareContainer.lookup("#" + "curves")).getChildren().forEach(node -> node.setVisible(false));

        compareContainer.lookup("#" + bestId).setVisible(true);
        compareContainer.lookup("#" + avgId).setVisible(true);
        compareContainer.lookup("#" + worstId).setVisible(true);
        compareContainer.lookup("#" + spaceId).setVisible(true);

        ((Label)compareContainer.lookup("#bestTime")).setText(avg);
        ((Label)compareContainer.lookup("#avgTime")).setText(avg);
        ((Label)compareContainer.lookup("#worstTime")).setText(worst);
        ((Label)compareContainer.lookup("#spaceComp")).setText(space);
    }
}