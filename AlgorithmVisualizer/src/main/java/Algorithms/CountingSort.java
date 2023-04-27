package Algorithms;

import UserInterface.VisualizerController;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;

//TODO::Make a new viz for this.

/**
 * CountingSort algorithm
 */
public class CountingSort {


    //circles which are nodes
    private StackPane[] inputArray;
    private StackPane[] shiftedIndex;
    private StackPane[] possibleValuesStackPane;
    private StackPane[] countedValues;
    private StackPane[] sortedArray;
    private StackPane[] indexValues;
    //actual int values of nodes
    private int[] nodeValues;
    //possible values
    private int[] possibleValues;
    //transitions
    //List containing all the transitions that this algorithm makes.
    //This allows us to go backwards and forwards.
    private ArrayList<AlgoState> transitions = new ArrayList<>();
    //Duration of the transitions.

    public static String bestTime = "Ω(n)";
    public static String averageTime = "θ(n + n^2/k + k)";
    public static String worstTime = "O(n*k)";
    public static String spaceComplexity = "O(n+k)";

    public String pseudoCode = " create B empty buckets\n" +
            " for each element\n" +
            "     map element into a bucket\n" +
            " for each bucket\n" +
            "     sort each bucket\n" +
            "       concat all the sorted elements\n" +
            " output the sorted elements";


    //Highlight Colors.
    private final Color BASE_COLOR = Color.web("#000000");
    private final Color PRIMARY_COLOR = Color.web("#aeff80");
    private final Color SECONDARY_COLOR = Color.web("#fc6868");
    private final Color TARGET_COLOR = Color.web("#96f6ff");

    private final static float ANIM_DURATION = 0.95f;


    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public CountingSort(StackPane[] inputArray, StackPane[] possibleValuesStackPane, StackPane[] countedValues, StackPane[] sortedArray, int[] nodeValues, int[] possibleValues, StackPane[] indexValues, StackPane[] shiftedIndex) {
        this.inputArray = inputArray;
        this.possibleValuesStackPane = possibleValuesStackPane;
        this.countedValues = countedValues;
        this.sortedArray = sortedArray;
        this.nodeValues = nodeValues;
        this.possibleValues = possibleValues;
        this.indexValues = indexValues;
        this.shiftedIndex = shiftedIndex;
    }

    /**
     * The function that ultimately does the sorting.
     * This function should make use of the swap and Highlight functions to make transitions.
     * The state of the algorithm should be saved in each step of the algorithm in the transitions list.
     * Each AlgoState in the transitions list represents one step in the algorithm.
     *
     * @return The Arraylist of transitions.
     */
    public ArrayList<AlgoState> RunAlgorithm() {

        int[] trueCountState = new int[countedValues.length];

        for (int i = 0; i < inputArray.length; i++) {
            Rectangle node = (Rectangle) inputArray[i].lookup("#myRect");
            AlgoState stage = new AlgoState(SecondaryHighlightNode(node));
            stage.StoreVariable("i", 0);
            transitions.add(stage);


            Text textValue = (Text) inputArray[i].lookup("#myValue");
            int value = Integer.parseInt(textValue.getText());

            Rectangle nodeValueRect = (Rectangle) possibleValuesStackPane[value].lookup("#myRect");
            //SecondaryHighlightNode(nodeValueRect);

            stage = new AlgoState(SecondaryHighlightNode(nodeValueRect));
            stage.StoreVariable("i", 0);
            transitions.add(stage);


            Rectangle nodeCountRect = (Rectangle) countedValues[value].lookup("#myRect");
            //SecondaryHighlightNode(nodeValueRect);

            stage = new AlgoState(SecondaryHighlightNode(nodeCountRect));
            stage.StoreVariable("i", 0);
            transitions.add(stage);

            Text textCount = (Text) countedValues[value].lookup("#myValue");
            int count = trueCountState[value];
            count++;
            Timeline updateText = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(textCount.textProperty(), String.valueOf(trueCountState[value]))),
                    new KeyFrame(Duration.seconds(0.95), new KeyValue(textCount.textProperty(), String.valueOf(count)))
            );

            // Create the animation to move the Text node to the center of the StackPane
            Timeline centerText = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(textCount.translateXProperty(), textCount.getTranslateX()),
                            new KeyValue(textCount.translateYProperty(), textCount.getTranslateY())
                    ),
                    new KeyFrame(Duration.seconds(0.95),
                            new KeyValue(textCount.translateXProperty(), countedValues[value].getWidth() / 2),
                            new KeyValue(textCount.translateYProperty(), countedValues[value].getHeight() / 2)
                    )
            );

            trueCountState[value] = count;

            ParallelTransition forward = new ParallelTransition(updateText, centerText);
            Pair<Transition, Transition> anims = new Pair<>(forward, forward);

            stage = new AlgoState(anims);
            stage.StoreVariable("i", 0);
            transitions.add(stage);

            stage = new AlgoState(ResetHighlightNode(nodeValueRect));
            stage.StoreVariable("i", 0);
            transitions.add(stage);

            stage = new AlgoState(ResetHighlightNode(nodeCountRect));
            stage.StoreVariable("i", 0);
            transitions.add(stage);

        }
        int newSum = 0;
        int[] shiftedValue = new int[indexValues.length];
        for (int k = 0; k < indexValues.length; k++) {
            shiftedValue[k]=newSum;
            newSum += trueCountState[k];


            Text textVal = (Text) indexValues[k].lookup("#myValue");
            Timeline updateText = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(textVal.textProperty(), String.valueOf(0))),
                    new KeyFrame(Duration.seconds(0.95), new KeyValue(textVal.textProperty(), String.valueOf(newSum)))
            );
            // Create the animation to move the Text node to the center of the StackPane
            Timeline centerText = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(textVal.translateXProperty(), textVal.getTranslateX()),
                            new KeyValue(textVal.translateYProperty(), textVal.getTranslateY())
                    ),
                    new KeyFrame(Duration.seconds(0.95),
                            new KeyValue(textVal.translateXProperty(), indexValues[k].getWidth() / 2),
                            new KeyValue(textVal.translateYProperty(), indexValues[k].getHeight() / 2)
                    )
            );

            ParallelTransition forward = new ParallelTransition(updateText, centerText);
            Pair<Transition, Transition> anims = new Pair<>(forward, forward);
            AlgoState stage = new AlgoState(anims);
            stage.StoreVariable("i", 0);
            transitions.add(stage);

            Rectangle node = (Rectangle) indexValues[k].lookup("#myRect");
            stage = new AlgoState(SecondaryHighlightNode(node));
            stage.StoreVariable("i", 0);
            transitions.add(stage);
        }

        for (int k = 0; k < indexValues.length; k++) {
            int newValue = shiftedValue[k];


            Text textVal = (Text) shiftedIndex[k].lookup("#myValue");
            Timeline updateText = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(textVal.textProperty(), String.valueOf(0))),
                    new KeyFrame(Duration.seconds(0.95), new KeyValue(textVal.textProperty(), String.valueOf(newValue)))
            );
            // Create the animation to move the Text node to the center of the StackPane
            Timeline centerText = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(textVal.translateXProperty(), textVal.getTranslateX()),
                            new KeyValue(textVal.translateYProperty(), textVal.getTranslateY())
                    ),
                    new KeyFrame(Duration.seconds(0.95),
                            new KeyValue(textVal.translateXProperty(), shiftedIndex[k].getWidth() / 2),
                            new KeyValue(textVal.translateYProperty(), shiftedIndex[k].getHeight() / 2)
                    )
            );

            ParallelTransition forward = new ParallelTransition(updateText, centerText);
            Pair<Transition, Transition> anims = new Pair<>(forward, forward);
            AlgoState stage = new AlgoState(anims);
            stage.StoreVariable("i", 0);
            transitions.add(stage);

            Rectangle node = (Rectangle) shiftedIndex[k].lookup("#myRect");
            stage = new AlgoState(SecondaryHighlightNode(node));
            stage.StoreVariable("i", 0);
            transitions.add(stage);
        }

        for (int k = 0; k < nodeValues.length; k++) {
            int val = 0;
            while (nodeValues[k] != val) val++;
            //highlight shifted index
            Rectangle node = (Rectangle) shiftedIndex[val].lookup("#myRect");
            AlgoState stage = new AlgoState(SecondaryHighlightNode(node));
            stage.StoreVariable("i", 0);
            transitions.add(stage);

            //highlight sorted array insert
            int indexInsert = shiftedValue[val];
            shiftedValue[val]++;
            trueCountState[val]++;
            node = (Rectangle) sortedArray[indexInsert].lookup("#myRect");
            stage = new AlgoState(SecondaryHighlightNode(node));
            stage.StoreVariable("i", 0);
            transitions.add(stage);
            //update sorted array text
            Text textVal = (Text) sortedArray[indexInsert].lookup("#myValue");
            Timeline updateText = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(textVal.textProperty(), String.valueOf(0))),
                    new KeyFrame(Duration.seconds(0.95), new KeyValue(textVal.textProperty(), String.valueOf(nodeValues[k])))
            );
            // Create the animation to move the Text node to the center of the StackPane
            Timeline centerText = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(textVal.translateXProperty(), textVal.getTranslateX()),
                            new KeyValue(textVal.translateYProperty(), textVal.getTranslateY())
                    ),
                    new KeyFrame(Duration.seconds(0.95),
                            new KeyValue(textVal.translateXProperty(), sortedArray[indexInsert].getWidth() / 2),
                            new KeyValue(textVal.translateYProperty(), sortedArray[indexInsert].getHeight() / 2)
                    )
            );

            ParallelTransition forward = new ParallelTransition(updateText, centerText);
            Pair<Transition, Transition> anims = new Pair<>(forward, forward);
            stage = new AlgoState(anims);
            stage.StoreVariable("i", 0);
            transitions.add(stage);

            //updated shifted text
            textVal = (Text) shiftedIndex[val].lookup("#myValue");
            updateText = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(textVal.textProperty(), String.valueOf(0))),
                    new KeyFrame(Duration.seconds(0.95), new KeyValue(textVal.textProperty(), String.valueOf(trueCountState[val] + 1)))
            );
            // Create the animation to move the Text node to the center of the StackPane
            centerText = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(textVal.translateXProperty(), textVal.getTranslateX()),
                            new KeyValue(textVal.translateYProperty(), textVal.getTranslateY())
                    ),
                    new KeyFrame(Duration.seconds(0.95),
                            new KeyValue(textVal.translateXProperty(), sortedArray[indexInsert].getWidth() / 2),
                            new KeyValue(textVal.translateYProperty(), sortedArray[indexInsert].getHeight() / 2)
                    )
            );

            forward = new ParallelTransition(updateText, centerText);
            anims = new Pair<>(forward, forward);
            stage = new AlgoState(anims);
            stage.StoreVariable("i", 0);
            transitions.add(stage);


        }

        return transitions;
    }


    /**
     * Returns a transition of highlighting a node with the secondary color.
     *
     * @param index Index of the node to highlight.
     * @return The transition highlighting the node.
     */
    final public Pair<Transition, Transition> SecondaryHighlightNode(Rectangle node) {
        StrokeTransition strokeChangeForward = new StrokeTransition(Duration.seconds(ANIM_DURATION), node, BASE_COLOR, SECONDARY_COLOR);
        StrokeTransition strokeChangeReverse = new StrokeTransition(Duration.seconds(ANIM_DURATION), node, SECONDARY_COLOR, BASE_COLOR);
        ParallelTransition forward = new ParallelTransition(strokeChangeForward);
        ParallelTransition reverse = new ParallelTransition(strokeChangeReverse);

        return new Pair<>(forward, reverse);
    }

    /**
     * Returns a transition of highlighting a node with the secondary color.
     *
     * @param index Index of the node to highlight.
     * @return The transition highlighting the node.
     */
    final public Pair<Transition, Transition> ResetHighlightNode(Rectangle node) {
        //Rectangle node = (Rectangle) inputArray[index].lookup("#myRect");
        StrokeTransition strokeChangeForward = new StrokeTransition(Duration.seconds(ANIM_DURATION), node, SECONDARY_COLOR, Color.WHITESMOKE);
        StrokeTransition strokeChangeReverse = new StrokeTransition(Duration.seconds(ANIM_DURATION), node, Color.WHITESMOKE, SECONDARY_COLOR);
        ParallelTransition forward = new ParallelTransition(strokeChangeForward);
        ParallelTransition reverse = new ParallelTransition(strokeChangeReverse);

        return new Pair<>(forward, reverse);
    }

}