import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

public class GetKLargest {

    private final int k;

    private final PriorityQueue<Integer> queue;

    public GetKLargest(int k) {
        this.k = k;
        this.queue = new PriorityQueue<>();
    }

    public void insert(int i) {
        queue.add(i);
        if (queue.size() > k) {
            queue.remove();
        }
    }

    public int[] getKLargest() {
        Integer[] arr = new Integer[k];
        queue.toArray(arr);

        int[] res = new int[k];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }

        return res;
    }

    public void loadArray(int[] values) {
        for (int value : values) {
            insert(value);
        }
    }

    public static void main(String[] args) {
        GetKLargest getKLargest = new GetKLargest(5);
        getKLargest.loadArray(new int[] {10, 100, 5, 7, 200, 4, 7, 11, 110});

        boolean success = Tester.test(
            x -> {
                GetKLargest g = new GetKLargest(10);
                g.loadArray(x);
                return g.getKLargest();
            },
            (x, y) -> {
                int[] arr = y.clone();
                Arrays.sort(arr);


                int[] sol = x.clone();
                if (sol.length != 10) return false;

                Arrays.sort(sol);
                for (int i = 0; i < 10; i++) {
                    if (sol[9 - i] != arr[arr.length - i - 1]) {
                        System.out.println(Arrays.toString(arr));
                        System.out.println(Arrays.toString(sol));
                        System.out.println(i);
                        return false;
                    }
                }

                return true;
            },
            x -> {
                int[] arr = new int[11];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = (new Random()).nextInt(100) - 50;
                }
                return arr;
            },
            Arrays::toString,
            Arrays::toString,10
        );
        System.out.println(success);
    }
}
