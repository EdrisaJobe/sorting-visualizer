package Algorithms;

import javafx.animation.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;

//TODO::Make a new viz for this.

/**
 * CountingSort algorithm
 */
public class CountingSort {


    //circles which are nodes
    private final StackPane[] inputArray;
    private final StackPane[] shiftedIndex;
    private final StackPane[] possibleValuesStackPane;
    private final StackPane[] countedValues;
    private final StackPane[] sortedArray;
    private final StackPane[] indexValues;
    //actual int values of nodes
    private final int[] nodeValues;
    //transitions
    //List containing all the transitions that this algorithm makes.
    //This allows us to go backwards and forwards.
    private final ArrayList<AlgoState> transitions = new ArrayList<>();
    //Duration of the transitions.

    public static String bestTime = "Ω(n+k)";
    public static String averageTime = "θ(n+k)";
    public static String worstTime = "O(n+k)";
    public static String spaceComplexity = "O(k)";

    public String pseudoCode = "Count the number of occurrences for each value \n\n" +
            "Add the previous count to each current count\n\n" +
            "Shift the new count values to the right one index to get the first index of insert\n\n" +
            "Loop back through input array. For each value find the first index of insert in the shifted index array.\n" +
            "Insert into sorted array at that index. Increment the index by 1\n";


    //Highlight Colors.
    private final Color BASE_COLOR = Color.web("#000000");
    private final Color STROKE_BASE = Color.WHITESMOKE;
    private final Color PRIMARY_COLOR = Color.web("#aeff80");
    private final Color SECONDARY_COLOR = Color.web("#fc6868");
    private final Color TARGET_COLOR = Color.web("#1E3F66");

    private final static float ANIM_DURATION = 0.95f;


    /**
     * @param inputArray              stack pane of input array
     * @param possibleValuesStackPane stack pane of possible values in the input
     * @param countedValues           ...
     * @param sortedArray             ...
     * @param nodeValues              ...
     * @param indexValues             ...
     * @param shiftedIndex            ...
     */
    public CountingSort(StackPane[] inputArray, StackPane[] possibleValuesStackPane, StackPane[] countedValues, StackPane[] sortedArray, int[] nodeValues, StackPane[] indexValues, StackPane[] shiftedIndex) {
        this.inputArray = inputArray;
        this.possibleValuesStackPane = possibleValuesStackPane;
        this.countedValues = countedValues;
        this.sortedArray = sortedArray;
        this.nodeValues = nodeValues;
        //possible values
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
        int[] sortedArrayStatus = new int[nodeValues.length];

        for (int i = 0; i < inputArray.length; i++) {
            //higlight node in the input array
            Rectangle node = (Rectangle) inputArray[i].lookup("#myRect");
            AlgoState stage = new AlgoState(CustomHighlightNode(new Rectangle[]{node}, STROKE_BASE, SECONDARY_COLOR));
            transitions.add(stage);

            //highlight value slot in possible input value
            int value = nodeValues[i];
            Rectangle nodeValueRect = (Rectangle) possibleValuesStackPane[value].lookup("#myRect");
            stage = new AlgoState(CustomHighlightNode(new Rectangle[]{nodeValueRect}, STROKE_BASE, SECONDARY_COLOR));
            transitions.add(stage);

            //highlight the count node under the possible input values
            Rectangle nodeCountRect = (Rectangle) countedValues[value].lookup("#myRect");
            stage = new AlgoState(CustomHighlightNode(new Rectangle[]{nodeCountRect}, STROKE_BASE, SECONDARY_COLOR));
            transitions.add(stage);

            //animate the text update
            Text textCount = (Text) countedValues[value].lookup("#myValue");
            ParallelTransition forward = UpdateText(textCount, trueCountState[value], trueCountState[value] += 1);
            Pair<Transition, Transition> anims = new Pair<>(forward, forward);
            stage = new AlgoState(anims);
            transitions.add(stage);

            //reset borders
            stage = new AlgoState(ResetHighlightNode(nodeValueRect));
            transitions.add(stage);
            stage = new AlgoState(ResetHighlightNode(nodeCountRect));
            transitions.add(stage);
        }
        int newSum = 0;
        int[] shiftedValue = new int[indexValues.length];
        for (int k = 0; k < indexValues.length; k++) {
            shiftedValue[k] = newSum;
            newSum += trueCountState[k];
            Rectangle[] sumBlock;
            if (k == 0) sumBlock = new Rectangle[]{(Rectangle) countedValues[k].lookup("#myRect")};
            else
                sumBlock = new Rectangle[]{(Rectangle) countedValues[k].lookup("#myRect"), (Rectangle) countedValues[k - 1].lookup("#myRect")};
            //for (int sumContributer = 0; sumContributer < (k + 1); sumContributer++) {
            //    sumBlock[sumContributer] = (Rectangle) countedValues[sumContributer].lookup("#myRect");
            //}
            //highlight the set of blocks contributing to the sum rectangle
            AlgoState stage = new AlgoState(CustomHighlightNode(sumBlock, STROKE_BASE, TARGET_COLOR));
            transitions.add(stage);

            //highlight the node to TARGGET
            Rectangle node = (Rectangle) indexValues[k].lookup("#myRect");
            stage = new AlgoState(CustomHighlightNode(new Rectangle[]{node}, STROKE_BASE, TARGET_COLOR));
            transitions.add(stage);

            Text textVal = (Text) indexValues[k].lookup("#myValue");
            //change the index values to the sum of the previous counts
            ParallelTransition forward = UpdateText(textVal, 0, newSum);
            Pair<Transition, Transition> anims = new Pair<>(forward, forward);
            stage = new AlgoState(anims);
            transitions.add(stage);
            //change the count values to the sum of the previous counts
            textVal = (Text) countedValues[k].lookup("#myValue");
            forward = UpdateText(textVal, trueCountState[k], newSum);
            anims = new Pair<>(forward, forward);
            stage = new AlgoState(anims);
            transitions.add(stage);

            //highlight the node to SECONDARY
            node = (Rectangle) indexValues[k].lookup("#myRect");
            stage = new AlgoState(CustomHighlightNode(new Rectangle[]{node}, TARGET_COLOR, SECONDARY_COLOR));
            transitions.add(stage);


            //reset highlight the set of blocks contributing to the sum rectangle
            stage = new AlgoState(CustomHighlightNode(sumBlock, TARGET_COLOR, STROKE_BASE));
            transitions.add(stage);
        }

        for (int k = 0; k < indexValues.length; k++) {
            Text textVal = (Text) shiftedIndex[k].lookup("#myValue");
            //change the index values to the sum of the previous counts
            ParallelTransition forward = UpdateText(textVal, 0, shiftedValue[k]);
            Pair<Transition, Transition> anims = new Pair<>(forward, forward);
            AlgoState stage = new AlgoState(anims);
            transitions.add(stage);
            //highlight the rectangle
            Rectangle node = (Rectangle) shiftedIndex[k].lookup("#myRect");
            stage = new AlgoState(CustomHighlightNode(new Rectangle[]{node}, STROKE_BASE, SECONDARY_COLOR));
            transitions.add(stage);

            //remove highlight from original index values
            if (k > 0) {
                node = (Rectangle) indexValues[k - 1].lookup("#myRect");
                stage = new AlgoState(ResetHighlightNode(node));
                transitions.add(stage);
            }

            if (k == (indexValues.length - 1)) {
                node = (Rectangle) indexValues[k].lookup("#myRect");
                stage = new AlgoState(ResetHighlightNode(node));
                transitions.add(stage);
            }
        }


        int[] indexInsert = new int[shiftedValue.length];
        System.arraycopy(shiftedValue, 0, indexInsert, 0, shiftedValue.length);

        for (int k = 0; k < nodeValues.length; k++) {
            int val = 0;
            while (nodeValues[k] != val) val++;

            //color input index
            Rectangle node = (Rectangle) inputArray[k].lookup("#myRect");
            AlgoState stage = new AlgoState(ColorNode(TARGET_COLOR, node));
            stage.StoreArrayStatus(sortedArrayStatus);
            transitions.add(stage);

            //highlight value array
            node = (Rectangle) possibleValuesStackPane[val].lookup("#myRect");
            stage = new AlgoState(CustomHighlightNode(new Rectangle[]{node}, STROKE_BASE, TARGET_COLOR));
            stage.StoreArrayStatus(sortedArrayStatus);
            transitions.add(stage);

            //highlight shifted index
            node = (Rectangle) shiftedIndex[val].lookup("#myRect");
            stage = new AlgoState(CustomHighlightNode(new Rectangle[]{node}, SECONDARY_COLOR, TARGET_COLOR));
            stage.StoreArrayStatus(sortedArrayStatus);
            transitions.add(stage);

            //highlight sorted array insert before insert
            int index = indexInsert[val];
            node = (Rectangle) sortedArray[index].lookup("#myRect");
            stage = new AlgoState(CustomHighlightNode(new Rectangle[]{node}, STROKE_BASE, SECONDARY_COLOR));
            stage.StoreArrayStatus(sortedArrayStatus);
            transitions.add(stage);

            //update sorted array text
            Text textVal = (Text) sortedArray[index].lookup("#myValue");
            //change the index values to the sum of the previous counts
            ParallelTransition forward = UpdateText(textVal, 0, nodeValues[k]);
            Pair<Transition, Transition> anims = new Pair<>(forward, forward);
            stage = new AlgoState(anims);
            sortedArrayStatus[index] = nodeValues[k];
            stage.StoreArrayStatus(sortedArrayStatus);
            transitions.add(stage);

            //highlight node after insertion
            node = (Rectangle) sortedArray[index].lookup("#myRect");
            stage = new AlgoState(CustomHighlightNode(new Rectangle[]{node}, SECONDARY_COLOR, TARGET_COLOR));
            stage.StoreArrayStatus(sortedArrayStatus);
            transitions.add(stage);

            //updated shifted text
            textVal = (Text) shiftedIndex[val].lookup("#myValue");
            forward = UpdateText(textVal, shiftedValue[val] + (index - shiftedValue[val]), indexInsert[val] += 1);
            anims = new Pair<>(forward, forward);
            stage = new AlgoState(anims);
            transitions.add(stage);


            //reset highlight shifted index
            node = (Rectangle) shiftedIndex[val].lookup("#myRect");
            stage = new AlgoState(CustomHighlightNode(new Rectangle[]{node}, TARGET_COLOR, SECONDARY_COLOR));
            stage.StoreArrayStatus(sortedArrayStatus);
            transitions.add(stage);

            //reset highlight value array
            node = (Rectangle) possibleValuesStackPane[val].lookup("#myRect");
            stage = new AlgoState(CustomHighlightNode(new Rectangle[]{node}, TARGET_COLOR, STROKE_BASE));
            stage.StoreArrayStatus(sortedArrayStatus);
            transitions.add(stage);


        }

        return transitions;
    }


    /**
     * @param node node to highlight
     * @param from start color
     * @param to   to color
     * @return return pair transition
     */
    final public Pair<Transition, Transition> CustomHighlightNode(Rectangle[] node, Color from, Color to) {
        ParallelTransition forward = new ParallelTransition();
        ParallelTransition reverse = new ParallelTransition();

        for (Rectangle rectangle : node) {
            StrokeTransition strokeChangeForward = new StrokeTransition(Duration.seconds(ANIM_DURATION), rectangle, from, to);
            StrokeTransition strokeChangeReverse = new StrokeTransition(Duration.seconds(ANIM_DURATION), rectangle, to, from);
            forward.getChildren().add(strokeChangeForward);
            reverse.getChildren().add(strokeChangeReverse);
        }


        return new Pair<>(forward, reverse);
    }

    /**
     * @param node node to highlight
     * @return return pair transition
     */
    final public Pair<Transition, Transition> ResetHighlightNode(Rectangle node) {
        StrokeTransition strokeChangeForward = new StrokeTransition(Duration.seconds(ANIM_DURATION), node, SECONDARY_COLOR, STROKE_BASE);
        StrokeTransition strokeChangeReverse = new StrokeTransition(Duration.seconds(ANIM_DURATION), node, STROKE_BASE, SECONDARY_COLOR);
        ParallelTransition forward = new ParallelTransition(strokeChangeForward);
        ParallelTransition reverse = new ParallelTransition(strokeChangeReverse);

        return new Pair<>(forward, reverse);
    }

    private ParallelTransition UpdateText(Text node, int from, int to) {

        Timeline updateText = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(node.textProperty(), String.valueOf(from))),
                new KeyFrame(Duration.seconds(0.95), new KeyValue(node.textProperty(), String.valueOf(to)))
        );

        // Create the animation to move the Text node to the center of the StackPane
        Timeline centerText = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.translateXProperty(), node.getTranslateX()),
                        new KeyValue(node.translateYProperty(), node.getTranslateY())
                ),
                new KeyFrame(Duration.seconds(0.95),
                        new KeyValue(node.translateXProperty(), inputArray[0].getWidth() / 2),
                        new KeyValue(node.translateYProperty(), inputArray[0].getHeight() / 2)
                )
        );

        return new ParallelTransition(updateText, centerText);
    }

    /**
     * Utility function returning a transition coloring a node.
     *
     * @param color New Color of the node.
     * @param node  The node to color.
     * @return The transition containing the filling of the node.
     */
    private Pair<Transition, Transition> ColorNode(Color color, Shape node) {
        FillTransition forward = new FillTransition(Duration.seconds(ANIM_DURATION));
        FillTransition reverse = new FillTransition(Duration.seconds(ANIM_DURATION));
        Color currentColor = (Color) node.getFill();
        forward.setShape(node);
        forward.setFromValue(currentColor);
        forward.setToValue(color);

        reverse.setShape(node);
        reverse.setFromValue(color);
        reverse.setToValue(currentColor);

        //node.setFill(color);

        reverse.setOnFinished(evnt -> node.setFill(currentColor));

        return new Pair<>(forward, reverse);
    }

}