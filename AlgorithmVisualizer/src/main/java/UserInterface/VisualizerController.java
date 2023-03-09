package UserInterface;

import Algorithms.AbstractAlgorithm;
import Algorithms.AlgoState;
import Algorithms.BubbleSort;
import Algorithms.InsertionSort;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
    private final static int X_GAP = 20;
    //Duration of the transitions
    protected final static float ANIM_DURATION = .95f;

    @FXML
    private Label statusText;
    @FXML
    private Pane visualizerPane;
    @FXML
    private ComboBox<String> dropdown;
    @FXML
    private Slider slider;
    private Rectangle[] boxes;
    private AnimationTimer timer;
    private AbstractAlgorithm algorithm;
    private ArrayList<AlgoState> transitions = null;
    private int currentTransitionIndex = 0;
    private float speed = 1;


    //Right and left movement transitions.
    private final TranslateTransition move_right = new TranslateTransition();
    private final TranslateTransition move_left = new TranslateTransition();


    /**
     * Initializes the UI elements.
     * @param url url
     * @param resourceBundle srBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dropdown.getItems().setAll("Bubble Sort", "Insertion Sort", "Quick Sort");
        timer = new AnimTimer();
    }


    /**
     * Moves one step forward in the animation.
     */
    @FXML
    protected void StepForward() {
        if(transitions == null){
            PrepareAlgorithm();
        }
        // Ignores if a transition is currently running
        if(lastTransitionsIsRunning()) {
        }
        else {
            transitions.get(currentTransitionIndex).lastTransition.setRate(speed);
            transitions.get(currentTransitionIndex).lastTransition.play();
            currentTransitionIndex++;
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
    protected void PrepareAlgorithm(){
        ResetAlgorithm();
        String selected = "";
        selected = dropdown.getValue();
        switch(selected)
        {
            case "Bubble Sort":
                algorithm = new BubbleSort(boxes);
                currentTransitionIndex = 0;
                this.transitions = algorithm.RunAlgorithm();
                break;
            case "Insertion Sort":
                algorithm = new InsertionSort(boxes);
                currentTransitionIndex = 0;
                this.transitions = algorithm.RunAlgorithm();
                break;
            // Empty dropdown
            default:
                // Needs to be handled with Exception or
        }
    }

    /**
     * Auto animates on a timer.
     */
    @FXML
    protected void AnimateOnTimer() {

        statusText.setText("Running Algorithm: " + dropdown.getValue());
        if(transitions == null) {
            PrepareAlgorithm();
        }

        timer.start();
    }


    /**
     * Generates a random array and displays it.
     */
    @FXML
    protected void GenerateRandomArray() {
        ResetAlgorithm();
        boxes = new Rectangle[10];
        //possible height values
        int[] poss_values = new int[]{60,235,195,100,210,150,170,80,135,120};
        //put into array list
        ArrayList<Integer> vals_list = new ArrayList<Integer>();
        for(int k = 0; k < 10;k++){
            vals_list.add(poss_values[k]);
        }
        //shuffle the order of the sizes
        Collections.shuffle(vals_list);

        for (int i = 0; i < 10; i++) {
            double height = vals_list.get(i);
            boxes[i] = new Rectangle(10, height);
            boxes[i].setX(X_GAP * i);

            //Boxes are normally displayed on the top,
            //this line moves them down.
            boxes[i].setTranslateY(500 - height);
        }
        visualizerPane.getChildren().clear();
        visualizerPane.getChildren().addAll(boxes);

    }

    @FXML
    protected void StopTimer() {
        timer.stop();
    }

    /**
     * Listens to slider and changes speed of animation
     */
    @FXML
    void speedUpdate(Event event) {
            // If a transition is currently running
            if (lastTransitionsIsRunning()) {
                if (currentTransitionIndex != transitions.size()) {
                    // Waits until currently running transition is finished
                    transitions.get(currentTransitionIndex-1).lastTransition.setOnFinished(e -> {
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
            System.out.println(currentTransitionIndex);
            System.out.println(transitions.size());

            if(currentTransitionIndex < transitions.size()) {
                transitions.get(currentTransitionIndex).lastTransition.setRate(speed);
                transitions.get(currentTransitionIndex).lastTransition.play();
                currentTransitionIndex++;
            }
            else{
                timer.stop();
                currentTransitionIndex = 0;
            }
        }
    }

    private boolean lastTransitionsIsRunning(){
        if(transitions != null)
            if(currentTransitionIndex == 0)
                return false;
            if (transitions.get(currentTransitionIndex-1).lastTransition.getStatus().toString().equals("RUNNING"))
               return true;
    return false;

    }


}