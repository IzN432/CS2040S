import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class BellmanFord {
    // DO NOT MODIFY THE TWO STATIC VARIABLES BELOW
    public static int INF = 20000000;
    public static int NEGINF = -20000000;

    // TODO: add additional attributes and/or variables needed here, if any
    ArrayList<ArrayList<IntPair>> adjList;

    int[] shortestDistances;

    int vertexCount;

    public BellmanFord(ArrayList<ArrayList<IntPair>> adjList) {
        // TODO: initialize your attributes here, if any
        this.adjList = adjList;
        vertexCount = adjList.size();
        shortestDistances = new int[vertexCount];
        Arrays.fill(shortestDistances, INF);
    }

    // TODO: add additional methods here, if any
    private boolean toUpdate(int source, int edge, int dest) {
        if (source == INF || source + edge >= dest) {
            return false;
        } else {
            return true;
        }
    }

    private int calculateMin(int source, int edge, int dest) {
        return toUpdate(source, edge, dest) ? source + edge : dest;
    }

    private int calculateNegativeCycle(int source, int edge, int dest) {
        return toUpdate(source, edge, dest) ? NEGINF : dest;
    }
    public void computeShortestPaths(int source) {
        shortestDistances[source] = 0;
        // relax |V| - 1 times
        for (int iteration = 0; iteration < vertexCount - 1; iteration++) {
            // iterate through every edge and relax accordingly
            for (int current = source, i = 0; i < vertexCount; current = (current + 1) % vertexCount, i++) {
                for (IntPair edge : adjList.get(current)) {
                    int weight = edge.second;
                    int destination = edge.first;

                    shortestDistances[destination] = calculateMin(shortestDistances[current],
                            weight, shortestDistances[destination]);
                }
            }
        }

        for (int iteration = 0; iteration < vertexCount - 1; iteration++) {
            // find negative cycle by checking for updates
            for (int current = source, i = 0; i < vertexCount; current = (current + 1) % vertexCount, i++) {
                for (IntPair edge : adjList.get(current)) {
                    int weight = edge.second;
                    int destination = edge.first;

                    shortestDistances[destination] = calculateNegativeCycle(shortestDistances[current],
                            weight, shortestDistances[destination]);
                }
            }
        }


    }

    public int getDistance(int node) { 
        // TODO: implement your getDistance operation here
        return shortestDistances[node];
    }

    public static void main(String[] args) {
        IntPair[][] adjArr = new IntPair[][] {
                new IntPair[] {new IntPair(1, 4), new IntPair(2, 5)}, // 0
                new IntPair[] {new IntPair(2, 5)}, // 1
                new IntPair[] {new IntPair(3, 3)}, // 2
                new IntPair[] {new IntPair(1, -5)}, // 3
        };

        ArrayList<ArrayList<IntPair>> adjList = new ArrayList<>();
        for (IntPair[] arr : adjArr) {
            ArrayList<IntPair> list = new ArrayList<>(Arrays.asList(arr));
            adjList.add(list);
        }

        BellmanFord bf = new BellmanFord(adjList);
        bf.computeShortestPaths(0);
        bf.getDistance(1);
    }
}
