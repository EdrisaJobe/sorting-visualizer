package Algorithms;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * InsertionSort algorithm
 */
public class InsertionSort extends AbstractAlgorithm{

    private Rectangle[] nodes;
    private ArrayList<AlgoState> transitions;
    private String code = " n = length(A)" +
            "\n for i = 1 to n - 1" +
            "\n     j = i" +
            "\n         while j > 0 and A[j-1] > A[j]" +
            "\n             swap(A[j], A[j-1])" +
            "\n             j = j - 1" +
            "\n         end while" +
            "\n end for";


    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public InsertionSort(Rectangle[] nodes) {
        super(nodes);
        super.pseudoCode = code;
        bestTime = "Ω(n)";
        averageTime = "θ(n^2)";
        worstTime = "O(n^2)";
        spaceComplexity = "O(1)";
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
                    ArrayList<AlgoState> swap_transitions = FullSwapProcedure(j, iloc);
                    swap_transitions.get(swap_transitions.size() - 1).StoreVariable("i", i);
                    swap_transitions.get(swap_transitions.size() - 1).StoreVariable("j", j);
                    transitions.addAll(swap_transitions);
                iloc = j;
                    j--;
                    if(j<0)
                        break;

            }
        }
        return transitions;
    }
}
