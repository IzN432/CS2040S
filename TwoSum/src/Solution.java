import java.util.HashMap;
import java.util.Map;

public class Solution {
    public static int solve(int[] arr, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int j : arr) {
            int count = map.getOrDefault(j, 0);
            map.put(j, count + 1);
        }

        int count = 0;
        for (int key : map.keySet()) {
            if (key * 2 == target) {
                count += map.get(key) / 2;
            } else {
                count += Math.min(map.getOrDefault(target - key, 0), map.get(key));
                map.put(key, 0);
            }
        }

        return count;
    }

    public static void main(String[] args) {
        solve(new int[] {1, 1, 3, 20}, 4);
    }

}
