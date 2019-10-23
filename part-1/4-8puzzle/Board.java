import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {
    private final int n;
    private final int[][] tiles;
    private final int[][] goal;
    private final ArrayList<Board> neighbors;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = new int[n][n];
        this.goal = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
                this.goal[i][j] = i*n + j + 1;
            }
        }

        this.goal[n-1][n-1] = 0;

        this.neighbors = new ArrayList<>();
    }

    // swaps two values in int[][]
    private void swap(int x0, int y0, int x1, int y1, int[][] m) {
        int temp = m[x0][y0];
        m[x0][y0] = m[x1][y1];
        m[x1][y1] = temp;
    }

    // string representation of this board
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        boardString.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boardString.append(" ").append(tiles[i][j]);
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int distance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n-1 && j == n-1) {
                    continue;
                }
                if (goal[i][j] != tiles[i][j]) {
                    distance++;
                }
            }
        }
        return distance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                if (tiles[i][j] != goal[i][j]) {
                    int[] goalCoords = this.findCoords(tiles[i][j], goal);
                    if (goalCoords != null) {
                        distance += Math.abs(i - goalCoords[0]) + Math.abs(j - goalCoords[1]);
                    }
                }
            }
        }
        return distance;
    }

    // coordinates in source for the given value
    private int[] findCoords(int value, int[][] source) {
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (source[x][y] == value) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board board = (Board) y;
        if (n != board.dimension()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != board.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        if (neighbors.isEmpty()) {
            int[] emptyTileCoords = findCoords(0, tiles);
            if (emptyTileCoords != null) {
                if (emptyTileCoords[0] > 0) {
                    Board board = new Board(tiles);
                    swap(emptyTileCoords[0] - 1, emptyTileCoords[1], emptyTileCoords[0], emptyTileCoords[1], board.tiles);
                    neighbors.add(board);
                }
                if (emptyTileCoords[0] < n - 1) {
                    Board board = new Board(tiles);
                    swap(emptyTileCoords[0], emptyTileCoords[1], emptyTileCoords[0] + 1, emptyTileCoords[1], board.tiles);
                    neighbors.add(board);
                }
                if (emptyTileCoords[1] > 0) {
                    Board board = new Board(tiles);
                    swap(emptyTileCoords[0], emptyTileCoords[1] - 1, emptyTileCoords[0], emptyTileCoords[1], board.tiles);
                    neighbors.add(board);
                }
                if (emptyTileCoords[1] < n - 1) {
                    Board board = new Board(tiles);
                    swap(emptyTileCoords[0], emptyTileCoords[1], emptyTileCoords[0], emptyTileCoords[1] + 1, board.tiles);
                    neighbors.add(board);
                }
            }
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinTiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twinTiles[i][j] = tiles[i][j];
            }
        }
        if (twinTiles[0][0] != 0 && twinTiles[0][1] != 0) {
            this.swap(0, 0, 0, 1, twinTiles);
        } else if (twinTiles[0][0] == 0) {
            this.swap(0, 1, 1, 0, twinTiles);
        } else {
            this.swap(0, 0, 1, 0, twinTiles);
        }
        return new Board(twinTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {

        int n = 3;

        int[][] tiles = new int[n][n];
        tiles[0][0] = 8;
        tiles[0][1] = 1;
        tiles[0][2] = 3;
        tiles[1][0] = 4;
        tiles[1][1] = 0;
        tiles[1][2] = 2;
        tiles[2][0] = 7;
        tiles[2][1] = 6;
        tiles[2][2] = 5;

        int[][] tiles2 = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles2[i][j] = i*n + j;
            }
        }

        Board board = new Board(tiles);
        StdOut.println(board.toString());

        Board board2 = new Board(tiles2);
        StdOut.println(board2.toString());

        StdOut.println("board equals to board2: " + board.equals(board2));

        StdOut.println("board hamming distance: " + board.hamming());

        StdOut.println("board manhattan distance: " + board.manhattan());

        StdOut.println("is board goal: " + board.isGoal());

        for (Board b : board.neighbors()) {
            StdOut.println(b);
        }

        for (Board b : board2.neighbors()) {
            StdOut.println(b);
        }

        StdOut.println("twin board:\n" + board.twin());
    }

}