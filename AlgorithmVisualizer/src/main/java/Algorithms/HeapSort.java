package Algorithms;

import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * HeapSort algorithm
 */
public class HeapSort extends AbstractAlgorithm{

    private Rectangle[] nodes;
    private ArrayList<AlgoState> transitions;

    private int x_gap;

    private int box_width;
    private String code =   "       n = length(A)\n" +
                            "       for i = n/2 to 1\n" +
                            "           Heapify(A, n , i)\n" +
                            "       for i = n to 2\n" +
                            "           exchange A[1] with A[i]\n" +
                            "           A.heapsize = A.heapsize - 1\n" +
                            "           Heapify(A, i, 0)";


    public HeapSort(){
        super.pseudoCode = code;
        bestTime = "Ω(n log n)";
        averageTime = "θ(n log n)";
        worstTime = "O(n log n)";
        spaceComplexity = "O(1)";
    }

    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public HeapSort(Rectangle[] nodes, int x_gap, int box_width) {
        super(nodes, x_gap, box_width);
        super.pseudoCode = code;

        bestTime = "Ω(n log n)";
        averageTime = "θ(n log n)";
        worstTime = "O(n log n)";
        spaceComplexity = "O(1)";

        this.nodes = super.nodes;
        this.transitions = super.transitions;
        this.x_gap = super.x_gap;
        this.box_width = super.box_width;
    }

    void heapify(Rectangle arr[], int n, int i)
    {

        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left].getHeight() > arr[largest].getHeight())
            largest = left;

        if (right < n && arr[right].getHeight() > arr[largest].getHeight())
            largest = right;

        if (largest != i) {
            ArrayList<AlgoState> swap_transitions = FullSwapProcedure(i, largest);
            swap_transitions.get(swap_transitions.size() - 1).StoreVariable("i", i);
            int[] copyArray = GetValues();
            swap_transitions.get(0).StoreArrayStatus(copyArray);
            transitions.addAll(swap_transitions);
            heapify(arr, n, largest);
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

        int size = nodes.length;

        for (int i = size / 2 - 1; i >= 0; i--)
            heapify(nodes, size, i);

        for (int i = size - 1; i >= 0; i--) {
            ArrayList<AlgoState> swap_transitions = FullSwapProcedure(0, i);
            swap_transitions.get(swap_transitions.size() - 1).StoreVariable("i", i);
            int[] copyArray = GetValues();
            swap_transitions.get(0).StoreArrayStatus(copyArray);
            transitions.addAll(swap_transitions);
            heapify(nodes, i, 0);
        }

        return transitions;
    }
}
