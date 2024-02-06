import java.util.Arrays;
import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class QueueSort {
    public static void main(String[] args) {
        Random random = new Random(1);
        int[] arr = new int[1024];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(1024);
        }
        Queue queue = new Queue(arr);

        // find smallest element
        int sortedCount = 0;
        while (sortedCount < queue.getLength()) {
            int minimum = Integer.MAX_VALUE;
            int minimumIndex = 0;

            // go through the unsorted part to find minimum
            for (int i = 0; i < queue.getLength() - sortedCount; i++) {
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

            for (int i = 0; i < queue.getLength() - minimumIndex; i++) {
                queue.queue(queue.dequeue());
            }

            sortedCount++;
        }

        System.out.println(queue);
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
        if (length == 0) {
            end = null;
            start = null;
        }
        return n.getValue();
    }

    public int getLength() {
        return length;
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