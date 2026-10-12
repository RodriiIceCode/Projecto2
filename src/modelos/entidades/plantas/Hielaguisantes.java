package modelos.entidades.plantas;

import modelos.entidades.zombis.Zombi;
import modelos.estructura.ArregloDinamico;

public class Hielaguisantes extends Planta {

    public Hielaguisantes(int fila, int columna) {
        // Vida 100, Ataque 15
        super("HIELAGUISANTES", 100, 15, fila, columna);
    }

    @Override
    public void ejecutarAccion(ArregloDinamico<Zombi> zombisActivos) {
        Zombi objetivo = null;

        for (int i = 0; i < zombisActivos.obtenerTamano(); i++) {
            Zombi z = zombisActivos.obtener(i);
            if (z.estaViva() && z.obtenerFila() == this.obtenerFila() && z.obtenerColumna() >= this.obtenerColumna()) {
                if (objetivo == null || z.obtenerColumna() < objetivo.obtenerColumna()) {
                    objetivo = z;
                }
            }
        }

        if (objetivo != null) {
            objetivo.recibirDano(this.obtenerAtaque());
            // Aplicamos el efecto de ralentización por 4 segundos (4 turnos)
            // Nota: Crearemos este método en la clase Zombi en la Fase 2.
            // objetivo.congelar(4);
        }
    }
}