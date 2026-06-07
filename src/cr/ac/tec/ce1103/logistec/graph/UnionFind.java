package cr.ac.tec.ce1103.logistec.graph;

/**
 * Estructura de datos Union-Find (Disjoint Set Union) con optimizaciones.
 *
 * <p>Implementa path compression en {@link #find(int)} y union by rank
 * en {@link #union(int, int)} para lograr operaciones casi O(1) amortizado.
 * Ideal para detectar ciclos y componentes conexas en grafos.</p>
 *
 * @author Job Jimenez
 * @version 1.0
 * @see Kruskal
 */
public class UnionFind {

    /** Arreglo de padres para cada nodo. */
    private final int[] parent;

    /** Rango (profundidad aproximada) de cada árbol. */
    private final int[] rank;

    /** Cantidad actual de conjuntos disjuntos. */
    private int components;

    /**
     * Inicializa la estructura con {@code n} elementos,
     * cada uno en su propio conjunto.
     *
     * @param n número de elementos
     */
    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        components = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    /**
     * Encuentra la raíz (representante) del conjunto que contiene {@code x}.
     *
     * <p>Aplica path compression: todos los nodos en el camino
     * apuntan directamente a la raíz para optimizar futuras búsquedas.</p>
     *
     * @param x elemento cuya raíz se busca
     * @return raíz del conjunto que contiene a {@code x}
     */
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    /**
     * Une los conjuntos que contienen a {@code x} e {@code y}.
     *
     * <p>Usa union by rank: el árbol más pequeño queda bajo el más grande
     * para mantener el árbol lo más plano posible.</p>
     *
     * @param x primer elemento
     * @param y segundo elemento
     * @return {@code true} si se realizó la unión, {@code false} si ya estaban unidos
     */
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) {
            return false;
        }

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }

        components--;
        return true;
    }

    /**
     * Verifica si dos elementos pertenecen al mismo conjunto.
     *
     * @param x primer elemento
     * @param y segundo elemento
     * @return {@code true} si están conectados, {@code false} en caso contrario
     */
    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    /**
     * Retorna la cantidad actual de conjuntos disjuntos.
     *
     * @return número de componentes conexas
     */
    public int getComponents() {
        return components;
    }
}
