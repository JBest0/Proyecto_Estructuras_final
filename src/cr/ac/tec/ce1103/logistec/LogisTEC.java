package cr.ac.tec.ce1103.logistec;

import cr.ac.tec.ce1103.logistec.algorithms.BFS;
import cr.ac.tec.ce1103.logistec.algorithms.Dijkstra;
import cr.ac.tec.ce1103.logistec.algorithms.FloydWarshall;
import cr.ac.tec.ce1103.logistec.algorithms.Kruskal;
import cr.ac.tec.ce1103.logistec.algorithms.Prim;
import cr.ac.tec.ce1103.logistec.algorithms.Warshall;
import cr.ac.tec.ce1103.logistec.graph.HashSet;
import cr.ac.tec.ce1103.logistec.graph.LinkedList;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import cr.ac.tec.ce1103.logistec.graph.ArrayList;
import cr.ac.tec.ce1103.logistec.graph.Edge;
import cr.ac.tec.ce1103.logistec.graph.HashMap;
import cr.ac.tec.ce1103.logistec.io.GraphLoader;
import cr.ac.tec.ce1103.logistec.io.GraphLoader.GraphData;
import cr.ac.tec.ce1103.logistec.model.Package;
import cr.ac.tec.ce1103.logistec.model.Truck;
import cr.ac.tec.ce1103.logistec.planner.RoutePlanner;

public class LogisTEC {

    private double[][] distMatrix;

    private GraphData data;

    private HashMap<String, Double> truckDistancia;

    private HashMap<String, Double> truckMejora;

    private HashMap<String, ArrayList<Integer>> truckRutas;

    public void ejecutar(String jsonPath) {
        System.out.println("=== LogisTEC - Planificacion Logistica ===\n");

        // Paso 1: Carga
        try {
            data = GraphLoader.load(jsonPath);
            System.out.println("Grafo cargado: " + data.V + " vertices");
            System.out.println("Deposito: " + data.vertexId.get(data.depotIndex));
            System.out.println("Paquetes: " + data.paquetes.size());
            System.out.println("Camiones: " + data.camiones.size());
            System.out.println();
        } catch (Exception e) {
            System.err.println("Error al cargar JSON: " + e.getMessage());
            return;
        }

        try {
            validarAlcanzabilidad();
            recorrerBFS();
            consultarCaminoMasCorto();
            calcularMatrizDistancias();
            compararMST();
            asignarPaquetes();
            planificarRutas();
            generarReporte();
            mostrarUI();
        } catch (Exception e) {
            System.err.println("[ERROR] Fallo durante la ejecucion: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private void validarAlcanzabilidad() {
        System.out.println("--- Validacion de alcanzabilidad ---");
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
            System.out.println("[Warshall] Pendiente de implementacion por el equipo.");
        }
        System.out.println();
    }

    private void consultarCaminoMasCorto() {
        System.out.println("--- Dijkstra: Camino mas corto desde deposito ---");
        try {
            Dijkstra d = new Dijkstra(data.graph, data.depotIndex);
            for (int i = 0; i < data.paquetes.size(); i++) {
                Package p = data.paquetes.get(i);
                if (p.isRechazado()) continue;
                Integer idxDest = data.vertexIndex.get(p.getDestino());
                if (idxDest == null) continue;
                double dist = d.getDistancia(idxDest);
                ArrayList<Integer> camino = d.getCamino(idxDest);
                System.out.print("  " + p.getId() + " -> " + p.getDestino() + ": ");
                if (dist == Dijkstra.INF) {
                    System.out.println("inalcanzable");
                } else {
                    for (int j = 0; j < camino.size(); j++) {
                        if (j > 0) System.out.print("->");
                        System.out.print(data.vertexId.get(camino.get(j)));
                    }
                    System.out.println(" | dist=" + String.format("%.0f", dist) + " m");
                }
            }
        } catch (Exception e) {
            System.out.println("[Dijkstra] Error: " + e.getMessage());
        }
        System.out.println();
    }

    private void recorrerBFS() {
        System.out.println("--- BFS desde deposito ---");
        try {
            BFS bfs = new BFS();
            for (int i = 0; i < data.V; i++) {
                ArrayList<Edge> edges = data.graph.get(i);
                if (edges != null) {
                    for (int j = 0; j < edges.size(); j++) {
                        Edge e = edges.get(j);
                        bfs.addEdge(e.from, e.to);
                    }
                }
            }
            HashSet<Integer> visited = new HashSet<>();
            LinkedList<Integer> queue = new LinkedList<>();
            visited.add(data.depotIndex);
            queue.add(data.depotIndex);

            System.out.print("  Orden BFS: ");
            boolean first = true;
            while (!queue.isEmpty()) {
                int node = queue.removeLast();
                if (!first) System.out.print(" -> ");
                System.out.print(data.vertexId.get(node));
                first = false;

                ArrayList<Integer> neighbors = bfs.getNeighbors(node);
                if (neighbors != null) {
                    for (int i = 0; i < neighbors.size(); i++) {
                        int neighbor = neighbors.get(i);
                        if (!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.add(neighbor);
                        }
                    }
                }
            }
            System.out.println();
            System.out.println("  Vertices alcanzables: " + visited.size() + "/" + data.V);
        } catch (Exception e) {
            System.out.println("[BFS] Error: " + e.getMessage());
        }
        System.out.println();
    }

    private void calcularMatrizDistancias() {
        System.out.println("--- Matriz de distancias minimas ---");
        try {
            distMatrix = FloydWarshall.shortestPaths(data.graph, data.V);
            System.out.println("Matriz " + data.V + "x" + data.V + " calculada.");
        } catch (UnsupportedOperationException e) {
            System.out.println("[FloydWarshall] Pendiente de implementacion por el equipo.");
            distMatrix = new double[data.V][data.V];
            for (int i = 0; i < data.V; i++) {
                for (int j = 0; j < data.V; j++) {
                    distMatrix[i][j] = (i == j) ? 0 : Double.POSITIVE_INFINITY;
                }
            }
        }
        System.out.println();
    }

    private void compararMST() {
        System.out.println("--- Comparacion Prim vs Kruskal ---");
        try {
            long t0 = System.nanoTime();
            Prim prim = new Prim(data.graph);
            ArrayList<Edge> mstPrim = prim.buildMST(data.depotIndex);
            long t1 = System.nanoTime();
            double costoPrim = Prim.totalWeight(mstPrim);
            double msPrim = (t1 - t0) / 1_000_000.0;

            ArrayList<Edge> allEdges = obtenerTodasLasAristas();
            t0 = System.nanoTime();
            ArrayList<Edge> mstKruskal = Kruskal.buildMST(data.V, allEdges);
            t1 = System.nanoTime();
            double costoKruskal = Kruskal.costoTotal(mstKruskal);
            double msKruskal = (t1 - t0) / 1_000_000.0;

            System.out.println("  Prim:    costo=" + String.format("%.0f", costoPrim)
                + " | aristas=" + mstPrim.size()
                + " | tiempo=" + String.format("%.3f", msPrim) + " ms");
            System.out.println("  Kruskal: costo=" + String.format("%.0f", costoKruskal)
                + " | aristas=" + mstKruskal.size()
                + " | tiempo=" + String.format("%.3f", msKruskal) + " ms");

            if (Math.abs(costoPrim - costoKruskal) < 0.001) {
                System.out.println("  Ambos algoritmos producen el mismo costo total.");
            } else {
                System.out.println("  ADVERTENCIA: Los costos NO coinciden.");
            }

            if (msKruskal > 0) {
                System.out.println("  Prim fue " + String.format("%.1f", msKruskal / msPrim)
                    + " veces mas rapido que Kruskal.");
            }

            System.out.println();
            System.out.println("  Aristas del MST (Prim):");
            Prim.printMST(mstPrim);
        } catch (Exception e) {
            System.out.println("  [MST] Error: " + e.getMessage());
        }
        System.out.println();
    }

    private ArrayList<Edge> obtenerTodasLasAristas() {
        ArrayList<Edge> allEdges = new ArrayList<>();
        for (int i = 0; i < data.V; i++) {
            ArrayList<Edge> edges = data.graph.get(i);
            if (edges == null) continue;
            for (int j = 0; j < edges.size(); j++) {
                Edge e = edges.get(j);
                if (e.from < e.to) {
                    allEdges.add(e);
                }
            }
        }
        return allEdges;
    }

    private void asignarPaquetes() {
        System.out.println("--- Asignacion de paquetes ---");
        try {
            HashMap<String, String> paqueteACamion = new HashMap<>();
            RoutePlanner.asignarBestFit(data.paquetes, data.camiones, paqueteACamion);

            int asignados = 0, rechazados = 0;
            for (int i = 0; i < data.paquetes.size(); i++) {
                Package p = data.paquetes.get(i);
                if (p.isRechazado()) {
                    rechazados++;
                    System.out.println("  Rechazado: " + p);
                } else {
                    asignados++;
                    System.out.println("  " + p.getId() + " -> camion " + paqueteACamion.get(p.getId()));
                }
            }
            System.out.println("Asignados: " + asignados + " | Rechazados: " + rechazados);
        } catch (Exception e) {
            System.out.println("  [Asignacion] Error: " + e.getMessage());
        }
        System.out.println();
    }

    private void planificarRutas() {
        System.out.println("--- Planificacion de rutas ---");

        truckDistancia = new HashMap<>();
        truckMejora = new HashMap<>();
        truckRutas = new HashMap<>();

        try {
            for (int i = 0; i < data.camiones.size(); i++) {
                Truck t = data.camiones.get(i);
                if (t.getParadas().isEmpty()) continue;

                System.out.println("Camion " + t.getId() + " ("
                    + t.getCargaActual() + "/" + t.getCapacidad() + " kg, "
                    + String.format("%.1f", t.porcentajeOcupacion()) + "% ocupado)");
                System.out.println("  Paradas: " + t.getParadas());

                int[] stops = RoutePlanner.obtenerIndicesParadas(t, data.vertexIndex);

                ArrayList<Integer> rutaNN = RoutePlanner.nearestNeighbor(
                    distMatrix, stops, data.depotIndex);
                double distNN = RoutePlanner.calcularDistanciaRuta(rutaNN, distMatrix);
                System.out.println("  NN:  " + formatearRuta(rutaNN)
                    + " | dist=" + String.format("%.0f", distNN) + " m");

                ArrayList<Integer> rutaMST = RoutePlanner.mstBased(
                    distMatrix, stops, data.depotIndex);

                ArrayList<Integer> mejorRuta = rutaNN;
                double mejorDist = distNN;
                String metodo = "NN";
                double mejora = 0.0;

                if (rutaMST != null) {
                    double distMST = RoutePlanner.calcularDistanciaRuta(rutaMST, distMatrix);
                    mejora = ((distNN - distMST) / distNN) * 100.0;
                    System.out.println("  MST: " + formatearRuta(rutaMST)
                        + " | dist=" + String.format("%.0f", distMST) + " m"
                        + " | mejora=" + String.format("%.1f", mejora) + "%");
                    if (distMST < mejorDist) {
                        mejorDist = distMST;
                        mejorRuta = rutaMST;
                        metodo    = "MST-based";
                    }
                } else {
                    System.out.println("  MST: pendiente de implementacion");
                }

                System.out.println("  Mejor ruta (" + metodo + "): "
                    + formatearRuta(mejorRuta)
                    + " | " + String.format("%.0f", mejorDist) + " m\n");

                truckDistancia.put(t.getId(), mejorDist);
                truckMejora.put(t.getId(), mejora);
                truckRutas.put(t.getId(), mejorRuta);
            }
        } catch (Exception e) {
            System.out.println("  [Rutas] Error: " + e.getMessage());
        }
    }

    private String formatearRuta(ArrayList<Integer> ruta) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ruta.size(); i++) {
            if (i > 0) sb.append("->");
            sb.append(data.vertexId.get(ruta.get(i)));
        }
        return sb.toString();
    }

    private void generarReporte() {
        System.out.println("=== Reporte final ===");
        try {
            System.out.println("--- Resumen por camion ---");
            for (int i = 0; i < data.camiones.size(); i++) {
                Truck t = data.camiones.get(i);
                String tid = t.getId();
                double dist = truckDistancia != null && truckDistancia.containsKey(tid)
                    ? truckDistancia.get(tid) : 0.0;
                double mejora = truckMejora != null && truckMejora.containsKey(tid)
                    ? truckMejora.get(tid) : 0.0;
                System.out.println("  " + tid + ": "
                    + t.getParadas().size() + " paradas | "
                    + String.format("%.0f", dist) + " m | "
                    + t.getCargaActual() + "/" + t.getCapacidad() + " kg ("
                    + String.format("%.1f", t.porcentajeOcupacion()) + "%) | "
                    + "mejora=" + String.format("%.1f", mejora) + "%");
            }

            System.out.println("--- Resumen de paquetes ---");
            int entregados = 0, rechazados = 0;
            for (int i = 0; i < data.paquetes.size(); i++) {
                Package p = data.paquetes.get(i);
                if (p.isRechazado()) {
                    rechazados++;
                    System.out.println("  Rechazado: " + p.getId() + " -> " + p.getDestino()
                        + " (" + p.getPeso() + " kg, prio=" + p.getPrioridad() + ")");
                } else {
                    entregados++;
                }
            }
            System.out.println("  Total: " + data.paquetes.size()
                + " | Entregados: " + entregados
                + " | Rechazados: " + rechazados);
        } catch (Exception e) {
            System.out.println("  [Reporte] Error: " + e.getMessage());
        }
        System.out.println();
    }

    private void mostrarUI() {
        try {
            cr.ac.tec.ce1103.logistec.ui.LogisTECFrame frame =
                new cr.ac.tec.ce1103.logistec.ui.LogisTECFrame(data, truckRutas);
            frame.setVisible(true);
        } catch (Exception e) {
            System.out.println("[UI] No se pudo iniciar la interfaz grafica: " + e.getMessage());
        }
    }

    // ────────────────────────────────────────────────────────
    // Menu interactivo - dinamico desde test_cases/
    // ────────────────────────────────────────────────────────

    /**
     * Escanea el directorio test_cases/ y retorna los archivos .json
     * ordenados alfabeticamente.
     */
    private static String[] listarTestCases() {
        File dir = new File("test_cases");
        File[] archivos = dir.listFiles(f -> f.isFile() && f.getName().endsWith(".json"));
        if (archivos == null || archivos.length == 0) {
            return new String[0];
        }
        Arrays.sort(archivos);
        String[] rutas = new String[archivos.length];
        for (int i = 0; i < archivos.length; i++) {
            rutas[i] = "test_cases/" + archivos[i].getName();
        }
        return rutas;
    }

    private static String mostrarMenu(BufferedReader in, String[] archivos) throws IOException {
        System.out.println();
        System.out.println("=== LogisTEC - Planificacion Logistica ===");
        System.out.println();

        if (archivos.length == 0) {
            System.out.println("No se encontraron archivos .json en test_cases/.");
            System.out.println("Agregue casos de prueba al directorio test_cases/ e intente de nuevo.");
            return null;
        }

        System.out.println("Seleccione caso de prueba:");
        for (int i = 0; i < archivos.length; i++) {
            String nombre = archivos[i].substring("test_cases/".length());
            System.out.println("  " + (i + 1) + ") " + nombre);
        }
        System.out.println("  x) Salir");
        System.out.println();
        System.out.print("Ingrese opcion [1-" + archivos.length + ", x]: ");
        System.out.flush();

        String linea = in.readLine();
        if (linea == null) return null;
        linea = linea.trim();

        if (linea.equalsIgnoreCase("x")) return null;

        try {
            int opcion = Integer.parseInt(linea);
            if (opcion >= 1 && opcion <= archivos.length) {
                return archivos[opcion - 1];
            }
        } catch (NumberFormatException e) {
        }

        System.out.println("Opcion invalida. Intente de nuevo.");
        return mostrarMenu(in, archivos);
    }

    // ────────────────────────────────────────────────────────
    // Punto de entrada
    // ────────────────────────────────────────────────────────

    /**
     * Punto de entrada de la aplicacion LogisTEC.
     * Sin argumentos: escanea test_cases/ y muestra menu interactivo.
     * Con argumento numerico (1-N): ejecuta el caso N del directorio.
     * Con argumento de archivo: ejecuta ese archivo directamente.
     */
    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String ruta;

            if (args.length > 0) {
                try {
                    int idx = Integer.parseInt(args[0]);
                    String[] archivos = listarTestCases();
                    if (idx >= 1 && idx <= archivos.length) {
                        ruta = archivos[idx - 1];
                    } else {
                        String msg = archivos.length > 0
                            ? "Opcion fuera de rango. Use 1-" + archivos.length + " o una ruta valida."
                            : "No hay archivos .json en test_cases/.";
                        System.out.println(msg);
                        return;
                    }
                } catch (NumberFormatException e) {
                    ruta = args[0];
                }

                new LogisTEC().ejecutar(ruta);
                break;
            }

            try {
                String[] archivos = listarTestCases();
                ruta = mostrarMenu(in, archivos);
                if (ruta == null) {
                    System.out.println("Hasta luego!");
                    break;
                }

                new LogisTEC().ejecutar(ruta);

                System.out.print("Desea ejecutar otro caso? (s/n): ");
                System.out.flush();
                String respuesta = in.readLine();
                if (respuesta == null || !respuesta.trim().equalsIgnoreCase("s")) {
                    System.out.println("Hasta luego!");
                    break;
                }
            } catch (IOException e) {
                System.err.println("Error de entrada/salida: " + e.getMessage());
                break;
            }
        }
    }
}
