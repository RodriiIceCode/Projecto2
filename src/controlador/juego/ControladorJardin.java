package controlador.juego;

import modelos.archivosbinarios.ConfiguracionNivel;
import modelos.archivosbinarios.GestorPartidas;
import modelos.dominio.Dificultad;
import modelos.dominio.EstadoPartida;
import modelos.dominio.GestorEstadisticas;
import modelos.dominio.Planta;
import modelos.dominio.Zombi;
import modelos.estructura.ArregloDinamico;
import vista.juego.VentanaJardin;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Color;

/**
 * Controlador del Jardín con motor de juego, guardado de partida en binario (.dat)
 * y registro automático de estadísticas.
 */
public class ControladorJardin {
    private final VentanaJardin vista;
    private final String nicknameJugador;

    private int cerebrosActuales;
    private int tiempoRestante;
    private String tipoZombiSeleccionado = null;

    private final int filas;
    private final int columnas;
    private final Planta[][] matrizPlantas;
    private final ArregloDinamico<Zombi> listaZombis;

    private Thread hiloJuego;
    private boolean enEjecucion = false;
    private boolean pausado = false;
    private int contadorSegundos = 0;

    // Métricas para estadísticas de la partida actual
    private int zombisInvocadosPartida = 0;
    private int cerebrosGastadosPartida = 0;

    // Constructor 1: Para NUEVA PARTIDA (desde JSON)
    public ControladorJardin(ConfiguracionNivel nivel, Dificultad dificultad, String nicknameJugador) {
        this.nicknameJugador = nicknameJugador;
        this.filas = nivel.obtenerFilas();
        this.columnas = nivel.obtenerColumnas();
        this.cerebrosActuales = nivel.obtenerCerebrosIniciales();
        this.tiempoRestante = nivel.obtenerDuracionSegundos();

        this.matrizPlantas = new Planta[filas][columnas];
        this.listaZombis = new ArregloDinamico<>();

        this.vista = new VentanaJardin(filas, columnas, nicknameJugador);

        cargarEstructuraInicial(nivel.obtenerJardin());
        inicializarEventos();
        inicializarHiloJuego();
    }

    // Constructor 2: Para REANUDAR PARTIDA (desde .dat)
    public ControladorJardin(EstadoPartida estado) {
        this.nicknameJugador = estado.obtenerNicknameJugador();
        this.filas = estado.obtenerFilas();
        this.columnas = estado.obtenerColumnas();
        this.cerebrosActuales = estado.obtenerCerebrosActuales();
        this.tiempoRestante = estado.obtenerTiempoRestante();

        this.matrizPlantas = estado.obtenerMatrizPlantas();
        this.listaZombis = estado.obtenerListaZombis();

        this.vista = new VentanaJardin(filas, columnas, nicknameJugador);

        redibujarTablero();
        inicializarEventos();
        inicializarHiloJuego();
    }

    public void iniciarJuego() {
        vista.setVisible(true);
        enEjecucion = true;
        hiloJuego.start();
    }

    private void cargarEstructuraInicial(String[][] matrizTexto) {
        vista.actualizarCerebros(cerebrosActuales);
        vista.actualizarTiempo(tiempoRestante);

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                String tipo = matrizTexto[f][c];
                if (!tipo.equalsIgnoreCase("VACIO")) {
                    matrizPlantas[f][c] = new Planta(tipo, obtenerVidaInicialPlanta(tipo), obtenerAtaqueInicialPlanta(tipo), f, c);
                }
            }
        }
        redibujarTablero();
    }

    private void inicializarHiloJuego() {
        hiloJuego = new Thread(() -> {
            while (enEjecucion) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }

                if (!pausado) {
                    contadorSegundos++;
                    tiempoRestante--;

                    if (contadorSegundos % 5 == 0) {
                        cerebrosActuales += 25;
                    }

                    procesarAtaquePlantas();
                    procesarAvanceYAtaqueZombis();

                    SwingUtilities.invokeLater(() -> {
                        vista.actualizarTiempo(tiempoRestante);
                        vista.actualizarCerebros(cerebrosActuales);
                        redibujarTablero();
                        validarEstadoFinJuego();
                    });
                }
            }
        });
    }

    private void procesarAtaquePlantas() {
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                Planta planta = matrizPlantas[f][c];
                if (planta != null && planta.estaViva() && planta.obtenerAtaque() > 0) {
                    Zombi objetivo = obtenerPrimerZombiEnCarril(f, c);
                    if (objetivo != null) {
                        objetivo.recibirDano(planta.obtenerAtaque());
                    }
                }
            }
        }
    }

    private void procesarAvanceYAtaqueZombis() {
        for (int i = 0; i < listaZombis.obtenerTamano(); i++) {
            Zombi zombi = listaZombis.obtener(i);
            if (!zombi.estaViva()) continue;

            int f = zombi.obtenerFila();
            int c = zombi.obtenerColumna();

            if (matrizPlantas[f][c] != null && matrizPlantas[f][c].estaViva()) {
                matrizPlantas[f][c].recibirDano(zombi.obtenerAtaque());
                if (!matrizPlantas[f][c].estaViva()) {
                    matrizPlantas[f][c] = null;
                }
            } else {
                if (contadorSegundos % 2 == 0) {
                    zombi.establecerPosicion(f, c - 1);
                }
            }
        }
    }

    private Zombi obtenerPrimerZombiEnCarril(int fila, int columnaMinima) {
        Zombi cercano = null;
        for (int i = 0; i < listaZombis.obtenerTamano(); i++) {
            Zombi z = listaZombis.obtener(i);
            if (z.estaViva() && z.obtenerFila() == fila && z.obtenerColumna() >= columnaMinima) {
                if (cercano == null || z.obtenerColumna() < cercano.obtenerColumna()) {
                    cercano = z;
                }
            }
        }
        return cercano;
    }

    private void redibujarTablero() {
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                Planta planta = matrizPlantas[f][c];
                Zombi zombi = obtenerZombiEnCasilla(f, c);

                if (zombi != null && zombi.estaViva()) {
                    vista.actualizarCasilla(f, c, zombi.obtenerNombre() + " (" + zombi.obtenerVidaActual() + ")", new Color(220, 160, 220));
                } else if (planta != null && planta.estaViva()) {
                    vista.actualizarCasilla(f, c, planta.obtenerNombre() + " (" + planta.obtenerVidaActual() + ")", new Color(144, 238, 144));
                } else {
                    vista.actualizarCasilla(f, c, "", new Color(200, 250, 200));
                }
            }
        }
    }

    private Zombi obtenerZombiEnCasilla(int fila, int columna) {
        for (int i = 0; i < listaZombis.obtenerTamano(); i++) {
            Zombi z = listaZombis.obtener(i);
            if (z.estaViva() && z.obtenerFila() == fila && z.obtenerColumna() == columna) {
                return z;
            }
        }
        return null;
    }

    private void validarEstadoFinJuego() {
        // Victoria Zombi: Un zombi llegó a la casa (columna < 0)
        for (int i = 0; i < listaZombis.obtenerTamano(); i++) {
            Zombi z = listaZombis.obtener(i);
            if (z.estaViva() && z.obtenerColumna() < 0) {
                detenerJuego();
                GestorEstadisticas.registrarResultado(nicknameJugador, true, zombisInvocadosPartida, cerebrosGastadosPartida);
                vista.mostrarMensaje("¡VICTORIA! Un zombi ingresó a la casa.");
                vista.dispose();
                return;
            }
        }

        // Derrota Zombi: Se agota el tiempo
        if (tiempoRestante <= 0) {
            detenerJuego();
            GestorEstadisticas.registrarResultado(nicknameJugador, false, zombisInvocadosPartida, cerebrosGastadosPartida);
            vista.mostrarMensaje("¡DERROTA! Se agotó el tiempo y las plantas resistieron.");
            vista.dispose();
        }
    }

    private void inicializarEventos() {
        vista.agregarListenerZombiBasico(e -> seleccionarZombi("Zombi Común", 50));
        vista.agregarListenerZombiCono(e -> seleccionarZombi("Zombi Cono", 100));
        vista.agregarListenerZombiCubeta(e -> seleccionarZombi("Zombi Cubeta", 150));

        vista.agregarListenerPausaGuardar(e -> procesarPausaGuardar());
        vista.agregarListenerRendirse(e -> procesarRendirse());

        for (int f = 0; f < filas; f++) {
            final int filaActual = f;
            for (int c = 0; c < columnas; c++) {
                final int columnaActual = c;
                vista.agregarListenerCasilla(f, c, e -> procesarClicCasilla(filaActual, columnaActual));
            }
        }
    }

    private void seleccionarZombi(String tipo, int costo) {
        if (cerebrosActuales < costo) {
            vista.mostrarMensaje("Cerebros insuficientes (" + costo + " requeridos).");
            return;
        }
        this.tipoZombiSeleccionado = tipo;
        vista.actualizarZombiSeleccionadoTexto(tipo + " (" + costo + " c.)");
    }

    private void procesarClicCasilla(int fila, int columna) {
        if (tipoZombiSeleccionado == null) {
            vista.mostrarMensaje("Selecciona primero un Zombi en el panel izquierdo.");
            return;
        }

        int costo = obtenerCostoZombi(tipoZombiSeleccionado);

        if (cerebrosActuales >= costo) {
            cerebrosActuales -= costo;
            zombisInvocadosPartida++;
            cerebrosGastadosPartida += costo;

            vista.actualizarCerebros(cerebrosActuales);

            Zombi nuevoZombi = new Zombi(tipoZombiSeleccionado, obtenerVidaInicialZombi(tipoZombiSeleccionado), 15, costo, fila, columna);
            listaZombis.agregar(nuevoZombi);

            tipoZombiSeleccionado = null;
            vista.actualizarZombiSeleccionadoTexto("Ninguno");
            redibujarTablero();
        }
    }

    private void procesarPausaGuardar() {
        pausado = true;

        int opcion = JOptionPane.showOptionDialog(
                vista,
                "Juego en Pausa.\n¿Deseas guardar la partida actual para continuar después?",
                "Pausa y Guardado",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Guardar y Salir", "Reanudar Juego", "Cancelar"},
                "Reanudar Juego"
        );

        if (opcion == 0) { // Guardar y Salir
            EstadoPartida estado = new EstadoPartida(
                    nicknameJugador, cerebrosActuales, tiempoRestante,
                    filas, columnas, matrizPlantas, listaZombis
            );

            boolean exito = GestorPartidas.guardarPartida(estado, nicknameJugador);
            if (exito) {
                vista.mostrarMensaje("Partida guardada exitosamente.");
                detenerJuego();
                vista.dispose();
            } else {
                vista.mostrarMensaje("Ocurrió un error al guardar la partida.");
                pausado = false;
            }
        } else {
            pausado = false;
        }
    }

    private void detenerJuego() {
        enEjecucion = false;
        if (hiloJuego != null) {
            hiloJuego.interrupt();
        }
    }

    private void procesarRendirse() {
        detenerJuego();
        vista.dispose();
    }

    private int obtenerCostoZombi(String tipo) {
        switch (tipo) {
            case "Zombi Cono": return 100;
            case "Zombi Cubeta": return 150;
            default: return 50;
        }
    }

    private int obtenerVidaInicialZombi(String tipo) {
        switch (tipo) {
            case "Zombi Cono": return 250;
            case "Zombi Cubeta": return 500;
            default: return 100;
        }
    }

    private int obtenerVidaInicialPlanta(String tipo) {
        switch (tipo.toUpperCase()) {
            case "NUEZ": return 400;
            case "PINCHOHIERBA": return 200;
            case "CARRONIVORA": return 150;
            default: return 100;
        }
    }

    private int obtenerAtaqueInicialPlanta(String tipo) {
        switch (tipo.toUpperCase()) {
            case "LANZAGUISANTES": return 20;
            case "HIELAGUISANTES": return 20;
            case "CARRONIVORA": return 40;
            case "PATATAPUM": return 100;
            default: return 0;
        }
    }
}