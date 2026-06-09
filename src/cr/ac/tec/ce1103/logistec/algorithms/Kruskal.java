package cr.ac.tec.ce1103.logistec.algorithms;

import cr.ac.tec.ce1103.logistec.graph.ArrayList;
import cr.ac.tec.ce1103.logistec.graph.Edge;

/**
 * Árbol de Expansión Mínima (MST) mediante el algoritmo de Kruskal.
 *
 * <p>Procesa las aristas en orden creciente de peso, agregándolas al MST
 * si conectan componentes disjuntas. Utiliza Union-Find para la detección
 * de ciclos. Complejidad: O(E log E) dominado por el ordenamiento.</p>
 *
 * @author Job Jimenez
 * @version 1.0
 * @see cr.ac.tec.ce1103.logistec.graph.UnionFind
 * @see cr.ac.tec.ce1103.logistec.algorithms.Prim
 */
public class Kruskal {

    /**
     * Construye el MST del grafo representado por su lista de aristas.
     *
     * @param V        número total de vértices
     * @param allEdges lista completa de aristas del grafo
     * @return lista de aristas que forman el MST (tamaño V-1 si el grafo es conexo)
     */
    public static ArrayList<Edge> buildMST(int V, ArrayList<Edge> allEdges) {
        throw new UnsupportedOperationException(
            "Kruskal.buildMST: implementar por el equipo");
    }
}
