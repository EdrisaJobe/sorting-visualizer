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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
    private ComboBox<String> speedDropdown;
    @FXML
    private Slider slider;
    @FXML
    private TextFlow pseudoText;


    private Circle[] treeNodes;
    private int[] nodeValues;

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

        speedDropdown.getItems().setAll("1x", "2x", "3x", "5x", "10x");
        speedDropdown.setValue("1x");

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
        String dropDownVal = sortDropdown.getValue();
        if(!algorithmName.equals(dropDownVal)) {
            algorithmName = sortDropdown.getValue();
            GenerateArray();
        }
    }

    /**
     * Prepares the transitions for the given algorithm on the given array
     */
    @FXML
    public void SearchDropdownHandler(){
        String dropDownVal = searchDropdown.getValue();
        if(!algorithmName.equals(dropDownVal)) {
            algorithmName = searchDropdown.getValue();
            GenerateArray();
        }
    }

    /**
     * Prepares the transitions for the given algorithm on the given array
     */
    @FXML
    public void SpeedDropdownHandler(){
        int value = Integer.parseInt(speedDropdown.getValue().replaceAll("[^0-9]", ""));

        // If a transition is currently running
        if (lastTransitionsIsRunning()) {
            if (currentTransitionIndex < transitions.size()) {
                // Waits until currently running transition is finished
                transitions.get(currentTransitionIndex-1).forwardTransition.setOnFinished(e -> {
                    this.speed = value;
                });
            }
            else
                this.speed = value;
        }
        else {
            this.speed = value;
        }
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

    /**
     * Generate binary tree
     * */
    @FXML
    protected void GenerateBinaryTree(){

        visualizerPane.getChildren().clear();
        //cirlces for nodes
        treeNodes = new Circle[10];
        //int value of node
        nodeValues = new int[10];
        //text for node
        Text[] treeText = new Text[10];
        //node liens
        Line[] node_lines = new Line[treeNodes.length-1];

        //possible root values
        ArrayList<Integer> root_vals_list = new ArrayList<Integer>(Arrays.asList(80,100,120));

        //possible small values
        ArrayList<Integer> left_vals_list = new ArrayList<Integer>(Arrays.asList(45,60,50,75,30,10,55,35,70,12));

        //possible large values
        ArrayList<Integer> right_vals_list = new ArrayList<Integer>(Arrays.asList(130,135,140,160,150,175,165,155,180,190));

        //shuffle the order of the sizes
        Collections.shuffle(root_vals_list);
        //shuffle the order of the sizes
        Collections.shuffle(left_vals_list);
        //shuffle the order of the sizes
        Collections.shuffle(right_vals_list);
        nodeValues[0] = root_vals_list.get(0);

        for (int i =1;i<nodeValues.length;i++){
            if (i%2 == 0)
                nodeValues[i] = right_vals_list.get(i);
            else nodeValues[i] = left_vals_list.get(i);
        }

        //
        //place the root node
        double rootX = 300;
        double rootY = 25;
        //set radius and x/y offset
        double radius = 20;
        double xOffset = 65;
        double yOffset = 45;

        Circle rootCircle = new Circle(rootX, rootY, radius);
        treeNodes[0] = rootCircle;
        visualizerPane.getChildren().add(rootCircle);
        int rootValue = nodeValues[0];
        Text rootText = new Text(String.valueOf(rootValue));
        rootText.setStroke(Color.WHITESMOKE);
        rootText.setLayoutX(rootX - 5);
        rootText.setLayoutY(rootY + 5);
        visualizerPane.getChildren().add(rootText);
        int placed_nodes = 1;
        for(int i=1;i<nodeValues.length;i++){
            double newCirclePosX = rootX;
            double newCirclePosY = rootY;

            int circleVal = nodeValues[i];
            int max = 190;
            int min = 10;
            int height = 1;

            Circle parent = treeNodes[0];
            for (int k=0;k<i;k++){
                if (nodeValues[k]<circleVal && nodeValues[k] >= min) {
                    height++;
                    newCirclePosX += Math.max(xOffset - radius,xOffset * (4-height));
                    newCirclePosY += Math.min(yOffset+(5*height),yOffset+20);
                    min = nodeValues[k];
                    parent = treeNodes[k];
                }
                else if (nodeValues[k] > circleVal && nodeValues[k] <= max){
                    height++;
                    newCirclePosX -= Math.max(xOffset - (2*radius),xOffset * (4-height));
                    newCirclePosY += Math.min(yOffset+(5*height),yOffset+20);
                    max = nodeValues[k];
                    parent = treeNodes[k];
                }
            }
            Circle newChild = new Circle(newCirclePosX, newCirclePosY, radius);
            Text newChildText = new Text(String.valueOf(nodeValues[i]));
            newChildText.setStroke(Color.WHITESMOKE);
            newChildText.setLayoutX(newCirclePosX - 5);
            newChildText.setLayoutY(newCirclePosY + 5);
            treeText[i] = newChildText;
            treeNodes[i] = newChild;
            placed_nodes ++;
            Line line1 = new Line(parent.getCenterX(), parent.getCenterY() + radius, newCirclePosX, newCirclePosY - radius);
            node_lines[i-1] = line1;
        }

        for (int i =1;i<nodeValues.length;i++){
            visualizerPane.getChildren().add(treeNodes[i]);
            visualizerPane.getChildren().add(treeText[i]);
            visualizerPane.getChildren().add(node_lines[i-1]);
        }

    }


    @FXML
    protected void StopTimer() {
            timer.stop();
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
            if (currentTransitionIndex == 0 || currentTransitionIndex > transitions.size() -1)
                return false;
            if (transitions.get(currentTransitionIndex - 1).forwardTransition.getStatus().toString().equals("RUNNING")
                    || transitions.get(currentTransitionIndex).reverseTransition.getStatus().toString().equals("RUNNING"))
                return true;
        }
        return false;
    }


}