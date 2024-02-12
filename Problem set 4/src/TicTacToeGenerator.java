import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class TicTacToeGenerator {

    /**
     * Sample values for the values in arbitrary tic-tac-toe
     */
    public final static int[][] SAMPLE_VALUES = new int[][] {
            {1, -2, 1, -3, -2, 2, -2, -1, 1}, // the provided example
            {0, 0, 0, 0, 1, 0, 0, 0, 0}, // only center has 1
            {0, 0, 0, 0, -1, 0, 0, 0, 0}, // only center has -1
            {1, 1, 1, 1, 1, 1, 1, 1, 1}, // all are the same (positive)
            {-1, -1, -1, -1, -1, -1, -1, -1, -1}, // all are the same (negative)
            {1, 1, 1, 1, 0, 1, 1, 1, 1}, // basically no tie tic-tac-toe
    };

    /**
     * Creates a tree for a game of arbitrary tic-tac-toe with the provided values
     * and writes it to the provided filename. WARNING: it deletes the file beforehand
     * @param fileName the file to be written to
     * @param values the values of the squares (top to bottom, left to right)
     */
    public static void createArbitraryFile(String fileName, int[] values, boolean threeInRowIsWin) {
        try {
            File file = new File(fileName);
            if (file.delete()) {
                System.out.println("File deleted");
            }
            if (file.createNewFile()) {
                System.out.println("File created");
            }

            FileWriter writer = new FileWriter(fileName);

            String data = generateArbitrary(new int[9], values, 1, threeInRowIsWin);
            writer.write(data.substring(0, data.length() - 1));
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates the string to be written to the file recursively
     * @param board the current board state
     * @param values the values for arbitrary tic-tac-toe
     * @param player the player that is currently playing
     * @return the string to be written to the file
     */
    private static String generateArbitrary(int[] board, int[] values, int player, boolean threeInRowIsWin) {
        StringBuilder buildString = new StringBuilder();
        int boardState = getBoardState(board, threeInRowIsWin);
        // find if it is a leaf
        if (boardState != 0) {
            return singleState(board, values, boardState, 0);
        }

        int emptyCount = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] != 0) continue;
            buildString.append(generateArbitrary(copyExceptPosition(board, i, player), values, 3 - player, threeInRowIsWin));
            emptyCount++;
        }

        buildString.insert(0, singleState(board, values, 0, emptyCount));
        return buildString.toString();
    }

    /**
     * Returns a single line in the file that represents the state
     * at that board state
     * @param board the board
     * @param values the arbitrary tic-tac-toe values
     * @param state the state of the board (see getBoardState)
     * @param numChildren the number of children
     * @return the single line string
     */
    private static String singleState(int[] board, int[] values, int state, int numChildren) {
        // number of children, is a leaf, isNegative, points, board state
        int isLeaf = 0;
        int value = 0;
        if (state != 0) {
            isLeaf = 1;
            if (state == 1) {
                // player 1 win
                value = 100;
            } else if (state == 2) {
                value = -100;
            }
        }
        if (state == 3) {
            for (int i = 0; i < board.length; i++) {
                if (board[i] == 1) {
                    value += values[i];
                } else {
                    value -= values[i];
                }
            }
        }
        int isNegative = 0;
        if (value < 0) {
            isNegative = 1;
        }

        return String.format("%d%d%d%c%s\n", numChildren, isLeaf, isNegative, (char) (Math.abs(value) + 65), boardToReadable(board));
    }

    /**
     * Converts the boards that are in integer to readable form
     * with Os and Xs
     * @param board the board to be converted
     * @return a prettier version
     */
    private static String boardToReadable(int[] board) {
        StringBuilder b = new StringBuilder();
        for (int j : board) {
            if (j == 0) b.append("_");
            else if (j == 1) b.append("X");
            else b.append("O");
        }
        return b.toString();
    }

    /**
     * Copies the board over but changes the value at position to value
     * @param board the board to be copied
     * @param position the position to be changed
     * @param value the value to change to
     * @return the editted copy of the board
     */
    private static int[] copyExceptPosition(int[] board, int position, int value) {
        int[] clone = board.clone();
        clone[position] = value;
        return clone;
    }

    /**
     * Returns the board state
     * @param board the board to be checked
     * @return 0 for nothing, 1 for player 1 win, 2 for player 2 win, 3 for draw
     */
    private static int getBoardState(int[] board, boolean threeInRowIsWin) {
        for (int[] endGame : endGames) {
            int value = -1;
            boolean failed = false;
            for (int i = 0; i < endGame.length; i++) {
                if (endGame[i] == 0) continue;
                if (board[i] != 0 && (board[i] == value || value == -1)) {
                    value = board[i];
                } else {
                    failed = true;
                    break;
                }
            }
            if (!failed) {
                if (threeInRowIsWin) return value;
                else return 3 - value;
            }
        }

        for (int i : board) {
            if (i == 0) {
                return 0;
            }
        }
        return 3;
    }

    /**
     * The list of end games that dictates a victory
     */
    private static final int[][] endGames = new int[][] {
            new int[] {1, 1, 1, 0, 0, 0, 0, 0, 0},
            new int[] {0, 0, 0, 1, 1, 1, 0, 0, 0},
            new int[] {0, 0, 0, 0, 0, 0, 1, 1, 1},
            new int[] {1, 0, 0, 1, 0, 0, 1, 0, 0},
            new int[] {0, 1, 0, 0, 1, 0, 0, 1, 0},
            new int[] {0, 0, 1, 0, 0, 1, 0, 0, 1},
            new int[] {1, 0, 0, 0, 1, 0, 0, 0, 1},
            new int[] {0, 0, 1, 0, 1, 0, 1, 0, 0}
    };
}
