import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first = null;
    private Node last = null;
    private int size = 0;

    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no more items");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Operation not supported");
        }
    }

    // construct an empty deque
    public Deque() {
        // Do nothing
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.previous = null;
        if (oldFirst != null) {
            oldFirst.previous = first;
        }
        else {
            last = first;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        Node newLast = new Node();
        newLast.item = item;
        newLast.next = null;
        newLast.previous = last;
        if (last != null) {
            last.next = newLast;
        }
        else {
            first = newLast;
        }
        last = newLast;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        Node oldFirst = first;
        Item item = oldFirst.item;
        first = oldFirst.next;
        if (first != null) {
            first.previous = null;
        }
        else {
            last = null;
        }
        oldFirst.next = null;
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        Node newLast = last.previous;
        Item item = last.item;
        last.previous = null;
        last = newLast;
        if (last != null) {
            last.next = null;
        }
        else {
            first = null;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();

        deque.addFirst(4);
        deque.addFirst(3);
        deque.addFirst(2);
        deque.addFirst(1);

        Iterator<Integer> dequeIterator = deque.iterator();
        if (dequeIterator.hasNext()) {
            StdOut.println(dequeIterator.next());
        }

        StdOut.println("Removed from back: " + deque.removeLast());
        StdOut.println("Deque size: " + deque.size());

        deque.addLast(5);
        deque.addLast(6);
        deque.addLast(7);
        deque.addLast(8);

        for (int item : deque) {
            StdOut.println("Item: " + item);
        }

        StdOut.println("Removed from front: " + deque.removeFirst());
        StdOut.println("Deque size: " + deque.size());

        for (int item : deque) {
            StdOut.println("Item: " + item);
        }

        while (!deque.isEmpty()) {
            StdOut.println("Removed from back: " + deque.removeLast());
        }
    }
}