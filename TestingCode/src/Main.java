import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        test(1000000, 10);
    }

    static Random random = new Random(1);
    private static void test(int iterations, int bound) {
        for (int i = 0; i < iterations; i++) {
            int height = random.nextInt(100) + 1;
            int width = random.nextInt(100) + 1;
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            int sol1 = mathSolution(x, y, width, height);
            int sol2 = yinSolution(x, y, width, height);
            if (sol1 != sol2)
                System.out.printf("H: %d, W: %d, x: %d, y: %d\n%d vs %d\n", height, width, x, y, sol1, sol2);


        }

    }

    private static int mathSolution(int x, int y, int width, int height) {
        int topBound = height - y - 1;
        int bottomBound = y;
        int leftBound = x;
        int rightBound = width - x - 1;

        int[] arr = new int[] {topBound, bottomBound, leftBound, rightBound};
        Arrays.sort(arr);

        int lowestBound = arr[0];

        // find the number of outer layer steps
        int outerLayers = width * height - (width - 2 * lowestBound) * (height - 2 * lowestBound);
        x = x - lowestBound;
        y = y - lowestBound;

        int newWidth = width - lowestBound * 2;
        int newHeight = height - lowestBound * 2;
        if (y == 0) return outerLayers + x;
        if (x == newWidth - 1) return outerLayers + newWidth - 1 + y;
        if (y == newHeight - 1) return outerLayers + newWidth - 1 + newHeight - 1 + newWidth - x - 1;
        if (x == 0) return outerLayers + newWidth - 1 + newHeight - 1 + newWidth - 1 + newHeight - y - 1;

        return 0;
    }

    private static int solution(int x, int y, int width, int height) {
        if (y == 0 && x == 0) return 0;
        for (int i = 0; i < width; i++) {
            x--;
            if (x == 0 && y == 0) return i + 1;
        }

        return width + solution(y - 1, - x - 1, height - 1, width);
    }

    private static int yinSolution(int x, int y, int width, int height) {
        return yinHelper(x + 1, y, width, height) - 1;
    }

    private static int yinHelper(int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            x -= 1;
            if (x == 0 && y == 0) {
                return i + 1;
            }
        }
        return width + yinHelper(y, -x, height - 1, width);
    }
}
