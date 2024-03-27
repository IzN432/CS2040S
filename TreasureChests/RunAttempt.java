import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

/**
 * class RunAttempt
 *
 * @author benleong
 * Description: Code that runs the 2 contests
 */
public class RunAttempt implements ITreasureExtractor {

    public static final int MAX_COUNT = 9999;

    int[] bitmap; // Bitmap used to keep track of the keys. 0 means a wrong key, 1 means a correct key
    int k = 0; // Number of correct keys

    int totalAttempts = 0;
    int totalCost = 0;

    /**
     * Attempts to unlock the chest with a given set of keys and returns true if the chest
     * was unlocked, false otherwise.
     *
     * @param keys    Key bitmap of size N, where N is the number
     *                of keys. A '1' at index i indicates that key i is to be
     *                used to unlock the chest.
     * @return true if the treasure chest was unlocked
     */
    @Override
    public boolean tryUnlockChest(int[] keys) {
        if (keys.length != bitmap.length) {
            throw new RuntimeException("Invalid key bitmap.");
        }
        if (totalAttempts > MAX_COUNT) {
            throw new RuntimeException("Count exceeded!");
        }

        int correctKeys = 0;
        int totalKeys = 0;
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] < 0 || keys[i] > 1) {
                throw new RuntimeException("Invalid key bitmap.");
            }
            if (keys[i] == 1) {
                totalKeys += 1;
            }
            correctKeys += keys[i] * bitmap[i];
        }
        if (totalKeys < k) {
            throw new RuntimeException("Invalid attempt - not enough keys!");
        }

        totalAttempts++;
        totalCost += totalKeys;
        return correctKeys == k;
    }

    /**
     * Tests if the given bitmap is the correct configuration (i.e. identifies
     * all the correct keys.
     */
    private boolean testCorrect(int[] testBitmap) {
        if (testBitmap.length != bitmap.length) {
            throw new RuntimeException("Invalid test bitmap.");
        }
        for (int i = 0; i < bitmap.length; i++) {
            if (bitmap[i] != testBitmap[i]) {
                return false;
            }
        }
        return true;
    }


    /**
     * Runs the test and return the number of attempts taken to find all the correct keys.
     *
     * @param f Algorithm to be tested
     * @return Number of attempts required to find the correct keys
     */
    private int findAttempts(IFindKeys f) {
        resetStatistics();
        int[] ans = f.findKeys(bitmap.length, k, this);
        if (!testCorrect(ans)) {
            throw new RuntimeException("Wrong answer!");
        }
        return totalAttempts;
    }

    /**
     * Runs the test and return the cost to find all the correct keys.
     *
     * @param f Algorithm to be tested
     * @return Cost incurred to find the correct keys
     */
    private int findCost(IFindKeys f) {
        resetStatistics();
        int[] ans = f.findKeys(bitmap.length, k, this);
        if (!testCorrect(ans)) {
            throw new RuntimeException("Wrong answer!");
        }
        return totalCost;
    }

    private void resetStatistics() {
        totalAttempts = 0;
        totalCost = 0;
    }

    private void setTestCase(int[] bitmap) {
        int count = 0;
        for (int i = 0; i < bitmap.length; i++) {
            if (bitmap[i] < 0 || bitmap[i] > 1) {
                throw new RuntimeException("Invalid key bitmap.");
            }
            count += bitmap[i];
        }
        this.bitmap = bitmap;
        this.k = count;
    }

    /**
     * main procedure - for testing
     *
     * @param args
     */
    public static void main(String[] args) {

        // TODO: Feel free to change the test case here.
        //  bitmap is an array of n keys where 1 means that the key is correct (corresponds to one of the locks)

        // runAttemptTests(1024, 17, 100);
        // runCostTests(1024, 17, 1);

        /*
        for (int i = 0; i < 1024; i++) {
            for (int j = i / 2; j >= 0; j--) {
                BigInteger a = FindKeysMinimumAttempts.choose(i, j);
                int estimate = (int) log(a);
                System.out.println("===== N: " + i + ", k: " + j + " ======");
                System.out.println(estimate + ": " + testAttempt(generate(i, j)));
                System.out.println("Cost: " + test)
            }
        }
        */

        // runAttemptTests(1024, 17, 100000);
    }
    public static double log(BigInteger val) {
        // https://stackoverflow.com/questions/739532/logarithm-of-a-bigdecimal
        // Get the minimum number of bits necessary to hold this value.
        int n = val.bitLength();

        // Calculate the double-precision fraction of this number; as if the
        // binary point was left of the most significant '1' bit.
        // (Get the most significant 53 bits and divide by 2^53)
        long mask = 1L << 52; // mantissa is 53 bits (including hidden bit)
        long mantissa = 0;
        int j = 0;
        for (int i = 1; i < 54; i++)
        {
            j = n - i;
            if (j < 0) break;

            if (val.testBit(j)) mantissa |= mask;
            mask >>>= 1;
        }
        // Round up if next bit is 1.
        if (j > 0 && val.testBit(j - 1)) mantissa++;

        double f = mantissa / (double)(1L << 52);

        // Add the logarithm to the number of bits, and subtract 1 because the
        // number of bits is always higher than necessary for a number
        // (ie. log2(val)<n for every val).
        return (n - 1 + Math.log(f) * 1.44269504088896340735992468100189213742664595415298D);
        // Magic number converts from base e to base 2 before adding. For other
        // bases, correct the result, NOT this number!
    }
    public static void runAttemptTests(int N, int k, int iterations) {
        int max = 0;
        System.out.printf("Testing on %d total keys with %d keys correct.\n", N, k);
        for (int i = 0; i < iterations; i ++) {
            int[] bitmap = generate(N, k);
            int attemptsTried = testAttempt(bitmap);

            if (max < attemptsTried) max = attemptsTried;
        }

        System.out.println("Max attempts: " + max);
    }

    public static void runCostTests(int N, int k, int iterations) {
        int max = 0;
        System.out.printf("Testing on %d total keys with %d keys correct.\n", N, k);

        for (int i = 0; i < iterations; i ++) {
            int cost = testCost(generate(N, k));

            if (max < cost) max = cost;
        }

        System.out.println("Max cost: " + max);
    }

    public static int testAttempt(int[] bitmap) {
        RunAttempt attemptRunner = new RunAttempt();
        attemptRunner.setTestCase(bitmap);

        IFindKeys minimum = new FindKeysMinimumAttempts();
        return attemptRunner.findAttempts(minimum);
    }

    public static int testCost(int[] bitmap) {
        RunAttempt attemptRunner = new RunAttempt();
        attemptRunner.setTestCase(bitmap);

        IFindKeys minimum = new FindKeysLowestCost();
        return attemptRunner.findCost(minimum);
    }

    static Random rng = new Random(113);

    private static int[] generate(int N, int k) {
        int[] array = new int[N];
        for (int i = 0; i < k; i++) {
            int pos;
            do {
                pos = rng.nextInt(N);
            } while (array[pos] == 1);

            array[pos] = 1;
        }
        return array;
    }

}
