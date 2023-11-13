import java.util.*;


class WeightedEdge implements Comparable<WeightedEdge> {
    int u;
    int v;
    double weight;

    public WeightedEdge(int u, int v, double weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    @Override
    public int compareTo(WeightedEdge o) {
        return Double.compare(o.weight, this.weight); // Sort in descending order
    }
}

class WeightedGraph {
    private List<List<WeightedEdge> > adjacencyList;

    public WeightedGraph(List<WeightedEdge> edges, int numberOfVertices) {
        adjacencyList = new ArrayList<>();
        for (int i = 0; i < numberOfVertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
        for (WeightedEdge edge : edges) {
            adjacencyList.get(edge.u).add(edge);
        }
    }

    public void printWeightedEdges() {
        for (int i = 0; i < adjacencyList.size(); i++) {
            System.out.print(i + " (" + i + "): ");
            for (WeightedEdge edge : adjacencyList.get(i)) {
                System.out.print("(" + edge.u + ", " + edge.v + ", " + edge.weight + ") ");
            }
            System.out.println();
        }
    }

    public MST getMinimumSpanningTree() {
        return new MST(this);
    }

    public static class MST {
        private List<Integer> parent;
        private List<Integer> root;
        private List<Double> weights;

        public MST(WeightedGraph graph) {
            int numberOfVertices = graph.adjacencyList.size();
            parent = new ArrayList<>(numberOfVertices);
            root = new ArrayList<>(numberOfVertices);
            weights = new ArrayList<>(numberOfVertices);

            for (int i = 0; i < numberOfVertices; i++) {
                parent.add(-1);
                root.add(i);
                weights.add(0.0);
            }

            List<WeightedEdge> edges = new ArrayList<>();
            for (int i = 0; i < numberOfVertices; i++) {
                edges.addAll(graph.adjacencyList.get(i));
            }

            edges.sort(WeightedEdge::compareTo); // Sort edges in descending order

            for (WeightedEdge edge : edges) {
                int rootU = findRoot(root, edge.u);
                int rootV = findRoot(root, edge.v);

                if (rootU != rootV) {
                    parent.set(rootU, rootV);
                    weights.set(rootU, edge.weight);
                }
            }
        }

        public double getTotalWeight() {
            return weights.stream().mapToDouble(Double::doubleValue).sum();
        }

        public int getRoot() {
            return root.get(0);
        }

        public List<WeightedEdge> getEdges() {
            List<WeightedEdge> mstEdges = new ArrayList<>();
            for (int i = 1; i < parent.size(); i++) {
                if (parent.get(i) != -1) {
                    mstEdges.add(new WeightedEdge(i, parent.get(i), weights.get(i)));
                }
            }
            return mstEdges;
        }

        private int findRoot(List<Integer> roots, int vertex) {
            int currentVertex = vertex;
            while (roots.get(currentVertex) != currentVertex) {
                currentVertex = roots.get(currentVertex);
            }
            return currentVertex;
        }
    }
}

public class Exercise29_04Extra3{
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter the number of vertices: ");
        int numberOfVertices = input.nextInt();
        input.nextLine(); // Consume newline

        System.out.printf("The number of vertices is %d\n", numberOfVertices);

        // Create a list to store weighted edges
        List<WeightedEdge> edges = new ArrayList<>();

        System.out.print("Enter the triplets in one line: ");
        String tripletsInput = input.nextLine();

        // Split the input by "|" to get triplets
        String[] triplets = tripletsInput.split("\\|");
        for (String triplet : triplets) {
            String[] values = triplet.trim().split(",");
            if (values.length == 3) {
                int u = Integer.parseInt(values[0].trim());
                int v = Integer.parseInt(values[1].trim());
                double w = Double.parseDouble(values[2].trim());
                edges.add(new WeightedEdge(u, v, w));
                edges.add(new WeightedEdge(v, u, w)); // Add the reverse edge
            }
        }

        // Create a WeightedGraph2 using the edges and number of vertices
        WeightedGraph graph = new WeightedGraph(edges, numberOfVertices);

        // Display the weighted edges
        graph.printWeightedEdges();

        // Calculate and display the minimum spanning tree
        WeightedGraph.MST tree = graph.getMinimumSpanningTree();
        System.out.println("Total weight in MST is " + tree.getTotalWeight());
        System.out.println("Root is: " + tree.getRoot());

        System.out.print("Edges: ");
        tree.getEdges().forEach(edge ->
                System.out.print("(" + edge.u + ", " + edge.v + ") "));
    }
}

