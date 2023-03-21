package Algorithms;

import javafx.animation.ParallelTransition;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * Bubblesort algorithm
 */
public class BubbleSort extends AbstractAlgorithm{

    private Rectangle[] nodes;
    private ArrayList<AlgoState> transitions;

    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public BubbleSort(Rectangle[] nodes) {
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

        int n = nodes.length;
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (nodes[j].getHeight() > nodes[j + 1].getHeight()) {
                    //Here we are doing a swap, so we are saving everything.
                    transitions.add(new AlgoState(PrimaryHighlightNode(j)));
                    transitions.add(new AlgoState(SecondaryHighlightNode(j+1)));

                    AlgoState currState = new AlgoState();
                    ParallelTransition swap = new ParallelTransition(SwapNodes(j,j+1), BaseColorNode(j), BaseColorNode(j+1));
                    currState.StoreTransition(swap);
                    currState.StoreVariable("i", i);
                    currState.StoreVariable("j", j);
                    transitions.add(currState);
                }

        return transitions;
    }
}
