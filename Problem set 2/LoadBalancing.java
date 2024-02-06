/**
 * Contains static routines for solving the problem of balancing m jobs on p processors
 * with the constraint that each processor can only perform consecutive jobs.
 */
public class LoadBalancing {

    /**
     * Checks if it is possible to assign the specified jobs to the specified number of processors such that no
     * processor's load is higher than the specified query load.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param queryLoad the maximum load allowed for any processor
     * @param p the number of processors
     * @return true iff it is possible to assign the jobs to p processors so that no processor has more than queryLoad load.
     */
    public static boolean isFeasibleLoad(int[] jobSizes, int queryLoad, int p) {
        // Check for invalid inputs. In this case, if queryLoad is less than or equal to 0, p is less than or equal to 0,
        // uninitialized or empty jobSizes
        if (queryLoad <= 0 || p <= 0 || jobSizes == null || jobSizes.length == 0) {
            return false;
        }



        int current_processor_load = 0;
        int i = 0;

        while (i < jobSizes.length) {
            // check for impossible task
            if (jobSizes[i] > queryLoad) {
                return false;
            }

            // add the processor load
            current_processor_load += jobSizes[i];

            // check current processor has gone over query load
            if (current_processor_load > queryLoad) {
                // subtract a processor and reset the load
                p --;
                current_processor_load = 0;

                if (p <= 0) {
                    // ran out of processors
                    return false;
                }

                // restarts loop on same i to continue with the next processor
            } else {
                i ++;
            }
        }

        return true;
    }

    /**
     * Returns the minimum achievable load given the specified jobs and number of processors.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param p the number of processors
     * @return the maximum load for a job assignment that minimizes the maximum load
     */
    public static int findLoad(int[] jobSizes, int p) {
        // Check for invalid inputs. In this case, check for negative p, uninitialized jobSizes or empty jobSizes
        if (p <= 0 || jobSizes == null || jobSizes.length == 0) {
            return -1;
        }

        // Set the high to be the largest jobSize
        int low = 0, high = 0;
        for (int jobSize : jobSizes) {
            high += jobSize;
        }

        // Perform binary search on the feasible load
        while (low != high) {
            int mid = (low + high) / 2;
            if (isFeasibleLoad(jobSizes, mid, p)) {
                // Since it is feasible at this load, it may be possible to go with a smaller load.
                high = mid;
            } else {
                // Since it is not feasible at this load, need to increase the minimum load.
                low = mid + 1;
            }
        }

        return low;
    }

    // These are some arbitrary testcases.
    public static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, 100, 80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83},
            {7},
            {0}
    };

    /**
     * Some simple tests for the findLoad routine.
     */
    public static void main(String[] args) {
        for (int p = 1; p < 30; p++) {
            System.out.println("Processors: " + p);
            for (int[] testCase : testCases) {
                System.out.println(findLoad(testCase, p));
            }
        }
    }
}
