package cr.ac.tec.ce1103.logistec;

import cr.ac.tec.ce1103.logistec.algorithms.FloydWarshall;
import cr.ac.tec.ce1103.logistec.algorithms.Kruskal;
import cr.ac.tec.ce1103.logistec.algorithms.Warshall;
import cr.ac.tec.ce1103.logistec.graph.ArrayList;
import cr.ac.tec.ce1103.logistec.graph.Edge;
import cr.ac.tec.ce1103.logistec.graph.HashMap;
import cr.ac.tec.ce1103.logistec.graph.Prim;
import cr.ac.tec.ce1103.logistec.io.GraphLoader;
import cr.ac.tec.ce1103.logistec.io.GraphLoader.GraphData;
import cr.ac.tec.ce1103.logistec.model.Package;
import cr.ac.tec.ce1103.logistec.model.Truck;
import cr.ac.tec.ce1103.logistec.planner.RoutePlanner;

/**
 * Aplicación principal del proyecto LogísTEC.
 *
 * <p>Integra la carga de datos, validación del grafo, cálculo del MST,
 * asignación de paquetes a camiones, planificación de rutas y generación
 * del reporte final. Los algoritmos marcados como stub serán implementados
 * por el equipo en una fase posterior.</p>
 *
 * @author Job Jimenez
 * @version 1.0
 * @see GraphLoader
 * @see RoutePlanner
 */
public class LogisTEC {

    /** Matriz de distancias mínimas (Floyd-Warshall) disponible globalmente. */
    private double[][] distMatrix;

    /** Datos cargados desde el JSON. */
    private GraphData data;

    /**
     * Ejecuta el flujo completo de LogísTEC.
     *
     * @param jsonPath ruta al archivo JSON de configuración
     */
    public void ejecutar(String jsonPath) {
        System.out.println("=== LogísTEC — Planificación Logística ===\n");

        // ── Paso 1: Carga ──────────────────────────────────
        try {
            data = GraphLoader.load(jsonPath);
            System.out.println("Grafo cargado: " + data.V + " vértices");
            System.out.println("Depósito: " + data.vertexId.get(data.depotIndex));
            System.out.println("Paquetes: " + data.paquetes.size());
            System.out.println("Camiones: " + data.camiones.size());
            System.out.println();
        } catch (Exception e) {
            System.err.println("Error al cargar JSON: " + e.getMessage());
            return;
        }

        // ── Paso 2: Validación (Warshall) ──────────────────
        validarAlcanzabilidad();

        // ── Paso 3: Matriz de distancias (Floyd-Warshall) ──
        calcularMatrizDistancias();

        // ── Paso 4: MST con Prim ────────────────────────────
        generarMSTPrim();

        // ── Paso 5: MST con Kruskal ─────────────────────────
        generarMSTKruskal();

        // ── Paso 6: Asignación best-fit ─────────────────────
        asignarPaquetes();

        // ── Paso 7: Planificar rutas por camión ─────────────
        planificarRutas();

        // ── Paso 8: Reporte final ───────────────────────────
        generarReporte();

        // ── Paso 9: Mostrar UI ──────────────────────────────
        mostrarUI();
    }

    /**
     * Verifica la alcanzabilidad de los paquetes desde el depósito.
     *
     * <p>Utiliza el algoritmo de Warshall (stub por implementar).
     * Marca como rechazados los paquetes con destino inalcanzable.</p>
     */
    private void validarAlcanzabilidad() {
        System.out.println("--- Validación de alcanzabilidad ---");
        try {
            boolean[][] reachable = Warshall.transitiveClosure(data.graph, data.V);
            int rechazados = 0;
            for (int i = 0; i < data.paquetes.size(); i++) {
                Package p = data.paquetes.get(i);
                Integer idx = data.vertexIndex.get(p.getDestino());
                if (idx != null && !reachable[data.depotIndex][idx]) {
                    p.marcarRechazado();
                    rechazados++;
                }
            }
            System.out.println("Paquetes inalcanzables marcados: " + rechazados);
        } catch (UnsupportedOperationException e) {
            System.out.println("[Warshall] Pendiente de implementación por el equipo.");
        }
        System.out.println();
    }

    /**
     * Calcula la matriz de distancias mínimas con Floyd-Warshall.
     *
     * <p>(Stub por implementar.)</p>
     */
    private void calcularMatrizDistancias() {
        System.out.println("--- Matriz de distancias mínimas ---");
        try {
            distMatrix = FloydWarshall.shortestPaths(data.graph, data.V);
            System.out.println("Matriz " + data.V + "×" + data.V + " calculada.");
        } catch (UnsupportedOperationException e) {
            System.out.println("[FloydWarshall] Pendiente de implementación por el equipo.");
            // Inicializar matriz por defecto para evitar NPE
            distMatrix = new double[data.V][data.V];
            for (int i = 0; i < data.V; i++) {
                for (int j = 0; j < data.V; j++) {
                    distMatrix[i][j] = (i == j) ? 0 : Double.POSITIVE_INFINITY;
                }
            }
        }
        System.out.println();
    }

    /**
     * Construye el MST usando el algoritmo de Prim (ya implementado).
     */
    private void generarMSTPrim() {
        System.out.println("--- MST con Prim ---");
        try {
            Prim prim = new Prim(data.graph);
            ArrayList<Edge> mst = prim.buildMST(data.depotIndex);
            Prim.printMST(mst);
        } catch (Exception e) {
            System.out.println("[Prim] Error: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * Construye el MST usando el algoritmo de Kruskal (stub).
     */
    private void generarMSTKruskal() {
        System.out.println("--- MST con Kruskal ---");
        try {
            ArrayList<Edge> allEdges = obtenerTodasLasAristas();
            ArrayList<Edge> mst = Kruskal.buildMST(data.V, allEdges);
            Prim.printMST(mst);
        } catch (UnsupportedOperationException e) {
            System.out.println("[Kruskal] Pendiente de implementación por el equipo.");
        }
        System.out.println();
    }

    /**
     * Obtiene una lista plana con todas las aristas del grafo (sin duplicados).
     *
     * @return lista de aristas únicas
     */
    private ArrayList<Edge> obtenerTodasLasAristas() {
        ArrayList<Edge> allEdges = new ArrayList<>();
        for (int i = 0; i < data.V; i++) {
            ArrayList<Edge> edges = data.graph.get(i);
            if (edges == null) continue;
            for (int j = 0; j < edges.size(); j++) {
                Edge e = edges.get(j);
                // Evitar duplicados: agregar solo si from < to
                if (e.from < e.to) {
                    allEdges.add(e);
                }
            }
        }
        return allEdges;
    }

    /**
     * Asigna paquetes a camiones usando best-fit.
     */
    private void asignarPaquetes() {
        System.out.println("--- Asignación de paquetes ---");
        RoutePlanner.asignarBestFit(data.paquetes, data.camiones);
        int asignados = 0;
        int rechazados = 0;
        for (int i = 0; i < data.paquetes.size(); i++) {
            Package p = data.paquetes.get(i);
            if (p.isRechazado()) {
                rechazados++;
            } else {
                asignados++;
            }
        }
        System.out.println("Paquetes asignados: " + asignados);
        System.out.println("Paquetes rechazados: " + rechazados);
        System.out.println();
    }

    /**
     * Planifica la ruta de cada camión usando NN y MST-based.
     */
    private void planificarRutas() {
        System.out.println("--- Planificación de rutas ---");

        // Construir mapeo: destino (string) → lista de paquetes para ese destino
        // Luego asignar esas paradas al camión que recibió los paquetes
        HashMap<String, ArrayList<Package>> destinoPaquetes = new HashMap<>();
        for (int i = 0; i < data.paquetes.size(); i++) {
            Package p = data.paquetes.get(i);
            if (p.isRechazado()) continue;
            if (!destinoPaquetes.containsKey(p.getDestino())) {
                destinoPaquetes.put(p.getDestino(), new ArrayList<>());
            }
            destinoPaquetes.get(p.getDestino()).add(p);
        }

        // Asignar paradas a los camiones según los paquetes que recibieron
        // Nota: en best-fit los paquetes se asignan a camiones pero no guardamos
        // esa relación. Para este prototipo, agrupamos por destino y asignamos
        // equitativamente entre los camiones con carga.
        ArrayList<Truck> camionesConCarga = new ArrayList<>();
        for (int i = 0; i < data.camiones.size(); i++) {
            if (data.camiones.get(i).getCargaActual() > 0) {
                camionesConCarga.add(data.camiones.get(i));
            }
        }

        if (camionesConCarga.size() == 0) {
            System.out.println("No hay camiones con carga asignada.");
            System.out.println();
            return;
        }

        // Distribuir los destinos entre los camiones con carga
        Object[] destinos = destinoPaquetes.keys();
        int idx = 0;
        for (int i = 0; i < destinos.length; i++) {
            String destino = (String) destinos[i];
            Truck t = camionesConCarga.get(idx % camionesConCarga.size());
            t.getParadas().add(destino);
            idx++;
        }

        // Para cada camión, calcular rutas con NN y MST-based
        for (int i = 0; i < data.camiones.size(); i++) {
            Truck t = data.camiones.get(i);
            if (t.getParadas().isEmpty()) continue;

            System.out.println("Camión " + t.getId() + " (" + t.getCargaActual()
                + "/" + t.getCapacidad() + " kg, "
                + String.format("%.1f", t.porcentajeOcupacion()) + "% ocupado)");
            System.out.println("  Paradas: " + t.getParadas());

            int[] stops = RoutePlanner.obtenerIndicesParadas(t, data.vertexIndex);
            double mejorDist = Double.POSITIVE_INFINITY;
            ArrayList<Integer> mejorRuta = null;
            String metodo = "";

            // Nearest Neighbor
            ArrayList<Integer> rutaNN = RoutePlanner.nearestNeighbor(distMatrix, stops, data.depotIndex);
            double distNN = RoutePlanner.calcularDistanciaRuta(rutaNN, distMatrix);
            System.out.println("  NN: distancia=" + String.format("%.0f", distNN) + " m");
            mejorDist = distNN;
            mejorRuta = rutaNN;
            metodo = "NN";

            // MST-based
            ArrayList<Integer> rutaMST = RoutePlanner.mstBased(distMatrix, stops, data.depotIndex);
            if (rutaMST != null) {
                double distMST = RoutePlanner.calcularDistanciaRuta(rutaMST, distMatrix);
                double mejora = ((distNN - distMST) / distNN) * 100.0;
                System.out.println("  MST: distancia=" + String.format("%.0f", distMST) + " m"
                    + " (mejora: " + String.format("%.1f", mejora) + "%)");

                if (distMST < mejorDist) {
                    mejorDist = distMST;
                    mejorRuta = rutaMST;
                    metodo = "MST-based";
                }
            } else {
                System.out.println("  MST: pendiente de implementación");
            }

            // Mostrar ruta elegida
            System.out.print("  Ruta (" + metodo + "): ");
            for (int j = 0; j < mejorRuta.size(); j++) {
                if (j > 0) System.out.print(" → ");
                System.out.print(data.vertexId.get(mejorRuta.get(j)));
            }
            System.out.println();
            System.out.println("  Distancia total: " + String.format("%.0f", mejorDist) + " m\n");
        }
    }

    /**
     * Genera el reporte final con el resumen de la planificación.
     */
    private void generarReporte() {
        System.out.println("=== Reporte final ===");
        System.out.println("Total paquetes: " + data.paquetes.size());
        int entregados = 0;
        int rechazados = 0;
        for (int i = 0; i < data.paquetes.size(); i++) {
            Package p = data.paquetes.get(i);
            if (p.isRechazado()) {
                rechazados++;
                System.out.println("  Rechazado: " + p.getId() + " → " + p.getDestino()
                    + " (" + p.getPeso() + " kg, prio=" + p.getPrioridad() + ")");
            } else {
                entregados++;
            }
        }
        System.out.println("Entregados: " + entregados + " | Rechazados: " + rechazados);
        System.out.println();
    }

    /**
     * Muestra la interfaz gráfica (Swing) con el grafo y las rutas.
     */
    private void mostrarUI() {
        try {
            cr.ac.tec.ce1103.logistec.ui.LogisTECFrame frame =
                new cr.ac.tec.ce1103.logistec.ui.LogisTECFrame(data);
            frame.setVisible(true);
        } catch (Exception e) {
            System.out.println("[UI] No se pudo iniciar la interfaz gráfica: " + e.getMessage());
        }
    }

    // ────────────────────────────────────────────────────────
    // Punto de entrada
    // ────────────────────────────────────────────────────────

    /**
     * Punto de entrada de la aplicación LogísTEC.
     *
     * @param args argumentos de línea de comandos; args[0] debe ser la ruta
     *             al archivo JSON de configuración
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java cr.ac.tec.ce1103.logistec.LogisTEC <archivo.json>");
            return;
        }
        new LogisTEC().ejecutar(args[0]);
    }
}
