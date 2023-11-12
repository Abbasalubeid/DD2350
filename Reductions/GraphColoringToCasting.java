import java.util.*;

public class GraphColoringToCasting {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int originalNumVertices = Integer.parseInt(scanner.nextLine().trim()); // Number of vertices (roles)
        int originalNumEdges = Integer.parseInt(scanner.nextLine().trim()); // Number of edges (scenes)
        int originalNumColors = Integer.parseInt(scanner.nextLine().trim()); // Number of colors (actors)

        List<Edge> originalEdges = new ArrayList<>();

        for (int i = 0; i < originalNumEdges; i++) {
            String[] edge = scanner.nextLine().trim().split(" ");

            int from = Integer.parseInt(edge[0]);
            int to = Integer.parseInt(edge[1]);

            originalEdges.add(new Edge(from, to));
        }

        convertToCastingProblem(originalNumVertices, originalNumColors, originalEdges);
    }

    private static void convertToCastingProblem(int originalVertices, int originalColors, List<Edge> originalEdges) {
        int newNumVertices = originalVertices + 2;
        int newNumActors = originalColors + 2;

        List<Edge> newEdges = new ArrayList<>();

        // Adding edges from new vertex 1 to all renumbered original vertices
        for (int i = 3; i <= newNumVertices; i++) {
            newEdges.add(new Edge(1, i));
        }

        newEdges.add(new Edge(2, 3));

        // Adding renumbered original edges
        for (Edge e : originalEdges) {
            newEdges.add(new Edge(e.from + 2, e.to + 2));
        }

        int newNumScenes = newEdges.size();

        System.out.println(newNumVertices);
        System.out.println(newNumScenes);
        System.out.println(newNumActors);

        // Printing the roles for p1 and p2
        System.out.println("1 1");
        System.out.println("1 2");

        // Printing the roles for other actors (excluding p1 and p2)
        for (int i = 3; i <= newNumVertices; i++) {
            System.out.print(newNumActors - 2);
            for (int j = 3; j <= newNumActors; j++) {
                System.out.print(" " + j);
            }
            System.out.println();
        }

        // Printing the new scenes
        for (Edge edge : newEdges) {
            System.out.println("2 " + edge.from + " " + edge.to);
        }
    }

    static class Edge {
        int from;
        int to;

        Edge(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }
}
