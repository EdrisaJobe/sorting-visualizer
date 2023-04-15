package Algorithms;

import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;

public abstract class AbstractAlgorithmTree {

    //Gap between blocks.
    public static int x_gap = 20;
    public static int box_width = 30;

    //Duration of the transitions.
    private final static float SWAP_ANIM_DURATION = 0.95f;

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
    private final Color TARGET_COLOR = Color.web("#96f6ff");



    public Circle[] nodes;
    public int[] values;
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
    public AbstractAlgorithmTree(Circle[] nodes, int[] values, Line[] connection){
        this.nodes = nodes.clone();
        this.values = values.clone();
        this.connection = connection.clone();
    }

    /**
     * Highlights the transition from one node to another
     * @param index1 Index of node1
     * @param index2 Index of node2
     * @return
     */
    final public Pair<Transition, Transition> ConnectNodes(int index1, int index2){


        Circle node1 = nodes[index1];
        Circle node2 = nodes[index2];
        Line connect = connection[index2];

        StrokeTransition parent = new StrokeTransition(Duration.millis(3000),node1,BASE_COLOR,TARGET_COLOR);

        StrokeTransition lineToChild = new StrokeTransition(Duration.millis(3000),connect,BASE_COLOR,TARGET_COLOR);

        StrokeTransition child = new StrokeTransition(Duration.millis(3000),node2,BASE_COLOR,TARGET_COLOR);

        ParallelTransition forward = new ParallelTransition(parent,lineToChild,child);
        ParallelTransition reverse = new ParallelTransition(child,lineToChild,parent);

        Pair<Transition, Transition> anims = new Pair<>(forward, reverse);

        //Think of it as storing multiple transitions in one transition.
        return anims;
    }

    /**
     * Returns a transition of highlighting a node with the base color.
     * @param index Index of the node to highlight.
     * @return The transition highlighting the node.
     */
    final public Pair<Transition, Transition> BaseColorNode(int index){
        return ColorNode(BASE_COLOR, nodes[index]);
    }


    /**
     * Returns a transition of highlighting a node with the primary color.
     * @param index Index of the node to highlight.
     * @return The transition highlighting the node.
     */
    final public Pair<Transition, Transition> PrimaryHighlightNode(int index){
        return ColorNode(PRIMARY_COLOR, nodes[index]);
    }

    /**
     * Returns a transition of highlighting a node with the primary color.
     * @param index Index of the node to highlight.
     * @return The transition highlighting the node.
     */
    final public Pair<Transition, Transition> SearchTargetHighlightNode(int index){
        return ColorNode(TARGET_COLOR, nodes[index]);
    }

    /**
     * Returns a transition of highlighting a node with the secondary color.
     * @param index Index of the node to highlight.
     * @return The transition highlighting the node.
     */
    final public Pair<Transition, Transition> SecondaryHighlightNode(int index){
        return ColorNode(SECONDARY_COLOR, nodes[index]);
    }


    /** UTILITY FUNCTION
     * Does a full swap procedure with swap animations and highlights between nodes i and j.
     * @param nodeOne Index of the node to swap.
     * @param nodeTwo Index of the node to swap.
     */
    final public ArrayList<AlgoState> FullSwapProcedure(int nodeOne, int nodeTwo){
        return null;
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

        node.setFill(color);

        reverse.setOnFinished(evnt -> {
            node.setFill(currentColor);
        });

        return new Pair<>(forward, reverse);
    }



}
