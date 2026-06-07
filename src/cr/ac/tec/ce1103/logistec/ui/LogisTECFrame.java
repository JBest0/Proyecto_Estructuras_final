package cr.ac.tec.ce1103.logistec.ui;

import cr.ac.tec.ce1103.logistec.graph.ArrayList;
import cr.ac.tec.ce1103.logistec.graph.Edge;
import cr.ac.tec.ce1103.logistec.graph.HashMap;
import cr.ac.tec.ce1103.logistec.io.GraphLoader.GraphData;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

/**
 * Interfaz gráfica básica para LogísTEC.
 *
 * <p>Dibuja el grafo de la ciudad, marca el depósito y los destinos
 * de paquetes pendientes. Cada ruta de camión se muestra con un color
 * distinto. Incluye un panel de texto con el resumen de la planificación.</p>
 *
 * @author Job Jimenez
 * @version 1.0
 * @see GraphData
 */
public class LogisTECFrame extends JFrame {

    private static final int VERTEX_RADIUS = 14;
    private static final int PANEL_WIDTH  = 900;
    private static final int PANEL_HEIGHT = 700;

    /** Colores para las rutas de cada camión. */
    private static final Color[] ROUTE_COLORS = {
        Color.BLUE, Color.MAGENTA, Color.ORANGE, Color.GREEN,
        Color.CYAN, Color.PINK, Color.YELLOW, Color.RED
    };

    private final GraphData data;

    /**
     * Construye la ventana principal con el grafo y el reporte.
     *
     * @param data datos del caso cargados desde el JSON
     */
    public LogisTECFrame(GraphData data) {
        this.data = data;
        initComponents();
    }

    /**
     * Inicializa los componentes de la ventana.
     */
    private void initComponents() {
        setTitle("LogísTEC — Planificación Logística");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de dibujo del grafo
        GraphPanel graphPanel = new GraphPanel();
        graphPanel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        add(new JScrollPane(graphPanel), BorderLayout.CENTER);

        // Panel de texto con información
        JTextArea infoArea = new JTextArea(8, 40);
        infoArea.setEditable(false);
        infoArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        infoArea.setText(construirInfo());
        add(new JScrollPane(infoArea), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Construye el texto informativo que se muestra en la parte inferior.
     *
     * @return cadena con el resumen
     */
    private String construirInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vértices: ").append(data.V);
        sb.append(" | Depósito: ").append(data.vertexId.get(data.depotIndex));
        sb.append(" | Paquetes: ").append(data.paquetes.size());

        int rechazados = 0;
        ArrayList<String> destinos = new ArrayList<>();
        for (int i = 0; i < data.paquetes.size(); i++) {
            if (data.paquetes.get(i).isRechazado()) {
                rechazados++;
            } else {
                String dest = data.paquetes.get(i).getDestino();
                if (!destinos.contains(dest)) {
                    destinos.add(dest);
                }
            }
        }
        sb.append(" | Destinos: ").append(destinos.size());
        sb.append(" | Rechazados: ").append(rechazados);
        sb.append("\nCamiones: ").append(data.camiones.size());
        for (int i = 0; i < data.camiones.size(); i++) {
            sb.append(" | ").append(data.camiones.get(i));
        }
        return sb.toString();
    }

    // ────────────────────────────────────────────────────────
    // Panel de dibujo del grafo
    // ────────────────────────────────────────────────────────

    /**
     * Panel personalizado que dibuja el grafo con sus aristas, vértices y rutas.
     */
    private class GraphPanel extends JPanel {

        /** Cache de índices de vértices con paquetes asignados. */
        private HashMap<Integer, Boolean> destinosConPaquetes;

        GraphPanel() {
            setBackground(Color.WHITE);
            destinosConPaquetes = new HashMap<>();

            // Determinar qué destinos tienen paquetes (no rechazados)
            for (int i = 0; i < data.paquetes.size(); i++) {
                String dest = data.paquetes.get(i).getDestino();
                Integer idx = data.vertexIndex.get(dest);
                if (idx != null && !data.paquetes.get(i).isRechazado()) {
                    destinosConPaquetes.put(idx, true);
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Escalar al tamaño del panel
            double scaleX = (double) getWidth()  / PANEL_WIDTH;
            double scaleY = (double) getHeight() / PANEL_HEIGHT;
            double scale  = Math.min(scaleX, scaleY);
            int offsetX   = (int) ((getWidth()  - PANEL_WIDTH  * scale) / 2);
            int offsetY   = (int) ((getHeight() - PANEL_HEIGHT * scale) / 2);

            // ── Dibujar aristas ───────────────────────────
            g2.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i < data.V; i++) {
                ArrayList<Edge> edges = data.graph.get(i);
                if (edges == null) continue;
                for (int j = 0; j < edges.size(); j++) {
                    Edge e = edges.get(j);
                    int x1 = (int) (data.vertexX[e.from] * scale) + offsetX;
                    int y1 = (int) (data.vertexY[e.from] * scale) + offsetY;
                    int x2 = (int) (data.vertexX[e.to]   * scale) + offsetX;
                    int y2 = (int) (data.vertexY[e.to]   * scale) + offsetY;
                    g2.drawLine(x1, y1, x2, y2);

                    // Mostrar peso de arista (solo para aristas con from < to para evitar duplicados)
                    if (e.from < e.to) {
                        String label = String.valueOf((int) e.weight);
                        int mx = (x1 + x2) / 2 + 5;
                        int my = (y1 + y2) / 2 - 5;
                        g2.setColor(Color.DARK_GRAY);
                        g2.drawString(label, mx, my);
                        g2.setColor(Color.LIGHT_GRAY);
                    }
                }
            }

            // ── Dibujar vértices ─────────────────────────
            int r = (int) (VERTEX_RADIUS * scale);
            for (int i = 0; i < data.V; i++) {
                int cx = (int) (data.vertexX[i] * scale) + offsetX;
                int cy = (int) (data.vertexY[i] * scale) + offsetY;

                // Elegir color según tipo
                if (i == data.depotIndex) {
                    g2.setColor(Color.RED);
                } else if (destinosConPaquetes.containsKey(i)) {
                    g2.setColor(Color.YELLOW);
                } else {
                    g2.setColor(Color.WHITE);
                }

                g2.fillOval(cx - r, cy - r, 2 * r, 2 * r);
                g2.setColor(Color.BLACK);
                g2.drawOval(cx - r, cy - r, 2 * r, 2 * r);

                // Etiqueta del vértice
                String label = data.vertexId.get(i);
                g2.drawString(label, cx + r + 2, cy + 4);
            }

            // ── Leyenda ──────────────────────────────────
            int lx = offsetX + 10;
            int ly = offsetY + 20;
            g2.setColor(Color.RED);
            g2.fillOval(lx, ly - 8, 10, 10);
            g2.setColor(Color.BLACK);
            g2.drawString("Depósito", lx + 14, ly + 2);

            ly += 18;
            g2.setColor(Color.YELLOW);
            g2.fillOval(lx, ly - 8, 10, 10);
            g2.setColor(Color.BLACK);
            g2.drawString("Entrega", lx + 14, ly + 2);

            ly += 18;
            g2.setColor(Color.WHITE);
            g2.fillOval(lx, ly - 8, 10, 10);
            g2.setColor(Color.BLACK);
            g2.drawOval(lx, ly - 8, 10, 10);
            g2.drawString("Intersección", lx + 14, ly + 2);
        }
    }
}
