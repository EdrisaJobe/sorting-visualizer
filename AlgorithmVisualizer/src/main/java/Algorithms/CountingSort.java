package Algorithms;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

//TODO::Make a new viz for this.

/**
 * CountingSort algorithm
 */
public class CountingSort extends AbstractAlgorithm {

    private Rectangle[] nodes;
    private ArrayList<AlgoState> transitions;

    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public CountingSort(Rectangle[] nodes) {
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


//        ArrayList<AlgoState> swap_transitions = FullSwapProcedure(j, j + 1);
//        swap_transitions.get(swap_transitions.size() - 1).StoreVariable("i", i);
//        swap_transitions.get(swap_transitions.size() - 1).StoreVariable("j", j);
//        transitions.addAll(swap_transitions);


        return transitions;
    }
}
