package Algorithms;

import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * An abstract algorithm class.
 *
 * Contains functionality to swap and highlight nodes.
 * For every swap or highlight that is made, the resulting transition and state of algorithm
 * should be saved in the transitions list. This list of transition will then be played back in the UI.
 *
 * The run algorithm function is where the sorting/searching is meant to be done and should be implemented
 * by a child class.
 */
public abstract class AbstractAlgorithm {

    //Gap between blocks.
    public static int x_gap = 20;
    public static int box_width = 30;

    //Duration of the transitions.
    private final static float SWAP_ANIM_DURATION = 0.95f;

    private final static float FILL_ANIM_DURATION = 0.95f;

    public String pseudoCode = "";

    public String bestTime = "";
    public String averageTime = "";
    public String worstTime = "";
    public String spaceComplexity = "";


    //Highlight Colors.
    private final Color BASE_COLOR = Color.web("#000000");
    private final Color PRIMARY_COLOR = Color.web("#aeff80");
    private final Color SECONDARY_COLOR = Color.web("#fc6868");
    private final Color TARGET_COLOR = Color.web("#96f6ff");



    public Rectangle[] nodes;

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


    public AbstractAlgorithm(){}

    /**
     * Constructor, sets the array of nodes.
     * @param nodes Array of boxes
     */
    public AbstractAlgorithm(Rectangle[] nodes, int x_gap, int box_width){
        this.nodes = nodes.clone();
        this.x_gap = x_gap;
        this.box_width = box_width;
    }

    /**
     * Swaps the locations of two nodes in the array and on screen.
     * @param index1 Index of node1
     * @param index2 Index of node2
     * @return
     */
    final public Pair<Transition, Transition> SwapNodes(int index1, int index2){

        //Right and left movement transitions.
        TranslateTransition move_right = new TranslateTransition();
        TranslateTransition move_left = new TranslateTransition();

        //Reverse transitions
        TranslateTransition move_right_reverse = new TranslateTransition();
        TranslateTransition move_left_reverse = new TranslateTransition();

        Rectangle node1 = nodes[index1];
        Rectangle node2 = nodes[index2];

        //Swap the locations in the array.
        nodes[index1] = node2;
        nodes[index2] = node1;

        double trans_right_amt = (index2 - index1) * (x_gap + box_width);
        double trans_left_amt = (index2 - index1) * (x_gap + box_width) * -1;

        move_right.setByX(trans_right_amt);
        move_left.setByX(trans_left_amt);
        move_right.setNode(node1);
        move_left.setNode(node2);
        move_right.setDuration(Duration.seconds(SWAP_ANIM_DURATION));
        move_left.setDuration(Duration.seconds(SWAP_ANIM_DURATION));

        // Set the reverse transitions
        move_right_reverse.setByX(trans_left_amt);
        move_left_reverse.setByX(trans_right_amt);
        move_right_reverse.setNode(node1);
        move_left_reverse.setNode(node2);
        move_right_reverse.setDuration(Duration.seconds(SWAP_ANIM_DURATION));
        move_left_reverse.setDuration(Duration.seconds(SWAP_ANIM_DURATION));

        // Store the transitions and their reverse
        ParallelTransition forward = new ParallelTransition(move_right, move_left);
        ParallelTransition reverse = new ParallelTransition(move_right_reverse, move_left_reverse);

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
        ArrayList<AlgoState> stages = new ArrayList<>();

        stages.add(new AlgoState(PrimaryHighlightNode(nodeOne)));
        stages.add(new AlgoState(SecondaryHighlightNode(nodeTwo)));

        AlgoState swapState = new AlgoState();

        swapState.StoreTransition(SwapNodes(nodeOne,nodeTwo), BaseColorNode(nodeOne), BaseColorNode(nodeTwo));

        stages.add(swapState);
        return stages;
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
