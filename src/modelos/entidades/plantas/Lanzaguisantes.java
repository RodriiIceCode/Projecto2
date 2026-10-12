package modelos.entidades.plantas;

import modelos.entidades.zombis.Zombi;
import modelos.estructura.ArregloDinamico;

public class Lanzaguisantes extends Planta {

    public Lanzaguisantes(int fila, int columna) {
        // tipoPlanta, vidaActual, ataque, fila, columna
        super("LANZAGUISANTES", 300, 20, fila, columna);
    }

    @Override
    public void ejecutarAccion(ArregloDinamico<Zombi> zombisActivos) {
        Zombi objetivo = null;

        for (int i = 0; i < zombisActivos.obtenerTamano(); i++) {
            Zombi z = zombisActivos.obtener(i);
            // Uso de obtenerColumna() y obtenerFila() heredados de tu Entidad
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