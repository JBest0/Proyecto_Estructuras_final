package cr.ac.tec.ce1103.logistec.graph;

/**
 * Algoritmo de Prim para encontrar el Árbol de Expansión Mínimo (MST).
 *
 * <p>Implementación lazy del algoritmo de Prim que utiliza un heap mínimo
 * para procesar aristas candidatas ordenadas por peso. El grafo se
 * representa como una lista de adyacencia.</p>
 *
 * <p><strong>Complejidad:</strong>
 * <ul>
 *   <li>Tiempo: O(E log E) donde E es el número de aristas</li>
 *   <li>Espacio: O(V + E) para almacenar grafo y estructuras auxiliares</li>
 * </ul>
 * </p>
 *
 * @author Job Jimenez
 * @version 1.0
 * @see Edge
 * @see MinHeap
 * @see ArrayList
 * @see HashMap
 * @see HashSet
 */
public class Prim {

    /** Lista de adyacencia: nodo -> lista de aristas que salen de ese nodo. */
    private final HashMap<Integer, ArrayList<Edge>> graph;

    /**
     * Construye un grafo vacío para Prim.
     */
    public Prim() {
        graph = new HashMap<>();
    }

    /**
     * Construye un objeto Prim usando un grafo existente.
     *
     * <p>Útil cuando el grafo ya ha sido cargado desde un archivo JSON
     * y se desea reutilizar su estructura sin volver a agregar aristas.</p>
     *
     * @param existingGraph lista de adyacencia previamente construida
     */
    public Prim(HashMap<Integer, ArrayList<Edge>> existingGraph) {
        this.graph = existingGraph;
    }

    /**
     * Agrega una arista no dirigida y ponderada entre dos nodos.
     *
     * <p>Almacena la arista en ambas direcciones para que el grafo
     * pueda ser recorrido desde cualquier nodo.</p>
     *
     * @param from   nodo origen
     * @param to     nodo destino
     * @param weight peso de la arista (debe ser no negativo)
     */
    public void addEdge(int from, int to, double weight) {
        addDirected(from, to, weight);
        addDirected(to, from, weight);
    }

    /**
     * Ejecuta el algoritmo de Prim desde el nodo especificado.
     *
     * <p>Retorna una lista de aristas que forman el Árbol de Expansión Mínimo.
     * Si el grafo es desconexo, solo se incluye la componente alcanzable
     * desde el nodo inicial. Verificar {@code mst.size() == V - 1} para
     * confirmar que el grafo está completamente conectado.</p>
     *
     * @param startNode nodo inicial del algoritmo (debe estar en el grafo)
     * @return lista de aristas del MST ordenadas por el orden de inserción
     * @throws IllegalArgumentException si el nodo inicial no existe en el grafo
     */
    public ArrayList<Edge> buildMST(int startNode) {
        if (!graph.containsKey(startNode))
            throw new IllegalArgumentException("Start node not found in graph: " + startNode);

        ArrayList<Edge> mst     = new ArrayList<>();
        HashSet<Integer> inMST  = new HashSet<>();
        MinHeap heap            = new MinHeap();

        visit(startNode, inMST, heap);

        while (!heap.isEmpty()) {
            Edge cheapest = heap.extractMin();

            if (inMST.contains(cheapest.to))
                continue;

            mst.add(cheapest);
            visit(cheapest.to, inMST, heap);
        }

        return mst;
    }

    /**
     * Calcula el peso total de una lista de aristas.
     *
     * @param edges lista de aristas (típicamente el resultado de buildMST)
     * @return suma de los pesos de todas las aristas
     */
    public static double totalWeight(ArrayList<Edge> edges) {
        double total = 0;
        for (int i = 0; i < edges.size(); i++)
            total += edges.get(i).weight;
        return total;
    }

    /**
     * Imprime cada arista del MST junto con el peso total.
     *
     * <p>Útil para depuración rápida o reportes de laboratorio.</p>
     *
     * @param edges lista de aristas a mostrar (típicamente el MST)
     */
    public static void printMST(ArrayList<Edge> edges) {
        System.out.println("MST edges (" + edges.size() + " total):");
        for (int i = 0; i < edges.size(); i++)
            System.out.println("  " + edges.get(i));
        System.out.println("Total weight: " + totalWeight(edges));
    }

    /**
     * Marca un nodo como parte del MST e inserta sus aristas candidatas en el heap.
     *
     * @param node nodo a visitar
     * @param inMST conjunto de nodos ya incluidos en el MST
     * @param heap heap mínimo de aristas candidatas
     */
    private void visit(int node, HashSet<Integer> inMST, MinHeap heap) {
        inMST.add(node);
        ArrayList<Edge> neighbors = graph.get(node);
        if (neighbors == null) return;

        for (int i = 0; i < neighbors.size(); i++) {
            Edge edge = neighbors.get(i);
            if (!inMST.contains(edge.to))
                heap.insert(edge);
        }
    }

    /**
     * Agrega una arista dirigida a la lista de adyacencia.
     *
     * @param from nodo origen
     * @param to   nodo destino
     * @param weight peso de la arista
     */
    private void addDirected(int from, int to, double weight) {
        if (!graph.containsKey(from))
            graph.put(from, new ArrayList<>());
        graph.get(from).add(new Edge(from, to, weight));
    }
}
