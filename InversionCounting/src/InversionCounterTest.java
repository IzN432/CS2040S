import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class InversionCounterTest {

    static Random rng = new Random(1);

    static long countSwaps(int[] arr) {
        long swaps = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                if (arr[j] < arr[i]) swaps ++;
            }
        }
        return swaps;
    }

    @Test
    public void countSwapsTest1() {
        for (int k = 0; k < 1000000; k++) {
            // create a random array of random length
            int[] arr = new int[rng.nextInt(4)];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = rng.nextInt(4);
            }
            System.out.println(Arrays.toString(arr));
            assertEquals(countSwaps(arr), InversionCounter.countSwaps(arr));
        }
    }

    @Test
    public void countSwapsTest2() {
        int[] arr = {2, 3, 4, 1};
        assertEquals(3L, InversionCounter.countSwaps(arr));
    }

    @Test
    public void countSwapsTest3() {
        int[] arr = {1, 2, 3, 4, 5};
        assertEquals(0L, InversionCounter.countSwaps(arr));
    }

    @Test
    public void countSwapsTest4() {
        int[] arr = {1, 1, 1, 1, 1};
        assertEquals(0L, InversionCounter.countSwaps(arr));
    }

    @Test
    public void mergeAndCountTest1() {
        int[] arr = {3, 1, 2};
        assertEquals(2L, InversionCounter.mergeAndCount(arr, 0, 0, 1, 2));
    }

    @Test
    public void mergeAndCountTest2() {
        int[] arr = {2, 3, 4, 1};
        assertEquals(3L, InversionCounter.mergeAndCount(arr, 0, 2, 3, 3));
    }

    @Test
    public void mergeAndCountTest3() {
        int[] arr = {9, 0, 1, 2, 11};
        assertEquals(3L, InversionCounter.mergeAndCount(arr, 0, 0, 1, 4));
    }
}