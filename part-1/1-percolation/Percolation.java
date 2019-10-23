import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private boolean [][] grid;
    private final WeightedQuickUnionUF wquUF;
    private final int topVirtualSiteIndex;
    private int openSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0, " + n + " is not valid");
        }
        this.n = n;
        // create grid
        grid = new boolean[n][n];
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                grid[row][col] = false;
            }
        }
        // create wquUF with additional index for the top virtual site (index = n * n)
        topVirtualSiteIndex = n * n;
        wquUF = new WeightedQuickUnionUF(n * n + 1);
        // connect top virtual site to top row
        for (int col = 0; col < n; col++) {
            wquUF.union(topVirtualSiteIndex, col);
        }
        // keep track of open sites
        openSites = 0;
    }

    // validates input: row and col
    private void validate(int row, int col) {
        if (row < 1 || row > n) {
            throw new IllegalArgumentException("row must be greater than 0 and less or equal than " + n);
        }
        if (col < 1 || col > n) {
            throw new IllegalArgumentException("col must be greater than 0 and less or equal than " + n);
        }
    }

    // calculates site index from row and col
    private int calculateSiteIndex(int row, int col) {
        return (row - 1) * n + col - 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) {
            return;
        }
        grid[row-1][col-1] = true;
        if (row > 1 && isOpen(row-1, col)) {
            wquUF.union(calculateSiteIndex(row, col), calculateSiteIndex(row-1, col));
        }
        if (row < n && isOpen(row+1, col)) {
            wquUF.union(calculateSiteIndex(row, col), calculateSiteIndex(row+1, col));
        }
        if (col > 1 && isOpen(row, col-1)) {
            wquUF.union(calculateSiteIndex(row, col), calculateSiteIndex(row, col-1));
        }
        if (col < n && isOpen(row, col+1)) {
            wquUF.union(calculateSiteIndex(row, col), calculateSiteIndex(row, col+1));
        }
        openSites++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return isOpen(row, col) && wquUF.connected(calculateSiteIndex(row, col), topVirtualSiteIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        for (int col = 1; col <= n; col++) {
            if (isFull(n, col)) {
                return true;
            }
        }
        return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(5);
        percolation.open(4, 2);
        percolation.open(3, 2);
        percolation.open(3, 3);
        percolation.open(1, 2);
        percolation.open(5, 2);
        percolation.open(1, 3);
        percolation.open(2, 3);
        StdOut.printf("Open sites = %d\n", percolation.numberOfOpenSites());
        StdOut.printf("Does the system percolate? %b\n", percolation.percolates());
    }
}
