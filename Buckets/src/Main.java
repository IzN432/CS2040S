
public class Main {
    int n;
    final static int k = 17;

    public Main(int n) {
        this.n = n;
    }

    public int solve() {
        if (n / 2 <= k) {
            return n;
        }

        int smallest = Integer.MAX_VALUE;
        for (int buckets = k; buckets < k + 3 && buckets < n / 2; buckets++) {
            int count = n / buckets;
            Main m = new Main(n - (buckets - k));

            smallest = Math.min(m.solve(), smallest);
        }

        return smallest;
    }

    public static void main(String[] args) {
        Main m = new Main(1024);
        System.out.println(m.solve());
    }
}