package cr.ac.tec.ce1103.logistec.algorithms;

import cr.ac.tec.ce1103.logistec.graph.ArrayList;
import cr.ac.tec.ce1103.logistec.graph.Edge;
import cr.ac.tec.ce1103.logistec.graph.HashMap;

/**
 * Cierre transitivo de un grafo mediante el algoritmo de Warshall.
 *
 * <p>Construye la matriz booleana P de alcanzabilidad entre todos los pares
 * de vértices. P[i][j] = {@code true} si existe algún camino (de cualquier
 * longitud) desde i hasta j. Complejidad: O(V³).</p>
 *
 * @author Job Jimenez
 * @version 1.0
 * @see FloydWarshall
 */
public class Warshall {

    /**
     * Calcula la matriz de cierre transitivo del grafo.
     *
     * @param graph lista de adyacencia del grafo (vértice → aristas salientes)
     * @param V     número total de vértices (asume IDs 0..V-1)
     * @return matriz booleana V×V donde P[i][j] = {@code true} si i alcanza a j
     */
    public static boolean[][] transitiveClosure(HashMap<Integer, ArrayList<Edge>> graph, int V) {
        throw new UnsupportedOperationException(
            "Warshall.transitiveClosure: implementar por el equipo");
    }
}
