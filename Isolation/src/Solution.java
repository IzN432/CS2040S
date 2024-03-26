import java.util.HashMap;
import java.util.Map;

public class Solution {
    // TODO: Implement your solution here
    public static int solve(int[] arr) {
        int maxLength = 1;

        Map<Integer, Boolean> map = new HashMap<>();
        int p1 = 0;
        int p2 = 1; // points to the next element to be added
        map.put(arr[p1], true);

        while (p2 < arr.length) {
            maxLength = Math.max(p2 - p1, maxLength);

            // remove the left side until no more duplicate
            while (map.getOrDefault(arr[p2], false)) {
                // while the thing p2 is pointing to is already inside the map
                map.put(arr[p1], false);
                p1++;
            }

            map.put(arr[p2], true);
            p2++;
        }

        return maxLength;
    }

    public static void main(String[] args) {
        solve(new int[] {4, 5, 2, 4});
    }
}