package modelos.entidades.plantas;

import modelos.entidades.zombis.Zombi;
import modelos.estructura.ArregloDinamico;

public class Repetidora extends Planta {

    public Repetidora(int fila, int columna) {
        // Vida igual al Lanzaguisantes (300), pero el doble de ataque (40)
        super("REPETIDORA", 300, 40, fila, columna);
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
        }
    }
}