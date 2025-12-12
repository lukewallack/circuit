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
  s