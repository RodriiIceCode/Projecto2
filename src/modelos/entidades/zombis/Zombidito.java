package modelos.entidades.zombis;

import modelos.entidades.plantas.Planta;
import modelos.estructura.ArregloDinamico;

public class Zombidito extends Zombi {

    public Zombidito(int fila, int columna) {
        // Vida 50, Rápido (1.7), Costo 0 (no invocable manualmente)
        super("ZOMBIDITO", 50, 15, 0, 1.7, fila, columna);
    }

    @Override
    public void ejecutarAccion(Planta[][] matrizPlantas, ArregloDinamico<Zombi> listaZombis, int maxColumnas) {
        if (!estaViva()) return;

        decrementarEfectos();
        double velEfectiva = estaCongelado() ? (velocidad * 0.6) : velocidad;

        Planta plantaEnfrente = (columna >= 0 && columna < maxColumnas) ? matrizPlantas[fila][columna] : null;

        if (plantaEnfrente != null && plantaEnfrente.estaViva() && !plantaEnfrente.obtenerNombre().equalsIgnoreCase("PINCHOHIERBA")) {
            plantaEnfrente.recibirDano(obtenerAtaque());
        } else {
            acumuladorMovimiento += velEfectiva;
            if (acumuladorMovimiento >= 1.0) {
                columna--;
                acumuladorMovimiento -= 1.0;
            }
        }
    }
}