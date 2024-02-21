import java.util.Arrays;
import java.util.List;

public class MedianFinder {

    private static class Node {
        private int weight;
        private Node left;
        private Node right;
        private int height;
        private int value;

        public Node(int x) {
            value = x;
        }
    }

    private Node root;

    private int size;

    public MedianFinder() {

    }
    public void insert(int x) {
        if (root == null) {
            root = new Node(x);
            size = 1;
        } else {
            root = insertHelper(x, root);
            size += 1;
        }
    }
    private Node insertHelper(int x, Node n) {
        if (n == null) {
            n = new Node(x);
            n.height = 0;
            n.weight = 1;
            return n;
        }
        if (x < n.value) {
            // go left
            n.left = insertHelper(x, n.left);
        } else {
            // go right
            n.right = insertHelper(x, n.right);
        }
        return balance(n);
    }
    private enum Direction {LEFT, RIGHT};
    private int getBalanceFactor(Node n) {
        if (n == null) return 0;
        return getHeight(n.right) - getHeight(n.left);
    }
    private Node balance(Node n) {
        computeStatistics(n);

        int factor = getBalanceFactor(n);
        if (Math.abs(factor) < 2) {
            return n;
        }

        if (factor < 0) {
            // left heavy
            int childFactor = getBalanceFactor(n.left);
            if (childFactor > 0) {
                // child right heavy
                n.left = rotate(n.left, Direction.LEFT);
            }
            return rotate(n, Direction.RIGHT);
        } else {
            // right heavy
            int childFactor = getBalanceFactor(n.right);
            if (childFactor < 0) {
                // child left heavy
                n.right = rotate(n.right, Direction.RIGHT);
            }
            return rotate(n, Direction.LEFT);
        }
    }
    private int getHeight(Node n) {
        return n == null ? -1 : n.height;
    }
    private int getWeight(Node n) {
        return n == null ? 0 : n.weight;
    }
    private void computeStatistics(Node n) {
        n.height = Math.max(getHeight(n.left), getHeight(n.right)) + 1;
        n.weight = getWeight(n.left) + getWeight(n.right) + 1;
    }
    private Node rotate(Node n, Direction d) {
        Node temp;
        if (d == Direction.LEFT) {
            temp = n.right;
            n.right = temp.left;
            temp.left = n;
        } else {
            temp = n.left;
            n.left = temp.right;
            temp.right = n;
        }
        computeStatistics(n);
        computeStatistics(temp);
        return temp;
    }
    public int getMedian() {
        int[] res = {0};
        int medianPos = (size + 2) / 2;
        if (root.left == null && root.right == null) {
            res[0] = root.value;
            root = null;
            return res[0];
        }
        root = getMedianHelper(root, medianPos, res);
        size--;
        return res[0];
    }
    private Node getMedianHelper(Node n, int rank, int[] res) {
        if (rank == getWeight(n.left) + 1) {
            // check if I have two children
            if (n.left != null && n.right != null) {
                // swap with my predecessor
                Node predecessor = n.left;
                while (predecessor.right != null) {
                    predecessor = predecessor.right;
                }
                int temp = predecessor.value;
                predecessor.value = n.value;
                n.value = temp;

                // recurse on my left, aiming to delete the predecessor now
                n.left = getMedianHelper(n.left, rank - 1, res);
            } else if (n.left != null) {
                // just give my left boy the spotlight
                res[0] = n.value;
                return n.left;
            } else if (n.right != null) {
                // give my right boi the spotlight
                res[0] = n.value;
                return n.right;
            } else {
                res[0] = n.value;
                return null;
            }
        } else if (rank <= getWeight(n.left)) {
            n.left = getMedianHelper(n.left, rank, res);
        } else if (rank > getWeight(n.left) + 1) {
            n.right = getMedianHelper(n.right,
                    rank - getWeight(n.left) - 1, res);
        }
        return balance(n);
    }

    private void printTree() {
        int[] arr = new int[size];
        inOrder(arr, root, 0);
        System.out.println(Arrays.toString(arr));
    }

    private int inOrder(int[] vals, Node n, int index) {
        if (n == null) return index;
        index = inOrder(vals, n.left, index);
        vals[index++] = n.value;
        return inOrder(vals, n.right, index);
    }

    private void inOrder(Node n) {
        if (n == null) return;
        inOrder(n.left);
        System.out.print(n.value);
        inOrder(n.right);
    }

    public static void main(String[] args) {
        MedianFinder mf = new MedianFinder();
        mf.insert(1);
        mf.insert(2);
        mf.insert(3);
        mf.insert(4);

        System.out.println(mf.getMedian());
        mf.printTree();
        System.out.println(mf.getMedian());
        mf.printTree();
        System.out.println(mf.getMedian());
        mf.printTree();
        System.out.println(mf.getMedian());
        mf.printTree();

    }
}
