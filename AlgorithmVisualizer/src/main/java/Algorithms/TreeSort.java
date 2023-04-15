package Algorithms;

import javafx.animation.Transition;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * BinarySearch algorithm
 */
public class TreeSort extends AbstractAlgorithmTree {

    private Circle[] nodes;

    private int[] treeValues;

    private Line[] treeLines;
    private ArrayList<AlgoState> transitions;
    private String code = "    Create Binary Tree from input\n" +
            "    Traverse in-order\n";


    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public TreeSort(Circle[] nodes, int[] values, Line[] connection) {
        super(nodes, values, connection);
        super.pseudoCode = code;
        bestTime = "Ω(1)";
        averageTime = "θ(log n)";
        worstTime = "O(log n)";
        spaceComplexity = "O(1)";
        this.nodes = super.nodes;
        this.treeValues = super.values;
        this.treeLines = super.connection;
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
        AlgoState stage;

        for (int i = 1; i < nodes.length; i++) {
            int max = 190;
            int min = 10;
            int child = i;
            int parent = 0;
            for (int k = 0; k < i; k++) {
                if (treeValues[k] < treeValues[i] && treeValues[k] >= min) {
                    min = treeValues[k];
                    parent = k;
                } else if (treeValues[k] > treeValues[i] && treeValues[k] <= max) {
                    max = treeValues[k];
                    parent = k;
                }
            }
            Pair<Transition,Transition> transition = ConnectNodes(parent, child);
            stage = new AlgoState(transition);
            transitions.add(stage);
        }
        return transitions;
    }
}
