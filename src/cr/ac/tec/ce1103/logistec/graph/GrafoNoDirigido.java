package cr.ac.tec.ce1103.logistec.graph;

/**
 * Grafo no dirigido y ponderado.
 *
 * <p>Estructura de datos que representa un grafo no dirigido con aristas
 * ponderadas. Los nodos se identifican mediante un entero ({@code int})
 * y las aristas usan la clase {@link Edge} existente en el proyecto.
 * La representación interna es una lista de adyacencia basada en
 * {@link HashMap} y {@link ArrayList}, compatible directamente con los
 * algoritmos del paquete
 * ({@link cr.ac.tec.ce1103.logistec.algorithms.Prim},
 * {@link cr.ac.tec.ce1103.logistec.algorithms.BFS},
 * {@link cr.ac.tec.ce1103.logistec.algorithms.DFS}).</p>
 *
 * <p><strong>Complejidad:</strong>
 * <ul>
 *   <li>Agregar vértice: O(1) promedio</li>
 *   <li>Agregar arista: O(1) promedio</li>
 *   <li>Verificar existencia de vértice: O(1) promedio</li>
 * </ul>
 * </p>
 *
 * @author Jeffry Vargas Chavarría
 * @version 1.0
 * @see Edge
 * @see HashMap
 * @see ArrayList
 * @see cr.ac.tec.ce1103.logistec.algorithms.Prim
 * @see cr.ac.tec.ce1103.logistec.algorithms.DFS
 */
public class GrafoNoDirigido {

    /** Lista de adyacencia: nodo -> lista de aristas incidentes. */
    private HashMap<Integer, ArrayList<Edge>> listaAdyacencia;

    /**
     * Construye un grafo no dirigido vacío.
     */
    public GrafoNoDirigido() {
        this.listaAdyacencia = new HashMap<>();
    }

    /**
     * Agrega un vértice al grafo si no existe previamente.
     *
     * @param id identificador numérico del vértice
     */
    public void agregarVertice(int id) {
        if (!listaAdyacencia.containsKey(id)) {
            listaAdyacencia.put(id, new ArrayList<>());
        }
    }

    /**
     * Agrega una arista no dirigida y ponderada entre dos nodos.
     *
     * <p>Almacena la arista en ambas direcciones para mantener la
     * simetría del grafo no dirigido. Si alguno de los nodos no existe,
     * lo crea automáticamente.</p>
     *
     * @param origen  nodo de origen
     * @param destino nodo de destino
     * @param peso    peso de la arista
     */
    public void agregarArista(int origen, int destino, double peso) {
        if (!listaAdyacencia.containsKey(origen)) {
            listaAdyacencia.put(origen, new ArrayList<>());
        }
        if (!listaAdyacencia.containsKey(destino)) {
            listaAdyacencia.put(destino, new ArrayList<>());
        }

        listaAdyacencia.get(origen).add(new Edge(origen, destino, peso));
        listaAdyacencia.get(destino).add(new Edge(destino, origen, peso));
    }

    /**
     * Verifica si un vértice existe en el grafo.
     *
     * @param id identificador del vértice a buscar
     * @return {@code true} si el vértice está en el grafo
     */
    public boolean existeVertice(int id) {
        return listaAdyacencia.containsKey(id);
    }

    /**
     * Retorna el número de vértices en el grafo.
     *
     * @return cantidad de vértices
     */
    public int cantidadVertices() {
        return listaAdyacencia.size();
    }

    /**
     * Retorna las aristas incidentes de un nodo.
     *
     * @param nodo identificador del nodo
     * @return lista de aristas que salen del nodo, o {@code null} si no existe
     */
    public ArrayList<Edge> getAristas(int nodo) {
        return listaAdyacencia.get(nodo);
    }

    /**
     * Retorna la lista de adyacencia completa del grafo.
     *
     * <p>Este método permite pasar el grafo a algoritmos como
     * {@link cr.ac.tec.ce1103.logistec.algorithms.Prim} o
     * {@link cr.ac.tec.ce1103.logistec.algorithms.DFS} que operan sobre la misma
     * representación interna.</p>
     *
     * @return mapa de adyacencia completo
     */
    public HashMap<Integer, ArrayList<Edge>> getListaAdyacencia() {
        return listaAdyacencia;
    }

    /**
     * Imprime la representación del grafo en consola.
     *
     * <p>Muestra cada vértice seguido de sus aristas incidentes
     * con el formato {@code origen -> destino(peso) ...}.</p>
     */
    public void mostrarGrafo() {
        Object[] claves = listaAdyacencia.keys();
        for (int i = 0; i < claves.length; i++) {
            int nodo = (Integer) claves[i];
            System.out.print(nodo + " -> ");

            ArrayList<Edge> aristas = listaAdyacencia.get(nodo);
            if (aristas != null) {
                for (int j = 0; j < aristas.size(); j++) {
                    Edge e = aristas.get(j);
                    System.out.print(e.to + "(" + e.weight + ") ");
                }
            }
            System.out.println();
        }
    }
}



