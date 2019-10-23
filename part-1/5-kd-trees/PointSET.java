import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<Point2D>();
    }

    private void validateInput(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        this.validateInput(p, "Point p cannot be null");
        if (!points.contains(p)) {
            points.add(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        this.validateInput(p, "Point p cannot be null");
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point: points) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        this.validateInput(rect, "Rectangle rect cannot be null");
        Stack<Point2D> result = new Stack<Point2D>();
        for (Point2D currentPoint: points) {
            if (rect.contains(currentPoint)) {
                result.push(currentPoint);
            }
        }
        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        this.validateInput(p, "Point p cannot be null");
        Point2D nearestPoint = null;
        double nearestPointDistance = Double.POSITIVE_INFINITY;
        for (Point2D currentPoint: points) {
            double currentPointDistance = p.distanceSquaredTo(currentPoint);
            if (currentPointDistance < nearestPointDistance) {
                nearestPoint = currentPoint;
                nearestPointDistance = currentPointDistance;
            }
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // optional
    }
}