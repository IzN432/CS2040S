import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class MyGameTree {

    private CustomTreeNode root = null;

    private TreeNodeFormatter formatter = new TreeNodeFormatter(TreeNodeFormatter.TIC_TAC_TOE);


    /**
     * Constructor for the game tree
     * @param root the tree node to act as the root
     */
    MyGameTree(CustomTreeNode root) {
        this.root = root;
    }

    /**
     * Sets the formatter to be used to print board state
     * @param formatter the formatter to be set
     */
    public void setFormatter(TreeNodeFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * Navigates down the tree, selecting the child of root
     * at the index provided
     *
     * @param index the index of the child selected
     */
    public void navigate(int index) {
        root = root.children[index];
    }

    /**
     * Prints out the children of the root node
     * @param flipValues 0 - don't show values, 1 - display normal values, 2 - display inverted values
     */
    public void printOptions(int flipValues) {
        int multiplier = 1;
        if (flipValues == 2) {
            multiplier = -1;
        }
        System.out.println(this);

        if (root.numChildren == 0) {
            System.out.println("No children, this is a leaf node");
            return;
        }

        String[][] lines = new String[root.numChildren][11];

        for (int i = 0; i < root.numChildren; i++) {
            lines[i] = formatter.format(root.children[i]).split("\n");
        }

        System.out.println();
        for (int j = 0; j < lines.length; j++) {
            String a;
            if (flipValues == 0) {
                a = String.format(" option: %d", j);

            } else {
                a = String.format(" option: %d, value: %d", j, root.children[j].value * multiplier);

            }
            int numSpaces = 26 - a.length();
            a = a + " ".repeat(numSpaces);
            System.out.printf(a);
        }
        System.out.println();
        for (int i = 0; i < 11; i++) {
            for (String[] line : lines) {
                System.out.printf("%s   ", line[i]);
            }
            System.out.println();
        }
    }

    public boolean hasChildren() {
        return root.numChildren > 0;
    }

    public int numChildren() {
        return root.numChildren;
    }

    /**
     * Returns the current state of the game tree, along with its
     * children
     *
     * @return string representation of the game state
     */
    @Override
    public String toString() {
        return formatter.format(root);
    }

    /**
     * Populates the entire game tree with the values at each node
     */
    private void findValues() {
        findValueHelper(1, root);
    }

    /**
     * Helper function to determine the value at each node
     * @param player the player that is playing in this node
     * @param n the node that we are looking at
     * @return the best value the player can get from this node
     */
    private int findValueHelper(int player, CustomTreeNode n) {
        if (n == null) {
            return Integer.MIN_VALUE;
        } else if (n.leaf) {
            return n.value;
        }

        if (player == 1) {
            int maxValue = Integer.MIN_VALUE;
            for (CustomTreeNode child : n.children) {
                maxValue = Math.max(maxValue, findValueHelper(3 - player, child));
            }

            n.value = maxValue;
            return maxValue;
        } else {
            int minValue = Integer.MAX_VALUE;
            for (CustomTreeNode child : n.children) {
                minValue = Math.min(minValue, findValueHelper(3 - player, child));
            }

            n.value = minValue;
            return minValue;
        }

    }

    /**
     * Returns the ending points assuming both players play to their best abilities
     * @return the resulting points, positive indicating player 1 wins, negative indicating
     * player 2 wins, 0 indicates a draw
     */
    public int findVictor() {
        return root.value;
    }

    /**
     * Creates the game tree from a file with my own custom format
     *
     * @param fileName the name of the file to be used
     * @return a game tree created from the file
     */
    public static MyGameTree readTreeFromFile(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            MyGameTree tree = new MyGameTree(readTreeHelper(reader));
            tree.findValues();
            return tree;
        } catch (IOException e) {
            System.out.println("Failed to load file");
            throw new RuntimeException(e.toString());
        }
    }

    /**
     * Helper function for readTreeFromFile
     *
     * @param reader a buffered reader to be used to read from the file
     * @return the tree node that acts as the root
     * @throws IOException when buffered reader fails to read
     */
    private static CustomTreeNode readTreeHelper(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        if (s == null) {
            throw new java.io.IOException("File ended too soon.");
        }
        CustomTreeNode node = new CustomTreeNode();

        // first character is the number of children
        node.numChildren = Integer.parseInt(s.substring(0, 1));
        node.children = new CustomTreeNode[node.numChildren];

        // if it is a leaf, the second character will be a 1
        node.leaf = (s.charAt(1) == '1');
        node.value = Integer.MIN_VALUE;
        if (node.leaf) {
            // third character is positive or negative
            int negative = Character.getNumericValue(s.charAt(2)) * -2 + 1;
            // fourth character is ascii + 65
            int v = s.charAt(3) - 65;
            node.value = v * negative;
        }
        node.name = s.substring(4);

        for (int i = 0; i < node.numChildren; i++) {
            node.children[i] = readTreeHelper(reader);
        }
        return node;
    }

    /**
     * Simulates a player vs player game
     * @param tree the game tree
     * @param displayValues whether to display the values of each node
     */
    public static void pvp(MyGameTree tree, boolean displayValues) {
        int player = 1;
        Scanner sc = new Scanner(System.in);
        int count = 0;
        while (tree.hasChildren()) {
            // get player option
            System.out.printf("======== ROUND %d ========\n\nCurrent board state:\n", count / 2);
            count++;

            if (displayValues) {
                tree.printOptions(player);
            } else {
                tree.printOptions(0);
            }

            int playerChoice = -1;
            while (playerChoice < 0 || playerChoice > tree.numChildren() - 1) {
                System.out.printf("Player %d choose an option (0-%d): ", player, tree.numChildren() - 1);
                playerChoice = Integer.parseInt(sc.nextLine());
            }
            tree.navigate(playerChoice);
            player = 3 - player;
        }

        if (tree.root.value > 0) {
            // player 1 win
            System.out.println("Player 1 won with a score of " + tree.root.value);
        } else if (tree.root.value < 0) {
            // player 2 win
            System.out.println("Player 2 won with a score of " + tree.root.value);
        } else {
            // draw
            System.out.println("It was a draw!");
        }
    }

    /**
     * Simulates a player vs computer game where the computer has access to the game tree
     * @param tree the game tree
     * @param computerTurn which turn the computer will take (1 for it to go first, 2 for player to go first)
     * @param displayValues whether to display the values of each node
     */
    public static void pve(MyGameTree tree, int computerTurn, boolean displayValues) {
        int player = 1;
        Scanner sc = new Scanner(System.in);
        int count = 0;
        while (tree.hasChildren()) {
            // get player option
            System.out.printf("======== ROUND %d ========\n\nCurrent board state:\n", count / 2);
            count++;

            if (displayValues) {
                tree.printOptions(player);
            } else {
                tree.printOptions(0);
            }

            int playerChoice = -1;

            if (player == computerTurn) {
                if (computerTurn == 1) {
                    // find max
                    int max = Integer.MIN_VALUE;
                    int index = -1;
                    CustomTreeNode[] children = tree.root.children;
                    for (int i = 0; i < children.length; i++) {
                        CustomTreeNode child = children[i];
                        if (max < child.value) {
                            index = i;
                            max = child.value;
                        }
                    }
                    playerChoice = index;
                } else {
                    // find min
                    int min = Integer.MAX_VALUE;
                    int index = -1;
                    CustomTreeNode[] children = tree.root.children;
                    for (int i = 0; i < children.length; i++) {
                        CustomTreeNode child = children[i];
                        if (min > child.value) {
                            index = i;
                            min = child.value;
                        }
                    }
                    playerChoice = index;
                }
                System.out.printf("Player %d choose an option (0-%d): %d\n", player, tree.numChildren() - 1, playerChoice);
            }
            while (playerChoice < 0 || playerChoice > tree.numChildren() - 1) {
                try {
                    System.out.printf("Player %d choose an option (0-%d): ", player, tree.numChildren() - 1);
                    playerChoice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {}
            }
            tree.navigate(playerChoice);
            player = 3 - player;
            System.out.println();
        }

        if (tree.root.value > 0) {
            // player 1 win
            System.out.println("Player 1 won with a score of " + tree.root.value);
        } else if (tree.root.value < 0) {
            // player 2 win
            System.out.println("Player 2 won with a score of " + Math.abs(tree.root.value));
        } else {
            // draw
            System.out.println("It was a draw!");
        }

    }

    private final static String FILENAME = "variants/custom.txt";
    /**
     * Creates a file with arbitrary tic-tac-toe
     * @param values the values to be used
     */
    public static void createArbitraryTicTacToe(int[] values) {
        TicTacToeGenerator.createArbitraryFile(FILENAME, values, true);
    }

    /**
     * Creates a file with misère tic-tac-toe
     */
    public static void createMisereTicTacToe() {
        TicTacToeGenerator.createArbitraryFile(FILENAME, new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0}, false);
    }

    /**
     * Creates a file with no-tie tic-tac-toe
     */
    public static void createNoTieTicTacToe() {
        TicTacToeGenerator.createArbitraryFile(FILENAME, new int[] {1, 1, 1, 1, -1, 1, 1, 1, 1}, true);
    }
    public static void main(String[] args) {
        // Uncomment the line that you want, or if you already generated the file and don't want to wait
        // just uncomment all three

        createArbitraryTicTacToe(TicTacToeGenerator.SAMPLE_VALUES[0]);
        // createMisereTicTacToe();
        // createNoTieTicTacToe();

        MyGameTree tree = readTreeFromFile(FILENAME);

        // Print out the resulting value, positive means Player 1, negative Player 2, 0 means draw
        // System.out.println(tree.findVictor());

        // Choose between pve (computer) or pvp (player vs player)
        pve(tree, 2, true);
        // pvp(tree, true);
    }
}
