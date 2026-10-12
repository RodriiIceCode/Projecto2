package modelos.entidades.zombis;

import modelos.entidades.plantas.Planta;
import modelos.estructura.ArregloDinamico;

public class Zombistein extends Zombi {

    private boolean zombiditoLanzado;

    public Zombistein(int fila, int columna) {
        super("ZOMBISTEIN", 300, 0, 350, 0.7, fila, columna);
        this.zombiditoLanzado = false;
    }

    @Override
    public void ejecutarAccion(Planta[][] matrizPlantas, ArregloDinamico<Zombi> listaZombis, int maxColumnas) {
        if (!estaViva()) return;

        decrementarEfectos();

        // Lanza al Zombidito al llegar al 50% de vida
        if (!zombiditoLanzado && obtenerVidaActual() <= (vidaMaxima / 2)) {
            zombiditoLanzado = true;
            int columnaInfiltracion = Math.max(1, maxColumnas - 2); // Penúltima casilla
            listaZombis.agregar(new Zombidito(fila, columnaInfiltracion));
        }

        Planta plantaEnfrente = (columna >= 0 && columna < maxColumnas) ? matrizPlantas[fila][columna] : null;

        // Aplasta e ignora la vida de cualquier planta directamente
        if (plantaEnfrente != null && plantaEnfrente.estaViva()) {
            plantaEnfrente.recibirDano(plantaEnfrente.obtenerVidaActual());
        } else {
            double velEfectiva = estaCongelado() ? (velocidad * 0.6) : velocidad;
            acumuladorMovimiento += velEfectiva;
            if (acumuladorMovimiento >= 1.0) {
                columna--;
                acumuladorMovimiento -= 1.0;
            }
        }
    }
}