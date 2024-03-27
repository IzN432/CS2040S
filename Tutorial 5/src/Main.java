
public class Main {
    public static void main(String[] args) {

    }
}

class Student implements Comparable<Student> {
    String name;
    int height;
    double grade;

    @Override
    public int compareTo(Student s) {
        return this.height - s.height;
    }

}
class Tree {

    private static class Node implements Comparable<Student> {
        public static enum Direction {
            LEFT, RIGHT
        }

        Student s;
        Node left;
        Node right;
        int height = 0;
        public Node(Student s) {
            this.s = s;
        }

        @Override
        public int compareTo(Student s) {
            return this.s.compareTo(s);
        }

        public Node get(Direction d) {
            if (d == Direction.LEFT) {
                return left;
            } else if (d == Direction.RIGHT) {
                return right;
            } else {
                throw new RuntimeException("Invalid input in Node::get(Direction)");
            }
        }

        public void set(Direction d, Node n) {
            if (d == Direction.LEFT) {
                left = n;
            } else if (d == Direction.RIGHT) {
                right = n;
            } else {
                throw new RuntimeException("Invalid input in Node::get(Direction)");
            }
        }
    }

    private Node root;

    private int getHeight(Node n) {
        return n == null ? -1 : n.height;
    }

    public void insert(Student s) {
        if (root == null) {
            root = new Node(s);
        } else {
            root = insertHelper(root, s);
        }
    }

    private static Node insertHelper(Node n, Student s) {
        if (n == null) {
            return new Node(s);
        } else {
            int temp = n.compareTo(s);
            Node.Direction direction;
            if (temp < 0) {
                // the student is on the right
                direction = Node.Direction.RIGHT;
            } else {
                // the student is on the left
                direction = Node.Direction.LEFT;
            }

            n.set(direction, insertHelper(n.get(direction), s);
        }
    }

    private void computeStatistics() {

    }

    private Node balance(Node n) {

    }

}