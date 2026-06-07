package cr.ac.tec.ce1103.logistec.algorithms;

import cr.ac.tec.ce1103.logistec.graph.ArrayList;
import cr.ac.tec.ce1103.logistec.graph.Edge;
import cr.ac.tec.ce1103.logistec.graph.HashMap;

/**
 * Matriz de distancias mínimas entre todos los pares de vértices.
 *
 * <p>Implementa el algoritmo de Floyd-Warshall para calcular la distancia
 * más corta entre cualquier par de vértices en un grafo ponderado.
 * Complejidad: O(V³).</p>
 *
 * @author Job Jimenez
 * @version 1.0
 * @see Dijkstra
 * @see Warshall
 */
public class FloydWarshall {

    /** Constante que representa distancia infinita (no hay camino). */
    public static final double INF = Double.POSITIVE_INFINITY;

    /**
     * Calcula la matriz de distancias mínimas entre todos los pares.
     *
     * @param graph lista de adyacencia del grafo (vértice → aristas salientes)
     * @param V     número total de vértices (asume IDs 0..V-1)
     * @return matriz V×V donde D[i][j] = distancia mínima de i a j,
     *         o {@link #INF} si no hay camino
     */
    public static double[][] shortestPaths(HashMap<Integer, ArrayList<Edge>> graph, int V) {
        throw new UnsupportedOperationException(
            "FloydWarshall.shortestPaths: implementar por el equipo");
    }
}
