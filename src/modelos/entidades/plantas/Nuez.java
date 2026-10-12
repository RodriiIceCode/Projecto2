package modelos.entidades.plantas;

import modelos.entidades.zombis.Zombi;
import modelos.estructura.ArregloDinamico;

public class Nuez extends Planta {

    public Nuez(int fila, int columna) {
        // tipoPlanta, vidaActual, ataque, fila, columna
        // Le damos 4000 de vida (aguanta muchísimo) y 0 de ataque.
        super("NUEZ", 4000, 0, fila, columna);
    }

    @Override
    public void ejecutarAccion(ArregloDinamico<Zombi> zombisActivos) {
        // La Nuez es pasiva, no realiza ninguna acción en su turno.
        // Solo sirve para que los zombis pierdan tiempo comiéndosela.
    }
}