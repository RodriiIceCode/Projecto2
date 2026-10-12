package modelos.entidades.plantas;

import modelos.entidades.zombis.Zombi;
import modelos.estructura.ArregloDinamico;

public class Patatapum extends Planta {

    public Patatapum(int fila, int columna) {
        // tipoPlanta, vidaActual, ataque, fila, columna
        // 100 de vida y un ataque gigantesco (suficiente para matar a un caracubo).
        super("PATATAPUM", 100, 1500, fila, columna);
    }

    @Override
    public void ejecutarAccion(ArregloDinamico<Zombi> zombisActivos) {
        // Si la planta ya explotó/murió, no hace nada
        if (!this.estaViva()) return;

        for (int i = 0; i < zombisActivos.obtenerTamano(); i++) {
            Zombi z = zombisActivos.obtener(i);

            // Revisa si el zombi está vivo y en SU MISMA CASILLA (misma fila y columna)
            if (z.estaViva() && z.obtenerFila() == this.obtenerFila() && z.obtenerColumna() == this.obtenerColumna()) {

                // Le aplica el daño masivo al zombi
                z.recibirDano(this.obtenerAtaque());

                // La Patatapum se destruye al explotar (su vida llega a 0)
                this.recibirDano(this.obtenerVidaActual());

                break; // Solo explota una vez contra el primer zombi que toque
            }
        }
    }
}