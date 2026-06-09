package cr.ac.tec.ce1103.logistec.planner;

import cr.ac.tec.ce1103.logistec.algorithms.DFS;
import cr.ac.tec.ce1103.logistec.graph.ArrayList;
import cr.ac.tec.ce1103.logistec.graph.HashMap;
import cr.ac.tec.ce1103.logistec.model.Package;
import cr.ac.tec.ce1103.logistec.model.Truck;

/**
 * Planificador de rutas y asignación de paquetes para LogísTEC.
 *
 * <p>Proporciona tres funcionalidades principales:
 * <ol>
 *   <li>Asignación best-fit de paquetes a camiones según prioridad y peso.</li>
 *   <li>Heurística del vecino más cercano (Nearest Neighbor) para ordenar paradas.</li>
 *   <li>Heurística basada en MST (2-aproximación) para ordenar paradas.</li>
 * </ol>
 * </p>
 *
 * @author Job Jimenez
 * @version 1.0
 * @see cr.ac.tec.ce1103.logistec.io.GraphLoader
 */
public class RoutePlanner {

    // ────────────────────────────────────────────────────────
    // Asignación best-fit
    // ────────────────────────────────────────────────────────

    /**
     * Asigna paquetes a camiones usando la estrategia best-fit.
     *
     * <p>Ordena los paquetes por prioridad ascendente y luego por peso
     * descendente. Para cada paquete, lo asigna al camión con mayor
     * capacidad libre que pueda alojarlo. Si ningún camión puede llevarlo,
     * el paquete se marca como rechazado.</p>
     *
     * @param paquetes lista de paquetes a asignar
     * @param camiones lista de camiones disponibles
     */
    public static void asignarBestFit(ArrayList<Package> paquetes, ArrayList<Truck> camiones) {
        if (paquetes == null || paquetes.isEmpty()) return;
        if (camiones == null || camiones.isEmpty()) {
            for (int i = 0; i < paquetes.size(); i++) {
                paquetes.get(i).marcarRechazado();
            }
            return;
        }

        // Ordenar paquetes: prioridad ascendente, luego peso descendente
        ordenarPaquetes(paquetes);

        // Para cada paquete, buscar el mejor camión
        for (int i = 0; i < paquetes.size(); i++) {
            Package p = paquetes.get(i);
            if (p.isRechazado()) continue;

            Truck mejor = null;
            int mejorLibre = -1;

            for (int j = 0; j < camiones.size(); j++) {
                Truck t = camiones.get(j);
                if (t.puedeLlevar(p.getPeso()) && t.capacidadLibre() > mejorLibre) {
                    mejor = t;
                    mejorLibre = t.capacidadLibre();
                }
            }

            if (mejor != null) {
                mejor.agregarCarga(p.getPeso());
                mejor.getParadas().add(p.getDestino()); //Agregue esta linea porque el camión no guardaba qué paquetes lleva, esto agrega el destino como parada
            } else {
                p.marcarRechazado();
            }
        }
    }

    /**
     * Ordena los paquetes por prioridad ascendente y luego peso descendente.
     *
     * @param paquetes lista a ordenar
     */
    private static void ordenarPaquetes(ArrayList<Package> paquetes) {
        // Bubble sort simple por claridad (lista pequeña en la práctica)
        int n = paquetes.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Package a = paquetes.get(j);
                Package b = paquetes.get(j + 1);
                boolean swap = false;

                if (a.getPrioridad() > b.getPrioridad()) {
                    swap = true;
                } else if (a.getPrioridad() == b.getPrioridad() && a.getPeso() < b.getPeso()) {
                    swap = true;
                }

                if (swap) {
                    Package temp = paquetes.get(j);
                    paquetes.set(j, paquetes.get(j + 1));
                    paquetes.set(j + 1, temp);
                }
            }
        }
    }

    // ────────────────────────────────────────────────────────
    // Nearest Neighbor
    // ────────────────────────────────────────────────────────

    /**
     * Calcula la ruta usando la heurística del vecino más cercano.
     *
     * <p>Parte del depósito y en cada paso visita la parada no visitada
     * más cercana a la posición actual. Al finalizar, retorna al depósito.</p>
     *
     * @param distMatrix matriz V×V de distancias mínimas (Floyd-Warshall)
     * @param stops      arreglo con los índices de las paradas a visitar
     * @param depotIndex índice del depósito
     * @return lista de índices de vértice en orden de visita, comenzando
     *         por el depósito y terminando en el depósito
     */
    public static ArrayList<Integer> nearestNeighbor(
            double[][] distMatrix, int[] stops, int depotIndex) {

        ArrayList<Integer> ruta = new ArrayList<>();
        ruta.add(depotIndex);

        int n = stops.length;
        if (n == 0) {
            ruta.add(depotIndex);
            return ruta;
        }

        boolean[] visitado = new boolean[n];
        int current = depotIndex;

        for (int step = 0; step < n; step++) {
            int masCercano = -1;
            double menorDist = Double.POSITIVE_INFINITY;

            for (int i = 0; i < n; i++) {
                if (!visitado[i]) {
                    double d = distMatrix[current][stops[i]];
                    if (d < menorDist) {
                        menorDist = d;
                        masCercano = i;
                    }
                }
            }

            if (masCercano == -1) break;
            visitado[masCercano] = true;
            current = stops[masCercano];
            ruta.add(current);
        }

        // Regresar al depósito
        ruta.add(depotIndex);
        return ruta;
    }

    /**
     * Calcula la distancia total de una ruta.
     *
     * @param ruta       lista de índices de vértice en orden de visita
     * @param distMatrix matriz V×V de distancias mínimas
     * @return distancia total recorrida
     */
    public static double calcularDistanciaRuta(ArrayList<Integer> ruta, double[][] distMatrix) {
        double total = 0;
        for (int i = 0; i < ruta.size() - 1; i++) {
            total += distMatrix[ruta.get(i)][ruta.get(i + 1)];
        }
        return total;
    }

    // ────────────────────────────────────────────────────────
    // Heurística MST-based (2-aproximación)
    // ────────────────────────────────────────────────────────

    /**
     * Calcula la ruta usando la heurística basada en MST (2-aproximación).
     *
     * <p>Construye el MST inducido por {depósito} ∪ {paradas} usando las
     * distancias mínimas de la matriz Floyd-Warshall como pesos. Luego
     * realiza un recorrido DFS en preorden del MST para determinar el
     * orden de visita. Esta heurística garantiza una solución a lo sumo
     * 2 veces la óptima para distancias métricas.</p>
     *
     * @param distMatrix matriz V×V de distancias mínimas (Floyd-Warshall)
     * @param stops      arreglo con los índices de las paradas a visitar
     * @param depotIndex índice del depósito
     * @return lista de índices de vértice en orden de visita, comenzando
     *         por el depósito y terminando en el depósito, o {@code null}
     *         si el stub de DFS aún no ha sido implementado
     */
    public static ArrayList<Integer> mstBased(
            double[][] distMatrix, int[] stops, int depotIndex) {

        // Construir conjunto de vértices: {depósito} ∪ {paradas}
        int n = stops.length;
        int m = n + 1; // +1 por el depósito
        int[] vertices = new int[m];
        vertices[0] = depotIndex;
        for (int i = 0; i < n; i++) {
            vertices[i + 1] = stops[i];
        }

        // Construir MST con Prim O(m²) sobre la matriz densa
        HashMap<Integer, ArrayList<Integer>> mstAdj = new HashMap<>();
        for (int i = 0; i < m; i++) {
            mstAdj.put(vertices[i], new ArrayList<>());
        }

        boolean[] inMST = new boolean[m];
        double[] minWeight = new double[m];
        int[] parent = new int[m];

        for (int i = 0; i < m; i++) {
            minWeight[i] = Double.POSITIVE_INFINITY;
            parent[i] = -1;
        }
        minWeight[0] = 0;

        for (int iter = 0; iter < m; iter++) {
            // Extraer vértice no incluido con menor peso
            int u = -1;
            double min = Double.POSITIVE_INFINITY;
            for (int i = 0; i < m; i++) {
                if (!inMST[i] && minWeight[i] < min) {
                    min = minWeight[i];
                    u = i;
                }
            }

            if (u == -1) break;
            inMST[u] = true;

            // Agregar arista al MST (excepto la raíz)
            if (parent[u] != -1) {
                int uIdx = vertices[u];
                int pIdx = vertices[parent[u]];
                mstAdj.get(uIdx).add(pIdx);
                mstAdj.get(pIdx).add(uIdx);
            }

            // Relajar aristas hacia vértices no incluidos
            for (int v = 0; v < m; v++) {
                if (!inMST[v]) {
                    double d = distMatrix[vertices[u]][vertices[v]];
                    if (d < minWeight[v]) {
                        minWeight[v] = d;
                        parent[v] = u;
                    }
                }
            }
        }

        // Recorrer el MST en preorden usando el stub de DFS
        try {
            ArrayList<Integer> preorder = DFS.preorder(mstAdj, depotIndex);
            if (preorder == null || preorder.isEmpty()) {
                return null;
            }

            // Construir ruta: preorder + regreso al depósito
            ArrayList<Integer> ruta = new ArrayList<>();
            ruta.add(depotIndex);
            for (int i = 0; i < preorder.size(); i++) {
                int v = preorder.get(i);
                if (v != depotIndex) {
                    ruta.add(v);
                }
            }
            ruta.add(depotIndex);
            return ruta;

        } catch (UnsupportedOperationException e) {
            // Stub no implementado aún
            return null;
        }
    }

    // ────────────────────────────────────────────────────────
    // Utilidad: obtener paradas asignadas a un camión
    // ────────────────────────────────────────────────────────

    /**
     * Obtiene los índices de las paradas asignadas a un camión.
     *
     * <p>Convierte los identificadores de vértice (strings) de las paradas
     * del camión a sus índices enteros en el grafo.</p>
     *
     * @param truck       camión con paradas como identificadores string
     * @param vertexIndex mapeo de identificador a índice entero
     * @return arreglo con los índices de las paradas
     */
    public static int[] obtenerIndicesParadas(Truck truck, HashMap<String, Integer> vertexIndex) {
        ArrayList<String> paradas = truck.getParadas();
        int n = paradas.size();
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = vertexIndex.get(paradas.get(i));
        }
        return indices;
    }
}
