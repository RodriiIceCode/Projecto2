package modelos.dominio;

/**
 * Representa los tres niveles de dificultad del juego.
 */
public enum Dificultad {
    FACIL("Fácil", 1.0),
    MEDIO("Medio", 1.5),
    DIFICIL("Difícil", 2.0);

    private final String nombre;
    private final double multiplicadorDificultad;

    Dificultad(String nombre, double multiplicadorDificultad) {
        this.nombre = nombre;
        this.multiplicadorDificultad = multiplicadorDificultad;
    }

    public String obtenerNombre() {
        return nombre;
    }

    public double obtenerMultiplicadorDificultad() {
        return multiplicadorDificultad;
    }

    @Override
    public String toString() {
        return nombre;
    }
}