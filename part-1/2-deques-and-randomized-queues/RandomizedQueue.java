import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int n = 0;

    private class RandomArrayIterator implements Iterator<Item> {
        private final Item[] iteratorItems;
        private int i = n;

        public RandomArrayIterator() {
            iteratorItems = Arrays.copyOf(items, items.length);
            StdRandom.shuffle(iteratorItems, 0, n);
        }

        public boolean hasNext() {
            return i > 0;
        }

        public Item next() {
            if (i <= 0) {
                throw new NoSuchElementException("There are no more items");
            }
            return iteratorItems[--i];
        }

        public void remove() {
            throw new UnsupportedOperationException("Operation not supported");
        }
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = items[i];
        }
        items = copy;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (n == items.length) {
            resize(2 * items.length);
        }
        items[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Randomize queue is empty");
        }
        int randomIndex = StdRandom.uniform(n);
        Item item = items[randomIndex];
        items[randomIndex] = items[--n];
        items[n] = null;
        if (n > 0 && n == items.length/4) {
            resize(items.length/2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Randomize queue is empty");
        }
        int randomIndex = StdRandom.uniform(n);
        return items[randomIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomArrayIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<Integer>();

        randomizedQueue.enqueue(1);
        randomizedQueue.enqueue(2);
        randomizedQueue.enqueue(3);
        randomizedQueue.enqueue(4);

        Iterator<Integer> dequeIterator = randomizedQueue.iterator();
        if (dequeIterator.hasNext()) {
            StdOut.println(dequeIterator.next());
        }

        StdOut.println("Size: " + randomizedQueue.size());

        for (int item : randomizedQueue) {
            StdOut.println("It 1 - Item: " + item);
        }
        for (int item : randomizedQueue) {
            StdOut.println("It 2 - Item: " + item);
        }

        for (int i = 0; i < 10; i++) {
            StdOut.println("Sample item: " + randomizedQueue.sample());
        }

        while (!randomizedQueue.isEmpty()) {
            StdOut.println("removed item: " + randomizedQueue.dequeue());

            for (int item : randomizedQueue) {
                StdOut.println("Item: " + item);
            }
        }
    }
}