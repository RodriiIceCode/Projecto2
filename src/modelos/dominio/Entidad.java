package modelos.dominio;

import java.io.Serializable;

/**
 * Clase base abstracta para todos los elementos vivos/colocables del jardín.
 * Implementa Serializable para permitir el guardado de partidas binarias.
 */
public abstract class Entidad implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String nombre;
    protected int vidaActual;
    protected int vidaMaxima;
    protected int ataque;
    protected int fila;
    protected int columna;

    public Entidad(String nombre, int vidaMaxima, int ataque, int fila, int columna) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
        this.ataque = ataque;
        this.fila = fila;
        this.columna = columna;
    }

    public boolean estaViva() {
        return vidaActual > 0;
    }

    public void recibirDano(int cantidad) {
        this.vidaActual -= cantidad;
        if (this.vidaActual < 0) {
            this.vidaActual = 0;
        }
    }

    // Getters y Setters
    public String obtenerNombre() { return nombre; }
    public int obtenerVidaActual() { return vidaActual; }
    public int obtenerVidaMaxima() { return vidaMaxima; }
    public int obtenerAtaque() { return ataque; }
    public int obtenerFila() { return fila; }
    public int obtenerColumna() { return columna; }

    public void establecerPosicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }
}