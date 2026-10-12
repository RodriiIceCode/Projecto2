package modelos.entidades.zombis;

import modelos.entidades.plantas.Planta;
import modelos.estructura.ArregloDinamico;

public class ZombiDeportista extends Zombi {

    public ZombiDeportista(int fila, int columna) {
        // Daño 10 cada 0.5s = 20 por segundo en ciclo de 1s
        super("ZOMBI_DEPORTISTA", 100, 20, 150, 2.0, fila, columna);
        this.escudo = 150;
        this.escudoMaximo = 150;
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