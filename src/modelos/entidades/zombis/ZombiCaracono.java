package modelos.entidades.zombis;

import modelos.entidades.plantas.Planta;
import modelos.estructura.ArregloDinamico;

public class ZombiCaracono extends Zombi {

    public ZombiCaracono(int fila, int columna) {
        super("ZOMBI_CARACONO", 100, 20, 100, 1.0, fila, columna);
        this.escudo = 100;
        this.escudoMaximo = 100;
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