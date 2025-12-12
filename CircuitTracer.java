import java.awt.Point;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.Stack;
import java.util.List;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail
 */
public class CircuitTracer {
	/** Launch the program. 
	 * 
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		new CircuitTracer(args); //create this with args
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private void printUsage() {
		System.out.println("Usage: $ java CircuitTracer <-s | -q> <-c | -g> <filename>");
		System.out.println("where -s is stack configuration, -q is queue configuration");
		System.out.println("-c is console output, -g is gui output");
	}

	private void checkFNFE(File checkFile) throws FileNotFoundException {
		if (!checkFile.exists())
			throw new FileNotFoundException("Invalid file. File does not exist.");
	}
	
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	public CircuitTracer(String[] args) {
		if (args.length != 3) {
			printUsage();
			return; //exit the constructor immediately
		}

		// Check that argument 1 is either -s or -q
		if (!(args[0].equals("-s") || args[0].equals("-q"))) {
			printUsage();
			return;
		}
		// Check that second argument is either -c or -g, and handle gui mode
		if (!(args[1].equals("-c") || args[1].equals("-g"))) {
			printUsage();
			return;
		} else if (args[1].equals("-g")) {
			System.out.println("GUI mode currently unsupported.");
		}

		// Check that we have a valid file for our third argument
		String fileName = args[2];
		File circuitFile = new File(fileName);
		try {
			checkFNFE(circuitFile);
		} catch(FileNotFoundException FNFE) {
			System.err.println(FNFE);
		}

		Storage<TraceState> stateStore;
		if (args[0].equals("-s")) {
			stateStore = Storage.getStackInstance();
		} else {
			stateStore = Storage.getQueueInstance();
		}

		CircuitBoard cBoard = null;
		try {
			cBoard = new CircuitBoard(fileName);
		} catch(FileNotFoundException FNFE) {
			System.err.println(FNFE);
			return;
		} catch(InvalidFileFormatException exception) {
			System.err.println(exception);
			return;
		}

		List<TraceState> bestPaths = new ArrayList<>();
		Point sPoint = cBoard.getStartingPoint();
		List<Point> neighbors = getOpenNeighbors(cBoard, sPoint.x, sPoint.y);
		
		// for each open position adjacent to starting component
		for (Point neighbor : neighbors) {
			// add a new tracestate object (path) to statestore with open position as starting point
			stateStore.store(new TraceState(cBoard, neighbor.x, neighbor.y));
		}

		while (!stateStore.isEmpty()) {
			TraceState currState = stateStore.retrieve();

			if (currState.isSolution()) {
				int bestPathLength;
				int currentPathLength = currState.pathLength();
				if (bestPaths.isEmpty()) {
					bestPathLength = currentPathLength;
				} else {
					bestPathLength = bestPaths.getFirst().pathLength();
				}
				if (currentPathLength == bestPathLength) {
					// add to bestPaths
					bestPaths.add(currState);
				} else if (currentPathLength < bestPathLength) {
					// clear bestPaths & add current tracestate as new shortest path
					bestPaths.clear();
					bestPaths.add(currState);
				}
			} else {
				// generate all valid next tracestate objects from the current tracestate and add to statestore
				for (Point neighbor : getOpenNeighbors(currState.getBoard(), currState.getRow(), currState.getCol())) {
					stateStore.store(new TraceState(currState, neighbor.x, neighbor.y));
				}
			}
		}

		//TODO: output results to console or GUI, according to specified choice
		if (args[1].equals("-c")) {
			for (TraceState bestState : bestPaths) {
				System.out.println(bestState.toString());
			}
		}
	}
	
	/**
	 * Helper to get open adjacent points from a given target element in the circuit board more conveniently.
	 * @param cBoard a CircuitBoard object to be used
	 * @param targetRow the target element's row
	 * @param targetCol the target element's column
	 * @return a list of points representing open adjacent positions
	 */
	public static List<Point> getOpenNeighbors(CircuitBoard cBoard, int targetRow, int targetCol) {
		List<Point> neighbors = new ArrayList<>();
		final Point[] directions = {
			new Point(0, -1), // Left
			new Point(0, 1), // Right
			new Point(-1, 0), // Up
			new Point(1, 0) // Down
		};

		for (Point offset : directions) {
			int nRow, nCol;
			nRow = targetRow + offset.x;
			nCol = targetCol + offset.y;
			if (cBoard.isOpen(nRow, nCol)) {
				Point neighbor = new Point(nRow, nCol);
				neighbors.add(neighbor);
			}
		}

		return neighbors;
	}
} // class CircuitTracer