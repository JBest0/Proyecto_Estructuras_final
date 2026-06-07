package cr.ac.tec.ce1103.logistec.model;

import cr.ac.tec.ce1103.logistec.graph.ArrayList;

/**
 * Representa un camión de la flota de LogísTEC.
 *
 * <p>Cada camión tiene una capacidad máxima de carga y una lista de paradas
 * (identificadores de vértice) que debe visitar en orden. Inicia y termina
 * su ruta en el depósito.</p>
 *
 * @author Job Jimenez
 * @version 1.0
 * @see cr.ac.tec.ce1103.logistec.planner.RoutePlanner
 */
public class Truck {

    /** Identificador del camión (ej. "C01"). */
    private final String id;

    /** Capacidad máxima de carga en kilogramos. */
    private final int capacidad;

    /** Carga actual acumulada en kilogramos. */
    private int cargaActual;

    /** Paradas asignadas en orden de visita (identificadores de vértice). */
    private ArrayList<String> paradas;

    /**
     * Construye un camión con capacidad especificada.
     *
     * @param id        identificador del camión
     * @param capacidad capacidad máxima de carga en kg (debe ser positiva)
     */
    public Truck(String id, int capacidad) {
        this.id          = id;
        this.capacidad   = capacidad;
        this.cargaActual = 0;
        this.paradas     = new ArrayList<>();
    }

    /**
     * Obtiene el identificador del camión.
     *
     * @return identificador único
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene la capacidad máxima de carga.
     *
     * @return capacidad en kg
     */
    public int getCapacidad() {
        return capacidad;
    }

    /**
     * Obtiene la carga actual acumulada.
     *
     * @return carga en kg
     */
    public int getCargaActual() {
        return cargaActual;
    }

    /**
     * Calcula la capacidad libre disponible.
     *
     * @return capacidad restante en kg
     */
    public int capacidadLibre() {
        return capacidad - cargaActual;
    }

    /**
     * Verifica si puede transportar un peso adicional.
     *
     * @param peso peso a verificar en kg
     * @return {@code true} si hay espacio suficiente
     */
    public boolean puedeLlevar(int peso) {
        return cargaActual + peso <= capacidad;
    }

    /**
     * Agrega carga al camión (incrementa la carga actual).
     *
     * @param peso peso a agregar en kg
     */
    public void agregarCarga(int peso) {
        this.cargaActual += peso;
    }

    /**
     * Obtiene la lista de paradas asignadas al camión.
     *
     * @return lista de identificadores de vértice en orden de visita
     */
    public ArrayList<String> getParadas() {
        return paradas;
    }

    /**
     * Asigna la lista completa de paradas para este camión.
     *
     * @param paradas lista de identificadores de vértice en orden de visita
     */
    public void setParadas(ArrayList<String> paradas) {
        this.paradas = paradas;
    }

    /**
     * Calcula el porcentaje de ocupación de la capacidad.
     *
     * @return porcentaje entre 0 y 100
     */
    public double porcentajeOcupacion() {
        return (cargaActual / (double) capacidad) * 100.0;
    }

    /**
     * Retorna una representación legible del camión.
     *
     * @return cadena con formato {@code C01 (cap=50, carga=12/50)}
     */
    @Override
    public String toString() {
        return id + " (cap=" + capacidad + ", carga=" + cargaActual + "/" + capacidad + ")";
    }
}
