package modelos.entidades.zombis;

import modelos.entidades.plantas.Planta;
import modelos.estructura.ArregloDinamico;

public class ZombiBailon extends Zombi {

    private int temporizadorBaile;

    public ZombiBailon(int fila, int columna) {
        super("ZOMBI_BAILON", 150, 15, 150, 0.8, fila, columna);
        this.temporizadorBaile = 0;
    }

    @Override
    public void ejecutarAccion(Planta[][] matrizPlantas, ArregloDinamico<Zombi> listaZombis, int maxColumnas) {
        if (!estaViva()) return;

        decrementarEfectos();
        temporizadorBaile++;

        // Cada 5 segundos baila e invoca 4 bailarines adyacentes
        if (temporizadorBaile >= 5) {
            temporizadorBaile = 0;
            invocarBailarines(listaZombis, maxColumnas);
            return; // Pierde el movimiento de ese segundo mientras baila
        }

        double velEfectiva = estaCongelado() ? (velocidad * 0.6) : velocidad;
        Planta plantaEnfrente = (columna >= 0 && columna < maxColumnas) ? matrizPlantas[fila][columna] : null;

        if (plantaEnfrente != null && plantaEnfrente.estaViva() && !plantaEnfrente.obtenerNombre().equalsIgnoreCase("PINCHOHIERBA")) {
            plantaEnfrente.recibirDano(obtenerAtaque());
        } else {
            acumuladorMovimiento += velEfectiva;
            if (acumuladorMovimiento >= 1.0) {
                columna--;
                acumuladorMovimiento -= 1.0;
            }
        }
    }

    private void invocarBailarines(ArregloDinamico<Zombi> listaZombis, int maxColumnas) {
        int[][] desplazamientos = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Arriba, abajo, adelante, atrás

        for (int[] d : desplazamientos) {
            int nf = fila + d[0];
            int nc = columna + d[1];

            if (nf >= 0 && nf < matrizPlantasLineas(listaZombis) && nc >= 0 && nc < maxColumnas) {
                listaZombis.agregar(new ZombiBailarin(nf, nc));
            }
        }
    }

    private int matrizPlantasLineas(ArregloDinamico<Zombi> listaZombis) {
        return 5; // Total de filas estándar del jardín
    }
}