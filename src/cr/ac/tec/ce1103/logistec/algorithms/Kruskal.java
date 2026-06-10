package cr.ac.tec.ce1103.logistec.algorithms;

import cr.ac.tec.ce1103.logistec.graph.ArrayList;
import cr.ac.tec.ce1103.logistec.graph.Edge;
import cr.ac.tec.ce1103.logistec.graph.UnionFind;

/**
 * Árbol de Expansión Mínima (MST) mediante el algoritmo de Kruskal.
 *
 * <p>Procesa las aristas en orden creciente de peso, agregándolas al MST
 * si conectan componentes disjuntas. Utiliza Union-Find para detección
 * de ciclos. Complejidad: O(E log E) dominado por el ordenamiento.</p>
 *
 * @author Luis Acuña
 * @version 1.0
 */
public class Kruskal {

    /**
     * Construye el MST del grafo representado por su lista de aristas.
     *
     * <p>Los IDs de los vértices en las aristas pueden ser arbitrarios;
     * se mapean internamente a índices 0..V-1 para el Union-Find.</p>
     *
     * @param V        número total de vértices
     * @param allEdges lista completa de aristas del grafo (sin duplicados dirigidos)
     * @return lista de aristas que forman el MST
     */
    public static ArrayList<Edge> buildMST(int V, ArrayList<Edge> allEdges) {

        // 1. Recolectar IDs únicos y mapearlos a índices 0..V-1
        //    Usamos un arreglo simple porque V es conocido
        int[] ids = new int[V];
        int count = 0;

        for (int i = 0; i < allEdges.size(); i++) {
            Edge e = allEdges.get(i);

            // Agregar e.from si no está
            boolean foundFrom = false;
            for (int j = 0; j < count; j++) {
                if (ids[j] == e.from) { foundFrom = true; break; }
            }
            if (!foundFrom && count < V) ids[count++] = e.from;

            // Agregar e.to si no está
            boolean foundTo = false;
            for (int j = 0; j < count; j++) {
                if (ids[j] == e.to) { foundTo = true; break; }
            }
            if (!foundTo && count < V) ids[count++] = e.to;
        }

        // 2. Ordenar aristas por peso ascendente (insertion sort)
        int n = allEdges.size();
        for (int i = 1; i < n; i++) {
            Edge key = allEdges.get(i);
            int j = i - 1;
            while (j >= 0 && allEdges.get(j).weight > key.weight) {
                allEdges.set(j + 1, allEdges.get(j));
                j--;
            }
            allEdges.set(j + 1, key);
        }

        // 3. Procesar aristas con Union-Find
        UnionFind uf = new UnionFind(V);
        ArrayList<Edge> mst = new ArrayList<>();

        for (int i = 0; i < n && mst.size() < V - 1; i++) {
            Edge e = allEdges.get(i);

            // Obtener índices internos de from y to
            int idxFrom = -1, idxTo = -1;
            for (int j = 0; j < count; j++) {
                if (ids[j] == e.from) idxFrom = j;
                if (ids[j] == e.to)   idxTo   = j;
            }
            if (idxFrom == -1 || idxTo == -1) continue;

            if (uf.find(idxFrom) != uf.find(idxTo)) {
                uf.union(idxFrom, idxTo);
                mst.add(e);
            }
        }

        return mst;
    }

    /**
     * Calcula el costo total del MST.
     *
     * @param mst lista de aristas del MST
     * @return suma de pesos
     */
    public static double costoTotal(ArrayList<Edge> mst) {
        double total = 0;
        for (int i = 0; i < mst.size(); i++) {
            total += mst.get(i).weight;
        }
        return total;
    }
}
