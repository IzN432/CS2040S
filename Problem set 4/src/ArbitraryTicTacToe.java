import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ArbitraryTicTacToe {

    private final static int[][] sampleValues = new int[][] {
            new int[] {0, 0, 0, 0, 1, 0, 0, 0, 0},
            new int[] {0, 0, 0, 0, -1, 0, 0, 0, 0},
            new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1},
            new int[] {-1, -1, -1, -1, -1, -1, -1, -1, -1},
    };

    public static void main(String[] args) {
        try {
            File file = new File("variants/custom_arbitrary.txt");
            file.delete();
            if (file.createNewFile()) {
                System.out.println("File created");
            }

            FileWriter writer = new FileWriter("variants/custom_arbitrary.txt");

            String data = generateArbitrary(new int[9], sampleValues[3], 1);
            writer.write(data.substring(0, data.length() - 1));
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GameTree.main(new String[0]);
    }

    // generating arbitrary from values
    private static String generateArbitrary(int[] board, int[] values, int player) {
        StringBuilder buildString = new StringBuilder();
        int boardState = getBoardState(board);
        // find if it is a leaf
        if (boardState != 0) {
            return singleState(board, values, boardState, 0);
        }

        int emptyCount = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] != 0) continue;
            buildString.append(generateArbitrary(copyExceptPosition(board, i, player), values, 3 - player));
            emptyCount++;
        }

        buildString.insert(0, singleState(board, values, 0, emptyCount));
        return buildString.toString();
    }

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

    private static String boardToReadable(int[] board) {
        StringBuilder b = new StringBuilder();
        for (int j : board) {
            if (j == 0) b.append("_");
            else if (j == 1) b.append("X");
            else b.append("O");
        }
        return b.toString();
    }

    private static int[] copyExceptPosition(int[] board, int position, int value) {
        int[] clone = board.clone();
        clone[position] = value;
        return clone;
    }

    // return 0 for nothing, 1 for player 1 win, 2 for player 2 win, 3 for draw
    private static int getBoardState(int[] board) {
        for (int[] endGame : endGames) {
            int value = -1;
            boolean failed = false;
            for (int i = 0; i < endGame.length; i++) {
                if (endGame[i] == 0) continue;
                if (board[i] == value || value == -1) {
                    value = board[i];
                } else {
                    failed = true;
                }
            }
            if (!failed) return value;
        }

        for (int i : board) {
            if (i == 0) {
                return 0;
            }
        }
        return 3;
    }

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
