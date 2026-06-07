package cr.ac.tec.ce1103.logistec.model;

/**
 * Representa un paquete a ser entregado por LogísTEC.
 *
 * <p>Cada paquete tiene un identificador único, un destino (vértice del grafo),
 * un peso en kilogramos y una prioridad (1 = más alta, 3 = más baja).</p>
 *
 * @author Job Jimenez
 * @version 1.0
 */
public class Package {

    /** Identificador único del paquete (ej. "P01"). */
    private final String id;

    /** Identificador del vértice de destino en el grafo (ej. "G"). */
    private final String destino;

    /** Peso del paquete en kilogramos. */
    private final int peso;

    /** Prioridad del paquete: 1 (alta), 2 (media), 3 (baja). */
    private final int prioridad;

    /** {@code true} si el paquete fue marcado como no entregable. */
    private boolean rechazado;

    /**
     * Construye un paquete con los parámetros dados.
     *
     * @param id        identificador único del paquete
     * @param destino   vértice de destino en el grafo
     * @param peso      peso en kilogramos (debe ser positivo)
     * @param prioridad prioridad 1-3 (1 es la más alta)
     */
    public Package(String id, String destino, int peso, int prioridad) {
        this.id        = id;
        this.destino   = destino;
        this.peso      = peso;
        this.prioridad = prioridad;
        this.rechazado = false;
    }

    /**
     * Obtiene el identificador del paquete.
     *
     * @return identificador único
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el vértice de destino del paquete.
     *
     * @return identificador del vértice destino
     */
    public String getDestino() {
        return destino;
    }

    /**
     * Obtiene el peso del paquete en kilogramos.
     *
     * @return peso en kg
     */
    public int getPeso() {
        return peso;
    }

    /**
     * Obtiene la prioridad del paquete.
     *
     * @return prioridad (1 = más alta)
     */
    public int getPrioridad() {
        return prioridad;
    }

    /**
     * Verifica si el paquete fue marcado como rechazado.
     *
     * @return {@code true} si el paquete no puede ser entregado
     */
    public boolean isRechazado() {
        return rechazado;
    }

    /**
     * Marca el paquete como rechazado (no asignable a ningún camión).
     */
    public void marcarRechazado() {
        this.rechazado = true;
    }

    /**
     * Retorna una representación legible del paquete.
     *
     * @return cadena con formato {@code P01 -> G (5 kg, prio=1)}
     */
    @Override
    public String toString() {
        return id + " -> " + destino + " (" + peso + " kg, prio=" + prioridad + ")";
    }
}
