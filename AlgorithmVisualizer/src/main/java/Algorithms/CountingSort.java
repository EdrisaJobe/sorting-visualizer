package Algorithms;

import UserInterface.VisualizerController;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.Transition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
public class CountingSort{


    //circles which are nodes
    private StackPane[] inputArray;
    private StackPane[] possibleValuesStackPane;
    private StackPane[] countedValues;
    private StackPane[] sortedArray;
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
    public CountingSort(StackPane[] inputArray, StackPane[] possibleValuesStackPane, StackPane[] countedValues,StackPane[] sortedArray,int[] nodeValues,int[] possibleValues) {
        this.inputArray = inputArray;
        this.possibleValuesStackPane = possibleValuesStackPane;
        this.countedValues = countedValues;
        this.sortedArray = sortedArray;
        this.nodeValues = nodeValues;
        this.possibleValues = possibleValues;
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

        for(int i=0;i<inputArray.length;i++){
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
            Text textCount = (Text) countedValues[i].lookup("#myValue");
            stage = new AlgoState(SecondaryHighlightNode(nodeCountRect));
            stage.StoreVariable("i", 0);
            transitions.add(stage);


        }
        return transitions;
    }




    /**
     * Returns a transition of highlighting a node with the secondary color.
     * @param index Index of the node to highlight.
     * @return The transition highlighting the node.
     */
    final public Pair<Transition, Transition> SecondaryHighlightNode(Rectangle node){
        //Rectangle node = (Rectangle) inputArray[index].lookup("#myRect");
        StrokeTransition strokeChangeForward = new StrokeTransition(Duration.seconds(ANIM_DURATION),node,BASE_COLOR,SECONDARY_COLOR);
        StrokeTransition strokeChangeReverse = new StrokeTransition(Duration.seconds(ANIM_DURATION),node,SECONDARY_COLOR,BASE_COLOR);
        ParallelTransition forward = new ParallelTransition(strokeChangeForward);
        ParallelTransition reverse = new ParallelTransition(strokeChangeReverse);

        return new Pair<>(forward, reverse);
    }

}
