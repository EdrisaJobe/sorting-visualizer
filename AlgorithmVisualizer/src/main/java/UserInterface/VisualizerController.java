package UserInterface;

import Algorithms.*;
import javafx.animation.AnimationTimer;
import javafx.animation.Transition;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;

public class VisualizerController implements Initializable {

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
    private ComboBox<String> nDropdown;
    @FXML
    private Slider slider;
    @FXML
    private Text pseudoText;
    @FXML
    private Label algoState;
    @FXML
    private Label bestTime;
    @FXML
    private Label avgTime;
    @FXML
    private Label worstTime;
    @FXML
    private Label spaceComp;


    private int treeSize = 9;
    private Circle[] treeNodes;
    private int[] nodeValues;
    private Line[] nodeLines;

    private Rectangle[] boxes;
    private AnimationTimer timer;
    private AbstractAlgorithm algorithm;
    private AbstractAlgorithmTree algorithmTree;
    private String algorithmName = "Bubble Sort";
    private ArrayList<AlgoState> transitions = null;
    private int currentTransitionIndex = 0;
    private float speed = 1;

    //Gap between blocks.
    private int x_gap;

    private int box_width;
    private int numOfBoxes = 10;

    private final float BOX_WIDTH_RATIO = 2/3F;
    private final float PANE_HEIGHT_RATIO = 5/6F;
    private final float MIN_BAR_HEIGHT = 1/6F;

    private final float HORIZONTAL_BUFFER = 4;



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

        nDropdown.getItems().setAll("10", "25", "50", "100");
        nDropdown.setValue("10");

        timer = new AnimTimer();
    }


    /**
     * Moves one step forward in the animation.
     */
    @FXML
    protected void StepForward() {
        System.out.println(BubbleSort.bestTime);
        System.out.println(TreeSort.bestTime);

        // Ignores if a transition is currently running
        if(transitions != null && !lastTransitionsIsRunning() && currentTransitionIndex < transitions.size()){
            playNextAnim();
        }
    }

    private void playNextAnim(){
        Transition currentTransition = transitions.get(currentTransitionIndex).forwardTransition;

        UpdateState();
        currentTransition.setRate(speed);
        currentTransition.play();
        currentTransitionIndex++;
    }

    /**
     * Moves one step forward in the animation.
     */
    @FXML
    protected void StepBackward() {

        // Ignores if a transition is currently running
        if(transitions != null && !lastTransitionsIsRunning() && currentTransitionIndex > 0){
            currentTransitionIndex--;

            Transition currentTransition = transitions.get(currentTransitionIndex).reverseTransition;

            UpdateState();
            currentTransition.setRate(speed);
            currentTransition.play();
        }
    }


    private void UpdateState(){
        var variables = transitions.get(currentTransitionIndex).variables;

        if(variables.size() > 0) {
            StringBuilder vars = new StringBuilder("Algo State: ");
            for (int i = 0; i < variables.size()- 1; i++) {
                vars.append(variables.get(i).toString()).append(", ");
            }
            vars.append(variables.get(variables.size()-1).toString());
            algoState.setText(String.valueOf(vars));
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
            if(algorithmName == "Binary Search"){
                GenerateRandomBinaryTree();
            }
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

            if(algorithmName == "Binary Search"){
                GenerateRandomBinaryTree();
            }
            else
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
    @FXML
    public void nDropdownHandler(){

        String dropDownVal = nDropdown.getValue();
        if(!algorithmName.equals(dropDownVal)) {
            this.numOfBoxes = Integer.parseInt(dropDownVal);
            GenerateArray();
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
                algorithm = new BubbleSort(boxes, x_gap, box_width);
                break;
            case "Insertion Sort":
                algorithm = new InsertionSort(boxes, x_gap, box_width);
                break;
            case "Quick Sort":
                algorithm = new QuickSort(boxes, x_gap, box_width);
                break;
            case "Selection Sort":
                algorithm = new SelectionSort(boxes, x_gap, box_width);
                break;
            case "Merge Sort":
                algorithm = new MergeSort(boxes, x_gap, box_width);
                break;
            case "Bucket Sort":
                algorithm = new BucketSort(boxes, x_gap, box_width);
                break;
            case "Heap Sort":
                algorithm = new HeapSort(boxes, x_gap, box_width);
                break;
            case "Binary Search":
                algorithmTree = new TreeSort(treeNodes, nodeValues, nodeLines);
                break;
            case "Linear Search":
                algorithm = new LinearSearch(boxes, x_gap, box_width);
                break;
        }

        if(algorithm != null){
            statusText.setText("Selected Algorithm: " + algorithmName);
            pseudoText.setText(algorithm.pseudoCode);
            bestTime.setText(algorithm.bestTime);
            avgTime.setText(algorithm.averageTime);
            worstTime.setText(algorithm.worstTime);
            spaceComp.setText(algorithm.spaceComplexity);
            this.transitions = algorithm.RunAlgorithm();

            //Loop until we find valid algo state as initial state.
            var variables = transitions.get(0).variables;

            int index = 0;
            while(variables.size() == 0){
                index++;
                variables = transitions.get(index).variables;
            }

            if(variables.size() > 0) {
                StringBuilder vars = new StringBuilder("Algo State: ");
                for (int i = 0; i < variables.size()- 1; i++) {
                    vars.append(variables.get(i).toString()).append(", ");
                }
                vars.append(variables.get(variables.size()-1).toString());
                algoState.setText(String.valueOf(vars));
            }
        }
        else if(algorithmTree != null){
            statusText.setText("Selected Algorithm: " + algorithmName);
            pseudoText.setText(algorithmTree.pseudoCode);
            bestTime.setText(algorithmTree.bestTime);
            avgTime.setText(algorithmTree.averageTime);
            worstTime.setText(algorithmTree.worstTime);
            spaceComp.setText(algorithmTree.spaceComplexity);
            this.transitions = algorithmTree.RunAlgorithm();
            //Loop until we find valid algo state as initial state.
            var variables = transitions.get(0).variables;
            int index = 0;
            while(variables.size() == 0){
                index++;
                variables = transitions.get(index).variables;
            }

            if(variables.size() > 0) {
                StringBuilder vars = new StringBuilder("Algo State: ");
                for (int i = 0; i < variables.size()- 1; i++) {
                    vars.append(variables.get(i).toString()).append(", ");
                }
                vars.append(variables.get(variables.size()-1).toString());
                algoState.setText(String.valueOf(vars));
            }
        }

    }

    /**
     * Auto animates on a timer.
     */
    @FXML
    protected void AnimateOnTimer() {

        if(transitions != null) {
            timer.start();
        }

    }


    /**
     * Generates a random array and sets up the algorithm
     */
    @FXML
    protected void GenerateArray() {

        double b = visualizerPane.getWidth() - HORIZONTAL_BUFFER;
        this.box_width = (int)(b/ numOfBoxes * BOX_WIDTH_RATIO);
        this.x_gap = (int)b / numOfBoxes - box_width;

        if(box_width == 0)
            this.box_width = 1;
        boolean random = !algorithmName.equals("Binary Search");

        ResetAlgorithm();
        StopTimer();

        int paneHeight = (int)(visualizerPane.getHeight() * PANE_HEIGHT_RATIO);
        int minHeight = (int)(visualizerPane.getHeight() * MIN_BAR_HEIGHT);
        boxes = new Rectangle[numOfBoxes];

        //possible height values
        int[] poss_values = new int[numOfBoxes];
        int increment = (paneHeight-minHeight) / numOfBoxes;
        for(int i = 0; i < numOfBoxes; i++){
            poss_values[i] = minHeight + increment * i;
        }

        //put into array list
        ArrayList<Integer> vals_list = new ArrayList<Integer>();
        for(int k = 0; k < poss_values.length;k++){
            vals_list.add(poss_values[k]);
        }
        if(random) {
            //shuffle the order of the sizes
            Collections.shuffle(vals_list);
        }
        float adj = 0;
        if(x_gap != 0);
            adj = x_gap/2.0F;
        for (int i = 0; i < poss_values.length; i++) {
            double height = vals_list.get(i);
            boxes[i] = new Rectangle(box_width, height);
            boxes[i].setTranslateX((x_gap+box_width) * i + (visualizerPane.getWidth()/2.0f - (x_gap+box_width) * (poss_values.length/2.0f)) + adj);

            //Boxes are normally displayed on the top,
            //this line moves them down.
            boxes[i].setTranslateY(visualizerPane.getHeight() - height);
        }
        visualizerPane.getChildren().clear();
        visualizerPane.getChildren().addAll(boxes);

        PrepareAlgorithm();
    }

    //generates an array based on 3 pools of random numbers
    //this provides a more balanced tree
    @FXML
    protected int[] generateRandomTreeValues(){

        int newValues[] = new int[treeSize];

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
        newValues[0] = root_vals_list.get(0);

        for (int i =1;i<newValues.length;i++){
            if (i%2 == 0)
                newValues[i] = right_vals_list.get(i);
            else newValues[i] = left_vals_list.get(i);
        }
        return newValues;

    }


    /**
     * Generate random binary tree
     * */
    @FXML
    protected void GenerateRandomBinaryTree(){
        GenerateBinaryTree(generateRandomTreeValues());
        PrepareAlgorithm();
    }

    /**
     * Generate random binary tree
     * */
    @FXML
    protected void GenerateBinaryTree(int [] newNodeVals){

        visualizerPane.getChildren().clear();

        //int value of node
        nodeValues = newNodeVals;

        //cirlces for nodes
        treeNodes = new Circle[nodeValues.length];

        //text for node
        Text[] treeText = new Text[nodeValues.length];
        //node liens
        nodeLines = new Line[treeNodes.length-1];

        //place the root node
        double rootX = visualizerPane.getWidth()/2;
        double rootY = 25;
        //set radius and x/y offset
        double radius = 20;
        double xOffset = 35;
        double yOffset = 45;

        Circle rootCircle = new Circle(rootX, rootY, radius);
        treeNodes[0] = rootCircle;
        rootCircle.setStrokeWidth(8);
        int rootValue = nodeValues[0];
        Text rootText = new Text(String.valueOf(rootValue));
        rootText.setStroke(Color.WHITESMOKE);
        rootText.setLayoutX(rootX - 5);
        rootText.setLayoutY(rootY + 5);
        treeText[0] = rootText;
        for(int i=1;i<nodeValues.length;i++){
            double newCirclePosX = rootX;
            double newCirclePosY = rootY;
            int circleVal = nodeValues[i];
            int max = 190;
            int min = 10;
            int height = 0;

            Circle parent = treeNodes[0];
            for (int k=0;k<i;k++){
                if (nodeValues[k]<circleVal && nodeValues[k] >= min) {
                    height++;
                    newCirclePosX += Math.max(xOffset ,xOffset * (7-2*height));
                    newCirclePosY += Math.min(yOffset+(5*height),yOffset+20);
                    min = nodeValues[k];
                    parent = treeNodes[k];
                }
                else if (nodeValues[k] > circleVal && nodeValues[k] <= max){
                    height++;
                    newCirclePosX -= Math.max(xOffset ,xOffset * (7-2*height));
                    newCirclePosY += Math.min(yOffset+(5*height),yOffset+20);
                    max = nodeValues[k];
                    parent = treeNodes[k];
                }
            }
            Circle newChild = new Circle(newCirclePosX, newCirclePosY, radius);
            newChild.setStrokeWidth(8);
            Text newChildText = new Text(String.valueOf(nodeValues[i]));
            newChildText.setStroke(Color.WHITESMOKE);
            newChildText.setLayoutX(newCirclePosX - 5);
            newChildText.setLayoutY(newCirclePosY + 5);
            treeText[i] = newChildText;
            treeNodes[i] = newChild;
            Line line1 = new Line(parent.getCenterX(), parent.getCenterY() + radius, newCirclePosX, newCirclePosY - radius);
            line1.setStrokeWidth(5);
            nodeLines[i-1] = line1;
        }

        drawTree(treeNodes,treeText,nodeLines);

    }

    @FXML
    protected void drawTree(Circle[] nodes, Text[] labels,Line[] lineSet){
        visualizerPane.getChildren().clear();
        visualizerPane.getChildren().add(nodes[0]);
        visualizerPane.getChildren().add(labels[0]);
        for (int i =1;i<nodes.length;i++){
            visualizerPane.getChildren().add(nodes[i]);
            visualizerPane.getChildren().add(labels[i]);
            visualizerPane.getChildren().add(lineSet[i-1]);
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
                playNextAnim();
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
            return transitions.get(currentTransitionIndex - 1).forwardTransition.getStatus().toString().equals("RUNNING")
                    || transitions.get(currentTransitionIndex).reverseTransition.getStatus().toString().equals("RUNNING");
        }
        return false;
    }


}