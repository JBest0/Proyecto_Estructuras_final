package cr.ac.tec.ce1103.logistec.graph;

/**
 * Conjunto (set) genérico basado en HashMap.
 *
 * <p>Implementa el contrato de Set usando un HashMap interno donde
 * las claves son los elementos y los valores son siempre un objeto dummy.
 * Proporciona operaciones de O(1) promedio.</p>
 *
 * @param <E> tipo de elementos en el conjunto
 * @author Job Jimenez
 * @version 1.0
 * @see HashMap
 */
public class HashSet<E> implements Set<E> {

    /** Objeto dummy almacenado para cada clave. */
    private static final Object PRESENT = new Object();

    /** Mapa interno que almacena los elementos. */
    private final HashMap<E, Object> map;

    /**
     * Construye un conjunto vacío con capacidad y factor de carga por defecto.
     */
    public HashSet() {
        map = new HashMap<>();
    }

    /**
     * Construye un conjunto vacío con parámetros de capacidad especificados.
     *
     * @param initialCapacity número inicial de buckets
     * @param loadFactor factor de carga para redimensionamiento
     */
    public HashSet(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
    }

    /**
     * Agrega un elemento al conjunto si no está ya presente.
     *
     * @param element elemento a agregar
     * @return {@code true} si el elemento se agregó (no existía previamente)
     */
    @Override
    public boolean add(E element) {
        return map.put(element, PRESENT) == null;
    }

    /**
     * Elimina un elemento del conjunto si está presente.
     *
     * @param element elemento a eliminar
     * @return {@code true} si el elemento fue encontrado y eliminado
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object element) {
        return map.remove((E) element) == PRESENT;
    }

    /**
     * Verifica si el conjunto contiene un elemento específico.
     *
     * @param element elemento a buscar
     * @return {@code true} si el elemento está en el conjunto
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object element) {
        return map.containsKey((E) element);
    }

    /**
     * Retorna el número de elementos en el conjunto.
     *
     * @return cantidad de elementos
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * Verifica si el conjunto está vacío.
     *
     * @return {@code true} si no contiene elementos
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Elimina todos los elementos del conjunto.
     */
    @Override
    public void clear() {
        map.clear();
    }

    /**
     * Retorna un arreglo con todos los elementos del conjunto.
     *
     * @return arreglo de elementos
     */
    @Override
    public Object[] toArray() {
        return map.keys();
    }

    /**
     * Retorna una representación legible del conjunto.
     *
     * @return cadena con formato {@code [elemento1, elemento2, ...]}
     */
    @Override
    public String toString() {
        Object[] elements = toArray();
        if (elements.length == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < elements.length; i++) {
            sb.append(elements[i]);
            if (i < elements.length - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}