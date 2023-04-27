package Algorithms;

import javafx.animation.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;

public abstract class AbstractAlgorithmTree {

    private final static float FILL_ANIM_DURATION = 0.95f;

    public String pseudoCode = "";

    public static String bestTime = "";
    public static String averageTime = "";
    public static String worstTime = "";
    public static String spaceComplexity = "";


    //Highlight Colors.
    private final Color BASE_COLOR = Color.web("#000000");
    private final Color PRIMARY_COLOR = Color.web("#aeff80");
    private final Color SECONDARY_COLOR = Color.web("#fc6868");
    private final Color TARGET_COLOR = Color.INDIANRED;



    //circles which are nodes
    public double vizzWidth;
    //circles which are nodes
    public double vizzHeight;
    //stackpane which holds both the nodes and the text
    public StackPane[] nodes;
    //actual int values of nodes
    public int[] nodeValues;
    public Line[] connection;

    //List containing all the transitions that this algorithm makes.
    //This allows us to go backwards and forwards.
    public ArrayList<AlgoState> transitions = new ArrayList<>();


    /**
     * The function that ultimately does the sorting.
     * This function should make use of the swap and Highlight functions to make transitions.
     * The state of the algorithm should be saved in each step of the algorithm in the transitions list.
     * Each AlgoState in the transitions list represents one step in the algorithm.
     * @return The Arraylist of transitions.
     */
    public abstract ArrayList<AlgoState> RunAlgorithm();


    /**
     * Constructor, sets the array of nodes.
     * @param nodes Array of boxes
     */
    public AbstractAlgorithmTree(StackPane[] nodes, int[] values, Line[] connection){
        this.nodes = nodes.clone();
        this.nodeValues = values.clone();
        this.connection = connection.clone();
    }

    /**
     * Highlights the transition line from child to parent
     * @param index1 Index of node1
     * @return animation of stroke change
     */
    final public Pair<Transition, Transition> ConnectNodes(int index1){

        Line connect = connection[index1-1];
        StrokeTransition lineToChild = new StrokeTransition(Duration.seconds(FILL_ANIM_DURATION),connect,BASE_COLOR,SECONDARY_COLOR);
        StrokeTransition lineToParent = new StrokeTransition(Duration.seconds(FILL_ANIM_DURATION),connect,SECONDARY_COLOR,BASE_COLOR);
        ParallelTransition forward = new ParallelTransition(lineToChild);
        ParallelTransition reverse = new ParallelTransition(lineToParent);

        //Think of it as storing multiple transitions in one transition.
        return new Pair<>(forward, reverse);
    }
    /**
     * Returns a transition of highlighting a node with the primary color.
     * @param index Index of the node to highlight.
     * @return The transition highlighting the node.
     */
    final public Pair<Transition, Transition> SearchTargetHighlightNode(int index){

        Circle nodeToColor = (Circle) nodes[index].lookup("#myCircle");
        return ColorNode(TARGET_COLOR, nodeToColor);
    }

    /**
     * Utility function returning a transition coloring a node.
     * @param color New Color of the node.
     * @param node The node to color.
     * @return The transition containing the filling of the node.
     */
    private Pair<Transition, Transition> ColorNode(Color color, Shape node){
        FillTransition forward = new FillTransition(Duration.seconds(FILL_ANIM_DURATION));
        FillTransition reverse = new FillTransition(Duration.seconds(FILL_ANIM_DURATION));
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


    /**
     * Utility function returning a transition which changes the stroke of a node
     * @return The transition containing the filling of the node.
     */
    public Pair<Transition, Transition> HighlightRing(int index1){
        Circle nodeToHighLight = (Circle) nodes[index1].lookup("#myCircle");
        StrokeTransition strokeChangeForward = new StrokeTransition(Duration.seconds(FILL_ANIM_DURATION),nodeToHighLight,BASE_COLOR,SECONDARY_COLOR);
        ParallelTransition forward = new ParallelTransition(strokeChangeForward);
        StrokeTransition strokeChangeReverse = new StrokeTransition(Duration.seconds(FILL_ANIM_DURATION),nodeToHighLight,SECONDARY_COLOR,BASE_COLOR);
        ParallelTransition reverse = new ParallelTransition(strokeChangeReverse);

        return new Pair<>(forward, reverse);
    }

}