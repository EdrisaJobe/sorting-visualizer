package Algorithms;

import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;
import java.util.*;

//TODO::Make it visualize the buckets.

/**
 * BucketSort algorithm
 */
public class BucketSort extends AbstractAlgorithm {

    private Rectangle[] nodes;
    private ArrayList<AlgoState> transitions;

    private int x_gap;

    private int box_width;
    private String code =   " create B empty buckets\n" +
                            " for each element\n" +
                            "     map element into a bucket\n" +
                            " for each bucket\n" +
                            "     sort each bucket\n" +
                            "       concat all the sorted elements\n" +
                            " output the sorted elements";

    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public BucketSort(Rectangle[] nodes, int x_gap, int box_width) {
        super(nodes, x_gap, box_width);
        super.pseudoCode = code;

        bestTime = "Ω(n)";
        averageTime = "θ(n + n^2/k + k)";
        worstTime = "O(n*k)";
        spaceComplexity = "O(n+k)";

        this.nodes = super.nodes;
        this.transitions = super.transitions;
        this.x_gap = super.x_gap;
        this.box_width = super.box_width;
    }

    private int hash(int num, int size)
    {
        return num / (size*10);
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
        int n = 10;

        Vector<Rectangle>[] buckets = new Vector[n];

        for (int i = 0; i < n; i++) {
            buckets[i] = new Vector<Rectangle>();
        }

        for (int i = 0; i < n; i++) {
            buckets[hash((int) nodes[i].getHeight(), n)].add(nodes[i]);
        }


        for (int i = 0; i < n; i++) {
            int size = buckets[i].size();
            for (int x = 0; x < size - 1; x++)
                for (int y = 0; y < size - x - 1; y++)
                    if (buckets[i].get(y).getHeight() > buckets[i].get(y+1).getHeight()) {
                        Rectangle temp = buckets[i].get(y);
                        buckets[i].set(y, buckets[i].get(y+1));
                        buckets[i].set(y+1, temp);
                    }
            for (Rectangle rect: buckets[i]) {
                System.out.print(rect.getHeight() + " ");

            }
            System.out.println();
        }

        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < buckets[i].size(); j++) {
                Rectangle bucketElement = buckets[i].get(j);
                int arrIndex = index;
                while (nodes[arrIndex] != bucketElement) {
                    arrIndex++;
                }
                ArrayList<AlgoState> swap_transitions = FullSwapProcedure(index, arrIndex);
                swap_transitions.get(swap_transitions.size() - 1).StoreVariable("i", index);
                swap_transitions.get(swap_transitions.size() - 1).StoreVariable("j", j);
                swap_transitions.get(swap_transitions.size() - 1).StoreVariable("buckets", n);
                transitions.addAll(swap_transitions);
                index++;
            }
        }

        return transitions;
    }
}
