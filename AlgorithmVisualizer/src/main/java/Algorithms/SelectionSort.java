package Algorithms;

import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * Bubblesort algorithm
 */
public class SelectionSort extends AbstractAlgorithm{

    private Rectangle[] nodes;
    private ArrayList<AlgoState> transitions;

    private int x_gap;

    private int box_width;
    private String code =   "   repeat n times\n" +
                            "   min = first unsorted element\n" +
                            "   for each unsorted element\n" +
                            "       if element < currentMin\n" +
                            "           set element as new min\n" +
                            "   swap(min, first unsorted post)\n";

    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public SelectionSort(Rectangle[] nodes, int x_gap, int box_width) {
        super(nodes, x_gap, box_width);
        super.pseudoCode = code;
        bestTime = "Ω(n^2)";
        averageTime = "θ(n^2)";
        worstTime = "O(n^2)";
        spaceComplexity = "O(1)";
        this.nodes = super.nodes;
        this.transitions = super.transitions;
        this.x_gap = super.x_gap;
        this.box_width= super.box_width;
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

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++)
                if (nodes[j].getHeight() < nodes[min_idx].getHeight())
                    min_idx = j;

            ArrayList<AlgoState> swap_transitions = FullSwapProcedure(i, min_idx);
            swap_transitions.get(swap_transitions.size() - 1).StoreVariable("i", i);
            swap_transitions.get(swap_transitions.size() - 1).StoreVariable("min", min_idx);
            transitions.addAll(swap_transitions);
        }
        return transitions;
    }
}
