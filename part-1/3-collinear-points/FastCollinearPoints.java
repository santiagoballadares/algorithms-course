import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {
    private final LineSegment[] segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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
        for (int i = 0; i < n; i++) {
            Point p = sortedPoints[i];
            Point[] pointsSortedBySlope = sortedPoints.clone();
            Arrays.sort(pointsSortedBySlope, p.slopeOrder());

            int j = 0;
            while (j < n) {
                if (p.compareTo(pointsSortedBySlope[j]) == 0) {
                    j++;
                    continue;
                }
                LinkedList<Point> candidates = new LinkedList<>();
                candidates.add(pointsSortedBySlope[j]);
                while (j < n && p.slopeTo(pointsSortedBySlope[j]) == p.slopeTo(candidates.peekFirst())) {
                    if (p.compareTo(pointsSortedBySlope[j]) == 0) {
                        j++;
                        continue;
                    }
                    candidates.add(pointsSortedBySlope[j++]);
                }
                if (candidates.size() >= 3 && p.compareTo(candidates.peekFirst()) < 0) {
                    linkedList.add(new LineSegment(p, candidates.removeLast()));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
