public class GrafoNoDirigido<T> {

    linkedlist<vertice<T>> listaDeAdyacencia = new linkedlist<>();

    public GrafoNoDirigido() {
    }

    public void agregarVertice(T dato) {
        if (buscarVertice(dato) != null) {
            System.out.println("El vertice ya existe");
            return;
        }

        vertice<T> nuevoVertice = new vertice<>(dato);
        listaDeAdyacencia.agregarNodo(nuevoVertice);
    }

    public vertice<T> buscarVertice(T dato) {
        linkedlist<vertice<T>>.Nodo temp = listaDeAdyacencia.head;

        while (temp != null) {
            vertice<T> actual = temp.getValor();

            if (actual.valor.equals(dato)) {
                return actual;
            }

            temp = temp.getNext();
        }

        return null;
    }

    public void agregarArista(T datoOrigen, T datoDestino, int peso) {
        vertice<T> origen = buscarVertice(datoOrigen);
        vertice<T> destino = buscarVertice(datoDestino);

        if (origen == null) {
            System.out.println("El vertice origen no existe");
            return;
        }

        if (destino == null) {
            System.out.println("El vertice destino no existe");
            return;
        }

        origen.crearArista(destino, peso);
        destino.crearArista(origen, peso);
    }
    public void DFS(T datoInicio) {
    vertice<T> inicio = buscarVertice(datoInicio);

    if (inicio == null) {
        System.out.println("El vertice inicial no existe");
        return;
    }

    linkedlist<vertice<T>> visitados = new linkedlist<>();

    DFSRecursivo(inicio, visitados);
 }

    private void DFSRecursivo(vertice<T> actual, linkedlist<vertice<T>> visitados) {
    visitados.agregarNodo(actual);

    System.out.println(actual.valor);

    linkedlist<arista<T>>.Nodo tempArista = actual.listaDeAristas.head;

    while (tempArista != null) {
        arista<T> aristaActual = tempArista.getValor();
        vertice<T> vecino = aristaActual.destino;

        if (!visitados.buscar(vecino)) {
            DFSRecursivo(vecino, visitados);
        }

        tempArista = tempArista.getNext();
     }
    }
    public void mostrarGrafo() {
    linkedlist<vertice<T>>.Nodo temp = listaDeAdyacencia.head;

    while (temp != null) {
        vertice<T> actual = temp.getValor();

        System.out.print(actual.valor + " -> ");

        linkedlist<arista<T>>.Nodo tempArista = actual.listaDeAristas.head;

        while (tempArista != null) {
            arista<T> aristaActual = tempArista.getValor();

            System.out.print(aristaActual.destino.valor + "(" + aristaActual.peso + ") ");

            tempArista = tempArista.getNext();
        }

        System.out.println();

        temp = temp.getNext();
    }
    }
    }
class arista<T> {

    int peso;
    vertice<T> destino;

    public arista(vertice<T> vertice, int peso) {
        this.destino = vertice;
        this.peso = peso;
    }
}
class vertice<T> {

    T valor;
    linkedlist<arista<T>> listaDeAristas = new linkedlist<>();

    public vertice(T dato) {
        this.valor = dato;
    }

    public void crearArista(vertice<T> vertice, int peso) {
        arista<T> nuevaArista = new arista<>(vertice, peso);
        listaDeAristas.agregarNodo(nuevaArista);
    }
}



