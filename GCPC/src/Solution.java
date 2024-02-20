import java.util.Arrays;

public class Solution {
    // TODO: Include your data structures here

    private static class Team implements Comparable<Team> {
        private final int teamID;
        private long score;
        private long penalty;

        public Team(int teamID, int score, int penalty) {
            this.teamID = teamID;
            this.score = score;
            this.penalty = penalty;
        }

        public void increaseScore(long amount) {
            this.score += amount;
        }

        public void increasePenalty(long amount) {
            this.penalty += amount;
        }

        @Override
        public int compareTo(Team t) {
            if (this.score > t.score) return -1;
            else if (this.score < t.score) return 1;

            if (this.penalty < t.penalty) return -1;
            else if (this.penalty > t.penalty) return 1;

            return 0;
        }
    }

    private static class Node {
        private int height;
        private int weight;
        private int numTeams;
        private Team value;
        private Node left;
        private Node right;

        public String toString() {
            return String.format("%d score: %d penalty: %d H: %d W: %d", numTeams,
                    value.score, value.penalty, height, weight);
//            return String.format("Team %d, score: %d, penalty: %d. H: %d, W: %d, NT: %d",
//                    value.teamID, value.score, value.penalty, height, weight, numTeams);
        }
    }
    private Node root;

    private int getRank(Team team) {
        return getRankHelper(team, root);
    }
    private int getRankHelper(Team team, Node n) {
        int comparison = team.compareTo(n.value);
        if (comparison == 0) {
            return getWeight(n.left) + 1;
        } else if (comparison < 0) {
            return getRankHelper(team, n.left);
        } else {
            return getWeight(n.left) + 1 + getRankHelper(team, n.right);
        }
    }

    private Node findTeam(Team team) {
        return findTeamHelper(team, root);
    }
    private Node findTeamHelper(Team team, Node n) {
        int comparison = team.compareTo(n.value);
        if (comparison == 0) {
            return n;
        } else if (comparison < 0) {
            return findTeamHelper(team, n.left);
        } else {
            return findTeamHelper(team, n.right);
        }
    }

    private int inOrderTraversal(Node[] l, int index, Node n) {
        if (n == null) return index;
        index = inOrderTraversal(l, index, n.left);
        l[index++] = n;
        index = inOrderTraversal(l, index, n.right);
        return index;
    }
    private int countNodes(Node n) {
        if (n == null) return 0;
        return 1 + countNodes(n.left) + countNodes(n.right);
    }
    private void printTree() {
        Node[] l = new Node[countNodes(root)];
        inOrderTraversal(l, 0, root);
        System.out.println(Arrays.toString(l));
        char[][] lines = new char[root.height + 2][countNodes(root)];
        for (char[] line : lines) {
            Arrays.fill(line, ' ');
        }
        for (int i = 0; i < l.length; i++) {
            lines[l[i].height + 1][i] = l[i].toString().charAt(0);
        }

        for (int i = lines.length - 1; i >= 0; i--) {
            System.out.println(new String(lines[i]));
        }
    }


    private int getHeight(Node n) {
        return n == null ? -1 : n.height;
    }

    private int getWeight(Node n) {
        return n == null ? 0 : n.weight;
    }

    // NOTE: Left heavy is positive, Right heavy is negative
    private int getBalanceFactor(Node n) {
        return getHeight(n.left) - getHeight(n.right);
    }

    private boolean getNotBalanced(Node n) {
        return Math.abs(getBalanceFactor(n)) >= 2;
    }

    private void insertTeam(Team team) {
        root = insertTeamHelper(team, root);
    }

    private Node insertTeamHelper(Team team, Node n) {
        if (n == null) {
            Node newNode = new Node();
            newNode.height = -1;
            newNode.weight = 1;
            newNode.numTeams = 1;
            newNode.value = team;

            return newNode;
        }

        int comparison = team.compareTo(n.value);
        if (comparison == 0) {
            n.numTeams += 1;
            n.weight += 1;
        } else if (comparison < 0) {
            // search left
            Node res = insertTeamHelper(team, n.left);
            n.left = res;

            n.height = Math.max(getHeight(res), getHeight(n.right)) + 1;
            n.weight = getWeight(res) + getWeight(n.right) + 1;
        } else {
            // search right
            Node res = insertTeamHelper(team, n.right);
            n.right = res;

            n.height = Math.max(getHeight(res), getHeight(n.left)) + 1;
            n.weight = getWeight(res) + getWeight(n.left) + 1;
        }

        // rotate it to balance
        if (getNotBalanced(n)) {
            return balance(n);
        }

        return n;
    }

    private Node balance(Node n) {
        int factor = getBalanceFactor(n);
        if (factor > 0) {
            // meaning I am left heavy
            int childFactor = getBalanceFactor(n.left);
            if (childFactor < 0) {
                // meaning my child is right heavy
                n.left = rotate(n.left, Direction.LEFT);
            }
            return rotate(n, Direction.RIGHT);
        } else {
            // meaning I am right heavy
            int childFactor = getBalanceFactor(n.right);
            if (childFactor > 0) {
                // meaning my child is left heavy
                n.right = rotate(n.right, Direction.RIGHT);
            }
            return rotate(n, Direction.LEFT);
        }
    }

    private enum Direction {LEFT, RIGHT};
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

        n.height = Math.max(getHeight(n.left), getHeight(n.right)) + 1;
        n.weight = getWeight(n.left) + getWeight(n.right) + 1;

        temp.height = Math.max(getHeight(temp.left), getHeight(temp.right)) + 1;
        temp.weight = getWeight(temp.left) + getWeight(temp.right) + 1;

        return temp;
    }
    private void deleteNode(Team team) {
        root = deleteNodeHelper(team, root);
    }

    private Node deleteNodeHelper(Team team, Node n) {
        int comparison = team.compareTo(n.value);

        if (comparison == 0) {
            if (n.numTeams == 1) {
                // delete

                // if it is a leaf node, just delete
                if (n.left == null && n.right == null) {
                    return null;
                }

                // if it has only one child, we just replace him with that child
                if (n.right == null) {
                    return n.left;
                }

                if (n.left == null) {
                    return n.right;
                }

                // else find the in order predecessor and replace with him
                Node child = n.left;
                while (child.right != null) {
                    child = child.right;
                }

                Team val = child.value;
                child.value = n.value;
                n.value = val;

                int numTeams = child.numTeams;
                child.numTeams = n.numTeams;
                n.numTeams = numTeams;

                n.left = deleteNodeHelper(team, n.left);
                n.height = Math.max(getHeight(n.left), getHeight(n.right)) + 1;
                n.weight = getWeight(n.left) + getWeight(n.right) + 1;

                return n;
            } else {
                // minus one
                n.numTeams -= 1;
                n.weight -= 1;
                return n;
            }
        } else if (comparison < 0) {
            // search left
            Node res = deleteNodeHelper(team, n.left);
            n.left = res;

            n.height = Math.max(getHeight(res), getHeight(n.right)) + 1;
            n.weight = getWeight(res) + getWeight(n.right) + 1;

        } else {
            // search right
            Node res = deleteNodeHelper(team, n.right);
            n.right = res;

            n.height = Math.max(getHeight(res), getHeight(n.left)) + 1;
            n.weight = getWeight(res) + getWeight(n.left) + 1;
        }

        if (getNotBalanced(n)) {
            balance(n);
        }

        return n;
    }


    private final Team[] teams;
    public Solution(int numTeams) {
        teams = new Team[numTeams];
        for (int i = 0; i < teams.length; i++) {
            teams[i] = new Team(i + 1, 0, 0);
        }
        for (Team t : teams) {
            insertTeam(t);
        }
        // printTree();
    }

    public int update(int team, long newPenalty){
        System.out.println("TEAM: " + team);
        Team teamToUpdate = teams[team - 1];

        // delete the node
        deleteNode(teamToUpdate);

        // update the team values
        teamToUpdate.increaseScore(1);
        teamToUpdate.increasePenalty(newPenalty);

        // insert back the node
        insertTeam(teamToUpdate);

        printTree();
        // find and return the rank of team 1
        return getRank(teams[0]);
    }

}
