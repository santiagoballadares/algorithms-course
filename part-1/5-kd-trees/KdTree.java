import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private enum Partition { VERTICAL, HORIZONTAL }
    private Node root;
    private int size;

    private static class Node {
        private final Point2D point;
        private final Partition partition;
        private final RectHV rect;
        private Node leftOrBottom;
        private Node rightOrTop;

        Node(Point2D point, Partition partition, RectHV rect) {
            this.point = point;
            this.partition = partition;
            this.rect = rect;
        }

        public Partition nextPartition() {
            return partition == Partition.VERTICAL ?
                Partition.HORIZONTAL :
                Partition.VERTICAL;
        }

        public RectHV leftOrBottomRect() {
            return partition == Partition.VERTICAL ?
                new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax()) :
                new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
        }

        public RectHV rightOrTopRect() {
            return partition == Partition.VERTICAL ?
                new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax()) :
                new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
        }

        public boolean isRightOrTopOf(Point2D p) {
            return (partition == Partition.VERTICAL && point.x() > p.x()) ||
                (partition == Partition.HORIZONTAL && point.y() > p.y());
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    private void validateInput(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        this.validateInput(p, "Point p cannot be null");
        if (root == null) {
            root = new Node(p, Partition.VERTICAL, new RectHV(0, 0, 1, 1));
            size = 1;
        } else {
            Node lastNode;
            Node currentNode = root;
            do {
                if (currentNode.point.equals(p)) {
                    return;
                }
                lastNode = currentNode;
                if (currentNode.isRightOrTopOf(p)) {
                    currentNode = currentNode.leftOrBottom;
                } else {
                    currentNode = currentNode.rightOrTop;
                }
            } while (currentNode != null);

            if (lastNode.isRightOrTopOf(p)) {
                lastNode.leftOrBottom = new Node(p, lastNode.nextPartition(), lastNode.leftOrBottomRect());
            } else {
                lastNode.rightOrTop = new Node(p, lastNode.nextPartition(), lastNode.rightOrTopRect());
            }
            size++;
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        this.validateInput(p, "Point p cannot be null");
        Node currentNode = root;
        while (currentNode != null) {
            if (currentNode.point.equals(p)) {
                return true;
            }
            if (currentNode.isRightOrTopOf(p)) {
                currentNode = currentNode.leftOrBottom;
            } else {
                currentNode = currentNode.rightOrTop;
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        this.drawNode(root, null);
    }

    private void drawNode(Node node, Node lastNode) {
        if (node == null) {
            return;
        }
        this.drawNode(node.leftOrBottom, node);

        StdDraw.setPenColor(StdDraw.BLACK);
        node.point.draw();

        if (node.partition == Partition.VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            double y0 = lastNode == null ?
                node.rect.ymin() :
                node.isRightOrTopOf(lastNode.point) ? lastNode.point.y() : lastNode.rect.ymax();
            double y1 = lastNode == null ?
                node.rect.ymax() :
                node.isRightOrTopOf(lastNode.point) ? lastNode.rect.ymin() : lastNode.point.y();
            StdDraw.line(node.point.x(), y0, node.point.x(), y1);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            double x0 = node.isRightOrTopOf(lastNode.point) ? lastNode.point.x() : lastNode.rect.xmin();
            double x1 = node.isRightOrTopOf(lastNode.point) ? lastNode.rect.xmax() : lastNode.point.x();
            StdDraw.line(x0, node.point.y(), x1, node.point.y());
        }

        this.drawNode(node.rightOrTop, node);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        this.validateInput(rect, "Rectangle rect cannot be null");
        Stack<Point2D> result = new Stack<Point2D>();
        this.addPoints(root, rect, result);
        return result;
    }

    private void addPoints(Node node, RectHV rect, Stack<Point2D> result) {
        if (node == null) {
            return;
        }
        if (rect.contains(node.point)) {
            result.push(node.point);
            this.addPoints(node.leftOrBottom, rect, result);
            this.addPoints(node.rightOrTop, rect, result);
        } else {
            if (node.isRightOrTopOf(new Point2D(rect.xmin(), rect.ymin()))) {
                this.addPoints(node.leftOrBottom, rect, result);
            }
            if (!node.isRightOrTopOf(new Point2D(rect.xmax(), rect.ymax()))) {
                this.addPoints(node.rightOrTop, rect, result);
            }
        }
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        this.validateInput(p, "Point p cannot be null");
        if (this.isEmpty()) {
            return  null;
        }
        return this.nearestPoint(p, root.point, root);
    }

    private Point2D nearestPoint(Point2D targetPoint, Point2D nearestPoint, Node currentNode) {
        if (currentNode == null) {
            return nearestPoint;
        }
        double nearestPointDistance = targetPoint.distanceSquaredTo(nearestPoint);
        if (currentNode.rect.distanceSquaredTo(targetPoint) < nearestPointDistance) {
            double currentPointDistance = targetPoint.distanceSquaredTo(currentNode.point);
            if (currentPointDistance < nearestPointDistance) {
                nearestPoint = currentNode.point;
            }
            if (currentNode.isRightOrTopOf(targetPoint)) {
                nearestPoint = this.nearestPoint(targetPoint, nearestPoint, currentNode.leftOrBottom);
                nearestPoint = this.nearestPoint(targetPoint, nearestPoint, currentNode.rightOrTop);
            } else {
                nearestPoint = this.nearestPoint(targetPoint, nearestPoint, currentNode.rightOrTop);
                nearestPoint = this.nearestPoint(targetPoint, nearestPoint, currentNode.leftOrBottom);
            }
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // optional
    }
}