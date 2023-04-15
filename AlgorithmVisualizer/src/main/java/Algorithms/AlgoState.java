package Algorithms;

import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
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
        Transition forwardTranstionContainer = null;
        Transition reverseTranstionContainer = null;

        for (Pair<Transition, Transition> transition: transitions) {
            if(transition.getKey() instanceof ParallelTransition){
                if(forwardTranstionContainer == null){
                    forwardTranstionContainer = new ParallelTransition();
                }
                ((ParallelTransition) forwardTranstionContainer).getChildren().add(transition.getKey());
            }else if (transition.getKey() instanceof SequentialTransition){
                if(forwardTranstionContainer == null){
                    forwardTranstionContainer = new SequentialTransition();
                }
                ((SequentialTransition) forwardTranstionContainer).getChildren().add(transition.getKey());
            }

            if(transition.getValue() instanceof ParallelTransition){
                if(reverseTranstionContainer == null){
                    reverseTranstionContainer = new ParallelTransition();
                }
                ((ParallelTransition) reverseTranstionContainer).getChildren().add(transition.getKey());
            }else if (transition.getValue() instanceof SequentialTransition){
                if(reverseTranstionContainer == null){
                    reverseTranstionContainer = new SequentialTransition();
                }
                ((SequentialTransition) reverseTranstionContainer).getChildren().add(transition.getKey());
            }
        }
        forwardTransition = forwardTranstionContainer;
        reverseTransition = reverseTranstionContainer;
    }


    public void StoreVariable(String variable, int value){
        variables.add(new Pair<>(variable, value));
    }


    public void StoreTransition(Pair<Transition, Transition>... transitions){
        Transition forwardTranstionContainer = null;
        Transition reverseTranstionContainer = null;

        for (Pair<Transition, Transition> transition: transitions) {
            if(transition.getKey() instanceof ParallelTransition){
                if(forwardTranstionContainer == null){
                    forwardTranstionContainer = new ParallelTransition();
                }
                ((ParallelTransition) forwardTranstionContainer).getChildren().add(transition.getKey());
            }else if (transition.getKey() instanceof SequentialTransition){
                if(forwardTranstionContainer == null){
                    forwardTranstionContainer = new SequentialTransition();
                }
                ((SequentialTransition) forwardTranstionContainer).getChildren().add(transition.getKey());
            }

            if(transition.getValue() instanceof ParallelTransition){
                if(reverseTranstionContainer == null){
                    reverseTranstionContainer = new ParallelTransition();
                }
                ((ParallelTransition) reverseTranstionContainer).getChildren().add(transition.getKey());
            }else if (transition.getValue() instanceof SequentialTransition){
                if(reverseTranstionContainer == null){
                    reverseTranstionContainer = new SequentialTransition();
                }
                ((SequentialTransition) reverseTranstionContainer).getChildren().add(transition.getKey());
            }
        }
        forwardTransition = forwardTranstionContainer;
        reverseTransition = reverseTranstionContainer;
    }
}
