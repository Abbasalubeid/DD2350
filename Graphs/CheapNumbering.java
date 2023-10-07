import java.util.*;

public class CheapNumbering {
    int minimumDifference = 0;

    private static class Graph {
        List<Integer> vertices;
        List<Edge> edges;

        Graph(int numVertices) {
            this.vertices = new ArrayList<>();
            for (int i = 1; i <= numVertices; i++) {  // Start from 1 for 1-indexing
                this.vertices.add(i);
            }
            this.edges = new ArrayList<>();
            System.out.println("Constructed a new graph with " + numVertices + " vertices.");
        }

        void addEdge(int u, int v) {
            this.edges.add(new Edge(u, v));
            System.out.println("Added edge (" + u + ", " + v + ")");
        }

        void visualize() {
            System.out.println("\nVisualizing Graph:");
            System.out.println("--------------------");
            for (int vertex : vertices) {
                List<String> connectedVertices = new ArrayList<>();
                for (Edge edge : edges) {
                    if (edge.u == vertex) {
                        connectedVertices.add(String.valueOf(edge.v));
                    } else if (edge.v == vertex) {
                        connectedVertices.add(String.valueOf(edge.u));
                    }
                }
                System.out.println("Vertex " + vertex + " connected to: " + String.join(", ", connectedVertices));
            }
            System.out.println("--------------------\n");
        }

        private static class Edge {
            int u, v;

            Edge(int u, int v) {
                this.u = u;
                this.v = v;
            }
        }
    }

}
