public class Solution {
    // TODO: Include your data structures here

<<<<<<< HEAD
    private static class Node implements Comparable<Node> {
        private long energy;
        private long value;
        private int height;
        private Node right;
        private Node left;
        public Node(long energy, long value) {
            this.energy = energy;
            this.value = value;
        }

        @Override
        public int compareTo(Node n) {
            if (this.energy > n.energy) {
                return 1;
            } else if (this.energy < n.energy) {
                return -1;
            }
            if (this.value > n.value) {
                return 1;
            } else if (n.value > this.value) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    private Node root;
=======
>>>>>>> ee8bda445685cc3ae73061c50416204f706fc60e
    public Solution() {
        // TODO: Construct/Initialise your data structures here
    }

    void add(long energy, long value) {
        // TODO: Implement your insertion operation here
<<<<<<< HEAD
        Node nodeToAdd = new Node(energy, value);
        nodeToAdd.height = 0;
        if (root == null) {
            root = nodeToAdd;
        } else {
            root = insert(nodeToAdd, root);
        }
    }

    private Node insert(Node nodeToAdd, Node curr) {
        if (curr == null) {
            return nodeToAdd;
        }

        if (nodeToAdd.compareTo(curr) < 0) {
            // go left
            curr.left = insert(nodeToAdd, curr.left);
        } else {
            // go right
            curr.right = insert(nodeToAdd, curr.right);
        }
        return balance(curr);
    }
    private int getHeight(Node n) {
        return n == null ? -1 : n.height;
    }
    private void computeHeight(Node n) {
        n.height = Math.max(getHeight(n.left), getHeight(n.right)) + 1;
    }
    private Node balance(Node n) {
        computeHeight(n);
        int factor = balanceFactor(n);
        if (Math.abs(factor) < 2) {
            return n;
        }
        if (factor < 0) {
            // right heavy
            int childFactor = balanceFactor(n.right);
            if (childFactor > 0) {
                // child left heavy
                n.right = rotate(n.right, Direction.RIGHT);
            }
            return rotate(n, Direction.LEFT);
        } else {
            // left heavy
            int childFactor = balanceFactor(n.left);
            if (childFactor < 0) {
                // child right heavy
                n.left = rotate(n.left, Direction.LEFT);
            }
            return rotate(n, Direction.RIGHT);
        }
    }
    private int balanceFactor(Node n) {
        return getHeight(n.left) - getHeight(n.right);
    }
    private enum Direction {LEFT, RIGHT, START};
    private Node rotate(Node n, Direction dir) {
        Node temp;
        if (dir == Direction.LEFT) {
            temp = n.right;
            n.right = temp.left;
            temp.left = n;
        } else {
            temp = n.left;
            n.left = temp.right;
            temp.right = n;
        }
        computeHeight(n);
        computeHeight(temp);
        return temp;
    }
    long query(long remainingEnergy) {
        // TODO: Implement your query operation here
        long value = 0L;
        while (remainingEnergy > 0) {
            Node n = findHighest(remainingEnergy, root, null);
            if (n == null) break;
            remainingEnergy -= n.energy;
            value += n.value;
            root = deleteHighest(new Node(n.energy, n.value), root);
        }
        return value;
    }
    private Node findHighest(long energy, Node n, Node prev) {
        if (n == null) {
            Node curr = prev;
            if (prev == null) return null;
            while (curr.right != null && curr.right.energy == curr.energy) {
                curr = curr.right;
            }
            return curr;
        }
        if (energy > n.energy) {
            return findHighest(energy, n.right, n);
        } else if (energy < n.energy) {
            return findHighest(energy, n.left, prev);
        } else {
            Node curr = n;
            while (curr.right != null && curr.right.energy == curr.energy) {
                curr = curr.right;
            }
            return curr;
        }
    }
    private void printTree() {
        printTree(root);
        System.out.println();
    }
    private void printTree(Node n) {
        if (n == null) return;
        printTree(n.left);
        System.out.print(n.value + ", ");
        printTree(n.right);
    }
    private Node deleteHighest(Node search, Node n) {
        if (search.compareTo(n) < 0) {
            n.left = deleteHighest(search, n.left);
        } else if (search.compareTo(n) > 0) {
            n.right = deleteHighest(search, n.right);
        } else {
            // check if two children
            if (n.left != null && n.right != null) {
                // swap with predecessor
                Node predecessor = n.left;
                while (predecessor.right != null) {
                    predecessor = predecessor.right;
                }
                long tempEnergy = predecessor.energy;
                long tempValue = predecessor.value;
                predecessor.energy = n.energy;
                predecessor.value = n.value;
                n.energy = tempEnergy;
                n.value = tempValue;

                n.left = deleteHighest(search, n.left);
            } else if (n.left != null) {
                return n.left;
            } else if (n.right != null) {
                return n.right;
            } else {
                return null;
            }
        }
        return balance(n);
    }

    public static void main(String[] args) {
        Solution sol = new Solution();
        sol.add(10, 100);
        sol.add(10, 50);
        sol.add(5, 10);
        sol.add(1, 10);
        sol.printTree();
        System.out.println(sol.query(15));
    }
=======
    }

    long query(long remainingEnergy) {
        // TODO: Implement your query operation here

        return 0L;
    }

>>>>>>> ee8bda445685cc3ae73061c50416204f706fc60e
}
