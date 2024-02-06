import java.util.Arrays;
import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class QueueSort {
    static Random rng = new Random(1);
    public static void main(String[] args) {
       System.out.println(testSort());
    }

    private static boolean testSort() {
        for (int i = 0; i < 10000; i++) {
            int[] arr = new int[100];
            for (int j = 0; j < arr.length; j++) {
                arr[j] = rng.nextInt(100);
            }

            Queue queue = new Queue(arr);
            queueSort(queue);
            System.out.println(queue);
            if (!isSorted(queue)) return false;
        }
        return true;
    }

    private static boolean isSorted(Queue queue) {
        int previous = Integer.MIN_VALUE;
        while (!queue.isEmpty()) {
            int value = queue.dequeue();
            if (value < previous) return false;
            previous = value;
        }
        return true;
    }
    private static void queueSort(Queue queue) {
        int sortedCount = 0;
        int length = queueGetLength(queue);
        while (sortedCount < length) {
            int minimum = Integer.MAX_VALUE;
            int minimumIndex = 0;

            // go through the unsorted part to find minimum
            for (int i = 0; i < length - sortedCount; i++) {
                int value = queue.dequeue();
                if (value < minimum) {
                    minimumIndex = i;
                    minimum = value;
                }
                queue.queue(value);
            }

            // go through the sorted part
            for (int i = 0; i < sortedCount; i++) {
                queue.queue(queue.dequeue());
            }

            // insert the minimum in
            queue.queue(minimum);

            // find the previous minimum to remove my boi
            for (int i = 0; i < minimumIndex; i++) {
                queue.queue(queue.dequeue());
            }

            // remove my boi
            queue.dequeue();

            for (int i = 0; i < length - minimumIndex; i++) {
                queue.queue(queue.dequeue());
            }

            sortedCount++;
        }
    }

    private static int queueGetLength(Queue queue) {
        Queue helper = new Queue();
        int count = 0;
        while (!queue.isEmpty()) {
            Node n = queue.removeNode();
            helper.addNode(n);
            count++;
        }
        while (!helper.isEmpty()) {
            Node n = helper.removeNode();
            queue.addNode(n);
        }
        return count;
    }
}

class Queue {
    private Node start;
    private Node end;
    private int length;

    public Queue() {

    }

    public Queue(int[] arr) {
        this();
        for (int j : arr) {
            this.queue(j);
        }
    }

    public void queue(int value) {
        Node n = new Node(value);

        if (start == null) {
            start = n;
            end = n;
        }
        n.setNext(start);
        start.setPrevious(n);
        start = n;
        length++;
    }

    public int dequeue() {
        Node n = end;
        end = n.getPrevious();
        length--;
        if (end == null) start = null;
        return n.getValue();
    }

    public Node removeNode() {
        Node n = end;
        end = n.getPrevious();
        length--;
        if (end == null) start = null;
        n.setPrevious(null);
        return n;
    }

    public void addNode(Node n) {
        if (start == null) {
            start = n;
            end = n;
        }
        n.setNext(start);
        start.setPrevious(n);
        start = n;
        length++;
    }
    public boolean isEmpty() {
        return start == null;
    }

    public String toString() {
        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            arr[i] = this.dequeue();
            this.queue(arr[i]);
        }
        return Arrays.toString(arr);
    }
}

class Node {
    private Node previous;
    private Node next;
    private final int value;
    public Node(int value) {
        this.value = value;
    }

    public void setNext(Node n) {
        next = n;
    }

    public void setPrevious(Node n) {
        previous = n;
    }

    public Node getNext() {
        return next;
    }

    public Node getPrevious() {
        return previous;
    }

    public int getValue() {
        return value;
    }
}