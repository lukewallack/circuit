****************
* Circuit (Final Project)
* CS 221
* 12/12/2025
* Luke Wallack
**************** 

## ANALYSIS

### How does the choice of Storage configuration (stack vs queue) affect the sequence in which paths are explored in the search algorithm? (This requires more than a "stacks are LIFOs and queues are FIFOs" answer.)
  The choice of storage configuration changes which paths are explored by the search algorithm in each iteration. The end result is the same, but the sequence gets altered. The stack storage configuration will result in a very different sequence than the queue storage configuration due to the depth-first nature of stack storage evaluation. This means that in the case where we have 2 options that must be evaluated, option 2 will be evaluated until a solution is found, and only after that is complete, option 1 will begin to be evaluated. This differs greatly from the breadth-first queue storage configuration, which would evaluate option 1, then option 2, then the child nodes that result from evaluating option 1, and then the child nodes that result from evaluation option 2. I have also attached two visuals I created below to demonstrate the differences in path sequence.

  **Basic Stack Algorithm Visualization:**
  ![StackAlgorithm](assets/StackAlgorithm.svg)
  **Basic Queue Algorithm Visualization:**
  ![QueueAlgorithm](assets/QueueAlgorithm.svg)

### Is the total number of search states (possible paths) affected by the choice of stack or queue?
  No matter what, the total number of search states do not change at all due to using a stack versus a queue, or vice versa. The only thing that changes when switching between these two configurations is the order by which the search states are explored. When using a stack or a queue storage configuration, you will receive the same solutions, and the same number of search states.

### Is using one of the storage structures likely to find a solution in fewer steps than the other? Always?
  Using a stack configuration, you could find a solution in fewer steps than a queue variation. This results from the depth-first nature of search state exploration, if the first path that the stack decides to explore just happens to have a solution in a short number of steps. However, it is not always the case that the stack configuration will find a solution in fewer steps. This is because in many cases, the stack might spend many steps exploring a long path, whereas the queue might find the solution on a shorter path before the stack path ends. Because of this, in most cases the queue would be more efficient in finding a solution on average in fewer steps.

### Does using either of the storage structures guarantee that the first solution found will be a shortest path?
  The queue storage structure guarantees that the first solution found is on the shortest path. This is because it will explore each path in layers (like if you were to have a table of search states, it go down each column before moving to the next). Thus, the first solution found by the queue will also be the shortest path to a solution every time.

### How is memory use (the maximum number of states in Storage at one time) affected by the choice of underlying structure?
  Memory use is affected by the choice of underlying structure because of the differences between performing a depth-first and breadth-first search. In the stack structure, because the algorithm will tunnel vision on one path until it finds a solution, there will not be any states from other paths in the storage. This makes it more efficient storage-wise compared to a queue variation, which has to go through states in each path in every iteration.

### What is the Big-Oh runtime order for the search algorithm? What does the order reflect? (Maximum size of Storage? Number of board positions? Number of paths explored? Maximum path length? Something else?) What is 'n', the single primary input factor that increases the difficulty of the task?
  I would say that the Big-O runtime order for this search algoritm is O(n). This is because the while loop runs, iterating once per TraceState stored in stateStore (representing open positions), and each iteration does operations at a constant O(1) runtime. Thus, the total runtime is O(n), where n is the number of items (states) in stateStore. This runtime order reflects how efficient the algorithm is in the time it takes to compute the solution(s). In this case where the runtime is O(n), that suggests that the runtime of this algorithm scales at the same pace (linearly) as n (number of open states to explore) increases.

## OVERVIEW:

  This program takes text input files containing string representations of "circuit boards," which have a starting and end point, as well as open spaces. The program accomplishes the task of finding the best path (least possible steps) from the starting point to the end point. It is essentially a maze solver.


## INCLUDED FILES:

  * CircuitTracer.java - source file and driver class, algorithm implementation
  * CircuitBoard.java - source file, populates CircuitBoard objects using scanners on input files.
  * TraceState.java - source file, the search state for the CircuitTracer search algorithm. Represents a path in the circuit board.
  * Storage.java - source file, storage system that acts as either a stack or queue implementation, storing TraceState objects in this  project.
  * InvalidFileFormatException.java - source file, exception that gets thrown when populating the circuit board with an invalid format of  input file.
  * OccupiedPositionException.java - source file, exception that gets thrown when attempting to trace a path in the circuit board that  has already been occupied.
  * README.md - this file


## COMPILING AND RUNNING:

  From the directory containing all the source files, compile the program with the following command:
  $ javac CircuitTracer.java

  Next, run the compiled class file with the following command:
  $ java CircuitTracer <-s | -q> <-c | -g> <filename>
  where -s is stack configuration, -q is queue configuration,
  -c is for console output, and -g is for GUI output.

  Example run using a stack storage configuration with console output on file valid1.dat:
  $ java CircuitTracer -s -c valid1.dat

  Console output will give the results after the program finishes.


## PROGRAM DESIGN AND IMPORTANT CONCEPTS:

  The first thing to understand about this program is the goal: the program finds the best possible path that takes the least possible steps to get from one point to another when provided a text file containing a 2D array of items (in this case, chars). The input files must have two integers in the first line containing the metrics of the grid (rows, columns), and then the plain text representation of the grid below. It accomplishes this search via a implementation of a brute force searching algorithm, which goes through all possible solutions until it finds the one(s) that take the least number of steps. You can find this algorithm in CircuitTracer.java

  The algorithm begins by initializing a Storage object (we will call it stateStore), which is determined by user input as either a stack or queue, and stores objects of type TraceState. TraceState objects represent possible paths that exist from point 1 to point 2 in any given CircuitBoard. CircuitBoard objects are basically just the 2D array of chars that exist in the text files. After initializing the storage object, the next step of the algorithm is to initialize a list bestPaths of TraceState paths to contain the best path(s). 
  
  Now, the real part of the algorithm starts. Obviously you have to start at the starting point, which is determined in the text input files by the char '1'. This will be used to construct the first TraceState objects, as you will call the initial constructor for TraceState on any adjacent open positions (up, down, left, right) marked by 'O', and add them to the stateStore. While the stateStore is not empty, it will retrieve the next path using the Storage.retrieve() method, and check if that path is a solution. If it is a solution, and the length of that path is equal to the length of a path in our bestPaths list, then it will be added to bestPaths. Otherwise, if it is a solution and the length is less than the path in the bestPaths list, then the bestPaths list will be cleared and the current TraceState path will be added. If the current TraceState object is not a solution, then the algorithm will just generate the next valid TraceState objects using the second constructor and the current TraceState, and add them to the stateStore.

  With the main algorithm explained, the CircuitBoard class is also pretty important as well. It is the driver class, and is responsible for populating CircuitBoard objects (contains 2D arrays of type char) with characters from the previously mentioned input files. See the following for a sample valid input file:
  `valid.dat
  3 3
  1 O O 
  O X O 
  O 2 O 
  `
  The character '1' represents the start position, '2' is the target/end position, and X represents a barrier/unexplorable space. Most of the CircuitBoard methods are very self explanatory and are mainly used as helper methods to be used in the search algorithm, so I won't bother to go through them. Instead, it is more important to understand the command-line arguments. Argument one determines which storage type will be used; a stack or a queue. This is important because the type of storage will directly impact memory use, and changes the order in which the TraceState paths are explored. Argument two is less important, and just determines whether the output type is a console or GUI. Argument three is the input file name.


## DISCUSSION:

  Honestly there were a lot of issues that I encountered in this project, but most of them were actually just trying to understand the algorithm. I spent some time mapping out the search algorithm when using a stack vs using a queue, and in hindsight it is pretty simple, but it took me awhile to wrap my head around it for some reason. During programming, the main issue that I encountered was correctly handling exceptions. It took a majority of my time. I spent hours bashing my head against the wall trying to figure out why every invalid file test was failing, just to find out that I was not even handling InvalidFileFormatException's when the file was invalid. Embarrassing, I know. Of course, this was a simple fix, I just used the CircuitBoard constructor in a try catch block in CircuitTracer.java, and caught any InvalidFileFormatException.

  The main thing that I found challenging in this project was trying to understand how the algorithm worked, and the initial analysis at the top of this file. Honestly, I need to study runtime order and Big-O notation, it is super difficult for me to understand for some reason. I just don't feel confident in analyzing algorithms right now. It helped a lot to map out the algorithm though.
 
 
## EXTRA CREDIT:

  N/A


----------------------------------------------------------------------------