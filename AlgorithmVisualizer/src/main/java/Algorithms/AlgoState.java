package Algorithms;

import javafx.animation.Transition;
import javafx.util.Pair;

import java.util.ArrayList;


/**
 * A utility class that contains an algorithm's current "state" and transition.
 */
public class AlgoState {
    //The last transition that was made.
    public Transition lastTransition;

    public AlgoState(){
    }

    public AlgoState(Transition transition){
        lastTransition = transition;
    }

    //The state of the current variables, such as i,j,k.
    //for example:  (i, 5), (j,8), (k,3)
    public ArrayList<Pair<String, Integer>> variables = new ArrayList<>();

    public void StoreVariable(String variable, int value){
        variables.add(new Pair<>(variable, value));
    }

    public void StoreTransition(Transition transition){
        lastTransition = transition;
    }
}
