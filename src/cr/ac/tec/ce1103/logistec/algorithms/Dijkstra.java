package cr.ac.tec.ce1103.logistec.algorithms;

import cr.ac.tec.ce1103.logistec.graph.ArrayList;
import cr.ac.tec.ce1103.logistec.graph.Edge;
import cr.ac.tec.ce1103.logistec.graph.HashMap;

/**
 * Camino más corto desde un origen mediante el algoritmo de Dijkstra.
 *
 * <p>Calcula la distancia mínima desde un vértice origen a todos los demás
 * vértices del grafo. Utiliza internamente una cola de prioridad (MinHeap)
 * desarrollada por el equipo. Complejidad: O((V + E) log V).</p>
 *
 * @author Job Jimenez
 * @version 1.0
 * @see FloydWarshall
 */
public class Dijkstra {

    /** Constante que representa distancia infinita (no hay camino). */
    public static final double INF = Double.POSITIVE_INFINITY;

    /**
     * Calcula la distancia mínima desde el origen a todos los vértices.
     *
     * @param graph lista de adyacencia del grafo (vértice → aristas salientes)
     * @param start vértice origen
     * @return arreglo de tamaño V donde dist[v] = distancia desde start hasta v,
     *         o {@link #INF} si no hay camino
     */
    public static double[] shortestPath(HashMap<Integer, ArrayList<Edge>> graph, int start) {
        throw new UnsupportedOperationException(
            "Dijkstra.shortestPath: implementar por el equipo");
    }
}
