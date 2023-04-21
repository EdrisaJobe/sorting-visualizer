package UserInterface;

import Algorithms.*;
import javafx.animation.AnimationTimer;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

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
    private Label sortedArray;
    @FXML
    private Label inputArrayLabel;
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
    @FXML
    private Button btnGenArray;
    @FXML
    private Button btnGenTree;


    private int treeSize = 9;
    private Circle[] circleNodes;
    private StackPane[] bucketNodes;
    private int[] circleNodeValues;
    private Line[] treeNodeLines;
    private Text[] nodeLabels;
    static int treeMax = 190;
    static int treeMin = 10;

    private Rectangle[] boxes;
    private AnimationTimer timer;
    private AbstractAlgorithm algorithm;
    private AbstractAlgorithmTree algorithmTree;
    private BucketSort bucketAlgorithm;
    private String algorithmName = "Bubble Sort";
    private ArrayList<AlgoState> transitions = null;
    private int currentTransitionIndex = 0;
    private float speed = 1;

    //Gap between blocks.
    private int x_gap;

    private int box_width;
    private int numOfBoxes = 10;

    private final float BOX_WIDTH_RATIO = 2 / 3F;
    private final float PANE_HEIGHT_RATIO = 5 / 6F;
    private final float MIN_BAR_HEIGHT = 1 / 6F;

    private final float HORIZONTAL_BUFFER = 4;


    /**
     * Initializes the UI elements.
     *
     * @param url            url
     * @param resourceBundle srBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sortDropdown.getItems().setAll("Bubble Sort", "Insertion Sort", "Quick Sort",
                "Selection Sort", "Merge Sort", "Bucket Sort", "Heap Sort", "Tree Sort");
        sortDropdown.setValue("Bubble Sort");

        searchDropdown.getItems().setAll("Linear Search");
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
        if (transitions != null && !lastTransitionsIsRunning() && currentTransitionIndex < transitions.size()) {
            playNextAnim();
        }
    }

    private void playNextAnim() {
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
        if (transitions != null && !lastTransitionsIsRunning() && currentTransitionIndex > 0) {
            currentTransitionIndex--;

            Transition currentTransition = transitions.get(currentTransitionIndex).reverseTransition;

            UpdateState();
            currentTransition.setRate(speed);
            currentTransition.play();
        }
    }


    private void UpdateState() {
        var variables = transitions.get(currentTransitionIndex).variables;
        var arrayStats = transitions.get(currentTransitionIndex).arrayStatus;

        if (variables.size() > 0) {
            StringBuilder vars = new StringBuilder("Algo State: ");
            for (int i = 0; i < variables.size() - 1; i++) {
                vars.append(variables.get(i).toString()).append(", ");
            }
            vars.append(variables.get(variables.size() - 1).toString());
            algoState.setText(String.valueOf(vars));
        }

        //update array status
        StringBuilder sorted = new StringBuilder("");
        sorted.append(arrayStats);
        sortedArray.setText(String.valueOf(sorted));
    }


    /**
     * Reset to allow for change to the array or algorithm
     */
    protected void ResetAlgorithm() {
        algorithm = null;
        algorithmTree = null;
        currentTransitionIndex = 0;
        this.transitions = null;
    }


    /**
     * Prepares the transitions for the given algorithm on the given array
     */
    @FXML
    public void SortDropdownHandler() {
        String dropDownVal = sortDropdown.getValue();
        if (!algorithmName.equals(dropDownVal)) {
            algorithmName = sortDropdown.getValue();

            if (algorithmName.equals("Tree Sort")) {
                GenerateRandomBinaryTree();
            } else
                GenerateArray();
        }
    }

    /**
     * Prepares the transitions for the given algorithm on the given array
     */
    @FXML
    public void SearchDropdownHandler() {
        String dropDownVal = searchDropdown.getValue();
        if (!algorithmName.equals(dropDownVal)) {
            algorithmName = searchDropdown.getValue();
            if (algorithmName.equals("Tree Sort")) {
                GenerateRandomBinaryTree();
            } else
                GenerateArray();
        }
    }

    /**
     * Prepares the transitions for the given algorithm on the given array
     */
    @FXML
    public void SpeedDropdownHandler() {
        int value = Integer.parseInt(speedDropdown.getValue().replaceAll("[^0-9]", ""));

        // If a transition is currently running
        if (lastTransitionsIsRunning()) {
            if (currentTransitionIndex < transitions.size()) {
                // Waits until currently running transition is finished
                transitions.get(currentTransitionIndex - 1).forwardTransition.setOnFinished(e -> {
                    this.speed = value;
                });
            } else
                this.speed = value;
        } else {
            this.speed = value;
        }
    }

    @FXML
    public void nDropdownHandler() {

        String dropDownVal = nDropdown.getValue();
        if (!algorithmName.equals(dropDownVal)) {
            this.numOfBoxes = Integer.parseInt(dropDownVal);
            GenerateArray();
        }

    }

    /**
     * Prepares the transitions for the given algorithm on the given array
     */
    @FXML
    protected void PrepareAlgorithm() {

        switch (algorithmName) {
            case "Bubble Sort":
                algorithm = new BubbleSort(boxes, x_gap, box_width);
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Insertion Sort":
                algorithm = new InsertionSort(boxes, x_gap, box_width);
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Quick Sort":
                algorithm = new QuickSort(boxes, x_gap, box_width);
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Selection Sort":
                algorithm = new SelectionSort(boxes, x_gap, box_width);
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Merge Sort":
                algorithm = new MergeSort(boxes, x_gap, box_width);
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Bucket Sort":
                int[] test1 = GenerateRandomTreeValues();
                //will update once merged with main
                SetUpBucketSort(test1, 3);
                bucketAlgorithm = new BucketSort(bucketNodes, circleNodeValues, 3, visualizerPane.getWidth(), visualizerPane.getHeight());
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Heap Sort":
                algorithm = new HeapSort(boxes, x_gap, box_width);
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Tree Sort":
                algorithmTree = new TreeSort(circleNodes, circleNodeValues, treeNodeLines);
                btnGenArray.setDisable(true);
                btnGenTree.setDisable(false);
                break;
            case "Linear Search":
                algorithm = new LinearSearch(boxes, x_gap, box_width);
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
        }

        if (algorithm != null) {
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
            while (variables.size() == 0) {
                index++;
                variables = transitions.get(index).variables;

            }

            if (variables.size() > 0) {
                StringBuilder vars = new StringBuilder("Algo State: ");

                for (int i = 0; i < variables.size() - 1; i++) {
                    vars.append(variables.get(i).toString()).append(", ");
                }
                vars.append(variables.get(variables.size() - 1).toString());
                algoState.setText(String.valueOf(vars));
            }


        } else if (algorithmTree != null) {
            statusText.setText("Selected Algorithm: " + algorithmName);
            pseudoText.setText(algorithmTree.pseudoCode);
            bestTime.setText(algorithmTree.bestTime);
            avgTime.setText(algorithmTree.averageTime);
            worstTime.setText(algorithmTree.worstTime);
            spaceComp.setText(algorithmTree.spaceComplexity);
            this.transitions = algorithmTree.RunAlgorithm();
            //Loop until we find valid algo state as initial state.
            var variables = transitions.get(0).variables;
            var arrayStats = transitions.get(0).arrayStatus;
            int index = 0;
            while (variables.size() == 0) {
                index++;
                variables = transitions.get(index).variables;
                arrayStats = transitions.get(index).arrayStatus;
            }

            if (variables.size() > 0) {
                StringBuilder vars = new StringBuilder("Algo State: ");
                for (int i = 0; i < variables.size() - 1; i++) {
                    vars.append(variables.get(i).toString()).append(", ");
                }
                vars.append(variables.get(variables.size() - 1).toString());
                algoState.setText(String.valueOf(vars));
            }

            //update array status
            StringBuilder sorted = new StringBuilder("");
            sorted.append(arrayStats);
            sortedArray.setText(String.valueOf(sorted));

            StringBuilder input = new StringBuilder("");
            input.append(ConvertArrayToString(circleNodeValues));
            inputArrayLabel.setText(String.valueOf(input));
        } else if (bucketAlgorithm != null) {
            statusText.setText("Selected Algorithm: " + algorithmName);
            pseudoText.setText(bucketAlgorithm.pseudoCode);
            bestTime.setText(bucketAlgorithm.bestTime);
            avgTime.setText(bucketAlgorithm.averageTime);
            worstTime.setText(bucketAlgorithm.worstTime);
            spaceComp.setText(bucketAlgorithm.spaceComplexity);
            this.transitions = bucketAlgorithm.RunAlgorithm();
            //Loop until we find valid algo state as initial state.
            var variables = transitions.get(0).variables;
            var arrayStats = transitions.get(0).arrayStatus;
            int index = 0;
            while (variables.size() == 0) {
                index++;
                variables = transitions.get(index).variables;
                arrayStats = transitions.get(index).arrayStatus;
            }

            if (variables.size() > 0) {
                StringBuilder vars = new StringBuilder("Algo State: ");
                for (int i = 0; i < variables.size() - 1; i++) {
                    vars.append(variables.get(i).toString()).append(", ");
                }
                vars.append(variables.get(variables.size() - 1).toString());
                algoState.setText(String.valueOf(vars));
            }

            //update array status
            StringBuilder sorted = new StringBuilder("");
            sorted.append(arrayStats);
            sortedArray.setText(String.valueOf(sorted));

            StringBuilder input = new StringBuilder("");
            input.append(ConvertArrayToString(circleNodeValues));
            inputArrayLabel.setText(String.valueOf(input));

        }

    }

    /**
     * Auto animates on a timer.
     */
    @FXML
    protected void AnimateOnTimer() {

        if (transitions != null) {
            timer.start();
        }

    }


    /**
     * Generates a random array and sets up the algorithm
     */
    @FXML
    protected void GenerateArray() {

        double b = visualizerPane.getWidth() - HORIZONTAL_BUFFER;
        this.box_width = (int) (b / numOfBoxes * BOX_WIDTH_RATIO);
        this.x_gap = (int) b / numOfBoxes - box_width;

        if (box_width == 0)
            this.box_width = 1;
        boolean random = !algorithmName.equals("Binary Search");

        ResetAlgorithm();
        StopTimer();

        int paneHeight = (int) (visualizerPane.getHeight() * PANE_HEIGHT_RATIO);
        int minHeight = (int) (visualizerPane.getHeight() * MIN_BAR_HEIGHT);
        boxes = new Rectangle[numOfBoxes];

        //possible height values
        int[] poss_values = new int[numOfBoxes];
        int increment = (paneHeight - minHeight) / numOfBoxes;
        for (int i = 0; i < numOfBoxes; i++) {
            poss_values[i] = minHeight + increment * i;
        }

        //put into array list
        ArrayList<Integer> vals_list = new ArrayList<>();
        for (int k = 0; k < poss_values.length; k++) {
            vals_list.add(poss_values[k]);
        }
        if (random) {
            //shuffle the order of the sizes
            Collections.shuffle(vals_list);
        }
        float adj = 0;
        if (x_gap != 0) ;
        adj = x_gap / 2.0F;
        for (int i = 0; i < poss_values.length; i++) {
            double height = vals_list.get(i);
            boxes[i] = new Rectangle(box_width, height);
            boxes[i].setTranslateX((x_gap + box_width) * i + (visualizerPane.getWidth() / 2.0f - (x_gap + box_width) * (poss_values.length / 2.0f)) + adj);

            //Boxes are normally displayed on the top,
            //this line moves them down.
            boxes[i].setTranslateY(visualizerPane.getHeight() - height);
        }
        visualizerPane.getChildren().clear();
        visualizerPane.getChildren().addAll(boxes);

        PrepareAlgorithm();
    }

    /**
     * Generates a random array
     * size is set by global treeSize
     * pulls numbers from 3 seperate pools to create a tree with balanced nodes
     *
     * @return int array of random values generated from pool
     */
    @FXML
    protected int[] GenerateRandomTreeValues() {

        int[] newValues = new int[treeSize];

        //possible root values
        ArrayList<Integer> root_vals_list = new ArrayList<>(Arrays.asList(80, 100, 120));

        //possible small values
        ArrayList<Integer> left_vals_list = new ArrayList<>(Arrays.asList(45, 60, 50, 75, 30, treeMin, 55, 35, 70, 12));

        //possible large values
        ArrayList<Integer> right_vals_list = new ArrayList<>(Arrays.asList(130, 135, 140, 160, 150, 175, 165, 155, 180, treeMax));

        //shuffle the order of the sizes
        Collections.shuffle(root_vals_list);
        //shuffle the order of the sizes
        Collections.shuffle(left_vals_list);
        //shuffle the order of the sizes
        Collections.shuffle(right_vals_list);
        newValues[0] = root_vals_list.get(0);

        for (int i = 1; i < newValues.length; i++) {
            if (i % 2 == 0)
                newValues[i] = right_vals_list.get(i);
            else newValues[i] = left_vals_list.get(i);
        }
        return newValues;

    }


    /**
     * Generate random binary tree
     * generates a random array for a tree and draws the tree
     */
    @FXML
    protected void GenerateRandomBinaryTree() {
        GenerateBinaryTree(GenerateRandomTreeValues());
        ResetAlgorithm();
        PrepareAlgorithm();
    }

    /**
     * Generate binary tree from array of values
     * creates an array of Circles, Lines, Text and calls a function to draw them to the pane
     */
    @FXML
    protected void GenerateBinaryTree(int[] newNodeVals) {

        visualizerPane.getChildren().clear();

        //int value of node
        circleNodeValues = newNodeVals;

        //cirlces for nodes
        circleNodes = new Circle[circleNodeValues.length];

        //text for node
        Text[] treeText = new Text[circleNodeValues.length];
        //node liens
        treeNodeLines = new Line[circleNodes.length - 1];

        //place the root node
        double rootX = visualizerPane.getWidth() / 2;
        double rootY = 25;
        //set radius and x/y offset
        double radius = 20;
        double xOffset = 35;
        double yOffset = 45;

        Circle rootCircle = new Circle(rootX, rootY, radius);
        circleNodes[0] = rootCircle;
        rootCircle.setStrokeWidth(4);
        int rootValue = circleNodeValues[0];
        Text rootText = new Text(String.valueOf(rootValue));
        rootText.setStroke(Color.WHITESMOKE);
        rootText.setLayoutX(rootX - 5);
        rootText.setLayoutY(rootY + 5);
        treeText[0] = rootText;
        for (int i = 1; i < circleNodeValues.length; i++) {
            double newCirclePosX = rootX;
            double newCirclePosY = rootY;
            int circleVal = circleNodeValues[i];
            int max = treeMax;
            int min = treeMin;
            int height = 0;

            Circle parent = circleNodes[0];
            for (int k = 0; k < i; k++) {
                if (circleNodeValues[k] < circleVal && circleNodeValues[k] >= min) {
                    height++;
                    newCirclePosX += Math.max(xOffset, xOffset * (7 - 2 * height));
                    newCirclePosY += Math.min(yOffset + (5 * height), yOffset + 20);
                    min = circleNodeValues[k];
                    parent = circleNodes[k];
                } else if (circleNodeValues[k] > circleVal && circleNodeValues[k] <= max) {
                    height++;
                    newCirclePosX -= Math.max(xOffset, xOffset * (7 - 2 * height));
                    newCirclePosY += Math.min(yOffset + (5 * height), yOffset + 20);
                    max = circleNodeValues[k];
                    parent = circleNodes[k];
                }
            }
            Circle newChild = new Circle(newCirclePosX, newCirclePosY, radius);
            newChild.setStrokeWidth(4);
            Text newChildText = new Text(String.valueOf(circleNodeValues[i]));
            newChildText.setStroke(Color.WHITESMOKE);
            newChildText.setLayoutX(newCirclePosX - 5);
            newChildText.setLayoutY(newCirclePosY + 5);
            treeText[i] = newChildText;
            circleNodes[i] = newChild;
            Line line1 = new Line(parent.getCenterX(), parent.getCenterY() + radius, newCirclePosX, newCirclePosY - radius);
            line1.setStrokeWidth(4);
            treeNodeLines[i - 1] = line1;
        }

        drawTree(circleNodes, treeText, treeNodeLines);

    }

    /**
     * Takes an array of Circles, Text, And Lines and draws them to the pane
     *
     * @param nodes   circles which represent tree nodes
     * @param labels  labels which are the text used to represent the value of a node
     * @param lineSet lines connecting parents and children
     */
    @FXML
    protected void drawTree(Circle[] nodes, Text[] labels, Line[] lineSet) {
        visualizerPane.getChildren().clear();
        visualizerPane.getChildren().add(nodes[0]);
        visualizerPane.getChildren().add(labels[0]);
        for (int i = 1; i < nodes.length; i++) {
            visualizerPane.getChildren().add(nodes[i]);
            visualizerPane.getChildren().add(labels[i]);
            visualizerPane.getChildren().add(lineSet[i - 1]);
        }

    }

    /**
     * Draw buckets for bucket sorting
     *
     * @param numBuckets the number of buckets to draw
     */
    @FXML
    protected void DrawBuckets(int[] inputArray, int numBuckets) {
        //update node values
        circleNodeValues = inputArray;
        //get bucket labels(for drawing labels and sorting values)
        int[] bucketLabels = GetBucketLabels(inputArray, numBuckets);
        //clear pane
        visualizerPane.getChildren().clear();

        //padding for buckets
        int padding = 50;
        //set bottom layer of buckets
        Rectangle base = new Rectangle(visualizerPane.getWidth() - padding, 5);
        base.setX(padding / 2);
        base.setY(visualizerPane.getHeight() - 10);
        visualizerPane.getChildren().add(base);
        //draw dividers to break up base
        double bucketIncrement = base.getWidth() / (numBuckets);
        int dividerHeight = 35;
        int dividerWidth = 5;
        for (int i = 0; i < numBuckets + 1; i++) {
            Rectangle divider = new Rectangle(dividerWidth, dividerHeight + 5);
            divider.setX(padding / 2 + (bucketIncrement * i));
            divider.setY(base.getY() - dividerHeight);
            visualizerPane.getChildren().add(divider);
            Text bucketLabel = new Text(String.valueOf(bucketLabels[i]));
            bucketLabel.setStroke(Color.WHITESMOKE);
            bucketLabel.setLayoutX(divider.getX() - 5);
            bucketLabel.setLayoutY(divider.getY() - 10);
            visualizerPane.getChildren().add(bucketLabel);
        }
    }

    @FXML
    protected void DrawBucketNodes(int[] input) {

        //circle and text nodes
        bucketNodes = new StackPane[input.length];
        Text[] treeText = new Text[input.length];

        //place the root node
        double firstX = 50;
        double firstY = 50;
        //set radius and x/y offset
        double radius = 20;
        double xOffset = (radius*2)+5;
        double yOffset = xOffset;

        //calculate max number of nodes per row
        int maxNPR = (int) ((visualizerPane.getWidth() - firstX) / (xOffset));

        int r = 0;
        int pos = 0;

        nodeLabels = new Text[input.length];
        for (int i = 0; i < input.length; i++) {
            //create circle
            Circle newCircle = new Circle(radius);
            //label circle for later reference
            newCircle.setId("myCircle");
            //create text
            Text newText = new Text(String.valueOf(input[i]));
            newText.setStroke(Color.WHITESMOKE);

            //create stackpane
            StackPane stackPane = new StackPane();

            // Set the text as the child of the circle
            stackPane.getChildren().addAll(newCircle, newText);

            // Center the text inside the circle
            newText.setBoundsType(TextBoundsType.VISUAL);
            newText.setTextOrigin(VPos.CENTER);
            newText.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                newText.setTranslateX(newCircle.getCenterX() - newText.getBoundsInLocal().getWidth() / 2);
                newText.setTranslateY(newCircle.getCenterY() - newText.getBoundsInLocal().getHeight() / 2);
            });

            // palce the stackPane
            stackPane.setTranslateX(firstX + (pos * xOffset));
            stackPane.setTranslateY(firstY + (r * yOffset));
            //store stackPane
            bucketNodes[i] = stackPane;
            //draw stackpane
            visualizerPane.getChildren().add(stackPane);
            nodeLabels[i] = newText;
            pos++;
            if (i + 1 >= maxNPR * (r + 1)) {
                r++;
                pos = 0;
            }
        }
    }

    /**
     * this function returns all the integer values that represent the bucket ranges
     *
     * @param input
     * @param numBuckets
     * @return
     */
    @FXML
    protected int[] GetBucketLabels(int[] input, int numBuckets) {
        //you need one more label than you have buckets
        //create empty array
        int[] bucketLabels = new int[numBuckets + 1];
        //get min and max from array
        int min = 0;
        int max = 0;
        for (int i = 0; i < input.length; i++) {
            if (input[i] > max)
                max = input[i];
            if (input[i] < min)
                min = input[i];
        }
        //calculate range of values
        int diff = max - min;
        //divide range by number of buckets
        int bucketSize = diff / numBuckets;
        //set labels
        bucketLabels[0] = min - 1;
        for (int i = 1; i < numBuckets; i++) {
            bucketLabels[i] = min + bucketSize * i;
        }
        bucketLabels[numBuckets] = max + 1;
        return bucketLabels;
    }

    /**
     *
     * @param inputArray array to be sorted
     * @param numBuckets number of buckets
     */
    protected void SetUpBucketSort(int[] inputArray, int numBuckets) {
        DrawBuckets(inputArray, numBuckets);
        DrawBucketNodes(inputArray);
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
            if ((now - lastTime) / 1000000000.0 > ANIM_DURATION / speed) {
                doHandle();
                lastTime = now;
            }
        }

        private void doHandle() {

            if (currentTransitionIndex < transitions.size()) {
                playNextAnim();
            } else {
                timer.stop();
            }
        }
    }

    /**
     * Returns a boolean indicating if the last transition animation is playing.
     *
     * @return boolean
     */
    private boolean lastTransitionsIsRunning() {
        if (transitions != null) {
            if (currentTransitionIndex == 0 || currentTransitionIndex > transitions.size() - 1)
                return false;
            return transitions.get(currentTransitionIndex - 1).forwardTransition.getStatus().toString().equals("RUNNING")
                    || transitions.get(currentTransitionIndex).reverseTransition.getStatus().toString().equals("RUNNING");
        }
        return false;
    }

    /**
     * converts a passed array of integers to a string
     *
     * @param intArray array of ints to be convereted to string
     * @return converted array into string of form [*,*,...,]
     */
    public String ConvertArrayToString(int[] intArray) {
        StringBuilder arrayString = new StringBuilder("[");
        for (int i = 0; i < intArray.length; i++) {
            arrayString.append(intArray[i]);
            if (i != intArray.length - 1)
                arrayString.append(", ");
        }
        arrayString.append("]");
        return arrayString.toString();
    }


}