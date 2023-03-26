package UserInterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class QuizController implements Initializable {

    @FXML
    private Label qTitle, qQuestion;

    @FXML
    private Button q1, q2, q3, q4, q5, q6, q7, q8, q9, q10, qA, qB, qC, qD, resetButton;

    /**
     * Initializes the UI elements.
     * 
     * @param url            url
     * @param resourceBundle srBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /* QUESTION 1 - EASY QUESTIONS */
        q1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                // title and question displays
                qTitle.setText("Question 1");
                qQuestion.setText("What is Bubble sort's time best case time complexity?");

                // question answers to choose from
                qA.setText("A. O(log n) Logarithmic");
                qB.setText("B. O(n^2) Quadratic");
                qC.setText("C. O(n) Linear");
                qD.setText("D. O(1) Constant");

                // change the background color based on selection
                EventHandler<ActionEvent> answerHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Button selectedButton = (Button) event.getSource(); // Get the button that was clicked
                        if (selectedButton == qC) { // if qC button was clicked
                            q1.setStyle("-fx-background-color: green;");
                            qC.setStyle("-fx-background-color: green;"); // set the background color to green if correct
                            q2.setDisable(false);
                            resetButton.setDisable(false);
                        } else { // if any other button was clicked
                            selectedButton.setStyle("-fx-background-color: red;"); // set the background color to red if wrong
                            q1.setStyle("-fx-background-color: red;");
                        }
                    }
                };

                // Add the answerHandler to the buttons
                qA.setOnAction(answerHandler);
                qB.setOnAction(answerHandler);
                qC.setOnAction(answerHandler);
                qD.setOnAction(answerHandler);

                // reset the questions
                qA.setStyle("");
                qB.setStyle("");
                qC.setStyle("");
                qD.setStyle("");
            }
        });

        /* QUESTION 2 */
        q2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                qTitle.setText("Question 2");
                qQuestion.setText("A stable sorting algorithm must...");

                // question answers to choose from
                qA.setText("A. Maintains order of items with equal sort keys");
                qB.setText("B. Does not crash at all");
                qC.setText("C. Never runs out of memory");
                qD.setText("D. Does not exist");

                // change the background color based on selection
                EventHandler<ActionEvent> answerHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Button selectedButton = (Button) event.getSource(); // Get the button that was clicked
                        if (selectedButton == qA) { // if qC button was clicked
                            q2.setStyle("-fx-background-color: green;");
                            qA.setStyle("-fx-background-color: green;"); // set the background color to green if correct
                            q3.setDisable(false);
                        } else { // if any other button was clicked
                            selectedButton.setStyle("-fx-background-color: red;"); // set the background color to red if wrong
                            q2.setStyle("-fx-background-color: red;");
                        }
                    }
                };

                // Add the answerHandler to the buttons
                qA.setOnAction(answerHandler);
                qB.setOnAction(answerHandler);
                qC.setOnAction(answerHandler);
                qD.setOnAction(answerHandler);

                // reset the questions
                qA.setStyle("");
                qB.setStyle("");
                qC.setStyle("");
                qD.setStyle("");
            }
        });

        /* QUESTION 3 */
        q3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                qTitle.setText("Question 3");
                qQuestion.setText("Insertion sort has the same worst and average case of...");

                // question answers to choose from
                qA.setText("A. O(1) Constant");
                qB.setText("B. O(n^2) Quadratic");
                qC.setText("C. O(n) Linear");
                qD.setText("D. O(log n) Logarithmic");

                // change the background color based on selection
                EventHandler<ActionEvent> answerHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Button selectedButton = (Button) event.getSource(); // Get the button that was clicked
                        if (selectedButton == qB) { // if qC button was clicked
                            q3.setStyle("-fx-background-color: green;");
                            qB.setStyle("-fx-background-color: green;"); // set the background color to green if correct
                            q4.setDisable(false);
                        } else { // if any other button was clicked
                            selectedButton.setStyle("-fx-background-color: red;"); // set the background color to red if wrong
                            q3.setStyle("-fx-background-color: red;");
                        }
                    }
                };

                // Add the answerHandler to the buttons
                qA.setOnAction(answerHandler);
                qB.setOnAction(answerHandler);
                qC.setOnAction(answerHandler);
                qD.setOnAction(answerHandler);

                // reset the questions
                qA.setStyle("");
                qB.setStyle("");
                qC.setStyle("");
                qD.setStyle("");
            }
        });

        /* QUESTION 4 - MEDIUM QUESTIONS */
        q4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                qTitle.setText("Question 4");
                qQuestion.setText("Which of the following is example of in-place algorithm?");

                // question answers to choose from
                qA.setText("A. Insertion Sort");
                qB.setText("B. Bubble Sort");
                qC.setText("C. Merge Sort");
                qD.setText("D. All of the above");

                // change the background color based on selection
                EventHandler<ActionEvent> answerHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Button selectedButton = (Button) event.getSource(); // Get the button that was clicked
                        if (selectedButton == qC) { // if qC button was clicked
                            q4.setStyle("-fx-background-color: green;");
                            qC.setStyle("-fx-background-color: green;"); // set the background color to green if correct
                            q5.setDisable(false);
                        } else { // if any other button was clicked
                            selectedButton.setStyle("-fx-background-color: red;"); // set the background color to red if wrong
                            q4.setStyle("-fx-background-color: red;");
                        }
                    }
                };

                // Add the answerHandler to the buttons
                qA.setOnAction(answerHandler);
                qB.setOnAction(answerHandler);
                qC.setOnAction(answerHandler);
                qD.setOnAction(answerHandler);

                // reset the questions
                qA.setStyle("");
                qB.setStyle("");
                qC.setStyle("");
                qD.setStyle("");
            }
        });

        /* QUESTION 5 */
        q5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                qTitle.setText("Question 5");
                qQuestion.setText("In context with time-complexity, find the odd one...");

                // question answers to choose from
                qA.setText("A. Deletion from a linked list");
                qB.setText("B. Searching in a Hash Table");
                qC.setText("C. Adding an edge in a matrix");
                qD.setText("D. Heapify a Binary heap");

                // change the background color based on selection
                EventHandler<ActionEvent> answerHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Button selectedButton = (Button) event.getSource(); // Get the button that was clicked
                        if (selectedButton == qD) { // if qC button was clicked
                            q5.setStyle("-fx-background-color: green;");
                            qD.setStyle("-fx-background-color: green;"); // set the background color to green if correct
                            q6.setDisable(false);
                        } else { // if any other button was clicked
                            selectedButton.setStyle("-fx-background-color: red;"); // set the background color to red if wrong
                            q5.setStyle("-fx-background-color: red;");
                        }
                    }
                };

                // Add the answerHandler to the buttons
                qA.setOnAction(answerHandler);
                qB.setOnAction(answerHandler);
                qC.setOnAction(answerHandler);
                qD.setOnAction(answerHandler);

                // reset the questions
                qA.setStyle("");
                qB.setStyle("");
                qC.setStyle("");
                qD.setStyle("");
            }
        });

        /* QUESTION 6 */
        q6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                qTitle.setText("Question 6");
                qQuestion.setText("If the array is already sorted, which algorithm exhibits the best performance?");

                // question answers to choose from
                qA.setText("A. Heap Sort");
                qB.setText("B. Insertion Sort");
                qC.setText("C. Quick Sort");
                qD.setText("D. Merge Sort");

                // change the background color based on selection
                EventHandler<ActionEvent> answerHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Button selectedButton = (Button) event.getSource(); // Get the button that was clicked
                        if (selectedButton == qB) { // if qC button was clicked
                            q6.setStyle("-fx-background-color: green;");
                            qB.setStyle("-fx-background-color: green;"); // set the background color to green if correct
                            q7.setDisable(false);
                        } else { // if any other button was clicked
                            selectedButton.setStyle("-fx-background-color: red;"); // set the background color to red if wrong
                            q6.setStyle("-fx-background-color: red;");
                        }
                    }
                };

                // Add the answerHandler to the buttons
                qA.setOnAction(answerHandler);
                qB.setOnAction(answerHandler);
                qC.setOnAction(answerHandler);
                qD.setOnAction(answerHandler);

                // reset the questions
                qA.setStyle("");
                qB.setStyle("");
                qC.setStyle("");
                qD.setStyle("");
            }
        });

        /* QUESTION 7 */
        q7.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                qTitle.setText("Question 7");
                qQuestion.setText("Heap is an example of?");

                // question answers to choose from
                qA.setText("A. Complete binary tree");
                qB.setText("B. Binary search tree");
                qC.setText("C. Sparse tree");
                qD.setText("D. Spanning tree");

                // change the background color based on selection
                EventHandler<ActionEvent> answerHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Button selectedButton = (Button) event.getSource(); // Get the button that was clicked
                        if (selectedButton == qA) { // if qC button was clicked
                            q7.setStyle("-fx-background-color: green;");
                            qA.setStyle("-fx-background-color: green;"); // set the background color to green if correct
                            q8.setDisable(false);
                        } else { // if any other button was clicked
                            selectedButton.setStyle("-fx-background-color: red;"); // set the background color to red if wrong
                            q7.setStyle("-fx-background-color: red;");
                        }
                    }
                };

                // Add the answerHandler to the buttons
                qA.setOnAction(answerHandler);
                qB.setOnAction(answerHandler);
                qC.setOnAction(answerHandler);
                qD.setOnAction(answerHandler);

                // reset the questions
                qA.setStyle("");
                qB.setStyle("");
                qC.setStyle("");
                qD.setStyle("");
            }
            
        });

        /* QUESTION 8 - HARD QUESTIONS */
        q8.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                qTitle.setText("Question 8");
                qQuestion.setText("Which of the following search algorithms has a worst-case time complexity of O(n) for searching an array of size n?");

                // question answers to choose from
                qA.setText("A. Linear search");
                qB.setText("B. Binary search");
                qC.setText("C. Both A and B");
                qD.setText("D. None of the above");

                // change the background color based on selection
                EventHandler<ActionEvent> answerHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Button selectedButton = (Button) event.getSource(); // Get the button that was clicked
                        if (selectedButton == qD) { // if qC button was clicked
                            q8.setStyle("-fx-background-color: green;");
                            qD.setStyle("-fx-background-color: green;"); // set the background color to green if correct
                            q9.setDisable(false);
                        } else { // if any other button was clicked
                            selectedButton.setStyle("-fx-background-color: red;"); // set the background color to red if wrong
                            q8.setStyle("-fx-background-color: red;");
                        }
                    }
                };

                // Add the answerHandler to the buttons
                qA.setOnAction(answerHandler);
                qB.setOnAction(answerHandler);
                qC.setOnAction(answerHandler);
                qD.setOnAction(answerHandler);

                // reset the questions
                qA.setStyle("");
                qB.setStyle("");
                qC.setStyle("");
                qD.setStyle("");
            }
        });

        /* QUESTION 9 */
        q9.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                qTitle.setText("Question 9");
                qQuestion.setText("Which of the following search algorithms has a worst-case time complexity of O(n) for searching an array of size n?");

                // question answers to choose from
                qA.setText("A. Merge sort");
                qB.setText("B. Heap sort");
                qC.setText("C. Insertion sort");
                qD.setText("D. Quick sort");

                // change the background color based on selection
                EventHandler<ActionEvent> answerHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Button selectedButton = (Button) event.getSource(); // Get the button that was clicked
                        if (selectedButton == qC) { // if qC button was clicked
                            q9.setStyle("-fx-background-color: green;");
                            qC.setStyle("-fx-background-color: green;"); // set the background color to green if correct
                            q10.setDisable(false);
                        } else { // if any other button was clicked
                            selectedButton.setStyle("-fx-background-color: red;"); // set the background color to red if wrong
                            q9.setStyle("-fx-background-color: red;");
                        }
                    }
                };

                // Add the answerHandler to the buttons
                qA.setOnAction(answerHandler);
                qB.setOnAction(answerHandler);
                qC.setOnAction(answerHandler);
                qD.setOnAction(answerHandler);

                // reset the questions
                qA.setStyle("");
                qB.setStyle("");
                qC.setStyle("");
                qD.setStyle("");
            }
        });

        /* QUESTION 10 */
        q10.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                qTitle.setText("Question 10");
                qQuestion.setText("Which of the following sorting algorithms can be used to sort data that is too large to fit into memory?");

                // question answers to choose from
                qA.setText("A. Bubble sort");
                qB.setText("B. Insertion sort");
                qC.setText("C. Selection sort");
                qD.setText("D. Radix sort");

                // change the background color based on selection
                EventHandler<ActionEvent> answerHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Button selectedButton = (Button) event.getSource(); // Get the button that was clicked
                        if (selectedButton == qD) { // if qC button was clicked
                            q10.setStyle("-fx-background-color: green;");
                            qD.setStyle("-fx-background-color: green;"); // set the background color to green if correct
                        } else { // if any other button was clicked
                            selectedButton.setStyle("-fx-background-color: red;"); // set the background color to red if wrong
                            q10.setStyle("-fx-background-color: red;");
                        }
                    }
                };

                // Add the answerHandler to the buttons
                qA.setOnAction(answerHandler);
                qB.setOnAction(answerHandler);
                qC.setOnAction(answerHandler);
                qD.setOnAction(answerHandler);

                // reset the questions
                qA.setStyle("");
                qB.setStyle("");
                qC.setStyle("");
                qD.setStyle("");
            }
        });

        
        /* RESET THE QUIZ */
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                // reset the questions
                qA.setStyle("");
                qB.setStyle("");
                qC.setStyle("");
                qD.setStyle("");

                // reset the clickability
                q1.setDisable(false);
                q2.setDisable(true);
                q3.setDisable(true);
                q4.setDisable(true);
                q5.setDisable(true);
                q6.setDisable(true);
                q7.setDisable(true);
                q8.setDisable(true);
                q9.setDisable(true);
                q10.setDisable(true);
                resetButton.setDisable(true);

                // reset the button colors
                q1.setStyle("");
                q2.setStyle("");
                q3.setStyle("");
                q4.setStyle("");
                q5.setStyle("");
                q6.setStyle("");
                q7.setStyle("");
                q8.setStyle("");
                q9.setStyle("");
                q10.setStyle("");
            }
        });
        
    }
}
