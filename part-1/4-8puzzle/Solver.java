import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final boolean solvable;
    private SearchNode goalNode;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int manhattan;
        private final int moves;
        private final SearchNode previous;

        public SearchNode(Board board, int priority, int moves, SearchNode previous) {
            this.board = board;
            this.manhattan = priority;
            this.moves = moves;
            this.previous = previous;
        }

        public int compareTo(SearchNode other) {
            int thisPriority = manhattan + moves;
            int otherPriority = other.manhattan + other.moves;
            if (thisPriority > otherPriority) {
                return 1;
            }
            if (thisPriority < otherPriority) {
                return -1;
            }
            return 0;
        }

        public String toString() {
            return board.toString();
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        MinPQ<SearchNode> initialPQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();

        Board twinBoard = initial.twin();

        initialPQ.insert(new SearchNode(initial, initial.manhattan(), 0, null));
        twinPQ.insert(new SearchNode(twinBoard, twinBoard.manhattan(), 0, null));

        SearchNode currentNode = initialPQ.delMin();
        SearchNode twinNode = twinPQ.delMin();

        while (!currentNode.board.isGoal() && !twinNode.board.isGoal()) {
            Board previous;
            Board twinPrevious;

            if (currentNode.previous == null) {
                for (Board currentNeightbor : currentNode.board.neighbors()) {
                    initialPQ.insert(
                        new SearchNode(
                            currentNeightbor,
                            currentNeightbor.manhattan(),
                            currentNode.moves + 1,
                            currentNode
                        )
                    );
                }
                for (Board currentNeightbor : twinNode.board.neighbors()) {
                    twinPQ.insert(
                        new SearchNode(
                            currentNeightbor,
                            currentNeightbor.manhattan(),
                            twinNode.moves + 1,
                            twinNode
                        )
                    );
                }
            } else {
                previous = currentNode.previous.board;
                twinPrevious = twinNode.previous.board;
                for (Board currentNeightbor : currentNode.board.neighbors()) {
                    if (!currentNeightbor.equals(previous)) {
                        initialPQ.insert(
                            new SearchNode(
                                currentNeightbor,
                                currentNeightbor.manhattan(),
                                currentNode.moves + 1,
                                currentNode
                            )
                        );
                    }
                }
                for (Board currentNeightbor : twinNode.board.neighbors()) {
                    if (!currentNeightbor.equals(twinPrevious)) {
                        twinPQ.insert(
                            new SearchNode(
                                currentNeightbor,
                                currentNeightbor.manhattan(),
                                twinNode.moves + 1,
                                twinNode
                            )
                        );
                    }
                }
            }

            currentNode = initialPQ.delMin();
            twinNode = twinPQ.delMin();
        }

        if (currentNode.board.isGoal() && !twinNode.board.isGoal()) {
            solvable = true;
            goalNode = currentNode;
        } else {
            solvable = false;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable()) {
            return goalNode.moves;
        }
        return -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (isSolvable()) {
            Stack<Board> gameStack = new Stack<Board>();
            SearchNode currentNode = goalNode;
            while (currentNode != null) {
                gameStack.push(currentNode.board);
                currentNode = currentNode.previous;
            }
            return gameStack;
        }
        return null;
    }

    // test client
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        StdOut.println(initial);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

    }

}