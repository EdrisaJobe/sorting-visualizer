package UserInterface;

import Algorithms.*;
import javafx.animation.AnimationTimer;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
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
    private Label statusLabel;
    @FXML
    private Label sortedArrayLabel;
    @FXML
    private Label inputArraySatusLabel;
    @FXML
    private Pane visualizerPane;
    @FXML
    public ComboBox<String> sortDropdown;
    @FXML
    public ComboBox<String> searchDropdown;
    @FXML
    public ComboBox<String> speedDropdown;
    @FXML
    public ComboBox<String> nDropdown;
    @FXML
    private Text pseudoText;
    @FXML
    private Label algoStateLabel;
    @FXML
    private Label bestTimeLabel;
    @FXML
    private Label avgTimeLabel;
    @FXML
    private Label worstTimeLabel;
    @FXML
    private Label spaceCompLabel;
    @FXML
    private Button btnGenArray;
    @FXML
    private Button btnGenTree;
    @FXML
    private TextField arrayInputText;
    @FXML
    private Label arrayInputTextFieldLabel;
    @FXML
    private ComboBox<String> bucketSizeDropdown;

    MenuItem treeMenuItem;
    MenuItem arrayMenuItem;


    private int numBuckets = 2;
    private Line[] treeNodeLines;
    public static int treeMax = 190;
    public static int treeMin = 10;
    //stack panes used for the arrays in counting sort
    private StackPane[] stackPaneInputNodes;
    private StackPane[] stackPanePossibleValues;
    private StackPane[] stackPaneCountValues;
    private StackPane[] stackPaneIndexValues;

    private StackPane[] stackPaneShiftedIndex;
    private StackPane[] stackPaneSortedArray;
    //range of values in counting sort
    private final int[] possibleCountSortValues = new int[]{0, 1, 2, 3, 4, 5};


    private Rectangle[] boxes;
    private AnimationTimer timer;
    private AbstractAlgorithm algorithm;
    private BucketSort algorithmBucket;
    private AbstractAlgorithmTree algorithmTree;
    private CountingSort algorithmCounting;
    private String algorithmName = "Bubble Sort";
    private ArrayList<AlgoState> transitions = null;
    private int currentTransitionIndex = 0;
    private float speed = 1;
    private boolean isSearchMode = false;

    //Gap between blocks.
    private int x_gap;

    private int box_width;
    private int numOfBoxes = 10;
    private int[] nodeValuesInput = new int[numOfBoxes];


    /**
     * Initializes the UI elements.
     *
     * @param url            url
     * @param resourceBundle srBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sortDropdown.getItems().setAll("Bubble Sort", "Insertion Sort", "Quick Sort",
                "Selection Sort", "Merge Sort", "Bucket Sort", "Heap Sort", "Tree Sort", "Counting Sort");
        sortDropdown.setValue("Bubble Sort");

        searchDropdown.getItems().setAll("Linear Search", "Binary Search");
        searchDropdown.setValue("Pick Search Algorithm");

        speedDropdown.getItems().setAll("1x", "2x", "5x", "10x", "100x");
        speedDropdown.setValue("1x");

        nDropdown.getItems().setAll("10", "25", "50", "100");
        nDropdown.setValue("10");

        bucketSizeDropdown.getItems().setAll("2", "3", "4", "5");
        bucketSizeDropdown.setValue("2");

        timer = new AnimTimer();
    }


    /**
     * Moves one step forward in the animation.
     */
    @FXML
    protected void StepForward() {

        // Ignores if a transition is currently running
        if (transitions != null && !lastTransitionsIsRunning() && currentTransitionIndex < transitions.size()) {
            playNextAnim();
        }
    }

    /**
     * Plays next animation
     */
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


    /**
     * updates algorithm state at bottom of pane
     */
    private void UpdateState() {
        var variables = transitions.get(currentTransitionIndex).variables;
        var arrayStats = transitions.get(currentTransitionIndex).arrayStatus;

        if (variables.size() > 0) {
            StringBuilder vars = new StringBuilder("Algo State: ");
            for (int i = 0; i < variables.size() - 1; i++) {
                vars.append(variables.get(i).toString()).append(", ");
            }
            vars.append(variables.get(variables.size() - 1).toString());
            algoStateLabel.setText(String.valueOf(vars));
        }

        if (arrayStats != null) {
            sortedArrayLabel.setText(arrayStats);
        }
    }


    /**
     * Reset to allow for change to the array or algorithm
     */
    protected void ResetAlgorithm() {
        algorithm = null;
        algorithmTree = null;
        algorithmCounting = null;
        algorithmBucket = null;
        currentTransitionIndex = 0;
        this.transitions = null;
    }


    /**
     * Handles Sorting Algorithms Dropdown actions
     * Updates visualizer pane and algorithm on event
     */
    @FXML
    public void SortDropdownHandler() {
        if (isSearchMode) {
            arrayInputText.setDisable(false);
            arrayInputTextFieldLabel.setOpacity(1);
        }
        isSearchMode = false;

        String dropDownVal = sortDropdown.getValue();
        if (!algorithmName.equals(dropDownVal)) algorithmName = sortDropdown.getValue();
        if(algorithmName.equals("Tree Sort")) {
            btnGenTree.setDisable(false);
            btnGenArray.setDisable(true);
        }
        else{
            btnGenTree.setDisable(true);
            btnGenArray.setDisable(false);
        }
        LoadNewVisualizerPane();

    }



    /**
     * Handles Searching Dropdown events
     * Updates visualizer pane and algorithm on event
     */
    @FXML
    public void SearchDropdownHandler() {
        btnGenTree.setDisable(true);
        isSearchMode = true;
        String dropDownVal = searchDropdown.getValue();
        if (!algorithmName.equals(dropDownVal)) {
            algorithmName = searchDropdown.getValue();
            LoadNewVisualizerPane();
        }
        arrayInputText.setDisable(true);
        arrayInputTextFieldLabel.setOpacity(0.5);
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
                transitions.get(currentTransitionIndex - 1).forwardTransition.setOnFinished(e -> this.speed = value);
            } else
                this.speed = value;
        } else {
            this.speed = value;
        }
    }

    /**
     * Handles drop down events for the size of array
     * Updates visualizer pane and algorithm on event
     */
    @FXML
    public void nDropdownHandler() {
        String dropDownVal = nDropdown.getValue();
        if (!algorithmName.equals(dropDownVal)) {
            this.numOfBoxes = Integer.parseInt(dropDownVal);
            LoadNewVisualizerPane();
        }

    }

    /**
     * Using the current state of the algorithm this draws the right visualization
     */
    public void DrawInitialVisualization() {
        switch (algorithmName) {
            case "Tree Sort":
                GenerateBinaryTree();
                break;
            case "Bucket Sort":
                SetUpBucketSort(nodeValuesInput, numBuckets);
                break;
            case "Counting Sort":
                SetUpCountingSort(nodeValuesInput);
                break;
            default:
                SetUpBarGraph();
                break;
        }
    }

    /**
     * Gets the input array
     * Resets the algorithm
     * Draws the Visualizer
     * Prepares the Algorithm
     */
    public void LoadNewVisualizerPane(){
        StopTimer();
        GetInputArray();
        ResetAlgorithm();
        DrawInitialVisualization();
        PrepareAlgorithm();
    }

    /**
     * Prepares the transitions for the given algorithm on the given array
     */
    @FXML
    protected void PrepareAlgorithm() {

        switch (algorithmName) {
            case "Bubble Sort":
                bucketSizeDropdown.setVisible(false);
                algorithm = new BubbleSort(boxes, x_gap, box_width);
                break;
            case "Insertion Sort":
                bucketSizeDropdown.setVisible(false);
                algorithm = new InsertionSort(boxes, x_gap, box_width);
                break;
            case "Quick Sort":
                bucketSizeDropdown.setVisible(false);
                algorithm = new QuickSort(boxes, x_gap, box_width);
                break;
            case "Selection Sort":
                bucketSizeDropdown.setVisible(false);
                algorithm = new SelectionSort(boxes, x_gap, box_width);
                break;
            case "Merge Sort":
                bucketSizeDropdown.setVisible(false);
                algorithm = new MergeSort(boxes, x_gap, box_width);
                break;
            case "Bucket Sort":
                bucketSizeDropdown.setVisible(true);
                algorithmBucket = new BucketSort(stackPaneInputNodes, nodeValuesInput, numBuckets, visualizerPane.getWidth(), visualizerPane.getHeight());
                break;
            case "Heap Sort":
                bucketSizeDropdown.setVisible(false);
                algorithm = new HeapSort(boxes, x_gap, box_width);
                break;
            case "Tree Sort":
                bucketSizeDropdown.setVisible(false);
                algorithmTree = new TreeSort(stackPaneInputNodes, nodeValuesInput, treeNodeLines, visualizerPane.getWidth(), visualizerPane.getHeight());
                break;
            case "Linear Search":
                bucketSizeDropdown.setVisible(false);
                algorithm = new LinearSearch(boxes, x_gap, box_width);
                break;
            case "Binary Search":
                bucketSizeDropdown.setVisible(false);
                algorithm = new BinarySearch(boxes, x_gap, box_width);
                break;
            case "Counting Sort":
                algorithmCounting = new CountingSort(stackPaneInputNodes, stackPanePossibleValues, stackPaneCountValues, stackPaneSortedArray, nodeValuesInput, stackPaneIndexValues, stackPaneShiftedIndex);
                break;
        }
        boolean ifThereIsAlgorithm = false;
        if (algorithm != null) {
            statusLabel.setText("Selected Algorithm: " + algorithmName);
            pseudoText.setText(algorithm.pseudoCode);
            bestTimeLabel.setText(algorithm.bestTime);
            avgTimeLabel.setText(algorithm.averageTime);
            worstTimeLabel.setText(algorithm.worstTime);
            spaceCompLabel.setText(algorithm.spaceComplexity);
            this.transitions = algorithm.RunAlgorithm();
            ifThereIsAlgorithm = true;
        } else if (algorithmTree != null) {
            statusLabel.setText("Selected Algorithm: " + algorithmName);
            pseudoText.setText(algorithmTree.pseudoCode);
            bestTimeLabel.setText(AbstractAlgorithmTree.bestTime);
            avgTimeLabel.setText(AbstractAlgorithmTree.averageTime);
            worstTimeLabel.setText(AbstractAlgorithmTree.worstTime);
            spaceCompLabel.setText(AbstractAlgorithmTree.spaceComplexity);
            this.transitions = algorithmTree.RunAlgorithm();
            ifThereIsAlgorithm = true;
        } else if (algorithmBucket != null) {

            statusLabel.setText("Selected Algorithm: " + algorithmName);
            pseudoText.setText(algorithmBucket.pseudoCode);
            bestTimeLabel.setText(BucketSort.bestTime);
            avgTimeLabel.setText(BucketSort.averageTime);
            worstTimeLabel.setText(BucketSort.worstTime);
            spaceCompLabel.setText(BucketSort.spaceComplexity);
            this.transitions = algorithmBucket.RunAlgorithm();
            ifThereIsAlgorithm = true;
        } else if (algorithmCounting != null) {
            statusLabel.setText("Selected Algorithm: " + algorithmName);
            pseudoText.setText(algorithmCounting.pseudoCode);
            bestTimeLabel.setText(CountingSort.bestTime);
            avgTimeLabel.setText(CountingSort.averageTime);
            worstTimeLabel.setText(CountingSort.worstTime);
            spaceCompLabel.setText(CountingSort.spaceComplexity);
            this.transitions = algorithmCounting.RunAlgorithm();
            ifThereIsAlgorithm = true;
        }

        if (ifThereIsAlgorithm) {
            if (transitions.size() > 0) {
                //Loop until we find valid algo state as initial state.
                var variables = transitions.get(0).variables;

                int index = 0;
                boolean hasVariables = transitions.get(0).variables.size() !=0;
                if(hasVariables) {
                    while (variables.size() == 0) {
                        index++;
                        variables = transitions.get(index).variables;

                    }

                    StringBuilder vars = new StringBuilder("Algo State: ");

                    for (int i = 0; i < variables.size() - 1; i++) {
                        vars.append(variables.get(i).toString()).append(", ");
                    }
                    vars.append(variables.get(variables.size() - 1).toString());
                    algoStateLabel.setText(String.valueOf(vars));
                }else  algoStateLabel.setText("");
            }
            if (nodeValuesInput != null) {
                inputArraySatusLabel.setText(String.valueOf(ConvertArrayToString(nodeValuesInput)));
                sortedArrayLabel.setText("");
            }

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
     * Generates the Input array,
     * Either randomly for the algorithm
     * Or the user input
     */
    protected void GetInputArray() {
        if (!arrayInputText.getText().equals("") && !isSearchMode) {
            nodeValuesInput = convertToIntArray(arrayInputText.getText());
        } else if (algorithmName.equals("Counting Sort")) {
            nodeValuesInput = GenerateRandomCountingSortArray();
        } else if (algorithmName.equals("Tree Sort") || algorithmName.equals("Bucket Sort")) {
            nodeValuesInput = GenerateRandomTreeValues();
        } else nodeValuesInput = new int[numOfBoxes];
    }


    /**
     * Generates a bar graph for the input array
     */
    @FXML
    protected void SetUpBarGraph() {

        int size = this.nodeValuesInput.length;
        boolean userInput = !arrayInputText.getText().equals("");
        float HORIZONTAL_BUFFER = 4;
        double b = visualizerPane.getWidth() - HORIZONTAL_BUFFER;
        float BOX_WIDTH_RATIO = 2 / 3F;
        this.box_width = (int) (b / size * BOX_WIDTH_RATIO);
        this.x_gap = (int) b / size - box_width;
        if (box_width == 0)
            this.box_width = 1;
        float PANE_HEIGHT_RATIO = 5 / 6F;
        int paneHeight = (int) (visualizerPane.getHeight() * PANE_HEIGHT_RATIO);
        float MIN_BAR_HEIGHT = 1 / 6F;
        int minHeight = (int) (visualizerPane.getHeight() * MIN_BAR_HEIGHT);
        boxes = new Rectangle[size];

        int increment = (paneHeight - minHeight) / size;
        for (int i = 0; i < size; i++) {
            if (userInput && !isSearchMode)
                this.nodeValuesInput[i] = this.nodeValuesInput[i] + minHeight;
            else this.nodeValuesInput[i] = minHeight + increment * i;
        }
        //put into array list
        ArrayList<Integer> vals_list = new ArrayList<>();
        for (int j : this.nodeValuesInput) {
            vals_list.add(j);
        }
        if (!isSearchMode && arrayInputText.getText().equals("")) {
            //shuffle the order of the sizes
            Collections.shuffle(vals_list);
        }
        float adj;
        adj = x_gap / 2.0F;
        nodeValuesInput = new int[this.nodeValuesInput.length];
        for (int i = 0; i < this.nodeValuesInput.length; i++) {
            double height = vals_list.get(i);
            nodeValuesInput[i] = (int) height;
            boxes[i] = new Rectangle(box_width, height);
            boxes[i].setTranslateX((x_gap + box_width) * i + (visualizerPane.getWidth() / 2.0f - (x_gap + box_width) * (this.nodeValuesInput.length / 2.0f)) + adj);

            //Boxes are normally displayed on the top,
            //this line moves them down.
            boxes[i].setTranslateY(visualizerPane.getHeight() - height);
        }
        visualizerPane.getChildren().clear();
        visualizerPane.getChildren().addAll(boxes);
    }

    /**
     * Converts comma delimited string into int array
     *
     * @param input String of numbers to be converted
     * @return return int array
     */
    public static int[] convertToIntArray(String input) {
        String[] stringArray = input.split(","); // Split input string by comma
        int[] intArray = new int[stringArray.length];
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        // Parse each string element to int and find min/max values
        for (int i = 0; i < stringArray.length; i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
            if (intArray[i] < min) {
                min = intArray[i];
            }
            if (intArray[i] > max) {
                max = intArray[i];
            }
        }
        // Normalize each value to be between 0 and 100
        int range = max - min;
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = (int) (((double) (intArray[i] - min) / (double) range) * 250) + 50;
        }
        return intArray;
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

        int treeSize = 9;
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
     * Generate binary tree from array of values
     * creates an array of Circles, Lines, Text and calls a function to draw them to the pane
     */
    @FXML
    protected void GenerateBinaryTree() {

        visualizerPane.getChildren().clear();
        stackPaneInputNodes = new StackPane[nodeValuesInput.length];
        treeNodeLines = new Line[nodeValuesInput.length-1];

        //set radius and x/y offset
        double radius = 20;
        double xOffset = 35;
        double yOffset = 45;
        //place the root node
        double rootX = (visualizerPane.getWidth() / 2) -radius;
        double rootY = 25;

        double stokeWidth=4;


        //create root circle
        Circle rootCircle = new Circle(radius-(stokeWidth/2));
        //label circle for later reference
        rootCircle.setId("myCircle");
        rootCircle.setStrokeWidth(stokeWidth);
        rootCircle.setStroke(Color.BLACK);
        //create text
        Text circleText = new Text(String.valueOf(nodeValuesInput[0]));
        circleText.setStroke(Color.WHITESMOKE);
        //create stackpane
        StackPane stackPane = new StackPane();
        // Set the text as the child of the circle
        stackPane.getChildren().addAll(rootCircle, circleText);

        // Center the text inside the circle
        circleText.setBoundsType(TextBoundsType.VISUAL);
        circleText.setTextOrigin(VPos.CENTER);
        circleText.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            circleText.setTranslateX(rootCircle.getCenterX() - circleText.getBoundsInLocal().getWidth() / 2);
            circleText.setTranslateY(rootCircle.getCenterY() - circleText.getBoundsInLocal().getHeight() / 2);
        });

        // palce the stackPane
        stackPane.setTranslateX(rootX);
        stackPane.setTranslateY(rootY);
        //store stackPane
        stackPaneInputNodes[0] = stackPane;
        //draw stackpane
        visualizerPane.getChildren().add(stackPane);


        for (int i = 1; i < nodeValuesInput.length; i++) {
            double newCirclePosX = rootX;
            double newCirclePosY = rootY;
            int circleVal = nodeValuesInput[i];
            int max = treeMax;
            int min = treeMin;
            int height = 0;

            //Circle parent = (Circle) stackPaneNodes[0].lookup("#myCircle");
            StackPane parent = stackPaneInputNodes[0];
            for (int k = 0; k < i; k++) {
                if (nodeValuesInput[k] < circleVal && nodeValuesInput[k] >= min) {
                    height++;
                    newCirclePosX += Math.max(xOffset, xOffset * (7 - 2 * height));
                    newCirclePosY += Math.min(yOffset + (5 * height), yOffset + 20);
                    min = nodeValuesInput[k];
                    //parent = (Circle) stackPaneNodes[k].lookup("#myCircle");
                    parent = stackPaneInputNodes[k];
                } else if (nodeValuesInput[k] > circleVal && nodeValuesInput[k] <= max) {
                    height++;
                    newCirclePosX -= Math.max(xOffset, xOffset * (7 - 2 * height));
                    newCirclePosY += Math.min(yOffset + (5 * height), yOffset + 20);
                    max = nodeValuesInput[k];
                    //parent = (Circle) stackPaneNodes[k].lookup("#myCircle");
                    parent = stackPaneInputNodes[k];
                }
            }
            Line line1 = new Line(parent.getTranslateX()+radius, parent.getTranslateY()+radius + radius-5, newCirclePosX+radius, newCirclePosY+1);
            line1.setStrokeWidth(stokeWidth);
            treeNodeLines[i - 1] = line1;
            visualizerPane.getChildren().add(line1);

            Circle newChild = new Circle(radius-(stokeWidth/2));
            newChild.setStrokeWidth(stokeWidth);
            newChild.setStroke(Color.BLACK);
            newChild.setId("myCircle");
            Text newChildText = new Text(String.valueOf(nodeValuesInput[i]));
            newChildText.setStroke(Color.WHITESMOKE);

            //create stackpane
            stackPane = new StackPane();

            // Set the text as the child of the circle
            stackPane.getChildren().addAll(newChild, newChildText);

            // Center the text inside the circle
            newChildText.setBoundsType(TextBoundsType.VISUAL);
            newChildText.setTextOrigin(VPos.CENTER);
            newChildText.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                newChildText.setTranslateX(newChild.getCenterX() - newChildText.getBoundsInLocal().getWidth() / 2);
                newChildText.setTranslateY(newChild.getCenterY() - newChildText.getBoundsInLocal().getHeight() / 2);
            });

            // palce the stackPane
            stackPane.setTranslateX(newCirclePosX);
            stackPane.setTranslateY(newCirclePosY);
            //store stackPane
            stackPaneInputNodes[i] = stackPane;
            visualizerPane.getChildren().add(stackPane);

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
        StringBuilder arrayString = new StringBuilder();
        for (int i = 0; i < intArray.length; i++) {
            arrayString.append(intArray[i]);
            if (i != intArray.length - 1)
                arrayString.append(", ");
        }
        return arrayString.toString();
    }

    /**
     * Draw buckets for bucket sorting
     *
     * @param numBuckets the number of buckets to draw
     */
    @FXML
    protected void DrawBuckets(int[] inputArray, int numBuckets) {
        //update node values
        nodeValuesInput = inputArray;
        //get bucket labels(for drawing labels and sorting values)
        int[] bucketLabels = GetBucketLabels(inputArray, numBuckets);

        //padding for buckets
        int padding = 50;
        //set bottom layer of buckets
        Rectangle base = new Rectangle(visualizerPane.getWidth() - padding, 5);
        base.setX(padding / (double)2);
        base.setY(visualizerPane.getHeight() - 10);
        visualizerPane.getChildren().add(base);
        //draw dividers to break up base
        double bucketIncrement = base.getWidth() / (numBuckets);
        int dividerHeight = 35;
        int dividerWidth = 5;
        for (int i = 0; i < numBuckets + 1; i++) {
            Rectangle divider = new Rectangle(dividerWidth, dividerHeight + 5);
            divider.setX((padding / ((float) 2)) + (bucketIncrement * i));
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
        stackPaneInputNodes = new StackPane[input.length];

        //place the root node
        double firstX = 50;
        double firstY = 50;
        //set radius and x/y offset
        double radius = 20;
        double xOffset = (radius * 2) + 5;

        //calculate max number of nodes per row
        int maxNPR = (int) ((visualizerPane.getWidth() - firstX) / xOffset);

        int r = 0;
        int pos = 0;
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
            stackPane.setTranslateY(firstY + (r * xOffset));
            //store stackPane
            stackPaneInputNodes[i] = stackPane;
            //draw stackpane
            visualizerPane.getChildren().add(stackPane);
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
     * @param input Input array
     * @param numBuckets number of buckets
     * @return returns an integer array containing the diving integer values for the buckets
     */
    @FXML
    protected int[] GetBucketLabels(int[] input, int numBuckets) {
        //you need one more label than you have buckets
        //create empty array
        int[] bucketLabels = new int[numBuckets + 1];
        //get min and max from array
        int min = input[0];
        int max = 0;
        for (int j : input) {
            if (j > max)
                max = j;
            if (j < min)
                min = j;
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
     * @param inputArray array to be sorted
     * @param numBuckets number of buckets
     */
    protected void SetUpBucketSort(int[] inputArray, int numBuckets) {
        visualizerPane.getChildren().clear();
        DrawBuckets(inputArray, numBuckets);
        DrawBucketNodes(inputArray);
    }

    public void SetNumBuckets() {
        numBuckets = Integer.parseInt(bucketSizeDropdown.getValue());
        LoadNewVisualizerPane();
    }

    private void SetUpCountingSort(int[] inputArray) {
        visualizerPane.getChildren().clear();
        nodeValuesInput = inputArray;
        stackPaneInputNodes = new StackPane[nodeValuesInput.length];

        int squareSize = 30;
        int padding = 75;
        int textScale = 2;
        int strokeWidth = 4;
        Bounds endOfText = DrawArrayText(textScale, padding, squareSize);
        double nodeStartX = endOfText.getMaxX() + squareSize;
        stackPanePossibleValues = new StackPane[possibleCountSortValues.length];
        stackPaneCountValues = new StackPane[possibleCountSortValues.length];
        stackPaneIndexValues = new StackPane[possibleCountSortValues.length];
        stackPaneShiftedIndex = new StackPane[possibleCountSortValues.length];
        stackPaneSortedArray = new StackPane[nodeValuesInput.length];
        DrawCountingSortArrayBlocks(squareSize, padding, strokeWidth, nodeStartX);
    }


    private int[] GenerateRandomCountingSortArray() {

        int[] inputArray = new int[10];

        for (int i = 0; i < inputArray.length; i++) {
            inputArray[i] = (int) (Math.random() * 6);
        }

        return inputArray;
    }

    private Bounds DrawArrayText(int textScale, int padding, int squareSize) {

        String[] arrayText = new String[]{"Input Array:", "Possible Values:", "Count Values:", "Index Values:", "Shifted Index:", "Sorted Array:"};
        Bounds largestBound = null;

        //possible values and count values are closer together
        // skip padding is set to 1 after count values is added to the pane
        //this compensates the loop for missing one increment
        int skipPadding = 0;
        for (int i = 0; i < arrayText.length; i++) {
            Text newText = new Text(arrayText[i]);
            newText.setScaleX(textScale);
            newText.setScaleY(textScale);
            newText.setStroke(Color.WHITESMOKE);
            newText.setTranslateX(padding);
            if (i == 2 || i == 4) {
                newText.setTranslateY(padding * (i - skipPadding) + squareSize + 5);
                skipPadding += 1;
            } else newText.setTranslateY(padding * (i + 1 - skipPadding));
            visualizerPane.getChildren().add(newText);
            Bounds endOfText = newText.getBoundsInParent();
            if (largestBound == null) largestBound = endOfText;
            if (endOfText.getMaxX() > largestBound.getMaxX()) largestBound = endOfText;
        }
        return largestBound;
    }

    private void DrawCountingSortArrayBlocks(int squareSize, int padding, int strokeWidth, double nodeStartX) {

        int[] arraySizes = new int[]{nodeValuesInput.length, possibleCountSortValues.length, possibleCountSortValues.length, possibleCountSortValues.length, possibleCountSortValues.length, nodeValuesInput.length};
        int skipPadding = 0;
        for (int row = 0; row < arraySizes.length; row++) {

            for (int i = 0; i < arraySizes[row]; i++) {
                Rectangle newRect = new Rectangle(squareSize, squareSize);
                newRect.setStrokeWidth(strokeWidth);
                newRect.setId("myRect");
                newRect.setStroke(Color.WHITESMOKE);
                Text newText;
                if (row == 0) newText = new Text(String.valueOf(nodeValuesInput[i]));
                else if (row == 1) newText = new Text(String.valueOf(possibleCountSortValues[i]));
                else newText = new Text("0");

                newText.setStroke(Color.WHITESMOKE);
                newText.setId("myValue");

                //create stackpane
                StackPane stackPane = new StackPane();

                // Set the text as the child of the circle
                stackPane.getChildren().addAll(newRect, newText);

                // Center the text inside the circle
                newText.setBoundsType(TextBoundsType.VISUAL);
                newText.setTextOrigin(VPos.CENTER);
                Text finalNewText = newText;
                newText.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                    finalNewText.setTranslateX(newRect.getX() - finalNewText.getBoundsInLocal().getWidth() / 2);
                    finalNewText.setTranslateY(newRect.getY() - finalNewText.getBoundsInLocal().getHeight() / 2);
                });

                if (row == 2 || row == 4) {
                    stackPane.setTranslateY((padding * (row - skipPadding) + (squareSize + strokeWidth) / ((float) 2)));
                    if (i == arraySizes[row] - 1) skipPadding += 1;
                } else stackPane.setTranslateY((padding * (row + 1 - skipPadding)) - (squareSize + strokeWidth) / ((float) 2));

                stackPane.setTranslateX(nodeStartX + i * (squareSize + strokeWidth));

                visualizerPane.getChildren().add(stackPane);

                if (row == 0) stackPaneInputNodes[i] = stackPane;
                else if (row == 1) stackPanePossibleValues[i] = stackPane;
                else if (row == 2) stackPaneCountValues[i] = stackPane;
                else if (row == 3) stackPaneIndexValues[i] = stackPane;
                else if (row == 4) stackPaneShiftedIndex[i] = stackPane;
                else stackPaneSortedArray[i] = stackPane;
            }
        }
    }
}