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
 * @author Luis Acuña
 * @version 1.0
 */
public class FloydWarshall {

    /** Constante que representa distancia infinita (no hay camino). */
    public static final double INF = Double.POSITIVE_INFINITY;

    /**
     * Matriz de distancias resultante. dist[i][j] = distancia mínima
     * entre el vértice con índice i y el vértice con índice j.
     */
    private double[][] dist;

    /**
     * Mapeo de ID real del vértice -> índice en la matriz.
     * Necesario porque los IDs no son necesariamente 0..V-1.
     */
    private HashMap<Integer, Integer> idAIndice;

    /**
     * Mapeo inverso: índice en la matriz -> ID real del vértice.
     */
    private int[] indiceAId;

    /**
     * Número de vértices.
     */
    private int V;

    /**
     * Construye y ejecuta Floyd-Warshall sobre el grafo dado.
     *
     * @param graph lista de adyacencia del grafo (ID vértice → aristas)
     */
    public FloydWarshall(HashMap<Integer, ArrayList<Edge>> graph) {
        // --- 1. Recolectar todos los IDs y asignarles un índice ---
        Object[] nodos = graph.keys();
        V = nodos.length;

        idAIndice  = new HashMap<>();
        indiceAId  = new int[V];

        for (int i = 0; i < V; i++) {
            int id = (Integer) nodos[i];
            idAIndice.put(id, i);
            indiceAId[i] = id;
        }

        // --- 2. Inicializar matriz con INF, cero en diagonal ---
        dist = new double[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = (i == j) ? 0.0 : INF;
            }
        }

        // --- 3. Cargar pesos de las aristas existentes ---
        for (int i = 0; i < V; i++) {
            int idOrigen = indiceAId[i];
            ArrayList<Edge> aristas = graph.get(idOrigen);
            if (aristas == null) continue;

            for (int j = 0; j < aristas.size(); j++) {
                Edge e = aristas.get(j);
                Integer idxDestino = idAIndice.get(e.to);
                if (idxDestino == null) continue; // vértice desconocido

                // Si hay aristas paralelas, quedarse con la de menor peso
                if (e.weight < dist[i][idxDestino]) {
                    dist[i][idxDestino] = e.weight;
                }
            }
        }

        // --- 4. Núcleo de Floyd-Warshall ---
        // Para cada vértice intermedio k, intentar mejorar dist[i][j]
        // pasando por k: dist[i][k] + dist[k][j]
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                if (dist[i][k] == INF) continue; // optimización: saltar
                for (int j = 0; j < V; j++) {
                    if (dist[k][j] == INF) continue;
                    double candidato = dist[i][k] + dist[k][j];
                    if (candidato < dist[i][j]) {
                        dist[i][j] = candidato;
                    }
                }
            }
        }
    }

    /**
     * Retorna la distancia mínima entre dos vértices por su ID real.
     *
     * @param idOrigen  ID del vértice origen
     * @param idDestino ID del vértice destino
     * @return distancia mínima, o {@link #INF} si no hay camino
     */
    public double getDistancia(int idOrigen, int idDestino) {
        Integer i = idAIndice.get(idOrigen);
        Integer j = idAIndice.get(idDestino);
        if (i == null || j == null) return INF;
        return dist[i][j];
    }

    /**
     * Verifica si existe algún camino entre dos vértices.
     *
     * @param idOrigen  ID del vértice origen
     * @param idDestino ID del vértice destino
     * @return {@code true} si hay camino
     */
    public boolean hayCamino(int idOrigen, int idDestino) {
        return getDistancia(idOrigen, idDestino) < INF;
    }

    /**
     * Retorna la matriz completa de distancias (por índice, no por ID).
     * Útil para el planificador de rutas.
     *
     * @return matriz V×V de distancias mínimas
     */
    public double[][] getMatriz() {
        return dist;
    }

    /**
     * Retorna el índice interno de un vértice dado su ID real.
     *
     * @param id ID del vértice
     * @return índice en la matriz, o -1 si no existe
     */
    public int getIndice(int id) {
        Integer idx = idAIndice.get(id);
        return idx == null ? -1 : idx;
    }

    /**
     * Retorna el ID real de un vértice dado su índice interno.
     *
     * @param indice índice en la matriz
     * @return ID real del vértice
     */
    public int getId(int indice) {
        return indiceAId[indice];
    }

    /**
     * Imprime la matriz de distancias en consola.
     */
    public void imprimirMatriz() {
        System.out.print("     ");
        for (int j = 0; j < V; j++) {
            System.out.printf("%6d", indiceAId[j]);
        }
        System.out.println();

        for (int i = 0; i < V; i++) {
            System.out.printf("%4d ", indiceAId[i]);
            for (int j = 0; j < V; j++) {
                if (dist[i][j] == INF) {
                    System.out.print("   INF");
                } else {
                    System.out.printf("%6.0f", dist[i][j]);
                }
            }
            System.out.println();
        }
    }

    // ---------------------------------------------------------------
    // Método estático de compatibilidad con el stub original
    // ---------------------------------------------------------------

    /**
     * Versión estática para compatibilidad con el stub existente.
     * Internamente construye una instancia y devuelve la matriz.
     *
     * @param graph lista de adyacencia
     * @param V     número de vértices (no usado, se infiere del grafo)
     * @return matriz de distancias mínimas por índice interno
     */
    public static double[][] shortestPaths(
            HashMap<Integer, ArrayList<Edge>> graph, int V) {
        return new FloydWarshall(graph).getMatriz();
    }
}
