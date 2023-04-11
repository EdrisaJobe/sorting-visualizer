package Algorithms;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * LinearSearch algorithm
 */
public class LinearSearch extends AbstractAlgorithm{

    private Rectangle[] nodes;
    private ArrayList<AlgoState> transitions;

    private int x_gap;

    private int box_width;
    private String code =   "    index = 0\n" +
                            "    while (index < n)\n" +
                            "        if (list[index] == target)\n" +
                            "            RETURN index\n" +
                            "        i++\n" +
                            "    RETURN -1";


    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public LinearSearch(Rectangle[] nodes, int x_gap, int box_width) {
        super(nodes, x_gap, box_width);
        super.pseudoCode = code;
        bestTime = "Ω(1)";
        averageTime = "θ(n)";
        worstTime = "O(n)";
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
        Random rand = new Random();
        int targetIndex = rand.nextInt(6) + 4;

        state = new AlgoState();
        state.StoreTransition(SearchTargetHighlightNode(targetIndex));
        state.StoreVariable("i", 0);
        transitions.add(state);


        for (int i = 0; i < nodes.length; i++) {

            state = new AlgoState();
            state.StoreTransition(BaseColorNode(i > 0 ? i-1 : (0)), SecondaryHighlightNode(i));
            state.StoreVariable("i", i);
            transitions.add(state);

            if (nodes[i].getHeight() == nodes[targetIndex].getHeight()) {
                state = new AlgoState();
                state.StoreTransition(PrimaryHighlightNode(targetIndex));
                state.StoreVariable("i", i);
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
