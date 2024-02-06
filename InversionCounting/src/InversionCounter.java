class InversionCounter {

    public static long countSwaps(int[] arr) {
        if (arr.length == 0) return 0;
        int[] copy = new int[arr.length];
        System.arraycopy(arr, 0, copy, 0, arr.length);
        return countSwapHelper(copy, 0, copy.length - 1);
    }

    private static long countSwapHelper(int[] arr, int start, int end) {
        if (start >= end) return 0;
        // split into two
        int mid = (start + end) / 2;

        // run merge sort on both halves
        long swaps = 0;
        swaps += countSwapHelper(arr, start, mid);
        swaps += countSwapHelper(arr, mid + 1, end);
        swaps += mergeAndCount(arr, start, mid, mid + 1, end);
        // merge them back
        return swaps;
    }

    /**
     * Given an input array so that arr[left1] to arr[right1] is sorted and arr[left2] to arr[right2] is sorted
     * (also left2 = right1 + 1), merges the two so that arr[left1] to arr[right2] is sorted, and returns the
     * minimum amount of adjacent swaps needed to do so.
     */
    public static long mergeAndCount(int[] arr, int left1, int right1, int left2, int right2) {
        long swaps = 0;
        int[] temp = new int[right2 - left1 + 1];
        int pointer = 0;

        int initialLeft = left1;

        // loop until we finish at least 1
        while (left1 <= right1 && left2 <= right2) {
            if (arr[left1] <= arr[left2]) {
                // my left array's value is smaller or equals, meaning no unordered pairs created
                temp[pointer] = arr[left1];
                left1++;
            } else if (arr[left1] > arr[left2]) {
                // my right array's value is smaller, meaning there are unordered pairs.
                swaps += right1 - left1 + 1;
                temp[pointer] = arr[left2];
                left2++;
            }
            pointer++;
        }

        if (left1 <= right1) {
            // meaning my left array is not finished, so just append it to the end
            for (int i = left1; i <= right1; i++) {
                temp[pointer] = arr[i];
                pointer++;
            }
        } else {
            // meaning my right array is not finished, so just append it to the end
            for (int i = left2; i <= right2; i++) {
                temp[pointer] = arr[i];
                pointer++;
            }
        }

        // now insert it back into the array
        System.arraycopy(temp, 0, arr, initialLeft, temp.length);

        return swaps;
    }
}
