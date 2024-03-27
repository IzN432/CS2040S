public class BinaryTreeNode<T> {
    /**
     * The left child of the BinaryTreeNode
     */
    public BinaryTreeNode<T> left;
    /**
     * The right child of the BinaryTreeNode
     */
    public BinaryTreeNode<T> right;
    /**
     * The value of the BinaryTreeNode
     */
    public T value;

    /**
     * @return a string representation of the value
     */
    public String toString() {
        return "" + value;
    }

    /**
     * Constructor for BinaryTreeNode
     * @param t value at the node
     */
    public BinaryTreeNode(T t) {
        value = t;
    }

    /**
     * Returns an inOrderTraversal array of the subtree
     * with this BinaryTreeNode as the root
     * @return an array inOrderTraversal
     */
    public T[] inOrderTraversal() {
        // this warning can be suppressed because
        // I am only populating this array with T
        @SuppressWarnings("unchecked")
        T[] res = (T[]) new Object[getCount()];
        inOrderTraversalHelper(0, this, res);
        return res;
    }
    private static <T> int inOrderTraversalHelper(int index, BinaryTreeNode<T> n, T[] arr) {
        if (n == null) return index;
        index = inOrderTraversalHelper(index, n.left, arr);
        arr[index++] = n.value;
        return inOrderTraversalHelper(index, n.right, arr);
    }

    /**
     * @return the inOrder array of depths of the subtree with this BinaryTreeNode as the root
     */
    private int[] depthArray() {
        int[] depthArray = new int[getCount()];
        depthArrayHelper(0, this, 0, depthArray);
        return depthArray;
    }
    private static <T> int depthArrayHelper(int index, BinaryTreeNode<T> n, int depth, int[] depthArr) {
        if (n == null) return index;
        index = depthArrayHelper(index, n.left, depth + 1, depthArr);
        depthArr[index++] = depth;
        return depthArrayHelper(index, n.right, depth + 1, depthArr);
    }

    /**
     * @return the number of nodes in the subtree rooted at this BinaryTreeNode in O(n) time.
     */
    public int getCount() {
        return 1 + (left != null ? left.getCount() : 0) + (right != null ? right.getCount() : 0);
    }
    /**
     * @return the height of the subtree rooted at this BinaryTreeNode in O(n) time
     */
    public int getHeight() {
        int leftHeight = left == null ? -1 : left.getHeight();
        int rightHeight = right == null ? -1 : right.getHeight();
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public void printTree() {
        T[] inOrder = inOrderTraversal();
        int[] depthArray = depthArray();

        String[] strArr = new String[inOrder.length];
        for (int i = 0; i < inOrder.length; i++) {
            strArr[i] = inOrder[i].toString();
        }

        int[] prefixSumOfLengths = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            if (i == 0) {
                prefixSumOfLengths[i] = 0;
            } else {
                prefixSumOfLengths[i] = prefixSumOfLengths[i - 1] + strArr[i - 1].length();
            }
        }

        StringBuilder[] builders = new StringBuilder[getHeight() + 1];
        for (int i = 0; i < builders.length; i++) {
            builders[i] = new StringBuilder();
        }

        for (int i = 0; i < strArr.length; i++) {
            int depth = depthArray[i];
            StringBuilder b = builders[depth];
            b.append(" ".repeat((prefixSumOfLengths[i] - b.length()))).append(strArr[i]);
        }

        StringBuilder res = new StringBuilder();
        for (StringBuilder b : builders) {
            res.append(b);
            res.append("\n");
        }
        System.out.println(res.substring(0, res.length() - 1));
    }

    public static void main(String[] args) {
        BinaryTreeNode<Object> n = new BinaryTreeNode<>("100");
        n.left = new BinaryTreeNode<>("50");
        n.left.right = new BinaryTreeNode<>("69");
        n.right = new BinaryTreeNode<>("370");
        n.left.left = new BinaryTreeNode<>("25");
        n.right.left = new BinaryTreeNode<>("201");
        n.left.left.left = new BinaryTreeNode<>("1000");
        n.left.left.left.left = new BinaryTreeNode<>("3");
        n.printTree();
    }
}