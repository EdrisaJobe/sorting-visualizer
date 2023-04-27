package Algorithms;

import UserInterface.VisualizerController;
import javafx.animation.Transition;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Tree sort algorithm
 */
public class TreeSort extends AbstractAlgorithmTree {

    static int treeMax=VisualizerController.treeMax;
    static int treeMin = VisualizerController.treeMin;

    private final int[] nodeValues;

    private final ArrayList<AlgoState> transitions;


    /**
     * Constructor, sets the array of nodes.
     *
     * @param nodes Array of boxes
     */
    public TreeSort(StackPane[] nodes, int[] nodeValues, Line[] connection, double vizzWidth, double vizzHeight) {
        super(nodes, nodeValues, connection);
        String code = "    Create Binary Tree from input\n" +
                "    Traverse in-order\n";
        super.pseudoCode = code;
        bestTime = "Ω(1)";
        averageTime = "θ(log n)";
        worstTime = "O(log n)";
        spaceComplexity = "O(1)";
        this.nodeValues = super.nodeValues;
        this.transitions = super.transitions;
        this.vizzHeight= vizzHeight;
        this.vizzWidth = vizzWidth;
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
        //copy array of treeValues so we can alter the copy
        int[] valueCopy = this.nodeValues.clone();
        //create empty array to store the state of the sorted array
        int [] sortedArray = new int[nodeValues.length];
        //these array lists track what nodes have already been altered
        //this is to prevent from repeating animations
        ArrayList<Integer>connectedLines = new ArrayList<>();
        ArrayList<Integer>visitedNodes = new ArrayList<>();
        ArrayList<Integer>filledNodes = new ArrayList<>();
        int min = treeMin;
        int max = treeMax;
        int j =0;
        //each loop will sort one value
        while(j<valueCopy.length) {
            int child = 0;
            for (int k = 0; k < valueCopy.length; k++) {
                //check the current tree node to see if its part of the current path to a leaf
                if (valueCopy[k] >= min && valueCopy[k] <= max) {
                    //update max to current value
                    max = valueCopy[k];
                    //if the node is the root node there is no parents to connect we only highlight or fill in
                    if(k == 0) {
                        child = k;
                        Pair<Transition, Transition> transition = HighlightRing(child);
                        stage = new AlgoState(transition);
                        stage.StoreVariable("j", j);
                        stage.StoreArrayStatus(sortedArray);
                        if(!visitedNodes.contains(child)) {
                            transitions.add(stage);
                            visitedNodes.add(child);
                        }
                    }else{
                        //if the node is not the root node then we must highlight the node and its connection to its parent
                        child = k;
                        //line transition
                        Pair<Transition, Transition> transition = ConnectNodes(child);
                        stage = new AlgoState(transition);
                        stage.StoreVariable("i", j);
                        stage.StoreArrayStatus(sortedArray);
                        if(!connectedLines.contains(child)) {
                            transitions.add(stage);
                            connectedLines.add(child);
                        }
                        //node highlight transition
                        transition = HighlightRing(child);
                        stage = new AlgoState(transition);
                        stage.StoreVariable("k", j);
                        stage.StoreArrayStatus(sortedArray);
                        if(!visitedNodes.contains(child)) {
                            transitions.add(stage);
                            visitedNodes.add(child);
                        }
                    }
                }


            }
            //the last child we visit is the lowest node that is unsorted
            //fill the node to confirm its visited
            Pair<Transition, Transition>transition = SearchTargetHighlightNode(child);
            stage = new AlgoState(transition);
            stage.StoreVariable("k", j);

            if(!filledNodes.contains(child)) {
                transitions.add(stage);
                filledNodes.add(child);
                //update sorted array
                sortedArray[j] = nodeValues[child];
            }
            stage.StoreArrayStatus(sortedArray);
            //change the copied array so that the sorted value is -1
            //this ensures it is no longer considered in the loop as a possible lowest unsorted value
            valueCopy[child]=-1;
            min = max;
            max = treeMax;
            j++;
        }
        return transitions;
    }
}