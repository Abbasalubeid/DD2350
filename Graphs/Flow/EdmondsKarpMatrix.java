import java.util.*;

public class EdmondsKarpMatrix {

    private static final int INF = Integer.MAX_VALUE;

    private int[][] capacities;
    private int[][] residualCapacity;
    private int V;

    public EdmondsKarpMatrix(int V) {
        this.V = V + 1;
        capacities = new int[this.V][this.V];
        residualCapacity = new int[this.V][this.V];
    }

    public void addEdge(int from, int to, int capacity) {
        capacities[from][to] = capacity;
    }

    public int execute(int source, int sink) {
        int maxFlow = 0;

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                residualCapacity[i][j] = capacities[i][j];
            }
        }

        Map<Integer, Integer> parent = new HashMap<>();

        while (BFS(residualCapacity, parent, source, sink)) {
            int pathFlow = INF;
            int s = sink;
            while (s != source) {
                int residual = residualCapacity[parent.get(s)][s];
                pathFlow = Math.min(pathFlow, residual);
                s = parent.get(s);
            }

            maxFlow += pathFlow;

            int v = sink;
            while (v != source) {
                int u = parent.get(v);
                residualCapacity[u][v] -= pathFlow;
                residualCapacity[v][u] += pathFlow;
                v = u;
            }
        }

        return maxFlow;
    }

    private boolean BFS(int[][] residualCapacity, Map<Integer, Integer> parent, int source, int sink) {
        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v = 0; v < V; v++) {
                if (!visited[v] && residualCapacity[u][v] > 0) {
                    parent.put(v, u);
                    visited[v] = true;
                    queue.add(v);
                    if (v == sink) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public void printResults(Kattio io, int source, int sink, int maxFlow) {
        io.println(V - 1);
        io.println(source + " " + sink + " " + maxFlow);

        int count = 0;
        for (int u = 1; u < V; u++) {
            for (int v = 1; v < V; v++) {
                if (capacities[u][v] > 0 && capacities[u][v] - residualCapacity[u][v] > 0) {
                    count++;
                }
            }
        }
        io.println(count);

        for (int u = 1; u < V; u++) {
            for (int v = 1; v < V; v++) {
                if (capacities[u][v] > 0 && capacities[u][v] - residualCapacity[u][v] > 0) {
                    io.println(u + " " + v + " " + (capacities[u][v] - residualCapacity[u][v]));
                }
            }
        }
    }

    public static void main(String[] args) {
        Kattio io = new Kattio(System.in, System.out);
        int V = io.getInt();
        EdmondsKarpMatrix ek = new EdmondsKarpMatrix(V);
        int source = io.getInt();
        int sink = io.getInt();
        int E = io.getInt();
        for (int i = 0; i < E; i++) {
            int from = io.getInt();
            int to = io.getInt();
            int capacity = io.getInt();
            ek.addEdge(from, to, capacity);
        }

        int maxFlow = ek.execute(source, sink);
        ek.printResults(io, source, sink, maxFlow);
        io.flush();
    }
}