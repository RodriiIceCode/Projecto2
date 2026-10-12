package modelos.entidades.plantas;

import modelos.entidades.zombis.Zombi;
import modelos.estructura.ArregloDinamico;

public class Pinchohierba extends Planta {

    public Pinchohierba(int fila, int columna) {
        // Le damos una vida gigante para que los zombis normales no la maten.
        // En la Fase 2, le diremos a los zombis normales que IGNOREN atacar a esta planta.
        super("PINCHOHIERBA", 999999, 20, fila, columna);
    }

    @Override
    public void ejecutarAccion(ArregloDinamico<Zombi> zombisActivos) {
        // Daña a TODOS los zombis que pisen su casilla
        for (int i = 0; i < zombisActivos.obtenerTamano(); i++) {
            Zombi z = zombisActivos.obtener(i);

            if (z.estaViva() && z.obtenerFila() == this.obtenerFila() && z.obtenerColumna() == this.obtenerColumna()) {
                z.recibirDano(this.obtenerAtaque());
            }
        }
    }

    // Si un Zombistein la pisa, podemos llamar a este método para destruirla al instante
    public void aplastar() {
        this.recibirDano(this.obtenerVidaActual());
    }
}