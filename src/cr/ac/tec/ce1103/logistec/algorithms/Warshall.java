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
 * @author Luis Acuña
 * @version 1.0
 * @see FloydWarshall
 */
public class Warshall {

    /**
     * Matriz de alcanzabilidad resultante.
     * alcanzable[i][j] = true si existe camino del vértice con índice i al j.
     */
    private boolean[][] alcanzable;

    /**
     * Mapeo ID real del vértice -> índice en la matriz.
     */
    private HashMap<Integer, Integer> idAIndice;

    /**
     * Mapeo inverso: índice -> ID real.
     */
    private int[] indiceAId;

    /** Número de vértices. */
    private int V;

    /**
     * Construye y ejecuta el algoritmo de Warshall sobre el grafo dado.
     *
     * @param graph lista de adyacencia del grafo (ID vértice → aristas)
     */
    public Warshall(HashMap<Integer, ArrayList<Edge>> graph) {

        // --- 1. Construir mapeo ID <-> índice ---
        Object[] nodos = graph.keys();
        V = nodos.length;

        idAIndice = new HashMap<>();
        indiceAId = new int[V];

        for (int i = 0; i < V; i++) {
            int id = (Integer) nodos[i];
            idAIndice.put(id, i);
            indiceAId[i] = id;
        }

        // --- 2. Inicializar matriz booleana ---
        // Un vértice siempre se alcanza a sí mismo
        alcanzable = new boolean[V][V];
        for (int i = 0; i < V; i++) {
            alcanzable[i][i] = true;
        }

        // --- 3. Cargar aristas directas: si hay arista u->v, u alcanza v ---
        for (int i = 0; i < V; i++) {
            int idOrigen = indiceAId[i];
            ArrayList<Edge> aristas = graph.get(idOrigen);
            if (aristas == null) continue;

            for (int j = 0; j < aristas.size(); j++) {
                Edge e = aristas.get(j);
                Integer idxDestino = idAIndice.get(e.to);
                if (idxDestino != null) {
                    alcanzable[i][idxDestino] = true;
                }
            }
        }

        // --- 4. Núcleo de Warshall ---
        // Para cada vértice intermedio k:
        // si i alcanza k  Y  k alcanza j  =>  i alcanza j
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                if (!alcanzable[i][k]) continue; // optimización: saltar
                for (int j = 0; j < V; j++) {
                    if (alcanzable[k][j]) {
                        alcanzable[i][j] = true;
                    }
                }
            }
        }
    }

    /**
     * Indica si el vértice {@code idOrigen} puede alcanzar {@code idDestino}.
     *
     * @param idOrigen  ID real del vértice origen
     * @param idDestino ID real del vértice destino
     * @return {@code true} si existe algún camino entre ellos
     */
    public boolean esAlcanzable(int idOrigen, int idDestino) {
        Integer i = idAIndice.get(idOrigen);
        Integer j = idAIndice.get(idDestino);
        if (i == null || j == null) return false;
        return alcanzable[i][j];
    }

    /**
     * Retorna la matriz de alcanzabilidad completa (por índice interno).
     *
     * @return matriz booleana V×V
     */
    public boolean[][] getMatriz() {
        return alcanzable;
    }

    /**
     * Dado el depósito, reporta qué paquetes tienen destino inalcanzable.
     *
     * <p>Uso típico desde el validador del sistema:</p>
     * <pre>
     *   Warshall w = new Warshall(grafo.getListaAdyacencia());
     *   ArrayList&lt;String&gt; rechazados = w.paquetesInalcanzables(
     *       idDepósito, destinosPaquetes, idsPaquetes);
     * </pre>
     *
     * @param idDeposito      ID del vértice depósito
     * @param destinos        arreglo con el ID de destino de cada paquete
     * @param idsPaquetes     arreglo con el identificador string de cada paquete
     * @return lista con los IDs de paquetes cuyo destino no es alcanzable
     */
    public ArrayList<String> paquetesInalcanzables(
            int idDeposito, int[] destinos, String[] idsPaquetes) {

        ArrayList<String> rechazados = new ArrayList<>();
        for (int p = 0; p < destinos.length; p++) {
            if (!esAlcanzable(idDeposito, destinos[p])) {
                rechazados.add(idsPaquetes[p]);
            }
        }
        return rechazados;
    }

    /**
     * Imprime la matriz de alcanzabilidad en consola.
     */
    public void imprimirMatriz() {
        System.out.print("     ");
        for (int j = 0; j < V; j++) {
            System.out.printf("%4d", indiceAId[j]);
        }
        System.out.println();

        for (int i = 0; i < V; i++) {
            System.out.printf("%4d ", indiceAId[i]);
            for (int j = 0; j < V; j++) {
                System.out.printf("%4s", alcanzable[i][j] ? "T" : ".");
            }
            System.out.println();
        }
    }

    // ---------------------------------------------------------------
    // Método estático de compatibilidad con el stub original
    // ---------------------------------------------------------------

    /**
     * Versión estática para compatibilidad con el stub existente.
     *
     * @param graph lista de adyacencia
     * @param V     número de vértices (se infiere del grafo, parámetro ignorado)
     * @return matriz booleana de alcanzabilidad por índice interno
     */
    public static boolean[][] transitiveClosure(
            HashMap<Integer, ArrayList<Edge>> graph, int V) {
        return new Warshall(graph).getMatriz();
    }
}
