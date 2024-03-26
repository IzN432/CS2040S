import java.util.ArrayList;
import java.util.List;

public class Solution {
    public List<Integer> findDuplicates(int[] nums) {
        List<Integer> out = new ArrayList<>();

        for (int i = 0; i < nums.length; i++) {
            int position = nums[i] - 1;

            // this element is already in the correct spot
            if (position == i) continue;

            if (nums[position] == nums[i]) {
                // this element is not in the correct spot but there is a duplicate in the correct spot
                out.add(nums[i]);
            } else {
                // i am not in the correct spot, swap me into the correct spot
                int temp = nums[position];
                nums[position] = nums[i];
                nums[i] = temp;
                i--;
            }
        }

        return out;
    }

    public static void main(String[] args) {
        Solution sol = new Solution();
        List<Integer> output = sol.findDuplicates(new int[] {1, 1, 2, 3, 4, 4});
    }
}