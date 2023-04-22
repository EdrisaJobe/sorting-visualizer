package Algorithms;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.*;

//TODO::Make it visualize the buckets.

/**
 * BucketSort algorithm
 */
public class BucketSort {


    //circles which are nodes
    private double vizzWidth;
    //circles which are nodes
    private double vizzHeight;
    //stackpane which holds both the nodes and the text
    private StackPane[] nodes;
    //actual int values of nodes
    private int[] nodeValues;
    //bounds for each container
    private int[] bucketLabels;
    // array of arrays
    //each array stores the values in that particualr bucket
    private int[][] bucketContainers;
    //transitions
    //List containing all the transitions that this algorithm makes.
    //This allows us to go backwards and forwards.
    public ArrayList<AlgoState> transitions = new ArrayList<>();
    //Duration of the transitions.
    private final static float ANIM_DURATION = 0.95f;

    public static String bestTime = "Ω(n)";
    public static String averageTime = "θ(n + n^2/k + k)";
    public static String worstTime = "O(n*k)";
    public static String spaceComplexity = "O(n+k)";

    //Highlight Colors.
    private final Color BASE_COLOR = Color.web("#000000");
    private final Color PRIMARY_COLOR = Color.web("#aeff80");
    private final Color SECONDARY_COLOR = Color.web("#fc6868");
    private final Color TARGET_COLOR = Color.web("#96f6ff");

    private int x_gap = 45;
    private int bucketPadding =50;

    private int y_gap = 45;

    public String pseudoCode = " create B empty buckets\n" +
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
    public BucketSort(StackPane[] nodes, int[] nodeValues, int numBuckets, double vizzWidth, double vizzHeight) {

        this.bestTime = "Ω(n)";
        this.averageTime = "θ(n + n^2/k + k)";
        this.worstTime = "O(n*k)";
        this.spaceComplexity = "O(n+k)";

        this.nodes = nodes;
        this.nodeValues = nodeValues;
        this.vizzWidth = vizzWidth;
        this.vizzHeight = vizzHeight;
        GetBucketLabels(nodeValues, numBuckets);
    }

    /**
     * Returns an array of integers which serves as a means for sorting nodes into the right bucket such as [0,5,10] bucket 1 is in the range 0-5 and bucket 2 is in range 6-10
     *
     * @param nodeVals   the array of values to be bucket sorted
     * @param numBuckets is the number of buckets we need
     * @return is and int array
     */
    private void GetBucketLabels(int[] nodeVals, int numBuckets) {
        int[] bucketLabels = new int[numBuckets + 1];
        int min = 0;
        int max = 0;
        for (int i = 0; i < nodeVals.length; i++) {
            if (nodeVals[i] > max)
                max = nodeVals[i];
            if (nodeVals[i] < min)
                min = nodeVals[i];
        }
        int diff = max - min;
        int bucketSize = diff / numBuckets;
        bucketLabels[0] = min - 1;
        for (int i = 1; i < numBuckets; i++) {
            bucketLabels[i] = min + bucketSize * i;
        }
        bucketLabels[numBuckets] = max + 1;
        this.bucketLabels = bucketLabels;
    }

    /**
     * places the newNode into the bucketContainer at its sorted location
     *
     * @param bucketContainer current bucket container
     * @param newNode         node
     * @return index of insertion
     */
    protected int SortBucket(int[] bucketContainer, int newNode) {

        for (int i = 0; i < bucketContainer.length; i++) {
            if (newNode < bucketContainer[i]) {
                return i;
            }
        }
        //error
        return bucketContainer.length;
    }

    /**
     * The function that ultimately does the sorting.
     * This function should make use of the swap and Highlight functions to make transitions.
     * The state of the algorithm should be saved in each step of the algorithm in the transitions list.
     * Each AlgoState in the transitions list represents one step in the algorithm.
     *
     * @return The Arraylist of transitions.
     */
    public ArrayList<AlgoState> RunAlgorithm() {
        AlgoState stage;
        //get number of buckets
        int numBuckets = this.bucketLabels.length - 1;
        //reset bucket Containers
        this.bucketContainers = new int[numBuckets][];
        //initialize each bucket
        for (int j = 0; j < numBuckets; j++) {
            bucketContainers[j] = new int[0];
        }

        for (int i = 0; i < nodeValues.length; i++) {//for each value sort to appropriate bucket
            for (int k = 0; k < bucketLabels.length; k++) {
                if (nodeValues[i] < bucketLabels[k]) {//check each label to see if it belongs to this bucket or not
                    //get index of insertion into bucket
                    int newIndex = SortBucket(bucketContainers[k - 1], nodeValues[i]);
                    //if the value is not the last position move the nodes up to make room
                    if (newIndex != bucketContainers[k-1].length) {
                        Pair<Transition, Transition> transition = MakeRoom(newIndex, k - 1);
                        stage = new AlgoState(transition);
                        stage.StoreVariable("i", k);
                        transitions.add(stage);
                    }
                    //update bucket array
                    int[] newArray = new int[bucketContainers[k - 1].length + 1];
                    for (int pos = 0; pos < newArray.length; pos++) {
                        if (pos < newIndex) {
                            newArray[pos] = bucketContainers[k - 1][pos];
                        } else if (pos == newIndex) newArray[pos] = nodeValues[i];
                        else {
                            newArray[pos] = bucketContainers[k - 1][pos - 1];
                        }
                    }
                    bucketContainers[k - 1] = newArray;
                    //create animation for insertion
                    Pair<Transition, Transition> transition = PlaceIntoBucket(i, k - 1, newIndex);
                    stage = new AlgoState(transition);
                    stage.StoreVariable("j", k);
                    //store animation
                    transitions.add(stage);
                    break;
                }
            }
        }
        //loop through the bucket containers to color nodes in order and store their animation
        for (int bucket = 0; bucket < bucketContainers.length;bucket++){
            for (int val = 0 ; val <bucketContainers[bucket].length;val++){
                int nodeIndex = GetNode(bucketContainers[bucket][val]);
                Pair<Transition, Transition> transition  = SecondaryHighlightNode(nodeIndex);
                stage = new AlgoState(transition);
                stage.StoreVariable("i", val);
                transitions.add(stage);
            }
        }
        return transitions;
    }


    /**
     * Places node into bucket at sorted location
     *
     * @param nodeIndex   index which points to the node to move
     * @param bucket      bucket to be moved to
     * @param indexInsert index of bucket insertion
     * @return
     */
    final public Pair<Transition, Transition> PlaceIntoBucket(int nodeIndex, int bucket, int indexInsert) {
        //get starting position of stack pane
        double startX = nodes[nodeIndex].getTranslateX()+20;
        double startY = nodes[nodeIndex].getTranslateY()+20;
        //bucket increments will place you in the middle of buckets
        int bucketIncrement = (int) ((vizzWidth - 50) / (bucketContainers.length * 2));

        //get the end position of the node once its been inserted
        double endXNode = GetBucketX(bucketIncrement, bucket);
        double endYNode = GetBucketY(indexInsert);
        //create path from start to end
        Path path_forward = new Path();
        path_forward.getElements().add(new MoveTo(startX, startY));
        path_forward.getElements().add(new LineTo(endXNode, endYNode));
        //add path to a transition on the stackpane
        PathTransition node_path_forward = new PathTransition();
        node_path_forward.setDuration(Duration.seconds(ANIM_DURATION));
        node_path_forward.setPath(path_forward);
        node_path_forward.setNode(nodes[nodeIndex]);

        //do in reverse
        Path path_reverse = new Path();
        path_reverse.getElements().add(new MoveTo(endXNode, endYNode));
        path_reverse.getElements().add(new LineTo(startX, startY));

        PathTransition node_path_reverse = new PathTransition();
        node_path_reverse.setDuration(Duration.seconds(ANIM_DURATION));
        node_path_reverse.setPath(path_reverse);
        node_path_reverse.setNode(nodes[nodeIndex]);


        // Store the transitions and their reverse
        ParallelTransition forward = new ParallelTransition(node_path_forward);
        ParallelTransition reverse = new ParallelTransition(node_path_reverse);

        Pair<Transition, Transition> anims = new Pair<>(forward, reverse);

        //Think of it as storing multiple transitions in one transition.
        return anims;
    }

    /**
     *
     * @param value the value of some node
     * @return the index of the node in the node value array
     */
    private int GetNode(int value) {
        for (int x = 0; x < nodeValues.length; x++) {
            if (nodeValues[x] == value)
                return x;
        }
        return 0;
    }

    /**
     * Moves nodes upwards in a bucket to make room for a insertion
     *
     * @param insertIndex   circle to be moved
     * @param bucket bucket to be moved to
     * @return returns a transition which moves all the nodes at and above a certain index to make room for an insertion at said index
     */
    final public Pair<Transition, Transition> MakeRoom(int insertIndex, int bucket) {
        //calculate the bucket increment
        int bucketIncrement = (int) ((vizzWidth - bucketPadding) / (bucketContainers.length * 2));
        //create a transition for forwards backwards
        ParallelTransition forward = new ParallelTransition();
        ParallelTransition reverse = new ParallelTransition();
        for (int i = insertIndex; i < bucketContainers[bucket].length; i++) {
            //go through each node and increment the height on the pane
            double startX = GetBucketX(bucketIncrement, bucket);
            double startY = GetBucketY(i);
            double endY = startY - y_gap;

            Path path_forward = new Path();
            path_forward.getElements().add(new MoveTo(startX, startY));
            path_forward.getElements().add(new LineTo(startX, endY));

            Path path_reverse = new Path();
            path_reverse.getElements().add(new MoveTo(startX, endY));
            path_reverse.getElements().add(new LineTo(startX, startY));


            PathTransition node_path_forward = new PathTransition();
            node_path_forward.setDuration(Duration.seconds(ANIM_DURATION));
            node_path_forward.setPath(path_forward);
            node_path_forward.setNode(nodes[GetNode(bucketContainers[bucket][i])]);
            forward.getChildren().add(node_path_forward);

            PathTransition node_path_reverse = new PathTransition();
            node_path_reverse.setDuration(Duration.seconds(ANIM_DURATION));
            node_path_reverse.setPath(path_forward);
            node_path_reverse.setNode(nodes[GetNode(bucketContainers[bucket][i])]);
            reverse.getChildren().add(node_path_reverse);

        }

        Pair<Transition, Transition> anims = new Pair<>(forward, reverse);

        //Think of it as storing multiple transitions in one transition.
        return anims;
    }


    /**
     *
     * @param bucketIncrement size of bucket increment
     * @param bucket the bucket its being sorted into
     * @return the x position of the node after being inserted into bucket
     */
    private double GetBucketX(int bucketIncrement, int bucket) {
        double endX = bucketPadding + (bucketIncrement * ((bucket * 2) + 1)) - bucketPadding/2;
        return endX;

    }

    /**
     *
     * @param indexInsert in index of the bucket in which its being inserted
     * @return the y position of the node after being inserted
     */
    private double GetBucketY(int indexInsert) {
        double endY = vizzHeight - (y_gap * (indexInsert+1));
        return endY;

    }

    /**
     * Returns a transition of highlighting a node with the secondary color.
     * @param index Index of the node to highlight.
     * @return The transition highlighting the node.
     */
    final public Pair<Transition, Transition> SecondaryHighlightNode(int index){
        Circle node = (Circle) nodes[index].lookup("#myCircle");

        return ColorNode(SECONDARY_COLOR, node);
    }

    /**
     * Utility function returning a transition coloring a node.
     * @param color New Color of the node.
     * @param node The node to color.
     * @return The transition containing the filling of the node.
     */
    private Pair<Transition, Transition> ColorNode(Color color, Shape node){
        FillTransition forward = new FillTransition(Duration.seconds(ANIM_DURATION));
        FillTransition reverse = new FillTransition(Duration.seconds(ANIM_DURATION));
        Color currentColor = (Color) node.getFill();
        forward.setShape(node);
        forward.setFromValue(currentColor);
        forward.setToValue(color);

        reverse.setShape(node);
        reverse.setFromValue(color);
        reverse.setToValue(currentColor);

        //node.setFill(color);

        reverse.setOnFinished(evnt -> {
            node.setFill(currentColor);
        });

        return new Pair<>(forward, reverse);
    }


}
