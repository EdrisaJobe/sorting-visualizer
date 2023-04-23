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
    @FXML
    private TextField arrayInput;
    @FXML
    private Label arrayInputLabel;
    @FXML
    private ComboBox<String> bucketSize;


    private int treeSize = 9;
    private int numBuckets = 2;
    private Circle[] treeNodes;
    private StackPane[] stackPaneNodes;
    private StackPane[] stackPanePossibleValues;
    private StackPane[] stackPaneCountValues;
    private StackPane[] stackPaneSortedArray;
    private Text[] nodeLabels;
    private int[] NodeValues;
    private Line[] treeNodeLines;
    static int treeMax = 190;
    static int treeMin = 10;

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
                "Selection Sort", "Merge Sort", "Bucket Sort", "Heap Sort", "Tree Sort", "Counting Sort");
        sortDropdown.setValue("Bubble Sort");

        searchDropdown.getItems().setAll("Linear Search");
        searchDropdown.setValue("Pick Search Algorithm");

        speedDropdown.getItems().setAll("1x", "2x", "3x", "5x", "10x");
        speedDropdown.setValue("1x");

        nDropdown.getItems().setAll("10", "25", "50", "100");
        nDropdown.setValue("10");

        bucketSize.getItems().setAll("2", "3", "4",
                "5");
        bucketSize.setValue("2");

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
        isSearchMode = false;
        arrayInput.setDisable(false);
        arrayInputLabel.setOpacity(1);
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
        isSearchMode = true;
        String dropDownVal = searchDropdown.getValue();
        if (!algorithmName.equals(dropDownVal)) {
            algorithmName = searchDropdown.getValue();
            GenerateArray();
        }
        arrayInput.setDisable(true);
        arrayInputLabel.setOpacity(0.5);
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
        boolean isCustomVis = false;
        switch (algorithmName) {
            case "Bubble Sort":
                bucketSize.setVisible(false);
                algorithm = new BubbleSort(boxes, x_gap, box_width);
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Insertion Sort":
                bucketSize.setVisible(false);
                algorithm = new InsertionSort(boxes, x_gap, box_width);
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Quick Sort":
                bucketSize.setVisible(false);
                algorithm = new QuickSort(boxes, x_gap, box_width);
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Selection Sort":
                bucketSize.setVisible(false);
                algorithm = new SelectionSort(boxes, x_gap, box_width);
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Merge Sort":
                bucketSize.setVisible(false);
                algorithm = new MergeSort(boxes, x_gap, box_width);
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Bucket Sort":

                bucketSize.setVisible(true);
                int[] test1 = GenerateRandomTreeValues();
                //will update once merged with main
                SetUpBucketSort(test1, numBuckets);
                algorithmBucket = new BucketSort(stackPaneNodes, NodeValues, numBuckets, visualizerPane.getWidth(), visualizerPane.getHeight());
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Heap Sort":
                bucketSize.setVisible(false);
                algorithm = new HeapSort(boxes, x_gap, box_width);
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Tree Sort":
                bucketSize.setVisible(false);
                algorithmTree = new TreeSort(treeNodes, NodeValues, treeNodeLines);
                btnGenArray.setDisable(true);
                btnGenTree.setDisable(false);
                isCustomVis = true;
                break;
            case "Linear Search":
                bucketSize.setVisible(false);
                algorithm = new LinearSearch(boxes, x_gap, box_width);
                btnGenArray.setDisable(false);
                btnGenTree.setDisable(true);
                break;
            case "Counting Sort":
                SetUpCountingSort();
                algorithmCounting = new CountingSort(stackPaneNodes, stackPanePossibleValues, stackPaneCountValues, stackPaneSortedArray, NodeValues, new int[]{0, 1, 2, 3, 4, 5});
                //CountingSort(StackPane[] inputArray, StackPane[] possibleValuesStackPane, StackPane[] countedValues,StackPane[] sortedArray,int[] nodeValues,int[] possibleValues)
                break;
        }
        if (isCustomVis) {
            arrayInput.setDisable(true);
            arrayInputLabel.setOpacity(0.5);
        } else {
            arrayInput.setDisable(false);
            arrayInputLabel.setOpacity(1);
        }
        if (algorithm != null) {
            statusText.setText("Selected Algorithm: " + algorithmName);
            pseudoText.setText(algorithm.pseudoCode);
            bestTime.setText(algorithm.bestTime);
            avgTime.setText(algorithm.averageTime);
            worstTime.setText(algorithm.worstTime);
            spaceComp.setText(algorithm.spaceComplexity);
            this.transitions = algorithm.RunAlgorithm();

            if (transitions.size() > 0) {
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
            }

            if (NodeValues != null) {
                StringBuilder input = new StringBuilder("");
                input.append(ConvertArrayToString(NodeValues));
                inputArrayLabel.setText(String.valueOf(input));
                sortedArray.setText("");
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
            input.append(ConvertArrayToString(NodeValues));
            inputArrayLabel.setText(String.valueOf(input));
        } else if (algorithmBucket != null) {

            statusText.setText("Selected Algorithm: " + algorithmName);
            pseudoText.setText(algorithmBucket.pseudoCode);
            bestTime.setText(algorithmBucket.bestTime);
            avgTime.setText(algorithmBucket.averageTime);
            worstTime.setText(algorithmBucket.worstTime);
            spaceComp.setText(algorithmBucket.spaceComplexity);
            this.transitions = algorithmBucket.RunAlgorithm();
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
            input.append(ConvertArrayToString(NodeValues));
            inputArrayLabel.setText(String.valueOf(input));

        }
        else if (algorithmBucket != null) {

            statusText.setText("Selected Algorithm: " + algorithmName);
            pseudoText.setText(algorithmBucket.pseudoCode);
            bestTime.setText(algorithmBucket.bestTime);
            avgTime.setText(algorithmBucket.averageTime);
            worstTime.setText(algorithmBucket.worstTime);
            spaceComp.setText(algorithmBucket.spaceComplexity);
            this.transitions = algorithmBucket.RunAlgorithm();
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
            input.append(ConvertArrayToString(NodeValues));
            inputArrayLabel.setText(String.valueOf(input));

        }
        else if (algorithmCounting != null) {

            statusText.setText("Selected Algorithm: " + algorithmName);
            pseudoText.setText(algorithmCounting.pseudoCode);
            bestTime.setText(algorithmCounting.bestTime);
            avgTime.setText(algorithmCounting.averageTime);
            worstTime.setText(algorithmCounting.worstTime);
            spaceComp.setText(algorithmCounting.spaceComplexity);
            this.transitions = algorithmCounting.RunAlgorithm();
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
            input.append(ConvertArrayToString(NodeValues));
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

        ResetAlgorithm();
        StopTimer();
        boolean shouldRandomize = !algorithmName.equals("Binary Search");
        int[] poss_values = null;
        int size = numOfBoxes;

        if (!arrayInput.getText().equals("") && !isSearchMode) {
            System.out.println("NOT EMPTY");
            shouldRandomize = false;
            poss_values = convertToIntArray(arrayInput.getText());
            size = poss_values.length;
        }

        double b = visualizerPane.getWidth() - HORIZONTAL_BUFFER;
        this.box_width = (int) (b / size * BOX_WIDTH_RATIO);
        this.x_gap = (int) b / size - box_width;

        if (box_width == 0)
            this.box_width = 1;

        int paneHeight = (int) (visualizerPane.getHeight() * PANE_HEIGHT_RATIO);
        int minHeight = (int) (visualizerPane.getHeight() * MIN_BAR_HEIGHT);
        boxes = new Rectangle[size];

        //possible height values

        if (poss_values == null) {
            poss_values = new int[size];
            int increment = (paneHeight - minHeight) / size;
            for (int i = 0; i < size; i++) {
                poss_values[i] = minHeight + increment * i;
            }
        }

        //put into array list
        ArrayList<Integer> vals_list = new ArrayList<>();
        for (int k = 0; k < poss_values.length; k++) {
            vals_list.add(poss_values[k]);
        }
        if (shouldRandomize) {
            //shuffle the order of the sizes
            Collections.shuffle(vals_list);
        }

        float adj = 0;
        if (x_gap != 0) ;
        adj = x_gap / 2.0F;
        NodeValues = new int[poss_values.length];
        for (int i = 0; i < poss_values.length; i++) {
            double height = vals_list.get(i);
            NodeValues[i] = (int) height;
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
     * Converts comma delimited string into int array
     *
     * @param input
     * @return
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
        NodeValues = newNodeVals;

        //cirlces for nodes
        treeNodes = new Circle[NodeValues.length];

        //text for node
        Text[] treeText = new Text[NodeValues.length];
        //node liens
        treeNodeLines = new Line[treeNodes.length - 1];

        //place the root node
        double rootX = visualizerPane.getWidth() / 2;
        double rootY = 25;
        //set radius and x/y offset
        double radius = 20;
        double xOffset = 35;
        double yOffset = 45;

        Circle rootCircle = new Circle(rootX, rootY, radius);
        treeNodes[0] = rootCircle;
        rootCircle.setStrokeWidth(4);
        int rootValue = NodeValues[0];
        Text rootText = new Text(String.valueOf(rootValue));
        rootText.setStroke(Color.WHITESMOKE);
        rootText.setLayoutX(rootX - 5);
        rootText.setLayoutY(rootY + 5);
        treeText[0] = rootText;
        for (int i = 1; i < NodeValues.length; i++) {
            double newCirclePosX = rootX;
            double newCirclePosY = rootY;
            int circleVal = NodeValues[i];
            int max = treeMax;
            int min = treeMin;
            int height = 0;

            Circle parent = treeNodes[0];
            for (int k = 0; k < i; k++) {
                if (NodeValues[k] < circleVal && NodeValues[k] >= min) {
                    height++;
                    newCirclePosX += Math.max(xOffset, xOffset * (7 - 2 * height));
                    newCirclePosY += Math.min(yOffset + (5 * height), yOffset + 20);
                    min = NodeValues[k];
                    parent = treeNodes[k];
                } else if (NodeValues[k] > circleVal && NodeValues[k] <= max) {
                    height++;
                    newCirclePosX -= Math.max(xOffset, xOffset * (7 - 2 * height));
                    newCirclePosY += Math.min(yOffset + (5 * height), yOffset + 20);
                    max = NodeValues[k];
                    parent = treeNodes[k];
                }
            }
            Circle newChild = new Circle(newCirclePosX, newCirclePosY, radius);
            newChild.setStrokeWidth(4);
            Text newChildText = new Text(String.valueOf(NodeValues[i]));
            newChildText.setStroke(Color.WHITESMOKE);
            newChildText.setLayoutX(newCirclePosX - 5);
            newChildText.setLayoutY(newCirclePosY + 5);
            treeText[i] = newChildText;
            treeNodes[i] = newChild;
            Line line1 = new Line(parent.getCenterX(), parent.getCenterY() + radius, newCirclePosX, newCirclePosY - radius);
            line1.setStrokeWidth(4);
            treeNodeLines[i - 1] = line1;
        }

        drawTree(treeNodes, treeText, treeNodeLines);

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

    /**
     * Draw buckets for bucket sorting
     *
     * @param numBuckets the number of buckets to draw
     */
    @FXML
    protected void DrawBuckets(int[] inputArray, int numBuckets) {
        //update node values
        NodeValues = inputArray;
        //get bucket labels(for drawing labels and sorting values)
        int[] bucketLabels = GetBucketLabels(inputArray, numBuckets);

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
        stackPaneNodes = new StackPane[input.length];
        Text[] treeText = new Text[input.length];

        //place the root node
        double firstX = 50;
        double firstY = 50;
        //set radius and x/y offset
        double radius = 20;
        double xOffset = (radius * 2) + 5;
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
            stackPaneNodes[i] = stackPane;
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
        int min = input[0];
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
     * @param inputArray array to be sorted
     * @param numBuckets number of buckets
     */
    protected void SetUpBucketSort(int[] inputArray, int numBuckets) {
        visualizerPane.getChildren().clear();
        DrawBuckets(inputArray, numBuckets);
        DrawBucketNodes(inputArray);
    }

    public void SetNumBuckets() {
        numBuckets = Integer.valueOf(bucketSize.getValue());
        SetUpBucketSort(NodeValues, numBuckets);
        ResetAlgorithm();
        PrepareAlgorithm();
    }

    private void SetUpCountingSort() {

        int[] testArray = new int[]{0, 0, 1, 1, 2, 3, 4, 5};

        stackPaneNodes = new StackPane[testArray.length];

        int squareSize = 30;
        int padding = 75;
        Text inputArray = new Text("Input Array :");
        inputArray.setScaleX(2);
        inputArray.setScaleY(2);
        inputArray.setStroke(Color.WHITESMOKE);
        inputArray.setTranslateX(padding);
        inputArray.setTranslateY(padding);
        visualizerPane.getChildren().clear();
        visualizerPane.getChildren().add(inputArray);

        Bounds endOfText = inputArray.getBoundsInParent();
        double nodeStartX = endOfText.getMaxX() + squareSize;

        for (int i = 0; i < testArray.length; i++) {
            Rectangle newRect = new Rectangle(squareSize, squareSize);
            newRect.setStrokeWidth(4);
            newRect.setId("myRect");
            newRect.setStroke(Color.WHITESMOKE);

            Text newText = new Text(String.valueOf(testArray[i]));
            newText.setStroke(Color.WHITESMOKE);
            newText.setId("myValue");

            //create stackpane
            StackPane stackPane = new StackPane();

            // Set the text as the child of the circle
            stackPane.getChildren().addAll(newRect, newText);

            // Center the text inside the circle
            newText.setBoundsType(TextBoundsType.VISUAL);
            newText.setTextOrigin(VPos.CENTER);
            newText.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                newText.setTranslateX(newRect.getX() - newText.getBoundsInLocal().getWidth() / 2);
                newText.setTranslateY(newRect.getY() - newText.getBoundsInLocal().getHeight() / 2);
            });

            // palce the stackPane
            stackPane.setTranslateX(nodeStartX + (i * (squareSize+4)));
            stackPane.setTranslateY(padding - squareSize / 2);

            visualizerPane.getChildren().add(stackPane);
            stackPaneNodes[i] = stackPane;
        }

        DrawPossibleValues();
        DrawSortedArray();
    }

    private void DrawPossibleValues() {

        int[] rangeValues = new int[]{0, 1, 2, 3, 4, 5};
        int[] valueCounts = new int[rangeValues.length];
        stackPanePossibleValues = new StackPane[rangeValues.length];
        stackPaneCountValues = new StackPane[valueCounts.length];

        int squareSize = 30;
        int padding = 75;
        Text possValues = new Text("Possible Values :");
        possValues.setScaleX(2);
        possValues.setScaleY(2);
        possValues.setStroke(Color.WHITESMOKE);
        possValues.setTranslateX(padding);
        possValues.setTranslateY(padding * 2);
        visualizerPane.getChildren().add(possValues);

        Text countValues = new Text("Count Values :");
        countValues.setScaleX(2);
        countValues.setScaleY(2);
        countValues.setStroke(Color.WHITESMOKE);
        countValues.setTranslateX(padding);
        countValues.setTranslateY((padding * 2) + squareSize);
        visualizerPane.getChildren().add(countValues);

        Bounds endOfText = possValues.getBoundsInParent();
        double nodeStartX = endOfText.getMaxX() + squareSize;

        for (int i = 0; i < rangeValues.length; i++) {
            Rectangle newRectValue = new Rectangle(squareSize, squareSize);
            newRectValue.setId("myRect");
            newRectValue.setStrokeWidth(4);
            newRectValue.setStroke(Color.WHITESMOKE);
            Rectangle newRectCount = new Rectangle(squareSize, squareSize);
            newRectCount.setId("myRect");
            newRectCount.setStrokeWidth(4);
            newRectCount.setStroke(Color.WHITESMOKE);
            Text newTextValue = new Text(String.valueOf(rangeValues[i]));
            Text newTextCount = new Text(String.valueOf(valueCounts[i]));
            newTextValue.setStroke(Color.WHITESMOKE);
            newTextValue.setId("myValue");
            newTextCount.setStroke(Color.WHITESMOKE);
            newTextCount.setId("myValue");

            //create stackpane
            StackPane stackPaneValue = new StackPane();
            StackPane stackPaneCount = new StackPane();

            // Set the text as the child of the circle
            stackPaneValue.getChildren().addAll(newRectValue, newTextValue);
            stackPaneCount.getChildren().addAll(newRectCount, newTextCount);

            // Center the text inside the circle
            newTextValue.setBoundsType(TextBoundsType.VISUAL);
            newTextValue.setTextOrigin(VPos.CENTER);
            newTextValue.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                newTextValue.setTranslateX(newRectValue.getX() - newTextValue.getBoundsInLocal().getWidth() / 2);
                newTextValue.setTranslateY(newRectValue.getY() - newTextValue.getBoundsInLocal().getHeight() / 2);
            });

            // Center the text inside the circle
            newTextCount.setBoundsType(TextBoundsType.VISUAL);
            newTextCount.setTextOrigin(VPos.CENTER);
            newTextCount.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                newTextCount.setTranslateX(newRectCount.getX() - newTextCount.getBoundsInLocal().getWidth() / 2);
                newTextCount.setTranslateY(newRectCount.getY() - newTextCount.getBoundsInLocal().getHeight() / 2);
            });

            // palce the stackPane
            stackPaneValue.setTranslateX(nodeStartX + (i * (squareSize+4)));
            stackPaneValue.setTranslateY((padding * 2) - squareSize / 2);

            stackPaneCount.setTranslateX(nodeStartX + (i * (squareSize+4)));
            stackPaneCount.setTranslateY((padding * 2) - (squareSize / 2) + squareSize+4);

            visualizerPane.getChildren().addAll(stackPaneValue, stackPaneCount);
            stackPanePossibleValues[i] = stackPaneValue;
            stackPaneCountValues[i] = stackPaneCount;
        }


    }

    private void DrawSortedArray() {
        int[] testArray = new int[]{0, 0, 1, 1, 2, 3, 4, 5};
        int[] sortedArray = new int[testArray.length];
        stackPaneSortedArray = new StackPane[sortedArray.length];

        int squareSize = 30;
        int padding = 75;
        Text sortedArrayText = new Text("Sorted Array :");
        sortedArrayText.setScaleX(2);
        sortedArrayText.setScaleY(2);
        sortedArrayText.setStroke(Color.WHITESMOKE);
        sortedArrayText.setTranslateX(padding);
        sortedArrayText.setTranslateY(padding * 3);
        visualizerPane.getChildren().add(sortedArrayText);

        Bounds endOfText = sortedArrayText.getBoundsInParent();
        double nodeStartX = endOfText.getMaxX() + squareSize;

        for (int i = 0; i < sortedArray.length; i++) {
            Rectangle newRectValue = new Rectangle(squareSize, squareSize);
            newRectValue.setId("myRect");
            newRectValue.setStrokeWidth(4);
            newRectValue.setStroke(Color.WHITESMOKE);
            Text newTextValue = new Text(String.valueOf(sortedArray[i]));
            newTextValue.setStroke(Color.WHITESMOKE);
            newTextValue.setId("myValue");

            //create stackpane
            StackPane stackPaneValue = new StackPane();

            // Set the text as the child of the circle
            stackPaneValue.getChildren().addAll(newRectValue, newTextValue);

            // Center the text inside the circle
            newTextValue.setBoundsType(TextBoundsType.VISUAL);
            newTextValue.setTextOrigin(VPos.CENTER);
            newTextValue.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                newTextValue.setTranslateX(newRectValue.getX() - newTextValue.getBoundsInLocal().getWidth() / 2);
                newTextValue.setTranslateY(newRectValue.getY() - newTextValue.getBoundsInLocal().getHeight() / 2);
            });

            // palce the stackPane
            stackPaneValue.setTranslateX(nodeStartX + (i * squareSize));
            stackPaneValue.setTranslateY((padding * 3) - squareSize / 2);

            visualizerPane.getChildren().addAll(stackPaneValue);
            stackPaneSortedArray[i] = stackPaneValue;
        }
    }

}