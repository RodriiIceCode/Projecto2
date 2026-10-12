package modelos.entidades.zombis;

import modelos.entidades.plantas.Planta;
import modelos.estructura.ArregloDinamico;

public class ZombiBasico extends Zombi {

    public ZombiBasico(int fila, int columna) {
        // Nombre, Vida, Ataque, Costo, Velocidad, Fila, Columna
        super("ZOMBI_BASICO", 100, 15, 50, 1.0, fila, columna);
    }

    @Override
    public void ejecutarAccion(Planta[][] matrizPlantas, ArregloDinamico<Zombi> listaZombis, int maxColumnas) {
        if (!estaViva()) return;

        decrementarEfectos();
        double velEfectiva = estaCongelado() ? (velocidad * 0.6) : velocidad;

        Planta plantaEnfrente = (columna >= 0 && columna < maxColumnas) ? matrizPlantas[fila][columna] : null;

        // Ignora Pinchohierba para comer
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