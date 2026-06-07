package cr.ac.tec.ce1103.logistec.graph;

/**
 * Recorrido en anchura (BFS) de un grafo no dirigido.
 *
 * <p>Implementa el algoritmo BFS usando una cola (LinkedList) para procesar
 * nodos por niveles y un conjunto (HashSet) para rastrear visitados.</p>
 *
 * @author Job Jimenez
 * @version 1.0
 * @see LinkedList
 * @see HashSet
 */
public class BFS {

    /** Grafo representado como lista de adyacencia. */
    private HashMap<Integer, ArrayList<Integer>> graph;

    /**
     * Construye un grafo vacío.
     */
    public BFS() {
        this.graph = new HashMap<>();
    }

    /**
     * Agrega una arista no dirigida entre dos nodos.
     *
     * @param from nodo origen
     * @param to   nodo destino
     */
    public void addEdge(int from, int to) {
        if (!graph.containsKey(from)) {
            graph.put(from, new ArrayList<>());
        }
        graph.get(from).add(to);

        if (!graph.containsKey(to)) {
            graph.put(to, new ArrayList<>());
        }
        graph.get(to).add(from);
    }

    /**
     * Ejecuta BFS desde el nodo especificado, imprimiendo cada nodo visitado.
     *
     * @param start nodo inicial del recorrido
     */
    public void bfs(int start) {
        HashSet<Integer> visited = new HashSet<>();
        LinkedList<Integer> queue = new LinkedList<>();

        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            int node = queue.removeLast();

            System.out.println("Visitando nodo: " + node);

            ArrayList<Integer> neighbors = graph.get(node);
            if (neighbors != null) {
                for (int i = 0; i < neighbors.size(); i++) {
                    int neighbor = neighbors.get(i);
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }
    }

    /**
     * Punto de entrada para demostración del algoritmo BFS.
     *
     * @param args no utilizado
     */
    public static void main(String[] args) {
        BFS bfsGraph = new BFS();

        bfsGraph.addEdge(1, 2);
        bfsGraph.addEdge(1, 3);
        bfsGraph.addEdge(2, 4);
        bfsGraph.addEdge(2, 5);
        bfsGraph.addEdge(3, 6);

        System.out.println("Recorrido BFS desde el nodo 1:");
        bfsGraph.bfs(1);
    }
}