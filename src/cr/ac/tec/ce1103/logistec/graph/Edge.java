package cr.ac.tec.ce1103.logistec.graph;

/**
 * Representa una arista ponderada y no dirigida entre dos nodos de un grafo.
 *
 * <p>Diseñada como un POJO simple: los campos son públicos para evitar
 * ruido de getters en el código que la consume (Prim, Kruskal, etc.).</p>
 *
 * @author Job Jimenez
 * @version 1.0
 * @see MinHeap
 * @see Prim
 */
public class Edge {

    /** Nodo de origen de la arista. */
    public int from;

    /** Nodo de destino de la arista. */
    public int to;

    /** Peso o costo asociado a esta arista. */
    public double weight;

    /**
     * Construye una arista entre dos nodos con el peso dado.
     *
     * @param from   nodo de origen
     * @param to     nodo de destino
     * @param weight peso de la arista; debe ser un valor positivo
     */
    public Edge(int from, int to, double weight) {
        this.from   = from;
        this.to     = to;
        this.weight = weight;
    }

    /**
     * Retorna una representación legible de la arista.
     *
     * @return cadena con formato {@code (from -> to, w=peso)}
     */
    @Override
    public String toString() {
        return "(" + from + " -> " + to + ", w=" + weight + ")";
    }
}