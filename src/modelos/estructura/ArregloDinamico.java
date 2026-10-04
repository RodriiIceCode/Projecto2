package modelos.estructura;

/**
 * ArregloDinamico implementa un contenedor de tamaño variable sin usar java.util.
 * Utiliza genéricos para poder almacenar cualquier tipo de objeto (Zombis, Plantas, Cuentas).
 * Complejidad: Acceso O(1), Inserción O(1) amortizado, Eliminación O(n).
 *
 * @param <T> El tipo de dato a almacenar.
 */
public class ArregloDinamico<T> implements java.io.Serializable {
    private Object[] elementos;
    private int cantidadActual;
    private static final int CAPACIDAD_INICIAL = 10;

    /**
     * Constructor base que inicializa el arreglo primitivo.
     */
    public ArregloDinamico() {
        this.elementos = new Object[CAPACIDAD_INICIAL];
        this.cantidadActual = 0;
    }

    /**
     * Agrega un elemento al final del arreglo. Si se llena, redimensiona.
     * @param elemento Elemento a insertar.
     */
    public void agregar(T elemento) {
        if (cantidadActual == elementos.length) {
            redimensionar();
        }
        elementos[cantidadActual] = elemento;
        cantidadActual++;
    }

    /**
     * Devuelve el elemento en el índice solicitado haciendo un casteo seguro.
     */
    @SuppressWarnings("unchecked")
    public T obtener(int indice) {
        validarIndice(indice);
        return (T) elementos[indice];
    }

    /**
     * Elimina un elemento en un índice específico y desplaza los elementos restantes.
     * Es crucial para remover zombis o plantas cuando mueren.
     */
    public void eliminarEn(int indice) {
        validarIndice(indice);
        int elementosADesplazar = cantidadActual - indice - 1;
        if (elementosADesplazar > 0) {
            // System.arraycopy es una función nativa de C optimizada para movimiento de memoria
            System.arraycopy(elementos, indice + 1, elementos, indice, elementosADesplazar);
        }
        elementos[cantidadActual - 1] = null; // Liberar referencia para el Garbage Collector
        cantidadActual--;
    }

    public int obtenerTamano() {
        return cantidadActual;
    }

    /**
     * Duplica el tamaño del arreglo interno cuando se alcanza la capacidad máxima.
     */
    private void redimensionar() {
        Object[] nuevoArreglo = new Object[elementos.length * 2];
        System.arraycopy(elementos, 0, nuevoArreglo, 0, elementos.length);
        this.elementos = nuevoArreglo;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= cantidadActual) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
    }
}