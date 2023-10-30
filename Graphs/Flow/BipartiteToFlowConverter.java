import java.util.*;

public class BipartiteToFlowConverter {

    private int xSize, ySize, edgeCount;
    private ArrayList<Edge> edges = new ArrayList<>();
    private Kattio io;

    public static class Edge {
        public int from, to;
        public Edge(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

    public BipartiteToFlowConverter(Kattio io) {
        this.io = io;
    }

    public void readInput() {
        xSize = io.getInt();
        ySize = io.getInt();
        edgeCount = io.getInt();

        for (int i = 0; i < edgeCount; i++) {
            int from = io.getInt();
            int to = io.getInt();
            edges.add(new Edge(from, to));
        }
    }

    public void transformToFlow() {
        int source = xSize + ySize + 1;
        int sink = xSize + ySize + 2;

        int totalVertices = xSize + ySize + 2;
        io.println(totalVertices);
        io.println(source + " " + sink);

        int totalEdges = edgeCount + xSize + ySize;
        io.println(totalEdges);

        for (int i = 1; i <= xSize; i++) {
            io.println(source + " " + i + " " + 1);
        }

        for (Edge edge : edges) {
            io.println(edge.from + " " + edge.to + " " + 1);
        }

        for (int i = xSize + 1; i <= xSize + ySize; i++) {
            io.println(i + " " + sink + " " + 1);
        }

        io.flush();
    }

    // Transform the bipartite graph into a flow network suitable for the Edmonds-Karp algorithm
    public List<EdmondsKarpList.Edge> generateFlowGraph(int xSize, int ySize, List<Edge> bipartiteEdges) {
        List<EdmondsKarpList.Edge> flowEdges = new ArrayList<>();

        int source = xSize + ySize + 1;
        int sink = xSize + ySize + 2;

        // Connect the source to each vertex in set X. 
        for (int i = 1; i <= xSize; i++) {
            flowEdges.add(new EdmondsKarpList.Edge(source, i, 1));
        }

        // Retain the original bipartite edges between set X and set Y.
        for (Edge edge : bipartiteEdges) {
            flowEdges.add(new EdmondsKarpList.Edge(edge.from, edge.to, 1));
        }

        // Connect each vertex in set Y to the sink.
        for (int i = xSize + 1; i <= xSize + ySize; i++) {
            flowEdges.add(new EdmondsKarpList.Edge(i, sink, 1));
        }

        return flowEdges;
    }


    public void readMaxFlowSolution() {
        io.getInt(); // Number of vertices
        int s = io.getInt(); 
        int t = io.getInt(); 
        io.getInt(); // Total flow 
        int numOfEdgesWithPositiveFlow = io.getInt();

        // Clear existing edges and fill with maxflow solution
        edges.clear();
        for (int i = 0; i < numOfEdgesWithPositiveFlow; i++) {
            int from = io.getInt();
            int to = io.getInt();
            int flow = io.getInt();
            if (flow > 0 && from != s && to != t) { // Filter out the relevant edges
                edges.add(new Edge(from, to));
            }
        }
    }

    public void writeBipMatchSolution() {
        
        // Displaying number of matching edges
        io.println(edges.size());
    
        // Displaying the edges themselves
        for (Edge edge : edges) {
            io.println(edge.from + " " + edge.to);
        }
    }

    public void processAndPrintFlowSolution(EdmondsKarpList.FlowSolution solution) {
        // Extract the bipartite matching from the flow solution
        int s = solution.source;
        int t = solution.sink;
    
        edges.clear();
        for (EdmondsKarpList.Edge edge : solution.edges) {
            if (edge.from != s && edge.to != t) {
                edges.add(new Edge(edge.from, edge.to));
            }
        }
    
        // Print the bipartite matching solution
        writeBipMatchSolution();
    }
    
    

    public static void main(String[] args) {
        Kattio io = new Kattio(System.in, System.out);
        BipartiteToFlowConverter converter = new  BipartiteToFlowConverter(io);

        converter.readInput();
        converter.transformToFlow();
        converter.readMaxFlowSolution();
        converter.writeBipMatchSolution();
        converter.io.close();
    }
}
