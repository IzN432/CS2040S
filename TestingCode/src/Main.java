import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random(1);

        System.out.println(test(random));
    }

    public static boolean test(Random rng) {
        Main m = new Main();
        for (int j = 0; j < 1000; j++) {
            int[] input = new int[20];
            for (int i = 0; i < 20; i++) {
                input[i] = rng.nextInt(100) - 50;
            }
            int solution = m.solve(input);
            int solution2 = m.trivialSolve(input);
            if (solution != solution2) {
                System.out.println("Mine: " + solution);
                System.out.println("True: " + solution2);
                System.out.println("Array: " + Arrays.toString(input));
                return false;
            }
        }
        return true;
    }
    public int solve(int[] values) {
        int max = 0;
        int current = 0;
        int count = 0;
        PriorityQueue<Integer> queue = new PriorityQueue<>();

        for (int value : values) {
            if (value >= 0) {
                current += value;
                count++;
            }

            if (value < 0) {
                current += value;
                queue.add(value);
                count++;
                if (current < 0) {
                    count--;
                    current -= queue.remove();
                }
            }

            max = Math.max(max, count);
        }

        return max;
    }

    public int trivialSolve(int[] values) {
        return helper(values, 0, 0, 0);
    }

    private int helper(int[] values, int currentValue, int currentCount, int index) {
        if (currentValue < 0) return 0;

        if (index == values.length) return currentCount;

        return Math.max(helper(values, currentValue + values[index], currentCount + 1, index + 1),
                helper(values, currentValue, currentCount, index + 1));

    }
}