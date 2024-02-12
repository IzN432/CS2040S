
import java.util.Arrays;

/**
 * ScapeGoat Tree class
 * This class contains some of the basic code for implementing a ScapeGoat tree.
 * This version does not include any of the functionality for choosing which node
 * to scapegoat.  It includes only code for inserting a node, and the code for rebuilding
 * a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    public enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     * This class holds the data for a node in a binary tree.
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;

        TreeNode(int k) {
            key = k;
        }

        public String toString() {
            return "" + key;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node, Child child) {
        TreeNode nodeOfConcern;
        if (child == Child.LEFT) {
            nodeOfConcern = node.left;
        } else {
            nodeOfConcern = node.right;
        }

        if (nodeOfConcern == null) {
            return 0;
        }

        return 1 + countNodes(nodeOfConcern, Child.LEFT) +
                countNodes(nodeOfConcern, Child.RIGHT);
    }

    /**
     * Builds an array of nodes in the specified subtree
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    public TreeNode[] enumerateNodes(TreeNode node, Child child) {
        TreeNode[] result = new TreeNode[countNodes(node, child)];

        TreeNode nodeOfConcern;
        if (child == Child.LEFT) {
            nodeOfConcern = node.left;
        } else {
            nodeOfConcern = node.right;
        }
        inOrderTraversal(nodeOfConcern, result, 0);

        return result;
    }

    /**
     * Builds the order traversal into a provided array
     *
     * @param node  the parent node, not to be included in returned array
     * @param arr the array of tree nodes to be inserted to
     * @param base the lowest index allocated to this subtree
     * @return the next position available in this array
     */
    private int inOrderTraversal(TreeNode node, TreeNode[] arr, int base) {
        if (node == null) {
            return base;
        }

        int pos = inOrderTraversal(node.left, arr, base);
        arr[pos] = node;
        return inOrderTraversal(node.right, arr, pos + 1);
    }

    /**
     * Builds a tree from the list of nodes
     * Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    public TreeNode buildTree(TreeNode[] nodeList) {
        return buildTreeHelper(nodeList, 0, nodeList.length - 1);
    }

    private TreeNode buildTreeHelper(TreeNode[] nodeList, int start, int end) {
        if (start > end) {
            return null;
        }

        int mid = (end - start) / 2 + start;
        nodeList[mid].left = buildTreeHelper(nodeList, start, mid - 1);
        nodeList[mid].right = buildTreeHelper(nodeList, mid + 1, end);

        return nodeList[mid];
    }

    /**
    * Rebuilds the specified subtree of a node
    * 
    * @param node the part of the subtree to rebuild
    * @param child specifies which child is the root of the subtree to rebuild
    */
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null) return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        // Finally, replace the specified child with the new subtree
        if (child == Child.LEFT) {
            node.left = newChild;
        } else if (child == Child.RIGHT) {
            node.right = newChild;
        }
    }

    /**
    * Inserts a key into the tree
    *
    * @param key the key to insert
    */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        TreeNode node = root;

        while (true) {
            if (key <= node.key) {
                if (node.left == null) break;
                node = node.left;
            } else {
                if (node.right == null) break;
                node = node.right;
            }
        }

        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }
    }


    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 10; i++) {
            tree.insert(i);
        }

        tree.rebuild(tree.root, Child.RIGHT);
    }

    @Override
    public String toString() {
        TreeNode[] result = new TreeNode[countNodes(root, Child.LEFT) + 1 + countNodes(root, Child.RIGHT)];
        TreeNode[] left = enumerateNodes(root, Child.LEFT);
        TreeNode[] right = enumerateNodes(root, Child.RIGHT);
        System.arraycopy(left, 0, result, 0, left.length);
        System.arraycopy(right, 0, result, left.length + 1, right.length);
        result[left.length] = root;

        return Arrays.toString(result);
    }
}
