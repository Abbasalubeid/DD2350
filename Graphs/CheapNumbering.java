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

    public static boolean edgeExists(Graph g, int vertex1, int vertex2) {
        for (Graph.Edge edge : g.edges) {
            if ((edge.u == vertex1 && edge.v == vertex2) || (edge.u == vertex2 && edge.v == vertex1)) {
                return true;
            }
        }
        return false;
    }    

    public Map<Integer, Integer> cheapNumbering(Graph g) {
        List<Integer> vertices = g.vertices;
        List<Graph.Edge> edges = g.edges;
        Map<Integer, Integer> optimalAssignment = new HashMap<>();
        List<Integer> optimalNumbering = new ArrayList<>();
        int minSum = Integer.MAX_VALUE;

        List<List<Integer>> allPermutations = generatePermutations(new ArrayList<>(vertices), vertices.size());
        
        for (List<Integer> currentPermutation : allPermutations) {
            int currentSum = 0;
            System.out.println("\nChecking permutation: " + currentPermutation);
        
            for (Graph.Edge edge : edges) {
                int difference = Math.abs(currentPermutation.get(edge.u - 1) - currentPermutation.get(edge.v - 1));
                System.out.println("Edge (" + edge.u + ", " + edge.v + ") has difference " + difference);
                currentSum += difference;
            }
        
            System.out.println("Total difference for this permutation: " + currentSum);
        
            if (currentSum < minSum) {
                System.out.println("This is the best permutation found so far!");
                minSum = currentSum;
                minimumDifference = minSum;
                optimalNumbering = new ArrayList<>(currentPermutation);
            }
            else if (currentSum == minSum){
                System.out.println("This is also one possible solution!");
            }
        }
        

        for (int i = 0; i < optimalNumbering.size(); i++) {
            optimalAssignment.put(vertices.get(i), optimalNumbering.get(i));
        }

        return optimalAssignment;
    }


    // Heap’s Algorithm for generating permutations
    // Time Complexity: O(N*N!), where N is the size of the given array 'a'
    private List<List<Integer>> generatePermutations(List<Integer> a, int size) {
        List<List<Integer>> allPermutations = new ArrayList<>();
    
        if (size == 1) {
            allPermutations.add(new ArrayList<>(a));
            return allPermutations;
        }
    
        for (int i = 0; i < size; i++) {
            allPermutations.addAll(generatePermutations(a, size - 1));
    
            // If size is odd, swap the first and the last element
            // Otherwise, swap the ith and the last element
            if (size % 2 == 1) {
                Collections.swap(a, 0, size - 1);
            } else {
                Collections.swap(a, i, size - 1);
            }
        }
    
        return allPermutations;
    }
    

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Get user input for the number of vertices
        System.out.print("Enter the number of vertices: ");
        int numVertices = Integer.parseInt(scanner.nextLine());  // Using nextLine here to prevent issues with lingering newline characters
    
        Graph g = new Graph(numVertices);
        
        for (int i = 1; i <= numVertices; i++) {
            System.out.print("Enter vertices adjacent to vertex " + i + " (separated by space, press Enter when done): ");
            String[] adjacentVerticesStr = scanner.nextLine().split(" ");  // Splitting the line input into individual vertices
    
            for (String vStr : adjacentVerticesStr) {
                if (!vStr.trim().isEmpty()) {  // Check to ensure string is not empty
                    int v = Integer.parseInt(vStr);
                    if (v != i && v <= numVertices && v >= 1 && !edgeExists(g, i, v)) {
                        g.addEdge(i, v);
                    }
                }
            }
        }
    
        g.visualize();
    
        CheapNumbering solver = new CheapNumbering();
        System.out.println("Starting the exhaustive search...\n");
        Map<Integer, Integer> result = solver.cheapNumbering(g);
        System.out.println("\nExhaustive search completed!");
    
        System.out.println("\nOptimal Numbering: (total difference: " + solver.minimumDifference   + ")");
        for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
            System.out.println("Vertex " + entry.getKey() + " is assigned number " + entry.getValue());
        }
    }
}

