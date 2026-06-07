package cr.ac.tec.ce1103.logistec.algorithms;

import cr.ac.tec.ce1103.logistec.graph.ArrayList;
import cr.ac.tec.ce1103.logistec.graph.HashMap;

/**
 * Recorrido en profundidad (DFS) para grafos representados por lista de adyacencia.
 *
 * <p>Proporciona un recorrido preorden que se utiliza en la heurística
 * MST-based de planificación de rutas (2-aproximación para TSP métrico).</p>
 *
 * @author Job Jimenez
 * @version 1.0
 * @see cr.ac.tec.ce1103.logistec.planner.RoutePlanner
 */
public class DFS {

    /**
     * Realiza un recorrido DFS en preorden desde el vértice indicado.
     *
     * <p>El orden de visita de los vecinos sigue la numeración ascendente
     * de sus identificadores.</p>
     *
     * @param mst   lista de adyacencia del árbol (subgrafo acíclico conexo)
     * @param start vértice de inicio del recorrido
     * @return lista con los vértices en el orden de visita del preorden
     */
    public static ArrayList<Integer> preorder(HashMap<Integer, ArrayList<Integer>> mst, int start) {
        throw new UnsupportedOperationException(
            "DFS.preorder: implementar por el equipo");
    }
}
