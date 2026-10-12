package modelos.entidades.zombis;

import modelos.entidades.Entidad;
import modelos.entidades.plantas.Planta;
import modelos.estructura.ArregloDinamico;
import java.io.Serializable;

public abstract class Zombi extends Entidad implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String tipoZombi;
    protected int costoCerebros;
    protected double velocidad;
    protected int escudo;
    protected int escudoMaximo;
    protected int segundosCongelado;
    protected double acumuladorMovimiento;

    public Zombi(String tipoZombi, int vida, int ataque, int costoCerebros, double velocidad, int fila, int columna) {
        super(tipoZombi, vida, ataque, fila, columna);
        this.tipoZombi = tipoZombi;
        this.costoCerebros = costoCerebros;
        this.velocidad = velocidad;
        this.escudo = 0;
        this.escudoMaximo = 0;
        this.segundosCongelado = 0;
        this.acumuladorMovimiento = 0;
    }

    @Override
    public void recibirDano(int cantidad) {
        if (this.escudo > 0) {
            if (cantidad <= this.escudo) {
                this.escudo -= cantidad;
                return;
            } else {
                cantidad -= this.escudo;
                this.escudo = 0;
            }
        }
        super.recibirDano(cantidad);
    }

    public void congelar(int duracion) {
        this.segundosCongelado = duracion;
    }

    public boolean estaCongelado() {
        return this.segundosCongelado > 0;
    }

    public void decrementarEfectos() {
        if (this.segundosCongelado > 0) {
            this.segundosCongelado--;
        }
    }

    public String obtenerEstadoGrafico() {
        double porcentajeVida = (double) this.obtenerVidaActual() / (double) this.vidaMaxima;
        if (porcentajeVida <= 0.5) {
            return "SIN_BRAZO";
        }
        return "NORMAL";
    }

    public int obtenerCostoCerebros() { return costoCerebros; }
    public String obtenerTipoZombi() { return tipoZombi; }
    public int obtenerEscudo() { return escudo; }

    public abstract void ejecutarAccion(Planta[][] matrizPlantas, ArregloDinamico<Zombi> listaZombis, int maxColumnas);
}