package Algorithms;

import javafx.animation.ParallelTransition;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * LinearSearch algorithm
 */
public class LinearSearch extends AbstractAlgorithm{

    private Rectangle[] nodes;
    private ArrayList<AlgoState> transitions;

    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public LinearSearch(Rectangle[] nodes) {
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
        Random rand = new Random();
        int targetIndex = rand.nextInt(6) + 4;

        state = new AlgoState();
        state.StoreTransition(SearchTargetHighlightNode(targetIndex));
        transitions.add(state);


        for (int i = 0; i < nodes.length; i++) {

            state = new AlgoState();
            state.StoreTransition(BaseColorNode(i > 0 ? i-1 : (0)), SecondaryHighlightNode(i));
            transitions.add(state);

            if (nodes[i].getHeight() == nodes[targetIndex].getHeight()) {
                state = new AlgoState();
                state.StoreTransition(PrimaryHighlightNode(targetIndex));
                transitions.add(state);
                break;
            }
        }

        for (int i = 0; i < nodes.length; i++) {
            BaseColorNode(i);
        }
        return transitions;
    }
}
