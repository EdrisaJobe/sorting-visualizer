package Algorithms;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Bubblesort algorithm
 */
public class QuickSort extends AbstractAlgorithm{

    private Rectangle[] nodes;
    private ArrayList<AlgoState> transitions;

    private int x_gap;

    private int box_width;
    private String code =   "       if (low < high) " +
                            "\n         pi = partition(arr, low, high);" +
                            "\n         quickSort(arr, low, pi – 1);" +
                            "\n         quickSort(arr, pi + 1, high);";


    public QuickSort(){
        super.pseudoCode = code;
        super.bestTime = "Ω(n log n)";
        super.averageTime = "θ(n log n)";
        super.worstTime = "O(n^2)";
        super.spaceComplexity = "O(log n)";
    }

    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public QuickSort(Rectangle[] nodes, int x_gap, int box_width) {
        super(nodes, x_gap, box_width);
        super.pseudoCode = code;
        super.bestTime = "Ω(n log n)";
        super.averageTime = "θ(n log n)";
        super.worstTime = "O(n^2)";
        super.spaceComplexity = "O(log n)";
        this.nodes = super.nodes;
        this.transitions = super.transitions;
        this.x_gap = super.x_gap;
        this.box_width = super.box_width;

    }

    /**
     * Partition the array
     * @param arr The array to partition
     * @param low Lower bound
     * @param high Upper bound
     * @return Partition index
     */
    int partition(Rectangle[] arr, int low, int high)
    {
        Rectangle pivot = arr[high];
        int i = (low - 1);

        for (int j = low; j <= high - 1; j++) {
            if (arr[j].getHeight() < pivot.getHeight()) {
                i++;
                ArrayList<AlgoState> swap_transitions = FullSwapProcedure(i, j);
                swap_transitions.get(swap_transitions.size() - 1).StoreVariable("low", low);
                swap_transitions.get(swap_transitions.size() - 1).StoreVariable("high", high);
                int[] copyArray = GetValues();
                swap_transitions.get(0).StoreArrayStatus(copyArray);
                transitions.addAll(swap_transitions);
            }
        }

        ArrayList<AlgoState> swap_transitions = FullSwapProcedure(i+1, high);
        swap_transitions.get(swap_transitions.size() - 1).StoreVariable("low", low);
        swap_transitions.get(swap_transitions.size() - 1).StoreVariable("high", high);
        int[] copyArray = GetValues();
        swap_transitions.get(0).StoreArrayStatus(copyArray);
        transitions.addAll(swap_transitions);
        return (i + 1);
    }

    /* Recursive Quicksort Implementation
     */
    void quickSortRecursion(Rectangle[] arr, int low, int high)
    {

        if (low < high) {
            int partitionIndex = partition(arr, low, high);
            quickSortRecursion(arr, low, partitionIndex - 1);
            quickSortRecursion(arr, partitionIndex + 1, high);
        }
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

        quickSortRecursion(nodes, 0, nodes.length-1);

        return transitions;
    }
}
