package cr.ac.tec.ce1103.logistec.algorithms;

import cr.ac.tec.ce1103.logistec.graph.ArrayList;
import cr.ac.tec.ce1103.logistec.graph.Edge;
import cr.ac.tec.ce1103.logistec.graph.HashMap;
import cr.ac.tec.ce1103.logistec.graph.LinkedList;
import cr.ac.tec.ce1103.logistec.graph.MinHeap;

/**
 * Camino más corto desde un origen usando el algoritmo de Dijkstra.
 *
 * <p>Usa un MinHeap propio como cola de prioridad.
 * Complejidad: O((V + E) log V).</p>
 *
 * @author Luis Acuña
 * @version 1.0
 */
public class Dijkstra {

    public static final double INF = Double.POSITIVE_INFINITY;

    private double[] dist;
    private int[]    pred;
    private HashMap<Integer, Integer> idAIndice;
    private int[]    indiceAId;
    private int      V;

    /**
     * Ejecuta Dijkstra desde el vértice origen sobre el grafo dado.
     *
     * @param graph    lista de adyacencia del grafo
     * @param idOrigen ID del vértice origen
     */
    public Dijkstra(HashMap<Integer, ArrayList<Edge>> graph, int idOrigen) {

        // 1. Mapeo ID <-> índice
        Object[] nodos = graph.keys();
        V = nodos.length;
        idAIndice = new HashMap<>();
        indiceAId = new int[V];

        for (int i = 0; i < V; i++) {
            int id = (Integer) nodos[i];
            idAIndice.put(id, i);
            indiceAId[i] = id;
        }

        // 2. Inicializar dist[] y pred[]
        dist = new double[V];
        pred = new int[V];
        boolean[] resuelto = new boolean[V];

        for (int i = 0; i < V; i++) {
            dist[i] = INF;
            pred[i] = -1;
        }

        Integer idxOrigen = idAIndice.get(idOrigen);
        if (idxOrigen == null) return;
        dist[idxOrigen] = 0.0;

        // 3. Cola de prioridad: Edge(from=ignorado, to=índice, weight=distancia)
        MinHeap heap = new MinHeap();
        heap.insert(new Edge(idxOrigen, idxOrigen, 0.0));

        while (!heap.isEmpty()) {
            Edge actual = heap.extractMin();
            int u = actual.to; // índice interno del vértice extraído

            if (resuelto[u]) continue;
            resuelto[u] = true;

            // Relajar vecinos
            ArrayList<Edge> aristas = graph.get(indiceAId[u]);
            if (aristas == null) continue;

            for (int i = 0; i < aristas.size(); i++) {
                Edge e = aristas.get(i);
                Integer idxV = idAIndice.get(e.to);
                if (idxV == null || resuelto[idxV]) continue;

                double nueva = dist[u] + e.weight;
                if (nueva < dist[idxV]) {
                    dist[idxV] = nueva;
                    pred[idxV] = u;
                    // Insertar versión actualizada en el heap
                    heap.insert(new Edge(u, idxV, nueva));
                }
            }
        }
    }

    /**
     * Retorna la distancia mínima desde el origen al vértice dado.
     *
     * @param idDestino ID real del vértice destino
     * @return distancia mínima, o {@link #INF} si no hay camino
     */
    public double getDistancia(int idDestino) {
        Integer idx = idAIndice.get(idDestino);
        return idx == null ? INF : dist[idx];
    }

    /**
     * Reconstruye el camino mínimo desde el origen hasta el destino.
     *
     * @param idDestino ID real del vértice destino
     * @return lista de IDs en orden origen → destino, vacía si no hay camino
     */
    public ArrayList<Integer> getCamino(int idDestino) {
        ArrayList<Integer> camino = new ArrayList<>();
        Integer idx = idAIndice.get(idDestino);
        if (idx == null || dist[idx] == INF) return camino;

        // Reconstruir con LinkedList como pila (add inserta al inicio)
        LinkedList<Integer> pila = new LinkedList<>();
        int actual = idx;
        while (actual != -1) {
            pila.add(indiceAId[actual]); // queda en orden origen→destino
            actual = pred[actual];
        }

        for (int i = 0; i < pila.size(); i++) {
            camino.add(pila.get(i));
        }
        return camino;
    }
}
