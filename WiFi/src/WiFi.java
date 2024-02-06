import java.util.Arrays;
class WiFi {

    /**
     * Implement your solution here
     */
    public static double computeDistance(int[] houses, int numOfAccessPoints) {
        double lowest = 0;
        double highest = 0;
        for (int house : houses) {
            if (house > highest) {
                highest = house;
            }
            if (house < lowest) {
                lowest = house;
            }
        }

        double low = 0;
        double high = highest - lowest;

        double mid = 0.0;
        while (high - low >= 0.05) {
            mid = (high + low) / 2.0;
            if (WiFi.coverable(houses, numOfAccessPoints, mid)) {
                // min distance is below
                high = mid - 0.25;
            } else {
                // min distance is above
                low = mid + 0.25;
            }
        }

        return mid;
    }

    /**
     * Implement your solution here
     */
    public static boolean coverable(int[] houses, int numOfAccessPoints, double distance) {
        int[] sortedHouses = houses.clone();
        Arrays.sort(sortedHouses);

        double covered = 0;
        for (int i = 0; i < sortedHouses.length; i ++) {
            if (sortedHouses[i] <= covered) continue;

            covered = sortedHouses[i] + 2 * distance;
            numOfAccessPoints --;
        }
        return numOfAccessPoints >= 0;
    }
}
