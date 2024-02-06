import java.util.*;

public class FindKeysLowestCost implements IFindKeys {

    int N;
    int k;
    ITreasureExtractor treasureExtractor;

    @Override
    public int[] findKeys(int N, int k, ITreasureExtractor treasureExtractor) {
        this.N = N;
        this.k = k;
        this.treasureExtractor = treasureExtractor;
        memo = new IntLongPair[N + 1];

        CustomList positions = new CustomList(N);
        for (int i = 0; i < N; i++) {
            positions.add(i);
        }

        Pair[] pairs = new Pair[N];
        int pointer = 0;

        int numSuccess = 0;

        while (positions.getCount() > k) {
            // split into k + 1 buckets
            int numberOfBuckets = solveForOptimalNumberOfBuckets(positions.getCount());
            int bucketSize = positions.getCount() / numberOfBuckets;
            int remainder = positions.getCount() % numberOfBuckets;

            int startPosition = 0;
            int endPosition = bucketSize - 1;

            int remainingBuckets = numberOfBuckets;

            while (endPosition < positions.getCount()) {
                if (remainder > 0) {
                    endPosition++;
                    remainder--;
                }


                if (numSuccess >= k) {
                    pairs[pointer] = new Pair(startPosition, endPosition);
                    pointer++;

                    startPosition = endPosition + 1;
                    endPosition = startPosition + bucketSize - 1;
                    remainingBuckets--;

                    continue;
                } else if (remainingBuckets == 1) {
                    break;
                }

                int[] range = positions.getRange(startPosition, endPosition);

                Pair[] toRemove = new Pair[pointer];
                System.arraycopy(pairs, 0, toRemove, 0, pointer);

                boolean hasKeys = hasAtLeastOneKey(range, positions, toRemove);

                if (!hasKeys) {
                    pairs[pointer] = new Pair(startPosition, endPosition);
                    pointer++;
                } else {
                    numSuccess++;
                }

                startPosition = endPosition + 1;
                endPosition = startPosition + bucketSize - 1;
                remainingBuckets--;
            }

            Pair[] toRemove = new Pair[pointer];
            System.arraycopy(pairs, 0, toRemove, 0, pointer);
            positions.remove(toRemove);

            pairs = new Pair[N];
            pointer = 0;
            numSuccess = 0;
        }

        int[] bitmap = new int[N];
        for (int i : positions.getRange(0, k - 1)) {
            bitmap[i] = 1;
        }
        return bitmap;
    }

    private int attempts = 0;
    private boolean hasAtLeastOneKey(int[] positions, CustomList availablePositions, Pair[] toBeRemoved) {
        attempts++;
        int[] bitmap = new int[N];
        for (int i : availablePositions.getValues()) {
            bitmap[i] = 1;
        }

        for (Pair pair : toBeRemoved) {
            int[] removed = availablePositions.getRange(pair.firstValue, pair.secondValue);
            for (int i : removed) {
                bitmap[i] = 0;
            }
        }


        for (int position : positions) {
            bitmap[position] = 0;
        }

        int available = 0;
        for (int i = 0; i < bitmap.length; i++) {
            if (bitmap[i] == 1) {
                available++;
            }
        }

        if (available < k) {
            return true;
        }

        return !treasureExtractor.tryUnlockChest(bitmap);
    }

    private IntLongPair[] memo;
    private int solveForOptimalNumberOfBuckets(int currentN) {
        return costForGroupSize(currentN).small;
    }

    private IntLongPair costForGroupSize(int currentN) {
        if (currentN <= k) {
            return new IntLongPair(currentN, 0);
        }
        if (memo[currentN] != null) {
            return memo[currentN];
        } else {
            IntLongPair minPair = new IntLongPair(0, Long.MAX_VALUE);
            for (int numberOfBuckets = k + 1; numberOfBuckets <= currentN; numberOfBuckets++) {
                IntLongPair singleRoundCost = oneRoundCost(numberOfBuckets, currentN);
                long resultingCost = singleRoundCost.big + costForGroupSize(singleRoundCost.small).big;

                if (resultingCost < minPair.big && resultingCost > 0) {
                    minPair.big = resultingCost;
                    minPair.small = numberOfBuckets;
                }
            }
            memo[currentN] = minPair;
            return minPair;
        }
    }

    private IntLongPair oneRoundCost(int numberOfBuckets, int currentN) {

        int bucketSize = currentN / numberOfBuckets;
        int remainder = currentN % numberOfBuckets;

        // there are numberOfBuckets buckets, remainder of which are one bigger. Note that we will never check
        // the last bucket for key presence since it automatically progresses to the next round or does not,
        // depending on whether k buckets with keys were already discovered.

        // This means that the worst case would be if every bigger bucket
        // and every smaller bucket were checked, except for the last bucket.

        int numberOfBigGroupsDone = 0;
        long worstCaseCost = 0;

        for (int i = 0; i < numberOfBuckets - 1; i++) {
            if (numberOfBigGroupsDone < remainder) {
                numberOfBigGroupsDone++;
                worstCaseCost += currentN - bucketSize - 1;
            } else {
                worstCaseCost += currentN - bucketSize;
            }
        }

        // The worst case number of keys leftover would be if we take as many big groups as possible up to
        // k - 1 then take one small group. This is to ensure we search numberOfBuckets - 1 times to maximise
        // the cost of searching

        int keysLeftover = 0;

        // take as many big groups as possible
        int numberOfBigGroupsTaken = 0;
        for (int i = 0; i < k; i++) {
            if (numberOfBigGroupsTaken < remainder) {
                // take a big group
                keysLeftover += bucketSize + 1;
                numberOfBigGroupsTaken++;
            } else {
                // take a small group
                keysLeftover += bucketSize;
            }
        }

        // since the worst case needed us to check everything, we must not find k groups containing keys
        // until either the last or second last group. To ensure worst case, we take the second last which
        // has to be bigger than or equals to the last one

        if (remainder > k && remainder != numberOfBuckets - 1) {
            keysLeftover--;
        }

        // we return the number of keys leftover and the cost of this round
        return new IntLongPair(keysLeftover, worstCaseCost);
    }
}

class IntLongPair {
    int small;
    long big;

    public IntLongPair(int small, long big) {
        this.small = small;
        this.big = big;
    }

    @Override
    public String toString() {
        return String.format("%d - %d", small, big);
    }
}
class CustomList {
    final int[] values;
    int pointer = 0;

    public CustomList(int size) {
        values = new int[size];
        Arrays.fill(values, -1);
    }

    public void add(int value) {
        values[pointer] = value;
        pointer++;
    }

    public int[] getValues() {
        return this.getRange(0, pointer - 1);
    }

    public void remove(Pair[] pairs) {
        int earliestRemove = values.length;
        for (Pair pair : pairs) {
            for (int i = pair.firstValue; i <= pair.secondValue; i++) {
                values[i] = -1;
            }
            earliestRemove = Math.min(pair.firstValue, earliestRemove);
        }

        for (int i = earliestRemove; i < values.length; i++) {
            if (values[i] != -1) {
                for (int j = i; j >= 0; j--) {
                    if (j == 0 || values[j - 1] != -1) {
                        values[j] = values[i];
                        values[i] = -1;
                        break;
                    }
                }
            }
        }

        pointer = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] != -1) {
                pointer = i + 1;
            }
        }
    }

    public int[] getRange(int start, int end) {
        int[] range = new int[end - start + 1];
        System.arraycopy(values, start, range, 0, end + 1 - start);
        return range;
    }

    public int getCount() {
        return pointer;
    }

    @Override
    public String toString() {
        int[] output = new int[pointer];
        System.arraycopy(values, 0, output, 0, pointer);
        return Arrays.toString(output);
    }
}

class Pair {
    int firstValue;
    int secondValue;

    public Pair(int firstValue, int secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    @Override
    public String toString() {
        return String.format("%d - %d", firstValue, secondValue);
    }
}