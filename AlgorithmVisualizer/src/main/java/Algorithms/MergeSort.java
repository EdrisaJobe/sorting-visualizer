package Algorithms;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * MergeSort algorithm
 */
public class MergeSort extends AbstractAlgorithm{

    private Rectangle[] nodes;
    private ArrayList<AlgoState> transitions;

    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public MergeSort(Rectangle[] nodes) {
        super(nodes);
        this.nodes = super.nodes;
        this.transitions = super.transitions;
    }

    private int nextGap(int gap)
    {
        if (gap <= 1)
            return 0;
        return (int)Math.ceil(gap / 2.0);
    }

    public void merge(Rectangle[] arr,  int start, int end) {
        int gap = end - start + 1;
        for (gap = nextGap(gap); gap > 0; gap = nextGap(gap)) {
            for (int i = start; i + gap <= end; i++) {
                int j = i + gap;
                if (arr[i].getHeight() > arr[j].getHeight()) {
                    ArrayList<AlgoState> swap_transitions = FullSwapProcedure(i, j);
                    swap_transitions.get(swap_transitions.size() - 1).StoreVariable("left", start);
                    swap_transitions.get(swap_transitions.size() - 1).StoreVariable("right", end);
                    transitions.addAll(swap_transitions);
                }
            }
        }
    }

    public void MergeSortRecursive(Rectangle nodes[], int start, int end)
    {
        if (start == end)
            return;

        int mid = (start + end) / 2;
        MergeSortRecursive(nodes, start, mid);
        MergeSortRecursive(nodes, mid + 1, end);
        merge(nodes, start, end);
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

        MergeSortRecursive(nodes, 0,nodes.length-1);

        return transitions;
    }
}
