package Algorithms;

import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * Bubblesort algorithm
 */
public class BubbleSort extends AbstractAlgorithm{

    private Rectangle[] nodes;

    private int x_gap;
    private int box_width;
    private ArrayList<AlgoState> transitions;
    private String code =   "     for all elements of list" +
                            "\n         if list[i] > list[i+1]" +
                            "\n         swap(list[i], list[i+1])" +
                            "\n         end if " +
                            "\n      end for";

    public BubbleSort(){
        super.pseudoCode = code;
        bestTime = "Ω(n)";
        averageTime = "θ(n^2)";
        worstTime = "O(n^2)";
        spaceComplexity = "O(1)";
    }

    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public BubbleSort(Rectangle[] nodes, int x_gap, int box_width) {
        super(nodes, x_gap, box_width);
        super.pseudoCode = code;

        bestTime = "Ω(n)";
        averageTime = "θ(n^2)";
        worstTime = "O(n^2)";
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

        int n = nodes.length;
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (nodes[j].getHeight() > nodes[j + 1].getHeight()) {
                    ArrayList<AlgoState> swap_transitions = FullSwapProcedure(j, j+1);
                    swap_transitions.get(0).StoreVariable("i", i);
                    swap_transitions.get(0).StoreVariable("j", j);
                    transitions.addAll(swap_transitions);
                }

        return transitions;
    }
}
