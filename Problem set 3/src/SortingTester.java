import java.util.Random;

public class SortingTester {
    private static final Random rng = new Random(1);

    public static void main(String[] args) {
        ISort[] sorters = new ISort[] {new SorterA(), new SorterB(), new SorterC(), new SorterD(), new SorterE(), new SorterF()};

        /*
         * SORTER A: Merge sort
         * SORTER B: Dr Evil
         * SORTER C: Bubble sort
         * SORTER D: Selection sort
         * SORTER E: Quick sort
         * SORTER F: Insertion sort
         */

        System.out.println("====== STABLE CHECK ======");
        // Sorter B, Sorter D and Sorter E are not stable. One is selection sort, one is quick sort, one is Dr Evil
        for (int i = 0; i < 6; i++) {
            if (!isStable(sorters[i], 100)) {
                System.out.printf("%c failed stability check!\n", indexToLetter(i));
            } else {
                System.out.printf("%c succeeded stability check!\n", indexToLetter(i));
            }
        }

        System.out.println("\n====== CORRECTNESS CHECK ======");
        // Sorter B fails at size 10000 arrays, so it is Dr Evil
        for (int i : new int[] {1, 3, 4}) {
            if (!checkSort(sorters[i], 10000)) {
                System.out.println(indexToLetter(i) + " failed to sort!");
                // there's only one that will fail to sort, so we can break!
                break;
            }
        }

        System.out.println("\n===== INSERTION SORT and BUBBLE SORT ======");
        // Sorter C and Sorter F take very little time to sort a sorted array compared to a random array
        // so, they are insertion and bubble sort, but we do not know which is which yet. This does leave
        // us with Sorter A as merge sort though.
        System.out.println("The following percentages are the percentage of time it takes \nto sort a sorted array as compared to a random array:");
        for (int i : new int[] {0, 2, 5}) {
            System.out.printf("%c: %f%%\n", indexToLetter(i), compareSortedRuntime(sorters[i], 100));
        }

        System.out.println("\n===== INSERTION SORT vs BUBBLE SORT ======");
        // For a sorted array with the first element in the last position, bubble sort has theta N^2 time while
        // insertion sort has theta N time. Since Sorter C took around the same time as the random array,
        // Sorter C is bubble sort.
        for (int i : new int[] {2, 5}) {
            System.out.printf("%c: %f%%\n", indexToLetter(i), compareLeftShiftedRuntime(sorters[i], 100));
        }

        System.out.println("\n====== TIME COMPLEXITY (N LOG(N)) ======");
        // Sorter D runtime grows at a rate higher than N Log N, so it is selection sort
        for (int i : new int[] {3, 4}) {
            System.out.printf("%c: %f%%\n", indexToLetter(i), checkTimeComplexityNLogN(sorters[i], 1000));
        }

        // This leaves us with Sorter E as quick sort.
    }

    public static char indexToLetter(int i) {
        return (char) (65 + i);
    }
    public static KeyValuePair[] createRandomArray(int size) {
        if (size == 1) return new KeyValuePair[] {new KeyValuePair(0, 0)};

        KeyValuePair[] arr = new KeyValuePair[size];
        for (int i = 0; i < size; i++) {
            // use size - 1 to ensure there will be some non-unique numbers
            arr[i] = new KeyValuePair(rng.nextInt(size - 1), i);
        }
        return arr;
    }
    public static KeyValuePair[] createSortedArray(int size) {
        KeyValuePair[] arr = new KeyValuePair[size];
        for (int i = 0; i < size; i++) {
            arr[i] = new KeyValuePair(i, i);
        }
        return arr;
    }
    public static KeyValuePair[] createLeftShiftedArray(int size) {
        KeyValuePair[] arr = new KeyValuePair[size];
        for (int i = 0; i < size - 1; i++) {
            arr[i] = new KeyValuePair(i + 1, i);
        }
        arr[size - 1] = new KeyValuePair(0, size - 1);
        return arr;
    }
    public static boolean checkSort(ISort sorter, int size) {
        for (int j = 0; j < 100; j++) {
            KeyValuePair[] arr = createRandomArray(size);
            sorter.sort(arr);

            for (int i = 1; i < arr.length; i++) {
                if (arr[i - 1].compareTo(arr[i]) > 0) {
                    return false;
                }
            }
        }

        return true;
    }
    public static boolean isStable(ISort sorter, int size) {
        for (int j = 0; j < 100; j++) {
            KeyValuePair[] arr = createRandomArray(size);
            sorter.sort(arr);

            for (int i = 1; i < arr.length; i++) {
                if (arr[i].compareTo(arr[i - 1]) == 0) {
                    if (arr[i].getValue() < arr[i - 1].getValue()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
    public static double compareSortedRuntime(ISort sorter, int size) {
        // bubble sort and insertion sort have much better runtime on sorted arrays than random arrays\

        KeyValuePair[] sortedArray = createSortedArray(size);
        KeyValuePair[] randomArray = createRandomArray(size);

        long randomCost = sorter.sort(randomArray);
        long sortedCost = sorter.sort(sortedArray);

        return (double) (sortedCost) / randomCost * 100;
    }
    public static double compareLeftShiftedRuntime(ISort sorter, int size) {
        // if you move the first element to the end of a sorted list, insertion sort still does fine with an approximately 2n solution
        // but bubble sort ends up becoming n squared. so we can use this to compare the two

        KeyValuePair[] randomArray = createRandomArray(size);
        KeyValuePair[] leftShiftedArray = createLeftShiftedArray(size);

        long leftShiftedCost = sorter.sort(leftShiftedArray);
        long randomCost = sorter.sort(randomArray);

        return (double) (leftShiftedCost) / randomCost * 100;
    }
    public static double checkTimeComplexityNLogN(ISort sorter, int size) {
        KeyValuePair[] small = createRandomArray(size / 2);
        KeyValuePair[] big = createRandomArray(size);

        long smallCostAdjusted = (long) (sorter.sort(small) / ((size / 2) * Math.log((double) size / 2)));
        long bigCostAdjusted = (long) (sorter.sort(big) / (size * Math.log(size)));

        return (double) bigCostAdjusted / smallCostAdjusted * 100;
    }
}
