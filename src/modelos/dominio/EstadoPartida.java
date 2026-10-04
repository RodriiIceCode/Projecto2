package modelos.dominio;

import modelos.estructura.ArregloDinamico;
import java.io.Serializable;

/**
 * Contenedor de datos serializable para guardar y reanudar partidas (.dat).
 */
public class EstadoPartida implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String nicknameJugador;
    private final int cerebrosActuales;
    private final int tiempoRestante;
    private final int filas;
    private final int columnas;
    private final Planta[][] matrizPlantas;
    private final ArregloDinamico<Zombi> listaZombis;

    public EstadoPartida(String nicknameJugador, int cerebrosActuales, int tiempoRestante,
                         int filas, int columnas, Planta[][] matrizPlantas, ArregloDinamico<Zombi> listaZombis) {
        this.nicknameJugador = nicknameJugador;
        this.cerebrosActuales = cerebrosActuales;
        this.tiempoRestante = tiempoRestante;
        this.filas = filas;
        this.columnas = columnas;
        this.matrizPlantas = matrizPlantas;
        this.listaZombis = listaZombis;
    }

    public String obtenerNicknameJugador() { return nicknameJugador; }
    public int obtenerCerebrosActuales() { return cerebrosActuales; }
    public int obtenerTiempoRestante() { return tiempoRestante; }
    public int obtenerFilas() { return filas; }
    public int obtenerColumnas() { return columnas; }
    public Planta[][] obtenerMatrizPlantas() { return matrizPlantas; }
    public ArregloDinamico<Zombi> obtenerListaZombis() { return listaZombis; }
}