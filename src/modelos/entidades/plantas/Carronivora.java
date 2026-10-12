package modelos.entidades.plantas;

import modelos.entidades.zombis.Zombi;
import modelos.estructura.ArregloDinamico;

public class Carronivora extends Planta {

    private int turnosDigiriendo; // Contador para los 8 segundos

    public Carronivora(int fila, int columna) {
        // Vida 100, Daño base 200 (solo para Zombistein)
        super("CARRONIVORA", 100, 200, fila, columna);
        this.turnosDigiriendo = 0;
    }

    @Override
    public void ejecutarAccion(ArregloDinamico<Zombi> zombisActivos) {
        // Si está digiriendo, restamos un segundo y terminamos su turno
        if (turnosDigiriendo > 0) {
            turnosDigiriendo--;
            return;
        }

        for (int i = 0; i < zombisActivos.obtenerTamano(); i++) {
            Zombi z = zombisActivos.obtener(i);

            if (z.estaViva() && z.obtenerFila() == this.obtenerFila()) {
                // Ataca a los que estén en su misma casilla o una enfrente
                if (z.obtenerColumna() == this.obtenerColumna() || z.obtenerColumna() == this.obtenerColumna() + 1) {

                    if (z.obtenerNombre().equalsIgnoreCase("ZOMBISTEIN")) {
                        // Al Zombistein solo le hace daño (200), no se lo traga.
                        z.recibirDano(this.obtenerAtaque());
                    } else {
                        // Se traga al zombi entero (le quita toda su vida actual)
                        z.recibirDano(z.obtenerVidaActual());
                        // Comienza a digerir por 8 segundos
                        this.turnosDigiriendo = 8;
                    }
                    break; // Solo ataca a un zombi por turno
                }
            }
        }
    }
}