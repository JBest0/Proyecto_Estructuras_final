// Union-Find con path compression + union by rank
class UnionFind {
    private final int[] parent;
    private final int[] rank;
    private int components; // Número de conjuntos disjuntos

    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        components = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            // rank[] empieza en 0 (Java ya lo hace, explícito por claridad)
        }
    }

    // Path compression: todos los nodos en el camino apuntan directo a la raíz
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Recursión + compresión
        }
        return parent[x];
    }

    // Union by rank: el árbol más pequeño queda bajo el más grande
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) return false;

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

    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    // Cuántos conjuntos disjuntos hay actualmente
    public int getComponents() {
        return components;
    }
}
