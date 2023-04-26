package Algorithms;

import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.util.Pair;

import java.util.ArrayList;


/**
 * A utility class that contains an algorithm's current "state" and transition.
 */
public class AlgoState {
    //The last transition that was made.
    public Transition forwardTransition;
    public Transition reverseTransition;

    //The state of the current variables, such as i,j,k.
    //for example:  (i, 5), (j,8), (k,3)
    public ArrayList<Pair<String, Integer>> variables = new ArrayList<>();

    public String arrayStatus = null;

    public AlgoState(){
    }

    public AlgoState(Transition forward, Transition reverse){
        forwardTransition = forward;
        reverseTransition = reverse;
    }

    public AlgoState(Pair<Transition, Transition> transitions){
        forwardTransition = transitions.getKey();
        reverseTransition = transitions.getValue();
    }

    public AlgoState(Pair<Transition, Transition>... transitions){
        ParallelTransition forwardTranstionContainer = new ParallelTransition();
        ParallelTransition reverseTranstionContainer = new ParallelTransition();

        for (Pair<Transition, Transition> transition: transitions) {
            forwardTranstionContainer.getChildren().add(transition.getKey());
            reverseTranstionContainer.getChildren().add(transition.getValue());
        }
        forwardTransition = forwardTranstionContainer;
        reverseTransition = reverseTranstionContainer;
    }


    public void StoreVariable(String variable, int value){
        variables.add(new Pair<>(variable, value));
    }


    public void StoreTransition(Pair<Transition, Transition>... transitions){
        ParallelTransition forwardTranstionContainer = new ParallelTransition();
        ParallelTransition reverseTranstionContainer = new ParallelTransition();

        for (Pair<Transition, Transition> transition: transitions) {
            forwardTranstionContainer.getChildren().add(transition.getKey());
            reverseTranstionContainer.getChildren().add(transition.getValue());
        }
        forwardTransition = forwardTranstionContainer;
        reverseTransition = reverseTranstionContainer;
    }

    /**
     * converts and array of integers to a string
     * @param intArray array of integers to be converted to string
     * @return string form of array (ex. "[*,*,*,...,]")
     */
    public String ConvertArrayToString(int[] intArray){
        StringBuilder arrayString = new StringBuilder("");
        for(int i =0 ;i <intArray.length;i++){
            arrayString.append(intArray[i]);
            if(i != intArray.length-1)
                arrayString.append(", ");
        }
        arrayString.append("");
        return arrayString.toString();
    }

    /**
     * adds the passed array into the arrayStatus as a string
     * @param sorted int array containing the current state of sorted values
     */
    public void StoreArrayStatus(int[]sorted){
        String sortedArray = ConvertArrayToString(sorted);
        arrayStatus = sortedArray;
    }
}
