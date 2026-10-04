package modelos.archivosbinarios;

/**
 * Representa la configuración inicial extraída del archivo JSON del nivel.
 * Contiene la duración, cerebros iniciales y la matriz del jardín.
 */
public class ConfiguracionNivel {
    private final int duracionSegundos;
    private final int cerebrosIniciales;
    private final String[][] jardin; // Matriz [carriles][columnas]
    private final int filas;
    private final int columnas;

    public ConfiguracionNivel(int duracionSegundos, int cerebrosIniciales, String[][] jardin) {
        this.duracionSegundos = duracionSegundos;
        this.cerebrosIniciales = cerebrosIniciales;
        this.jardin = jardin;
        this.filas = jardin.length;
        this.columnas = (jardin.length > 0) ? jardin[0].length : 0;
    }

    // Getters explícitos sin Lombok
    public int obtenerDuracionSegundos() { return duracionSegundos; }
    public int obtenerCerebrosIniciales() { return cerebrosIniciales; }
    public String[][] obtenerJardin() { return jardin; }
    public int obtenerFilas() { return filas; }
    public int obtenerColumnas() { return columnas; }
}
