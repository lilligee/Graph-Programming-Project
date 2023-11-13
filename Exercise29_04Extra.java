// Look for WRITE YOUR CODE to write your code
import java.util.*;

class WeightedGraph<V> extends UnweightedGraph<V> {
    /** Construct an empty */
    public WeightedGraph() {
    }

    /** Construct a WeightedGraph from vertices and edged in arrays */
    public WeightedGraph(V[] vertices, int[][] edges) {
        createWeightedGraph(java.util.Arrays.asList(vertices), edges);
    }

    /** Construct a WeightedGraph from vertices and edges in list */
    public WeightedGraph(int[][] edges, int numberOfVertices) {
        List<V> vertices = new ArrayList<>();
        for (int i = 0; i < numberOfVertices; i++)
            vertices.add((V)(new Integer(i)));

        createWeightedGraph(vertices, edges);
    }

    /** Construct a WeightedGraph for vertices 0, 1, 2 and edge list */
    public WeightedGraph(List<V> vertices, List<WeightedEdge> edges) {
        createWeightedGraph(vertices, edges);
    }

    /** Construct a WeightedGraph from vertices 0, 1, and edge array */
    public WeightedGraph(List<WeightedEdge> edges,
                         int numberOfVertices) {
        List<V> vertices = new ArrayList<>();
        for (int i = 0; i < numberOfVertices; i++)
            vertices.add((V)(new Integer(i)));

        createWeightedGraph(vertices, edges);
    }

    /** Create adjacency lists from edge arrays */
    private void createWeightedGraph(List<V> vertices, int[][] edges) {
        this.vertices = vertices;

        for (int i = 0; i < vertices.size(); i++) {
            neighbors.add(new ArrayList<Edge>()); // Create a list for vertices
        }

        for (int i = 0; i < edges.length; i++) {
            neighbors.get(edges[i][0]).add(
                    new WeightedEdge(edges[i][0], edges[i][1], edges[i][2]));
        }
    }

    /** Create adjacency lists from edge lists */
    private void createWeightedGraph(
            List<V> vertices, List<WeightedEdge> edges) {
        this.vertices = vertices;

        for (int i = 0; i < vertices.size(); i++) {
            neighbors.add(new ArrayList<Edge>()); // Create a list for vertices
        }

        for (WeightedEdge edge: edges) {
            neighbors.get(edge.u).add(edge); // Add an edge into the list
        }
    }

    /** Return the weight on the edge (u, v) */
    public double getWeight(int u, int v) throws Exception {
        for (Edge edge : neighbors.get(u)) {
            if (edge.v == v) {
                return ((WeightedEdge)edge).weight;
            }
        }

        throw new Exception("Edge does not exit");
    }

    /** Display edges with weights */
    public void printWeightedEdges() {
        for (int i = 0; i < getSize(); i++) {
            System.out.print(getVertex(i) + " (" + i + "): ");
            for (Edge edge : neighbors.get(i)) {
                System.out.print("(" + edge.u +
                        ", " + edge.v + ", " + ((WeightedEdge)edge).weight + ") ");
            }
            System.out.println();
        }
    }

    /** Add edges to the weighted graph */
    public boolean addEdge(int u, int v, double weight) {
        return addEdge(new WeightedEdge(u, v, weight));
    }

    /** Get a minimum spanning tree rooted at vertex 0 */
    public MST getMinimumSpanningTree() {
        return getMinimumSpanningTree(0);
    }

    /** Get a minimum spanning tree rooted at a specified vertex */
    public MST getMinimumSpanningTree(int startingVertex) {
        // cost[v] stores the cost by adding v to the tree
        double[] cost = new double[getSize()];
        for (int i = 0; i < cost.length; i++) {
            cost[i] = Double.POSITIVE_INFINITY; // Initial cost
        }
        cost[startingVertex] = 0; // Cost of source is 0

        int[] parent = new int[getSize()]; // Parent of a vertex
        parent[startingVertex] = -1; // startingVertex is the root
        double totalWeight = 0; // Total weight of the tree thus far

        List<Integer> T = new ArrayList<>();

        // Expand T
        while (T.size() < getSize()) {
            // Find smallest cost v in V - T
            int u = -1; // Vertex to be determined
            double currentMinCost = Double.POSITIVE_INFINITY;
            for (int i = 0; i < getSize(); i++) {
                if (!T.contains(i) && cost[i] < currentMinCost) {
                    currentMinCost = cost[i];
                    u = i;
                }
            }

            if (u == -1) break; else T.add(u); // Add a new vertex to T
            totalWeight += cost[u]; // Add cost[u] to the tree

            // Adjust cost[v] for v that is adjacent to u and v in V - T
            for (Edge e : neighbors.get(u)) {
                if (!T.contains(e.v) && cost[e.v] > ((WeightedEdge)e).weight) {
                    cost[e.v] = ((WeightedEdge)e).weight;
                    parent[e.v] = u;
                }
            }
        } // End of while

        return new MST(startingVertex, parent, T, totalWeight);
    }

    /** MST is an inner class in WeightedGraph */
    public class MST extends SearchTree {
        private double totalWeight; // Total weight of all edges in the tree

        public MST(int root, int[] parent, List<Integer> searchOrder,
                   double totalWeight) {
            super(root, parent, searchOrder);
            this.totalWeight = totalWeight;
        }

        public double getTotalWeight() {
            return totalWeight;
        }
    }

    /** Find single source shortest paths */
    public ShortestPathTree getShortestPath(int sourceVertex) {
        // cost[v] stores the cost of the path from v to the source
        double[] cost = new double[getSize()];
        for (int i = 0; i < cost.length; i++) {
            cost[i] = Double.POSITIVE_INFINITY; // Initial cost set to infinity
        }
        cost[sourceVertex] = 0; // Cost of source is 0

        // parent[v] stores the previous vertex of v in the path
        int[] parent = new int[getSize()];
        parent[sourceVertex] = -1; // The parent of source is set to -1

        // T stores the vertices whose path found so far
        List<Integer> T = new ArrayList<>();

        // Expand T
        while (T.size() < getSize()) {
            // Find smallest cost v in V - T
            int u = -1; // Vertex to be determined
            double currentMinCost = Double.POSITIVE_INFINITY;
            for (int i = 0; i < getSize(); i++) {
                if (!T.contains(i) && cost[i] < currentMinCost) {
                    currentMinCost = cost[i];
                    u = i;
                }
            }

            if (u == -1) break; else T.add(u); // Add a new vertex to T

            // Adjust cost[v] for v that is adjacent to u and v in V - T
            for (Edge e : neighbors.get(u)) {
                if (!T.contains(e.v)
                        && cost[e.v] > cost[u] + ((WeightedEdge)e).weight) {
                    cost[e.v] = cost[u] + ((WeightedEdge)e).weight;
                    parent[e.v] = u;
                }
            }
        } // End of while

        // Create a ShortestPathTree
        return new ShortestPathTree(sourceVertex, parent, T, cost);
    }

    /** ShortestPathTree is an inner class in WeightedGraph */
    public class ShortestPathTree extends SearchTree {
        private double[] cost; // cost[v] is the cost from v to source

        /** Construct a path */
        public ShortestPathTree(int source, int[] parent,
                                List<Integer> searchOrder, double[] cost) {
            super(source, parent, searchOrder);
            this.cost = cost;
        }

        /** Return the cost for a path from the root to vertex v */
        public double getCost(int v) {
            return cost[v];
        }

        /** Print paths from all vertices to the source */
        public void printAllPaths() {
            System.out.println("All shortest paths from " +
                    vertices.get(getRoot()) + " are:");
            for (int i = 0; i < cost.length; i++) {
                printPath(i); // Print a path from i to the source
                System.out.println("(cost: " + cost[i] + ")"); // Path cost
            }
        }
    }
}
class WeightedEdge extends Edge
        implements Comparable<WeightedEdge> {
    public double weight; // The weight on edge (u, v)

    /** Create a weighted edge on (u, v) */
    public WeightedEdge(int u, int v, double weight) {
        super(u, v);
        this.weight = weight;
    }

    /** Compare two edges on weights */
    public int compareTo(WeightedEdge edge) {
        if (weight > edge.weight) {
            return 1;
        }
        else if (weight == edge.weight) {
            return 0;
        }
        else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return u + ", " + v + ", " + weight;
    }
}

class UnweightedGraph<V> implements Graph<V> {
    protected List<V> vertices = new ArrayList<>(); // Store vertices
    protected List<List<Edge>> neighbors
            = new ArrayList<>(); // Adjacency lists

    /** Construct an empty graph */
    protected UnweightedGraph() {
    }

    /** Construct a graph from vertices and edges stored in arrays */
    protected UnweightedGraph(V[] vertices, int[][] edges) {
        for (int i = 0; i < vertices.length; i++)
            addVertex(vertices[i]);

        createAdjacencyLists(edges, vertices.length);
    }

    /** Construct a graph from vertices and edges stored in List */
    protected UnweightedGraph(List<V> vertices, List<Edge> edges) {
        for (int i = 0; i < vertices.size(); i++)
            addVertex(vertices.get(i));

        createAdjacencyLists(edges, vertices.size());
    }

    /** Construct a graph for integer vertices 0, 1, 2 and edge list */
    protected UnweightedGraph(List<Edge> edges, int numberOfVertices) {
        for (int i = 0; i < numberOfVertices; i++)
            addVertex((V)(new Integer(i))); // vertices is {0, 1, ...}

        createAdjacencyLists(edges, numberOfVertices);
    }

    /** Construct a graph from integer vertices 0, 1, and edge array */
    protected UnweightedGraph(int[][] edges, int numberOfVertices) {
        for (int i = 0; i < numberOfVertices; i++)
            addVertex((V)(new Integer(i))); // vertices is {0, 1, ...}

        createAdjacencyLists(edges, numberOfVertices);
    }

    /** Create adjacency lists for each vertex */
    private void createAdjacencyLists(
            int[][] edges, int numberOfVertices) {
        for (int i = 0; i < edges.length; i++) {
            addEdge(edges[i][0], edges[i][1]);
        }
    }

    /** Create adjacency lists for each vertex */
    private void createAdjacencyLists(
            List<Edge> edges, int numberOfVertices) {
        for (Edge edge: edges) {
            addEdge(edge.u, edge.v);
        }
    }

    @Override /** Return the number of vertices in the graph */
    public int getSize() {
        return vertices.size();
    }

    @Override /** Return the vertices in the graph */
    public List<V> getVertices() {
        return vertices;
    }

    @Override /** Return the object for the specified vertex */
    public V getVertex(int index) {
        return vertices.get(index);
    }

    @Override /** Return the index for the specified vertex object */
    public int getIndex(V v) {
        return vertices.indexOf(v);
    }

    @Override /** Return the neighbors of the specified vertex */
    public List<Integer> getNeighbors(int index) {
        List<Integer> result = new ArrayList<>();
        for (Edge e: neighbors.get(index))
            result.add(e.v);

        return result;
    }

    @Override /** Return the degree for a specified vertex */
    public int getDegree(int v) {
        return neighbors.get(v).size();
    }

    @Override /** Print the edges */
    public void printEdges() {
        for (int u = 0; u < neighbors.size(); u++) {
            System.out.print(getVertex(u) + " (" + u + "): ");
            for (Edge e: neighbors.get(u)) {
                System.out.print("(" + getVertex(e.u) + ", " +
                        getVertex(e.v) + ") ");
            }
            System.out.println();
        }
    }

    @Override /** Clear the graph */
    public void clear() {
        vertices.clear();
        neighbors.clear();
    }

    @Override /** Add a vertex to the graph */
    public boolean addVertex(V vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            neighbors.add(new ArrayList<Edge>());
            return true;
        }
        else {
            return false;
        }
    }

    @Override /** Add an edge to the graph */
    public boolean addEdge(Edge e) {
        if (e.u < 0 || e.u > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + e.u);

        if (e.v < 0 || e.v > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + e.v);

        if (!neighbors.get(e.u).contains(e)) {
            neighbors.get(e.u).add(e);
            return true;
        }
        else {
            return false;
        }
    }

    @Override /** Add an edge to the graph */
    public boolean addEdge(int u, int v) {
        return addEdge(new Edge(u, v));
    }

    @Override /** Obtain a DFS tree starting from vertex v */
    /** To be discussed in Section 28.7 */
    public SearchTree dfs(int v) {
        List<Integer> searchOrder = new ArrayList<>();
        int[] parent = new int[vertices.size()];
        for (int i = 0; i < parent.length; i++)
            parent[i] = -1; // Initialize parent[i] to -1

        // Mark visited vertices
        boolean[] isVisited = new boolean[vertices.size()];

        // Recursively search
        dfs(v, parent, searchOrder, isVisited);

        // Return a search tree
        return new SearchTree(v, parent, searchOrder);
    }

    /** Recursive method for DFS search */
    private void dfs(int u, int[] parent, List<Integer> searchOrder,
                     boolean[] isVisited) {
        // Store the visited vertex
        searchOrder.add(u);
        isVisited[u] = true; // Vertex v visited

        for (Edge e : neighbors.get(u)) {
            if (!isVisited[e.v]) {
                parent[e.v] = u; // The parent of vertex e.v is u
                dfs(e.v, parent, searchOrder, isVisited); // Recursive search
            }
        }
    }

    @Override /** Starting bfs search from vertex v */
    /** To be discussed in Section 28.9 */
    public SearchTree bfs(int v) {
        List<Integer> searchOrder = new ArrayList<>();
        int[] parent = new int[vertices.size()];
        for (int i = 0; i < parent.length; i++)
            parent[i] = -1; // Initialize parent[i] to -1

        java.util.LinkedList<Integer> queue =
                new java.util.LinkedList<>(); // list used as a queue
        boolean[] isVisited = new boolean[vertices.size()];
        queue.offer(v); // Enqueue v
        isVisited[v] = true; // Mark it visited

        while (!queue.isEmpty()) {
            int u = queue.poll(); // Dequeue to u
            searchOrder.add(u); // u searched
            for (Edge e: neighbors.get(u)) {
                if (!isVisited[e.v]) {
                    queue.offer(e.v); // Enqueue w
                    parent[e.v] = u; // The parent of w is u
                    isVisited[e.v] = true; // Mark it visited
                }
            }
        }

        return new SearchTree(v, parent, searchOrder);
    }

    /** Tree inner class inside the UnweightedGraph class */
    /** To be discussed in Section 28.6 */
    public class SearchTree {
        private int root; // The root of the tree
        private int[] parent; // Store the parent of each vertex
        private List<Integer> searchOrder; // Store the search order

        /** Construct a tree with root, parent, and searchOrder */
        public SearchTree(int root, int[] parent,
                          List<Integer> searchOrder) {
            this.root = root;
            this.parent = parent;
            this.searchOrder = searchOrder;
        }

        /** Return the root of the tree */
        public int getRoot() {
            return root;
        }

        /** Return the parent of vertex v */
        public int getParent(int v) {
            return parent[v];
        }

        /** Return an array representing search order */
        public List<Integer> getSearchOrder() {
            return searchOrder;
        }

        /** Return number of vertices found */
        public int getNumberOfVerticesFound() {
            return searchOrder.size();
        }

        /** Return the path of vertices from a vertex to the root */
        public List<V> getPath(int index) {
            ArrayList<V> path = new ArrayList<>();

            do {
                path.add(vertices.get(index));
                index = parent[index];
            }
            while (index != -1);

            return path;
        }

        /** Print a path from the root to vertex v */
        public void printPath(int index) {
            List<V> path = getPath(index);
            System.out.print("A path from " + vertices.get(root) + " to " +
                    vertices.get(index) + ": ");
            for (int i = path.size() - 1; i >= 0; i--)
                System.out.print(path.get(i) + " ");
        }

        /** Print the whole tree */
        public void printTree() {
            System.out.println("Root is: " + vertices.get(root));
            System.out.print("Edges: ");
            for (int i = 0; i < parent.length; i++) {
                if (parent[i] != -1) {
                    // Display an edge
                    System.out.print("(" + vertices.get(parent[i]) + ", " +
                            vertices.get(i) + ") ");
                }
            }
            System.out.println();
        }
    }
}

class Edge {
    int u;
    int v;

    public Edge(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public boolean equals(Object o) {
        return u == ((Edge)o).u && v == ((Edge)o).v;
    }
}

interface Graph<V> {
    /** Return the number of vertices in the graph */
    public int getSize();

    /** Return the vertices in the graph */
    public java.util.List<V> getVertices();

    /** Return the object for the specified vertex index */
    public V getVertex(int index);

    /** Return the index for the specified vertex object */
    public int getIndex(V v);

    /** Return the neighbors of vertex with the specified index */
    public java.util.List<Integer> getNeighbors(int index);

    /** Return the degree for a specified vertex */
    public int getDegree(int v);

    /** Print the edges */
    public void printEdges();

    /** Clear the graph */
    public void clear();

    /** Add a vertex to the graph */
    public boolean addVertex(V vertex);

    /** Add an edge (u, v) to the graph. If a graph is
     undirected, you should invoke addEdge(u, v) and
     addEdge(v, u) to add an edge between u and v. */
    public boolean addEdge(int u, int v);

    /** Add an edge to the graph */
    public boolean addEdge(Edge e);

    /** Remove a vertex v from the graph */
    public default boolean remove(V v) {
        return true; // Implementation left as an exercise
    }

    /** Remove an edge (u, v) from the graph */
    public default boolean remove(int u, int v) {
        return true; // Implementation left as an exercise
    }

    /** Obtain a depth-first search tree */
    public UnweightedGraph<V>.SearchTree dfs(int v);

    /** Obtain a breadth-first search tree */
    public UnweightedGraph<V>.SearchTree bfs(int v);
}

public class Exercise29_04Extra{
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter the number of vertices: ");
        int numberOfVertices = input.nextInt();
        System.out.println("The number of vertices is " + numberOfVertices);

        System.out.print("Enter the triplets in one line: ");
        input.nextLine(); // Consume the previous newline character
        String line = input.nextLine();

        String[] triplets = line.split("\\|");

        List<WeightedEdge> edges = new ArrayList<>();

        for (String triplet : triplets) {
            String[] values = triplet.split(",");
            int u = Integer.parseInt(values[0].trim());
            int v = Integer.parseInt(values[1].trim());
            double weight = Double.parseDouble(values[2].trim());
            edges.add(new WeightedEdge(u, v, weight));
            edges.add(new WeightedEdge(v, u, weight)); // Adding the reverse edge
        }

        WeightedEdge[] edgeArray = edges.toArray(new WeightedEdge[0]);
        WeightedGraph<Integer> g = new WeightedGraph<>(List.of(edgeArray), numberOfVertices);

        g.printWeightedEdges();

        WeightedGraph<Integer>.MST tree = g.getMinimumSpanningTree();

        System.out.println("Total weight in MST is " + tree.getTotalWeight());
        System.out.println("Root is: " + tree.getRoot());
        System.out.print("Edges: ");
        tree.printTree();
    }
}
