import java.util.Arrays;
import java.util.Random;

public class IterativeMergeSort {
    public static void main(String[] args) {
        Random rng = new Random(1);
        int[] arr = new int[1024];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rng.nextInt(1024);
        }
        mergeSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void mergeSort(int[] arr) {
        int level = 2;

        while (true) {
            for (int i = 0; i < arr.length; i += level) {
                merge(arr, i, i + level / 2 - 1, i + level / 2, i + level - 1);
            }
            if (level > arr.length) break;
            level = level << 1;
        }

    }

    public static void merge(int[] arr, int left1, int right1, int left2, int right2) {
        if (left1 >= arr.length) return;
        int[] temp = new int[Math.min(right2, arr.length - 1) - left1 + 1];
        int pointer = 0;
        int pointer1 = left1;
        int pointer2 = left2;

        while (pointer1 <= right1 || pointer2 <= right2) {
            if (pointer2 >= arr.length && pointer1 >= arr.length) {
                break;
            } else if (pointer2 >= arr.length && pointer1 <= right1) {
                temp[pointer] = arr[pointer1];
                pointer++;
                pointer1++;
            } else if (pointer2 >= arr.length && pointer2 <= right2) {
                break;
            } else if (pointer1 <= right1 && (pointer2 > right2 || arr[pointer1] <= arr[pointer2])) {
                // we choose the left one
                temp[pointer] = arr[pointer1];
                pointer++;
                pointer1++;
            } else {
                // we choose the right one
                temp[pointer] = arr[pointer2];
                pointer++;
                pointer2++;
            }
        }

        System.arraycopy(temp, 0, arr, left1, temp.length);
    }
}
