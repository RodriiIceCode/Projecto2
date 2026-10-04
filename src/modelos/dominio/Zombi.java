package modelos.dominio;

/**
 * Representa un zombi en el jardín.
 */
public class Zombi extends Entidad implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private final String tipoZombi;
    private final int costoCerebros;

    public Zombi(String tipoZombi, int vida, int ataque, int costoCerebros, int fila, int columna) {
        super(tipoZombi, vida, ataque, fila, columna);
        this.tipoZombi = tipoZombi;
        this.costoCerebros = costoCerebros;
    }

    public String obtenerTipoZombi() { return tipoZombi; }
    public int obtenerCostoCerebros() { return costoCerebros; }
}