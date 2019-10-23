import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BruteCollinearPoints {
    private final LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("argument is null");
        }

        final int n = points.length;
        for (int i = 0; i < n; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("argument is null");
            }
        }

        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        for (int i = 0; i < n - 1; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i + 1]) == 0) {
                throw new IllegalArgumentException("repeated points");
            }
        }

        List<LineSegment> linkedList = new LinkedList<>();
        for (int p = 0; p < n - 3; p++) {
            for (int q = p + 1; q < n - 2; q++) {
                for (int r = q + 1; r < n - 1; r++) {
                    if (sortedPoints[p].slopeTo(sortedPoints[q]) == sortedPoints[p].slopeTo(sortedPoints[r])) {
                        for (int s = r + 1; s < n; s++) {
                            if (sortedPoints[p].slopeTo(sortedPoints[q]) == sortedPoints[p].slopeTo(sortedPoints[s])) {
                                linkedList.add(new LineSegment(sortedPoints[p], sortedPoints[s]));
                            }
                        }
                    }
                }
            }
        }

        Object[] array = linkedList.toArray();
        this.segments = Arrays.copyOf(array, array.length, LineSegment[].class);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.clone();
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
