package cr.ac.tec.ce1103.logistec.graph;

/**
 * Interfaz genérica para un mapa clave-valor.
 *
 * <p>Define el contrato básico para estructuras que almacenan
 * pares clave-valor con operaciones de búsqueda, inserción y eliminación.</p>
 *
 * @param <K> tipo de las claves
 * @param <V> tipo de los valores
 * @author Job Jimenez
 * @version 1.0
 */
public interface Map<K, V> {

    /**
     * Asocia una clave con un valor en el mapa.
     *
     * <p>Si la clave ya existe, sobrescribe su valor anterior.</p>
     *
     * @param key   clave
     * @param value valor a asociar
     * @return valor anterior para esa clave, o {@code null} si no existía
     */
    V put(K key, V value);

    /**
     * Obtiene el valor asociado a una clave.
     *
     * @param key clave a buscar
     * @return valor asociado, o {@code null} si no existe
     */
    V get(K key);

    /**
     * Elimina un par clave-valor del mapa.
     *
     * @param key clave a eliminar
     * @return valor que tenía asociado, o {@code null} si no existía
     */
    V remove(K key);

    /**
     * Verifica si el mapa contiene una clave específica.
     *
     * @param key clave a buscar
     * @return {@code true} si la clave existe
     */
    boolean containsKey(K key);

    /**
     * Verifica si alguna clave está asociada al valor especificado.
     *
     * @param value valor a buscar
     * @return {@code true} si existe al menos una clave con ese valor
     */
    boolean containsValue(V value);

    /**
     * Retorna el número de pares clave-valor en el mapa.
     *
     * @return cantidad de elementos
     */
    int size();

    /**
     * Verifica si el mapa está vacío.
     *
     * @return {@code true} si no contiene elementos
     */
    boolean isEmpty();

    /**
     * Elimina todos los pares clave-valor del mapa.
     */
    void clear();

    /**
     * Retorna un arreglo con todas las claves del mapa.
     *
     * @return arreglo de claves
     */
    Object[] keys();

    /**
     * Retorna un arreglo con todos los valores del mapa.
     *
     * @return arreglo de valores
     */
    Object[] values();
}