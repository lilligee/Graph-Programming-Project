import java.util.LinkedList;
import java.util.Scanner;

public class Exercise28_20Extra {
    int vertices;
    static LinkedList<Integer> adjacencyList[];

    @SuppressWarnings("unchecked")
    public Exercise28_20Extra(int vertices) {
        this.vertices = vertices;
        adjacencyList = new LinkedList[vertices];
        for (int i = 0; i < vertices; i++) {
            adjacencyList[i] = new LinkedList<>();
        }
    }

    public static void Edge(int source, int dest) {
        adjacencyList[source].addFirst(dest);
    }

    public static void isConnected(Exercise28_20Extra graph) {
        int vertices = graph.vertices;
        LinkedList<Integer> adjacencyList[] = Exercise28_20Extra.adjacencyList;
        boolean[] visited = new boolean[vertices];
        DFS(0, adjacencyList, visited);
        boolean connected = true;

        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {
                connected = false;
                break;
            }
        }

        if (connected){
            System.out.println("The graph is connected");
        } else {
            System.out.println("The graph is not connected");
        }
    }

    public static void DFS(int source, LinkedList<Integer> adjacencyList[], boolean[] visited) {
        visited[source] = true;
        for (int i = 0; i < adjacencyList[source].size(); i++) {
            int neighbor = adjacencyList[source].get(i);
            if (!visited[neighbor]) {
                DFS(neighbor, adjacencyList, visited);
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of vertices: ");
        int numVertices = sc.nextInt();
        System.out.println("The number of vertices is " + numVertices);
        sc.nextLine();

        Exercise28_20Extra g = new Exercise28_20Extra(numVertices);

        System.out.println("Enter the edges in " + numVertices + " lines:");

        for (int i = 0; i < numVertices; i++) {
            String line = sc.nextLine();
            String[] vertices = line.split(" ");

            System.out.print("\n"+i + " (" + vertices[0] + "): ");

            for(int j = 1; j < vertices.length; j++){
                Exercise28_20Extra.Edge(Integer.parseInt(vertices[0]), Integer.parseInt(vertices[j]));
                System.out.print("(" + vertices[0] + ", " + vertices[j] + ") ");
            }
            System.out.println();
        }
        sc.close();


        Exercise28_20Extra.isConnected(g);
    }
}
