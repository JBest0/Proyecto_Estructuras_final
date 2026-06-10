package cr.ac.tec.ce1103.logistec.algorithms;

import cr.ac.tec.ce1103.logistec.graph.ArrayList;
import cr.ac.tec.ce1103.logistec.graph.Edge;
import cr.ac.tec.ce1103.logistec.graph.HashMap;
import cr.ac.tec.ce1103.logistec.graph.HashSet;
import cr.ac.tec.ce1103.logistec.graph.LinkedList;

/**
 * Recorrido en profundidad (DFS) de un grafo no dirigido.
 *
 * <p>Implementa el algoritmo DFS de forma iterativa usando una pila
 * ({@link cr.ac.tec.ce1103.logistec.graph.LinkedList}) para procesar
 * nodos en profundidad y un conjunto
 * ({@link cr.ac.tec.ce1103.logistec.graph.HashSet}) para rastrear visitados.
 * La implementación iterativa evita el riesgo de {@code StackOverflowError}
 * en grafos grandes que tendría una versión recursiva.</p>
 *
 * <p>También proporciona un método estático {@link #preorder} para el
 * recorrido en preorden de un árbol, usado en la heurística MST-based
 * de planificación de rutas (2-aproximación para TSP métrico).</p>
 *
 * <p><strong>Complejidad:</strong>
 * <ul>
 *   <li>Tiempo: O(V + E) donde V es el número de vértices y E el de aristas</li>
 *   <li>Espacio: O(V) para el conjunto de visitados y la pila</li>
 * </ul>
 * </p>
 *
 * @author Jeffry Vargas Chavarría
 * @version 1.0
 * @see BFS
 * @see cr.ac.tec.ce1103.logistec.graph.GrafoNoDirigido
 * @see cr.ac.tec.ce1103.logistec.graph.LinkedList
 * @see cr.ac.tec.ce1103.logistec.graph.HashSet
 * @see cr.ac.tec.ce1103.logistec.graph.HashMap
 * @see cr.ac.tec.ce1103.logistec.planner.RoutePlanner
 */
public class DFS {

    /** Grafo representado como lista de adyacencia. */
    private HashMap<Integer, ArrayList<Edge>> graph;

    /**
     * Construye un grafo vacío.
     */
    public DFS() {
        this.graph = new HashMap<>();
    }

    /**
     * Construye un objeto DFS usando un grafo existente.
     *
     * <p>Útil cuando el grafo ya ha sido construido con
     * {@link cr.ac.tec.ce1103.logistec.graph.GrafoNoDirigido} y se desea
     * reutilizar su estructura sin volver a agregar aristas.</p>
     *
     * @param grafoExistente lista de adyacencia previamente construida
     */
    public DFS(HashMap<Integer, ArrayList<Edge>> grafoExistente) {
        this.graph = grafoExistente;
    }

    /**
     * Agrega una arista no dirigida y ponderada entre dos nodos.
     *
     * <p>Almacena la arista en ambas direcciones. Si alguno de los nodos
     * no existe, lo crea automáticamente.</p>
     *
     * @param origen  nodo origen
     * @param destino nodo destino
     * @param peso    peso de la arista
     */
    public void agregarArista(int origen, int destino, double peso) {
        if (!graph.containsKey(origen)) {
            graph.put(origen, new ArrayList<>());
        }
        if (!graph.containsKey(destino)) {
            graph.put(destino, new ArrayList<>());
        }

        graph.get(origen).add(new Edge(origen, destino, peso));
        graph.get(destino).add(new Edge(destino, origen, peso));
    }

    /**
     * Ejecuta DFS desde el nodo especificado y retorna el orden de visita.
     *
     * <p>Utiliza una pila explícita para el recorrido iterativo.
     * El orden retornado corresponde al orden en que los nodos son
     * visitados por primera vez.</p>
     *
     * @param inicio nodo inicial del recorrido
     * @return lista de nodos en orden de visita DFS
     */
    public ArrayList<Integer> dfs(int inicio) {
        ArrayList<Integer> orden = new ArrayList<>();
        HashSet<Integer> visitados = new HashSet<>();
        LinkedList<Integer> pila = new LinkedList<>();

        pila.add(inicio);

        while (!pila.isEmpty()) {
            int nodo = pila.remove(0);

            if (!visitados.contains(nodo)) {
                visitados.add(nodo);
                orden.add(nodo);

                ArrayList<Edge> vecinos = graph.get(nodo);
                if (vecinos != null) {
                    for (int i = 0; i < vecinos.size(); i++) {
                        int vecino = vecinos.get(i).to;
                        if (!visitados.contains(vecino)) {
                            pila.add(vecino);
                        }
                    }
                }
            }
        }

        return orden;
    }

    /**
     * Ejecuta DFS desde el nodo especificado e imprime cada nodo visitado.
     *
     * <p>Método de depuración que sigue el mismo patrón que
     * {@link BFS#bfs(int)}. Imprime cada nodo en el orden en que es
     * visitado por primera vez durante el recorrido en profundidad.</p>
     *
     * @param inicio nodo inicial del recorrido
     */
    public void dfsPrint(int inicio) {
        HashSet<Integer> visitados = new HashSet<>();
        LinkedList<Integer> pila = new LinkedList<>();

        pila.add(inicio);

        while (!pila.isEmpty()) {
            int nodo = pila.remove(0);

            if (!visitados.contains(nodo)) {
                visitados.add(nodo);

                System.out.println("Visitando nodo: " + nodo);

                ArrayList<Edge> vecinos = graph.get(nodo);
                if (vecinos != null) {
                    for (int i = 0; i < vecinos.size(); i++) {
                        int vecino = vecinos.get(i).to;
                        if (!visitados.contains(vecino)) {
                            pila.add(vecino);
                        }
                    }
                }
            }
        }
    }

    /**
     * Realiza un recorrido DFS en preorden desde el vértice indicado.
     *
     * <p>El orden de visita de los vecinos sigue la numeración ascendente
     * de sus identificadores. Método estático usado por el planificador
     * de rutas para la heurística MST-based (2-aproximación para TSP métrico).</p>
     *
     * @param mst   lista de adyacencia del árbol (subgrafo acíclico conexo)
     * @param start vértice de inicio del recorrido
     * @return lista con los vértices en el orden de visita del preorden
     */
    public static ArrayList<Integer> preorder(HashMap<Integer, ArrayList<Integer>> mst, int start) {
        ArrayList<Integer> orden = new ArrayList<>();
        HashSet<Integer> visitados = new HashSet<>();
        LinkedList<Integer> pila = new LinkedList<>();
    
        pila.add(start); // add() inserta al inicio → tope de la pila
    
        while (!pila.isEmpty()) {
            int nodo = pila.remove(0); // remove(0) saca del inicio → pop
    
            if (!visitados.contains(nodo)) {
                visitados.add(nodo);
                orden.add(nodo); // preorden: registrar al visitar
    
                ArrayList<Integer> vecinos = mst.get(nodo);
                if (vecinos != null) {
                    // Insertar en orden inverso para que el primero quede en el tope
                    for (int i = vecinos.size() - 1; i >= 0; i--) {
                        int vecino = vecinos.get(i);
                        if (!visitados.contains(vecino)) {
                            pila.add(vecino);
                        }
                    }
                }
            }
        }
        return orden;
    }

    /**
     * Punto de entrada para demostración del algoritmo DFS.
     *
     * @param args no utilizado
     */
    public static void main(String[] args) {
        DFS dfsGraph = new DFS();

        dfsGraph.agregarArista(1, 2, 1.0);
        dfsGraph.agregarArista(1, 3, 1.0);
        dfsGraph.agregarArista(2, 4, 1.0);
        dfsGraph.agregarArista(2, 5, 1.0);
        dfsGraph.agregarArista(3, 6, 1.0);

        System.out.println("Recorrido DFS desde el nodo 1:");
        dfsGraph.dfsPrint(1);
    }
}

