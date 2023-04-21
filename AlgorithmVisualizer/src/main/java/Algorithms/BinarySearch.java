package Algorithms;

import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

/**
 * BinarySearch algorithm
 */
public class BinarySearch extends AbstractAlgorithm{

    private Rectangle[] nodes;

    private int box_width;

    private int x_gap;
    private ArrayList<AlgoState> transitions;
    private String code =   "    left = lowest\n" +
            "    right = highest\n" +
            "    while (left <= right)\n" +
            "        middle = (left + right) / 2\n" +
            "        if(target = array[middle])\n" +
            "            RETURN middle\n" +
            "        else if(target < array[middle])\n" +
            "           right = middle - 1\n" +
            "        else\n" +
            "           left = middle + 1\n" +
            "    RETURN -1";



    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public BinarySearch(Rectangle[] nodes, int x_gap, int box_width) {
        super(nodes, x_gap, box_width);
        super.pseudoCode = code;
        bestTime = "Ω(1)";
        averageTime = "θ(log n)";
        worstTime = "O(log n)";
        spaceComplexity = "O(1)";
        this.nodes = super.nodes;
        this.transitions = super.transitions;
        this.x_gap = super.x_gap;
        this.box_width = super.box_width;
    }

    /**
     * The function that ultimately does the sorting.
     * This function should make use of the swap and Highlight functions to make transitions.
     * The state of the algorithm should be saved in each step of the algorithm in the transitions list.
     * Each AlgoState in the transitions list represents one step in the algorithm.
     *
     * @return The Arraylist of transitions.
     */
    @Override
    public ArrayList<AlgoState> RunAlgorithm() {
        AlgoState state;
        ParallelTransition highlightsForward;
        ParallelTransition highlightsReverse;
        Random rand = new Random();
        int targetIndex = rand.nextInt(6) + 4;
        double target =  nodes[targetIndex].getHeight();


        int first = 0;
        int last = nodes.length - 1;
        int mid = (first + last)/2;
        int old_first = 0;
        int old_last = last;

        state = new AlgoState();
        state.StoreTransition(SearchTargetHighlightNode(targetIndex));
        state.StoreVariable("left", first);
        state.StoreVariable("middle", mid);
        state.StoreVariable("right", last);
        state.StoreVariable("target", targetIndex);
        transitions.add(state);

        while( first <= last ){

            state = new AlgoState();
            state.StoreTransition(BaseColorNode(old_last), BaseColorNode(old_first), SecondaryHighlightNode(last), SecondaryHighlightNode(first));
            state.StoreVariable("left", first);
            state.StoreVariable("middle", mid);
            state.StoreVariable("right", last);
            state.StoreVariable("target", targetIndex);
            transitions.add(state);

            if ( nodes[mid].getHeight() < target){
                old_first = first;
                first = mid + 1;
            }else if ( nodes[mid].getHeight() == target){
                state = new AlgoState();
                state.StoreTransition(BaseColorNode(last), BaseColorNode(first), PrimaryHighlightNode(targetIndex));
                state.StoreVariable("left", first);
                state.StoreVariable("middle", mid);
                state.StoreVariable("right", last);
                state.StoreVariable("target", targetIndex);
                transitions.add(state);
                break;
            }else{
                old_last = last;
                last = mid - 1;
            }
            mid = (first + last)/2;
        }

        for (int i = 0; i < nodes.length; i++) {
            BaseColorNode(i);
        }
        return transitions;
    }
}