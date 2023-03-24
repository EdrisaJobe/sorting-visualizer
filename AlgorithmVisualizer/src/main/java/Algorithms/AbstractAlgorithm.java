package Algorithms;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

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


    /**
     * Constructor, sets the array of nodes.
     * @param nodes Array of boxes
     */
    public AbstractAlgorithm(Rectangle[] nodes){
        this.nodes = nodes.clone();
    }

    /**
     * Swaps the locations of two nodes in the array and on screen.
     * @param index1 Index of node1
     * @param index2 Index of node2
     */
    final public ParallelTransition SwapNodes(int index1, int index2){

        //Right and left movement transitions.
        TranslateTransition move_right = new TranslateTransition();
        TranslateTransition move_left = new TranslateTransition();

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
        move_right.setCycleCount(1);
        move_left.setCycleCount(1);

        //A Parallel transition contains the two above transitions and can play them at the same time.
        //Think of it as storing multiple transitions in one transition.
        return new ParallelTransition(move_right, move_left);
    }

    /**
     * Returns a transition of highlighting a node with the base color.
     * @param index Index of the node to highlight.
     * @return The transition highlighting the node.
     */
    final public Transition BaseColorNode(int index){
        return ColorNode(BASE_COLOR, nodes[index]);
    }


    /**
     * Returns a transition of highlighting a node with the primary color.
     * @param index Index of the node to highlight.
     * @return The transition highlighting the node.
     */
    final public Transition PrimaryHighlightNode(int index){
        return ColorNode(PRIMARY_COLOR, nodes[index]);
    }

    /**
     * Returns a transition of highlighting a node with the primary color.
     * @param index Index of the node to highlight.
     * @return The transition highlighting the node.
     */
    final public Transition SearchTargetHighlightNode(int index){
        return ColorNode(TARGET_COLOR, nodes[index]);
    }

    /**
     * Returns a transition of highlighting a node with the secondary color.
     * @param index Index of the node to highlight.
     * @return The transition highlighting the node.
     */
    final public Transition SecondaryHighlightNode(int index){
        return ColorNode(SECONDARY_COLOR, nodes[index]);
    }


    /** UTILITY FUNCTION
     * Does a full swap procedure with swap animations and highlights between nodes i and j.
     * @param i Index of the node to swap.
     * @param j Index of the node to swap.
     */
    final public ArrayList<AlgoState> FullSwapProcedure(int nodeOne, int nodeTwo){
        ArrayList<AlgoState> swap_transitions = new ArrayList<>();

        swap_transitions.add(new AlgoState(PrimaryHighlightNode(nodeOne)));
        swap_transitions.add(new AlgoState(SecondaryHighlightNode(nodeTwo)));

        AlgoState swapState = new AlgoState();
        ParallelTransition swap = new ParallelTransition(SwapNodes(nodeOne,nodeTwo), BaseColorNode(nodeOne), BaseColorNode(nodeTwo));
        swapState.StoreTransition(swap);
        swap_transitions.add(swapState);
        return swap_transitions;
    }

    /**
     * Utility function returning a transition coloring a node.
     * @param color New Color of the node.
     * @param node The node to color.
     * @return The transition containing the filling of the node.
     */
    private Transition ColorNode(Color color, Shape node){
        FillTransition ft = new FillTransition(Duration.seconds(FILL_ANIM_DURATION));
        ft.setShape(node);
        ft.setFromValue((Color) node.getFill());
        ft.setToValue(color);
        ft.setCycleCount(1);
        node.setFill(color);
        return ft;
    }
}
