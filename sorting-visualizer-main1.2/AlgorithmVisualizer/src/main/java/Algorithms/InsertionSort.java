package Algorithms;

import javafx.animation.ParallelTransition;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * InsertionSort algorithm variables currently are not stored
 */
public class InsertionSort extends AbstractAlgorithm{

    private Rectangle[] nodes;
    private ArrayList<AlgoState> transitions;

    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public InsertionSort(Rectangle[] nodes) {
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
        for (int i = 1; i < n; i++) {
            double temp = nodes[i].getHeight();
            int j = i - 1;
            int iloc = i;
            while (temp < nodes[j].getHeight()) {
                    //Here we are doing a swap, so we are saving everything.
                    transitions.add(new AlgoState(PrimaryHighlightNode(j)));
                    transitions.add(new AlgoState(SecondaryHighlightNode(iloc)));

                    AlgoState currState = new AlgoState();
                    ParallelTransition swap = new ParallelTransition(SwapNodes(iloc,j), BaseColorNode(iloc), BaseColorNode(j));
                    currState.StoreTransition(swap);
                    currState.StoreVariable("i", iloc);
                    currState.StoreVariable("j", j);
                    transitions.add(currState);

                    iloc = j;
                    j--;
                    if(j<0)
                        break;

            }
        }
        return transitions;
    }
}
