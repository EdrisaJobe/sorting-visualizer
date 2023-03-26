package UserInterface;

import Algorithms.*;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class VisualizerController implements Initializable {

    //Gap between blocks.
    private final static int X_GAP = AbstractAlgorithm.x_gap;

    private final static int BOX_WIDTH = AbstractAlgorithm.box_width;

    //Duration of the transitions
    protected final static float ANIM_DURATION = .95f;

    @FXML
    private Label statusText;
    @FXML
    private Pane visualizerPane;
    @FXML
    private ComboBox<String> sortDropdown;
    @FXML
    private ComboBox<String> searchDropdown;
    @FXML
    private Slider slider;


    private Rectangle[] boxes;
    private AnimationTimer timer;
    private AbstractAlgorithm algorithm;
    private String algorithmName = "Bubble Sort";
    private ArrayList<AlgoState> transitions = null;
    private int currentTransitionIndex = 0;
    private float speed = 1;


    /**
     * Initializes the UI elements.
     * @param url url
     * @param resourceBundle srBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sortDropdown.getItems().setAll("Bubble Sort", "Insertion Sort", "Quick Sort",
                "Selection Sort", "Merge Sort", "Bucket Sort", "Heap Sort");
        sortDropdown.setValue("Bubble Sort");

        searchDropdown.getItems().setAll("Binary Search", "Linear Search");
        searchDropdown.setValue("Pick Search Algorithm");
        timer = new AnimTimer();
    }


    /**
     * Moves one step forward in the animation.
     */
    @FXML
    protected void StepForward() {
        // Ignores if a transition is currently running
        if(transitions != null && !lastTransitionsIsRunning() && currentTransitionIndex < transitions.size()){
            transitions.get(currentTransitionIndex).forwardTransition.setRate(speed);
            transitions.get(currentTransitionIndex).forwardTransition.play();
            currentTransitionIndex++;
        }
    }

    /**
     * Moves one step forward in the animation.
     */
    @FXML
    protected void StepBackward() {
        // Ignores if a transition is currently running
        if(transitions != null && !lastTransitionsIsRunning() && currentTransitionIndex > 0){
            currentTransitionIndex--;
            transitions.get(currentTransitionIndex).reverseTransition.setRate(speed);
            transitions.get(currentTransitionIndex).reverseTransition.play();
        }
        else if(currentTransitionIndex == 0){
            transitions.get(currentTransitionIndex).reverseTransition.setRate(speed);
            transitions.get(currentTransitionIndex).reverseTransition.play();
        }
    }

    /**
     * Reset to allow for change to the array or algorithm
     */
    protected void ResetAlgorithm()
    {
        algorithm = null;
        currentTransitionIndex = 0;
        this.transitions = null;
    }



    /**
     * Prepares the transitions for the given algorithm on the given array
     */
    @FXML
    public void SortDropdownHandler(){
        algorithmName = sortDropdown.getValue();
        GenerateArray();
    }

    /**
     * Prepares the transitions for the given algorithm on the given array
     */
    @FXML
    public void SearchDropdownHandler(){
        algorithmName = searchDropdown.getValue();
        GenerateArray();
    }

    /**
     * Prepares the transitions for the given algorithm on the given array
     */
    @FXML
    protected void PrepareAlgorithm(){

        switch(algorithmName)
        {
            case "Bubble Sort":
                algorithm = new BubbleSort(boxes);
                break;
            case "Insertion Sort":
                algorithm = new InsertionSort(boxes);
                break;
            case "Quick Sort":
                algorithm = new QuickSort(boxes);
                break;
            case "Selection Sort":
                algorithm = new SelectionSort(boxes);
                break;
            case "Merge Sort":
                algorithm = new MergeSort(boxes);
                break;
            case "Bucket Sort":
                algorithm = new BucketSort(boxes);
                break;
            case "Heap Sort":
                algorithm = new HeapSort(boxes);
                break;
            case "Binary Search":
                algorithm = new BinarySearch(boxes);
                break;
            case "Linear Search":
                algorithm = new LinearSearch(boxes);
                break;
        }

        if(algorithm != null){
            this.transitions = algorithm.RunAlgorithm();
        }
    }

    /**
     * Auto animates on a timer.
     */
    @FXML
    protected void AnimateOnTimer() {

        statusText.setText("Running Algorithm: " + sortDropdown.getValue());
        if(transitions != null) {
            timer.start();
        }

    }


    /**
     * Resets Algorithm then generates a random array and displays it.
     */
    @FXML
    protected void GenerateArray() {

        boolean random = !algorithmName.equals("Binary Search");

        ResetAlgorithm();
        StopTimer();

        boxes = new Rectangle[10];

        //possible height values
        int[] poss_values = new int[]{60, 80, 100, 120, 135, 150, 170, 195, 210, 235};
        //put into array list
        ArrayList<Integer> vals_list = new ArrayList<Integer>();
        for(int k = 0; k < poss_values.length;k++){
            vals_list.add(poss_values[k]);
        }
        if(random) {
            //shuffle the order of the sizes
            Collections.shuffle(vals_list);
        }

        for (int i = 0; i < poss_values.length; i++) {
            double height = vals_list.get(i);
            boxes[i] = new Rectangle(BOX_WIDTH, height);
            boxes[i].setTranslateX((X_GAP+BOX_WIDTH) * i + (visualizerPane.getWidth()/2.0f - (X_GAP+BOX_WIDTH) * (poss_values.length/2.0f)) + 10);

            //Boxes are normally displayed on the top,
            //this line moves them down.
            boxes[i].setTranslateY(visualizerPane.getHeight() - height);
        }
        visualizerPane.getChildren().clear();
        visualizerPane.getChildren().addAll(boxes);

        PrepareAlgorithm();
    }

    @FXML
    protected void StopTimer() {
            timer.stop();
    }

    /**
     * Listens to slider and changes speed of animation
     */
    @FXML
    void SpeedUpdate(Event event) {
            // If a transition is currently running
            if (lastTransitionsIsRunning()) {
                if (currentTransitionIndex < transitions.size()) {
                    // Waits until currently running transition is finished
                    transitions.get(currentTransitionIndex-1).forwardTransition.setOnFinished(e -> {
                        this.speed = (float) slider.getValue();
                    });
                }
                else
                    this.speed = (float) slider.getValue();
            }
        else {
            this.speed = (float) slider.getValue();
        }
    }


    /**
     * Timer to auto animate swaps.
     */
    private class AnimTimer extends AnimationTimer {
        long lastTime = 0;
        @Override
        public void handle(long now) {
            if( (now - lastTime)/ 1000000000.0 > ANIM_DURATION / speed){
                doHandle();
                lastTime = now;
            }
        }

        private void doHandle() {

            if(currentTransitionIndex < transitions.size()) {
                transitions.get(currentTransitionIndex).forwardTransition.setRate(speed);
                transitions.get(currentTransitionIndex).forwardTransition.play();
                currentTransitionIndex++;
            }
            else{
                timer.stop();
            }
        }
    }

    /**
     * Returns a boolean indicating if the last transition animation is playing.
     * @return boolean
     */
    private boolean lastTransitionsIsRunning(){
        if(transitions != null) {
            if (currentTransitionIndex == 0)
                return false;
            if (transitions.get(currentTransitionIndex - 1).forwardTransition.getStatus().toString().equals("RUNNING"))
                return true;
        }
        return false;
    }


}