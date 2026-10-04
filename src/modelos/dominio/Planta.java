package modelos.dominio;

/**
 * Representa una planta colocada en el jardín.
 */
public class Planta extends Entidad implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private final String tipoPlanta; // "LANZAGUISANTES", "NUEZ", "PATATAPUM", "CARRONIVORA", "HIELAGUISANTES", "PINCHOHIERBA"

    public Planta(String tipoPlanta, int vida, int ataque, int fila, int columna) {
        super(tipoPlanta, vida, ataque, fila, columna);
        this.tipoPlanta = tipoPlanta;
    }

    public String obtenerTipoPlanta() {
        return tipoPlanta;
    }
}