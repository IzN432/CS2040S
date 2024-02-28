/**
 * ScapeGoat Tree class
 * <p>
 * This class contains some basic code for implementing a ScapeGoat tree. This version does not include any of the
 * functionality for choosing which node to scapegoat. It includes only code for inserting a node, and the code for
 * rebuilding a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    public enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     * <p>
     * This class holds the data for a node in a binary tree.
     * <p>
     * Note: we have made things public here to facilitate problem set grading/testing. In general, making everything
     * public like this is a bad idea!
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;
        public int weight;
        TreeNode(int k) {
            key = k;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node, Child child) {
        return child == Child.LEFT ? node.left.weight : node.right.weight;
    }

    /**
     * Builds an array of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    TreeNode[] enumerateNodes(TreeNode node, Child child) {
        TreeNode[] output = new TreeNode[countNodes(node, child)];
        enumerateNodesHelper(output, child == Child.LEFT ? node.left : node.right, 0);
        return output;
    }

    private int enumerateNodesHelper(TreeNode[] output, TreeNode node, int index) {
        if (node == null) {
            return index;
        }
        index = enumerateNodesHelper(output, node.left, index);
        output[index++] = node;
        return enumerateNodesHelper(output, node.right, index);
    }
    /**
     * Builds a tree from the list of nodes Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    TreeNode buildTree(TreeNode[] nodeList) {
        return buildTreeHelper(nodeList, 0, nodeList.length - 1);
    }

    private int getWeight(TreeNode n) {
        return n == null ? 0 : n.weight;
    }
    private TreeNode buildTreeHelper(TreeNode[] nodeList, int start, int end) {
        if (start > end) return null;
        int mid = (start + end) / 2;
        TreeNode middleNode = nodeList[mid];
        middleNode.left = buildTreeHelper(nodeList, start, mid - 1);
        middleNode.right = buildTreeHelper(nodeList, mid + 1, end);
        middleNode.weight = getWeight(middleNode.left) + getWeight(middleNode.right) + 1;
        return middleNode;
    }
    /**
     * Determines if a node is balanced. If the node is balanced, this should return true. Otherwise, it should return
     * false. A node is unbalanced if either of its children has weight greater than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {
        if (node == null) return true;
        return !(getWeight(node.left) > (2 / 3.0) * node.weight || getWeight(node.right) > (2 / 3.0) * node.weight);
    }

    /**
     * Rebuilds the specified subtree of a node.
     *
     * @param node  the part of the subtree to rebuild
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
        node.weight = getWeight(node.left) + getWeight(node.right) + 1;
    }

    /**
     * Inserts a key into the tree.
     *
     * @param key the key to insert
     */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            root.weight = 1;
            return;
        }

        TreeNode node = root;

        while (true) {
            node.weight++;
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
            node.left.weight = 1;
        } else {
            node.right = new TreeNode(key);
            node.right.weight = 1;
        }

        balance(key, root);
    }

    private boolean balance(int key, TreeNode n) {
        if (key == n.key) {
            return false;
        } else if (key < n.key) {
            if (balance(key, n.left)) {
                rebuild(n, Child.LEFT);
            }
            return !checkBalance(n);
        } else {
            if (balance(key, n.right)) {
                rebuild(n, Child.RIGHT);
            }
            return !checkBalance(n);
        }
    }

    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 100; i++) {
            tree.insert(i);
        }

        tree.rebuild(tree.root, Child.RIGHT);
    }
}
