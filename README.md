# The Sorting-Visualizer

The Algorithm code is now seperate from the UI code,
Below are summaries of the different classes we have.

## Abstract Algorithm
This is an abstract class that's meant to be extended.
By default, it contains functions for swapping and highlighting nodes.

The class contains a transitions list. The idea is that every single transition that we make is stored in this list. 
We run our sorting/searching algorithm, store every transition and return that list of transitions. We then play this list of transitions in the frontend UI.

The transition list is made of AlgoState Objects, which is just an object that can store an animation transition and the algorithms variables (more details later)

The swap and highlight functions return a transition. Whenever these functions are called, the returned transitions should be added to the transitions list to be saved 
along with the algorithms variables. The BubbleSort class shows how to extend the AbstractAlgorithm. 

## Algo State
This is a helper class to store a transition and a list of (String, int) pairs. The (String, int) pairs are meant to store the algorithms variables.
For example, BubbleSort has two loop variables, i and j. We would store these like ("i", 5). The reason we are storing these is so that we can display
them in pseudo code UI.

## Running & Testing
Simply clone the project within a directory and run any of the ".java" files within ...\sorting-visualizer\AlgorithmVisualizer\src\main\java\UserInterface, load up should be instant.

## Visualize page
![image](https://user-images.githubusercontent.com/48189579/233856443-0dd90a22-0166-4512-863c-a9cf7eaddb92.png)

## Compare page
![image](https://user-images.githubusercontent.com/48189579/233856496-3a4d13a1-4b2e-4ae1-a7d0-307ae13fbbf1.png)

## Algorithm Ranking page
![image](https://user-images.githubusercontent.com/48189579/233856525-56c245fc-ce99-41d8-ad94-4d86a58b9001.png)

## Quiz page
![image](https://user-images.githubusercontent.com/48189579/233856552-590f004c-52fd-427a-a7c1-dc8670e9bc88.png)
