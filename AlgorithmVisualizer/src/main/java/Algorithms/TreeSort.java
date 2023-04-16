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
        int[] valueCopy = this.treeValues.clone();
        ArrayList<Integer>connectedLines = new ArrayList<>();
        ArrayList<Integer>visitedNodes = new ArrayList<>();
        ArrayList<Integer>filledNodes = new ArrayList<>();
        int min = 10;
        int max = 190;
        int j =0;
        while(j<valueCopy.length) {
                int child = 0;
                int parent = 0;
                for (int k = 0; k < valueCopy.length; k++) {
                    if (valueCopy[k] >= min && valueCopy[k] <= max) {
                        max = valueCopy[k];
                        if(k == 0) {
                            child = k;
                            Pair<Transition, Transition> transition = HighlightRing(child);
                            stage = new AlgoState(transition);
                            stage.StoreVariable("j", j);
                            if(!visitedNodes.contains(child)) {
                                transitions.add(stage);
                                visitedNodes.add(child);
                            }
                        }else{
                            parent=child;
                            child = k;
                            Pair<Transition, Transition> transition = ConnectNodes(child);
                            stage = new AlgoState(transition);
                            stage.StoreVariable("i", j);
                            if(!connectedLines.contains(child)) {
                                transitions.add(stage);
                                connectedLines.add(child);
                            }

                            transition = HighlightRing(child);
                            stage = new AlgoState(transition);
                            stage.StoreVariable("k", j);
                            if(!visitedNodes.contains(child)) {
                                transitions.add(stage);
                                visitedNodes.add(child);
                            }
                        }
                    }


                }
            Pair<Transition, Transition>transition = SecondaryHighlightNode(child);
            stage = new AlgoState(transition);
            stage.StoreVariable("k", j);
            if(!filledNodes.contains(child)) {
                transitions.add(stage);
                filledNodes.add(child);
            }

            valueCopy[child]=-1;
            min = max;
            max = 190;
            j++;
        }
        return transitions;
    }
}
