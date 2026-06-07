package cr.ac.tec.ce1103.logistec.io;

import cr.ac.tec.ce1103.logistec.graph.ArrayList;
import cr.ac.tec.ce1103.logistec.graph.Edge;
import cr.ac.tec.ce1103.logistec.graph.HashMap;
import cr.ac.tec.ce1103.logistec.model.Package;
import cr.ac.tec.ce1103.logistec.model.Truck;

import java.io.FileReader;
import java.io.Reader;

/**
 * Carga la configuración de un caso desde un archivo JSON.
 *
 * <p>Utiliza la biblioteca Gson para parsear el archivo y construir
 * las estructuras internas del proyecto: grafo (lista de adyacencia),
 * lista de paquetes y lista de camiones.</p>
 *
 * <p>El formato esperado del JSON sigue el esquema definido en el
 * enunciado del proyecto LogísTEC.</p>
 *
 * @author Job Jimenez
 * @version 1.0
 */
public class GraphLoader {

    // ────────────────────────────────────────────────────────
    // Clases internas para deserialización JSON con Gson
    // ────────────────────────────────────────────────────────

    /** Estructura raíz del JSON. */
    private static class JsonRoot {
        JsonCiudad ciudad;
        JsonPaquete[] paquetes;
        JsonCamion[] camiones;
    }

    /** Sección "ciudad" del JSON. */
    private static class JsonCiudad {
        JsonVertice[] vertices;
        JsonArista[] aristas;
    }

    /** Un vértice del grafo. */
    private static class JsonVertice {
        String id;
        String tipo;   // "DEPOT" o "INTERSECCION"
        int x, y;
    }

    /** Una arista del grafo. */
    private static class JsonArista {
        String u, v;
        int distancia;
    }

    /** Un paquete a repartir. */
    private static class JsonPaquete {
        String id;
        String destino;
        int peso;
        int prioridad;
    }

    /** Un camión de la flota. */
    private static class JsonCamion {
        String id;
        int capacidad;
    }

    // ────────────────────────────────────────────────────────
    // Clase pública de resultado
    // ────────────────────────────────────────────────────────

    /**
     * Contiene todos los datos parseados de un archivo de configuración.
     *
     * <p>Incluye el grafo como lista de adyacencia, los mapeos entre
     * identificadores de vértice (string) e índices enteros, la lista
     * de paquetes y la flota de camiones.</p>
     */
    public static class GraphData {
        /** Grafo: lista de adyacencia (índices enteros de vértice). */
        public final HashMap<Integer, ArrayList<Edge>> graph;

        /** Mapeo: identificador de vértice → índice entero. */
        public final HashMap<String, Integer> vertexIndex;

        /** Mapeo inverso: índice entero → identificador de vértice. */
        public final HashMap<Integer, String> vertexId;

        /** Índice del depósito. */
        public final int depotIndex;

        /** Número total de vértices. */
        public final int V;

        /** Coordenada X de cada vértice (para dibujo). */
        public final int[] vertexX;

        /** Coordenada Y de cada vértice (para dibujo). */
        public final int[] vertexY;

        /** Lista de paquetes del caso. */
        public final ArrayList<Package> paquetes;

        /** Lista de camiones de la flota. */
        public final ArrayList<Truck> camiones;

        GraphData(HashMap<Integer, ArrayList<Edge>> graph,
                  HashMap<String, Integer> vertexIndex,
                  HashMap<Integer, String> vertexId,
                  int depotIndex, int V,
                  int[] vertexX, int[] vertexY,
                  ArrayList<Package> paquetes,
                  ArrayList<Truck> camiones) {
            this.graph       = graph;
            this.vertexIndex = vertexIndex;
            this.vertexId    = vertexId;
            this.depotIndex  = depotIndex;
            this.V           = V;
            this.vertexX     = vertexX;
            this.vertexY     = vertexY;
            this.paquetes    = paquetes;
            this.camiones    = camiones;
        }
    }

    // ────────────────────────────────────────────────────────
    // Carga principal
    // ────────────────────────────────────────────────────────

    /**
     * Carga un archivo JSON de configuración y construye las estructuras.
     *
     * <p>Requiere que Gson esté disponible en el classpath. El archivo debe
     * seguir el esquema del enunciado: ciudad.vertices[], ciudad.aristas[],
     * paquetes[], camiones[].</p>
     *
     * @param path ruta al archivo JSON
     * @return objeto {@link GraphData} con todos los datos del caso
     * @throws Exception si ocurre un error de E/S, parseo o validación
     */
    public static GraphData load(String path) throws Exception {
        // ── Parsear JSON con Gson ──────────────────────────
        JsonRoot root;
        try (Reader reader = new FileReader(path)) {
            root = new com.google.gson.Gson().fromJson(reader, JsonRoot.class);
        }

        if (root == null || root.ciudad == null) {
            throw new IllegalArgumentException("JSON inválido: no se encontró 'ciudad'");
        }

        // ── Construir mapeo de IDs de vértice ──────────────
        JsonVertice[] jsonVertices = root.ciudad.vertices;
        int V = jsonVertices.length;

        HashMap<String, Integer> vertexIndex = new HashMap<>();
        HashMap<Integer, String> vertexId    = new HashMap<>();

        int depotIndex = -1;
        for (int i = 0; i < V; i++) {
            String id = jsonVertices[i].id;
            vertexIndex.put(id, i);
            vertexId.put(i, id);
            if ("DEPOT".equals(jsonVertices[i].tipo)) {
                if (depotIndex != -1) {
                    throw new IllegalArgumentException("Múltiples vértices de tipo DEPOT");
                }
                depotIndex = i;
            }
        }

        if (depotIndex == -1) {
            throw new IllegalArgumentException("No se encontró un vértice de tipo DEPOT");
        }

        // ── Capturar coordenadas para dibujo ────────────────
        int[] vertexX = new int[V];
        int[] vertexY = new int[V];
        for (int i = 0; i < V; i++) {
            vertexX[i] = jsonVertices[i].x;
            vertexY[i] = jsonVertices[i].y;
        }

        // ── Construir lista de adyacencia ───────────────────
        HashMap<Integer, ArrayList<Edge>> graph = new HashMap<>();
        for (int i = 0; i < V; i++) {
            graph.put(i, new ArrayList<>());
        }

        if (root.ciudad.aristas != null) {
            for (JsonArista arista : root.ciudad.aristas) {
                Integer u = vertexIndex.get(arista.u);
                Integer v = vertexIndex.get(arista.v);
                if (u == null || v == null) {
                    throw new IllegalArgumentException(
                        "Arista refiere a vértice inexistente: (" + arista.u + ", " + arista.v + ")");
                }
                int weight = arista.distancia;

                // Arista no dirigida: agregar en ambas direcciones
                graph.get(u).add(new Edge(u, v, weight));
                graph.get(v).add(new Edge(v, u, weight));
            }
        }

        // ── Construir lista de paquetes ─────────────────────
        ArrayList<Package> paquetes = new ArrayList<>();
        if (root.paquetes != null) {
            for (JsonPaquete jp : root.paquetes) {
                if (jp.destino == null || vertexIndex.get(jp.destino) == null) {
                    throw new IllegalArgumentException(
                        "Paquete " + jp.id + " refiere a destino inexistente: " + jp.destino);
                }
                paquetes.add(new Package(jp.id, jp.destino, jp.peso, jp.prioridad));
            }
        }

        // ── Construir lista de camiones ─────────────────────
        ArrayList<Truck> camiones = new ArrayList<>();
        if (root.camiones != null) {
            for (JsonCamion jc : root.camiones) {
                camiones.add(new Truck(jc.id, jc.capacidad));
            }
        }

        return new GraphData(graph, vertexIndex, vertexId, depotIndex, V,
            vertexX, vertexY, paquetes, camiones);
    }
}
