import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int trials;
    private final double [] percolationThresholds;
    private double mean = Double.NaN;
    private double stddev = Double.NaN;
    private double confidenceLo = Double.NaN;
    private double confidenceHi = Double.NaN;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0, " + n + " is not valid");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("trials must be greater than 0, " + trials + " is not valid");
        }
        this.trials = trials;
        percolationThresholds = new double[trials];
        for (int trial = 0; trial < trials; trial++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }
            percolationThresholds[trial] = (1.0 * percolation.numberOfOpenSites()) / ((1.0 * n) * (1.0 * n));
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        if (Double.isNaN(mean)) {
            mean = StdStats.mean(percolationThresholds);
        }
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (Double.isNaN(stddev)) {
            stddev = StdStats.stddev(percolationThresholds);
        }
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        if (Double.isNaN(confidenceLo)) {
            confidenceLo = mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials));
        }
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        if (Double.isNaN(confidenceHi)) {
            confidenceHi = mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials));
        }
        return confidenceHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, t);
        StdOut.printf("mean = %f\n", percolationStats.mean());
        StdOut.printf("stddev = %f\n", percolationStats.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", percolationStats.confidenceLo(), percolationStats.confidenceHi());
    }
}
