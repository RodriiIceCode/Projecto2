package modelos.entidades.plantas;

import modelos.entidades.zombis.Zombi;
import modelos.estructura.ArregloDinamico;

public class NuezCascaraRabias extends Planta {

    public NuezCascaraRabias(int fila, int columna) {
        // Salud 400, Daño 0
        super("NUEZ_CASCARARABIAS", 400, 0, fila, columna);
    }

    @Override
    public void ejecutarAccion(ArregloDinamico<Zombi> zombisActivos) {
        // No ataca. El bloqueo de saltadores lo programaremos en el Zombi Saltador.
    }

    /**
     * Devuelve el estado de daño para que la Vista sepa qué imagen mostrar.
     */
    public String obtenerEstadoGrafico() {
        double porcentajeVida = (double) this.obtenerVidaActual() / 400.0;

        if (porcentajeVida > 0.65) return "INTACTA";
        if (porcentajeVida >= 0.35) return "AGRIETADA";
        return "MUY_DANADA";
    }
}