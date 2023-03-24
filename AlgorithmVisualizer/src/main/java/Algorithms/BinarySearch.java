package Algorithms;

import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * BinarySearch algorithm
 */
public class BinarySearch extends AbstractAlgorithm{

    private Rectangle[] nodes;
    private ArrayList<AlgoState> transitions;

    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public BinarySearch(Rectangle[] nodes) {
        super(nodes);
        this.nodes = super.nodes;
        this.transitions = super.transitions;
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
        ParallelTransition highlights;
        Random rand = new Random();
        int targetIndex = rand.nextInt(6) + 4;
        double target =  nodes[targetIndex].getHeight();


        int first = 0;
        int last = nodes.length - 1;
        int mid = (first + last)/2;
        int old_first = 0;
        int old_last = last;

        state = new AlgoState();
        highlights = new ParallelTransition(SearchTargetHighlightNode(targetIndex));
        state.StoreTransition(highlights);
        transitions.add(state);

        while( first <= last ){

            state = new AlgoState();
            highlights = new ParallelTransition(BaseColorNode(old_last), BaseColorNode(old_first), SecondaryHighlightNode(last), SecondaryHighlightNode(first));
            state.StoreTransition(highlights);
            transitions.add(state);

            if ( nodes[mid].getHeight() < target){
                old_first = first;
                first = mid + 1;
            }else if ( nodes[mid].getHeight() == target){
                state = new AlgoState();
                highlights = new ParallelTransition(BaseColorNode(last), BaseColorNode(first), PrimaryHighlightNode(targetIndex));
                state.StoreTransition(highlights);
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
