import java.math.BigInteger;
import java.util.Arrays;

public class FindKeysMinimumAttempts implements IFindKeys {
    int N;
    int k;
    ITreasureExtractor treasureExtractor;
    BigInteger currentSolutionSetSize;
    @Override
    public int[] findKeys(int N, int k, ITreasureExtractor treasureExtractor) {
        // The strategy I shall be employing is the - I know 122 is log(NCk) so lets just force the solution set
        // in half every time we ask a question strategy

        // Initialize my local fields
        this.N = N;
        this.k = k;
        this.treasureExtractor = treasureExtractor;

        // Custom Array to store the key positions I have found
        CustomArray keys = new CustomArray(k);

        // This is the size of the current solution set (well approximately)
        currentSolutionSetSize = choose(N, k);

        // current low
        int cl = 0;
        // current high
        int ch = N - 1;

        // we will escape the loop once we find all the keys
        while (keys.getCount() < k) {

            while (cl != ch) {
                // first, we want to find the midpoint in terms of number of solutions that fit into there
                // for example, we have m choose k ways for k keys to fit into the first m elements.
                int low = cl;
                // set high to ch - 1 else no new information if it reaches ch
                int high = ch - 1;

                int mid;

                BigInteger temp;

                // binary search for the lowest number where it is more than half
                while (low != high) {
                    mid = (low + high) / 2;
                    temp = choose(mid, k - keys.getCount()).subtract(choose(cl - 1, k - keys.getCount()))
                            .subtract(currentSolutionSetSize).abs();
                    if (compareDiffSmallerThanHalf(currentSolutionSetSize, temp)) {
                        high = mid;
                    } else {
                        low = mid + 1;
                    }
                }

                // so we found the breakpoint where mid choose k is greater than half of ch choose k
                mid = low;

//                if (mid < ch - 1) {
//                    BigInteger possible1 = choose(mid, k - keys.getCount()).subtract(choose(cl - 1, k - keys.getCount()))
//                            .subtract(current).abs().subtract(current.divide(BigInteger.valueOf(2)));
//                    BigInteger possible2 = choose(mid + 1, k - keys.getCount()).subtract(choose(cl - 1, k - keys.getCount()))
//                            .subtract(current).abs().subtract(current.divide(BigInteger.valueOf(2)));
//
//                    if (possible1.compareTo(possible2) > 0) {
//                        mid = mid + 1;
//                    }
//                }

                // shrink the current solution size
                currentSolutionSetSize = choose(mid, k - keys.getCount()).subtract(choose(cl - 1, k - keys.getCount()))
                       .subtract(currentSolutionSetSize).abs();

                // currentSolutionSetSize = currentSolutionSetSize.divide(BigInteger.valueOf(2));

                // check if the first mid-elements + any found keys has all the keys
                boolean containsAllKeys = hasAllKeys(keys.getValues(), 0, mid);

                if (containsAllKeys) {
                    // check for special case where there is only number of key left, so we have found all the keys
                    if (mid + 1 == k - keys.getCount()) {
                        for (int i = 0; i <= mid; i++) {
                            keys.add(i);
                        }
                        break;
                    }

                    // recurse onto the left side
                    ch = mid;

                    // since all the keys are contained here, the current solution set is now just however many
                    // ways we can fit the key into 0 to mid minus the base number.

                    // currentSolutionSetSize = choose(mid, k - keys.getCount())
                    //         .subtract(choose(cl - 1, k - keys.getCount()));
                } else {
                    // recurse onto the right side
                    cl = mid + 1;

                    // since the keys are not all inside the first m elements, the solution set is basically
                    // the complement of if it were in the first m elements. well, theoretically anyway
                    // for some reason this does not achieve as good of an attempt count as the above way of
                    // setting the current solution size. This is a problem for another day.

                    // currentSolutionSetSize = currentSolutionSetSize.subtract(choose(mid, k - keys.getCount())
                    //         .subtract(choose(cl - 1, k - keys.getCount())));

                }
            }

            // we already have all the keys
            if (keys.getCount() == k) break;

            // we found a key!
            keys.add(ch);
            cl = 0;
            ch = ch - 1;
            currentSolutionSetSize = choose(ch, k - keys.getCount());
        }

        int[] bitmap = new int[N];
        for (int i : keys.getValues()) {
            bitmap[i] = 1;
        }
        return bitmap;
    }

    public static boolean compareDiffSmallerThanHalf(BigInteger current, BigInteger compare) {
        // make sure the diff is smaller than half of available solutions
        return compare.compareTo(current.divide(BigInteger.valueOf(2))) <= 0;
    }
    public static BigInteger choose(int N, int k) {
        if (N < k || k == 0) return BigInteger.valueOf(0);

        BigInteger result = BigInteger.valueOf(1);
        for (int i = 0; i < k; i++) {
            result = result.multiply(BigInteger.valueOf(N - i));
        }

        for (int i = 1; i <= k; i++) {
            result = result.divide(BigInteger.valueOf(i));
        }

        return result;
    }

    int attempts = 0;
    public boolean hasAllKeys(int[] foundKeys, int start, int end) {
        if (end - start + 1 + foundKeys.length < k) return false;
        attempts++;
        int[] bitmap = new int[N];
        for (int i = 0; i < N; i++) {
            if (start <= i && i <= end) {
                bitmap[i] = 1;
            }
        }
        for (int foundKey : foundKeys) {
            bitmap[foundKey] = 1;
        }

        return treasureExtractor.tryUnlockChest(bitmap);
    }

}

class CustomArray {
    private final int[] values;
    private int pointer;

    public CustomArray(int size) {
        values = new int[size];
    }

    public void add(int value) {
        values[pointer] = value;
        pointer++;
    }

    public int[] getValues() {
        return Arrays.copyOf(values, pointer);
    }

    public int getCount() {
        return pointer;
    }
}