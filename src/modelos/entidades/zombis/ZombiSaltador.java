package modelos.entidades.zombis;

import modelos.entidades.plantas.Planta;
import modelos.estructura.ArregloDinamico;

public class ZombiSaltador extends Zombi {

    private boolean tieneGarrocha;

    public ZombiSaltador(int fila, int columna) {
        super("ZOMBI_SALTADOR", 120, 15, 100, 1.7, fila, columna);
        this.tieneGarrocha = true;
    }

    @Override
    public void ejecutarAccion(Planta[][] matrizPlantas, ArregloDinamico<Zombi> listaZombis, int maxColumnas) {
        if (!estaViva()) return;

        decrementarEfectos();
        double velEfectiva = estaCongelado() ? (velocidad * 0.6) : velocidad;

        Planta plantaEnfrente = (columna >= 0 && columna < maxColumnas) ? matrizPlantas[fila][columna] : null;

        if (plantaEnfrente != null && plantaEnfrente.estaViva()) {
            if (tieneGarrocha) {
                tieneGarrocha = false;
                this.velocidad = 1.0; // Pierde la garrocha y reduce su velocidad

                // Salta la casilla a menos que sea Nuez Cáscara-rabias
                if (!plantaEnfrente.obtenerNombre().equalsIgnoreCase("NUEZ_CASCARARABIAS")) {
                    columna--; // Salta por encima de la planta
                }
            } else if (!plantaEnfrente.obtenerNombre().equalsIgnoreCase("PINCHOHIERBA")) {
                plantaEnfrente.recibirDano(obtenerAtaque());
            }
        } else {
            acumuladorMovimiento += velEfectiva;
            if (acumuladorMovimiento >= 1.0) {
                columna--;
                acumuladorMovimiento -= 1.0;
            }
        }
    }

    public boolean tieneGarrocha() { return tieneGarrocha; }
}