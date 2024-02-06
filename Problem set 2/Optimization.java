/**
 * The Optimization class contains a static routine to find the maximum in an array that changes direction at most once.
 */
public class Optimization {

    /**
     * A set of test cases.
     */
    static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, -100, -80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83}
    };

    /**
     * Returns the maximum item in the specified array of integers which changes direction at most once.
     *
     * @param dataArray an array of integers which changes direction at most once.
     * @return the maximum item in data Array
     */
    public static int searchMax(int[] dataArray) {

        if (dataArray.length == 0) {
            // Return 0 if the array is empty

            return 0;
        } else if (dataArray.length == 1) {
            // If the array has only 1 element, it is by default the max element

            return dataArray[0];
        }

        // If the second element is greater than the first, the array has maxima at its extreme ends
        if (dataArray[0] > dataArray[1]) {
            return Math.max(dataArray[0], dataArray[dataArray.length - 1]);
        }

        // Else, the max is somewhere in the array and can be searched for with binary search
        int low = 0, high = dataArray.length - 1;

        while (low != high) {
            int mid = (high + low) / 2;
            if (dataArray[mid + 1] < dataArray[mid]) {
                // decreasing, the max is to the left
                high = mid;
            } else {
                // increasing, the max is to the right
                low = mid + 1;
            }
        }

        return dataArray[low];
    }

    /**
     * A routine to test the searchMax routine.
     */
    public static void main(String[] args) {
        for (int[] testCase : testCases) {
            System.out.println(searchMax(testCase));
        }
    }
}
