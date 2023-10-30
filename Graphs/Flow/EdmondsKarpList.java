import java.util.*;

public class EdmondsKarpList {

    private static final int INF = Integer.MAX_VALUE;

    public static class Edge {
        int from, to, capacity, flow;
        Edge reverse;

        Edge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }
    }

    private List<List<Edge>> graph;
    private int V;

    public EdmondsKarpList(int V) {
        this.V = V + 1;
        graph = new ArrayList<>(this.V);
        for (int i = 0; i < this.V; i++) {
            graph.add(new ArrayList<>());
        }
    }

    public void addEdges(List<Edge> flowEdges) {
        for (Edge edge : flowEdges) {
            addEdge(edge.from, edge.to, 1);
        }
    }    

    public void addEdge(int from, int to, int capacity) {
        Edge forwardEdge = new Edge(from, to, capacity);
        Edge reverseEdge = new Edge(to, from, 0);

        forwardEdge.reverse = reverseEdge;
        reverseEdge.reverse = forwardEdge;

        graph.get(from).add(forwardEdge);
        graph.get(to).add(reverseEdge);
    }

    public int execute(int source, int sink) {
        int maxFlow = 0;

        // Map to track the path found by BFS. It maps a node to its preceding edge in the path.
        Map<Integer, Edge> parent = new HashMap<>();

        // Find augmenting paths using BFS until no more such paths exist.
        while (BFS(source, sink, parent)) {
            int pathFlow = INF;

            // Traverse the path from sink to source to find the bottleneck value.
            // The bottleneck is the smallest residual capacity along the augmenting path.
            int s = sink;
            while (s != source) {
                Edge edgeToParent = parent.get(s);
                pathFlow = Math.min(pathFlow, edgeToParent.capacity - edgeToParent.flow);
                s = edgeToParent.from;
            }

            maxFlow += pathFlow;

            
            int v = sink;
            while (v != source) {
                Edge edgeToParent = parent.get(v);
                edgeToParent.flow += pathFlow;
                edgeToParent.reverse.flow -= pathFlow;
                v = edgeToParent.from;
            }
            parent.clear();
        }

        return maxFlow;
    }

    private boolean BFS(int source, int sink, Map<Integer, Edge> parent) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        // Start BFS traversal from the source node.
        queue.add(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            int u = queue.poll();

            // For each edge leaving the node 'u':
            for (Edge edge : graph.get(u)) {
                // If the ending node of the edge hasn't been visited and the edge has positive
                // residual capacity:
                if (!visited.contains(edge.to) && edge.capacity > edge.flow) {
                    // Add this edge to the parent map to keep track of the path.
                    parent.put(edge.to, edge);

                    // Mark the ending node of the edge as visited.
                    visited.add(edge.to);

                    queue.add(edge.to);

                    // If we've reached the sink node, then we've found an augmenting path and we
                    // return true.
                    if (edge.to == sink) {
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
        for (List<Edge> edges : graph) {
            for (Edge edge : edges) {
                if (edge.capacity > 0 && edge.flow > 0) {
                    count++;
                }
            }
        }
        io.println(count);

        for (List<Edge> edges : graph) {
            for (Edge edge : edges) {
                if (edge.capacity > 0 && edge.flow > 0) {
                    io.println(edge.from + " " + edge.to + " " + edge.flow);
                }
            }
        }

        io.flush();
    }

    public FlowSolution getFlowSolution(int source, int sink, int maxFlow) {
        List<Edge> positiveFlowEdges = new ArrayList<>();
    
        for (List<Edge> edgesList : graph) {
            for (Edge edge : edgesList) {
                if (edge.capacity > 0 && edge.flow > 0) {
                    positiveFlowEdges.add(edge);
                }
            }
        }
        return new FlowSolution(V - 1, source, sink, maxFlow, positiveFlowEdges);
    }
    
    public static class FlowSolution {
        public int numOfVertices, source, sink, totalFlow;
        public List<Edge> edges;
    
        public FlowSolution(int numOfVertices, int source, int sink, int totalFlow, List<Edge> edges) {
            this.numOfVertices = numOfVertices;
            this.source = source;
            this.sink = sink;
            this.totalFlow = totalFlow;
            this.edges = edges;
        }
    }

    public static void main(String[] args) {
        Kattio io = new Kattio(System.in, System.out);
        int V = io.getInt();
        EdmondsKarpList ek = new EdmondsKarpList(V);
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