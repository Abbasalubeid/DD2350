import java.util.ArrayList;

public class BipartiteToFlowConverter {

    private int xSize, ySize, edgeCount;
    private ArrayList<Edge> edges = new ArrayList<>();
    private Kattio io;

    private class Edge {
        int from, to;
        Edge(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

    public BipartiteToFlowConverter() {
        io = new Kattio(System.in, System.out);
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

    public void readMaxFlowSolution() {
        int numOfVertices = io.getInt();
        int s = io.getInt(); 
        int t = io.getInt(); 
        int totalFlow = io.getInt(); 
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
        // Displaying number of vertices in the matching: |X| and |Y|
        io.println(xSize + " " + ySize);
        
        // Displaying number of matching edges
        io.println(edges.size());
    
        // Displaying the edges themselves
        for (Edge edge : edges) {
            io.println(edge.from + " " + edge.to);
        }
    }
    

    public static void main(String[] args) {
        BipartiteToFlowConverter converter = new BipartiteToFlowConverter();
        converter.readInput();
        converter.transformToFlow();
        converter.readMaxFlowSolution();
        converter.writeBipMatchSolution();
        converter.io.close();
    }
}
