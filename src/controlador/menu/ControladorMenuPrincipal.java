package controlador.menu;

import controlador.juego.ControladorJardin;
import modelos.archivosbinarios.ConfiguracionNivel;
import modelos.archivosbinarios.GestorPartidas;
import modelos.archivosbinarios.LectorNivelJSON;
import modelos.dominio.Dificultad;
import modelos.dominio.EstadoPartida;
import modelos.dominio.GestorEstadisticas;
import vista.menu.VentanaMenuPrincipal;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * Controlador para la gestión del Menú Principal.
 * Incluye carga inteligente de niveles JSON, reanudación desde archivos binarios (.dat)
 * y visualización de estadísticas del jugador.
 */
public class ControladorMenuPrincipal {

    private final VentanaMenuPrincipal vista;
    private final String nicknameJugador;

    public ControladorMenuPrincipal(String nicknameJugador) {
        this.nicknameJugador = nicknameJugador;
        this.vista = new VentanaMenuPrincipal(nicknameJugador);

        inicializarEventos();
    }

    public void mostrarVentana() {
        this.vista.setVisible(true);
    }

    private void inicializarEventos() {
        vista.agregarListenerNuevaPartida(e -> procesarNuevaPartida());
        vista.agregarListenerReanudarPartida(e -> procesarReanudarPartida());

        // Aquí usamos los nombres exactos que tienes en tu ventana
        vista.agregarListenerEstadisticas(e -> procesarVerEstadisticas());
        vista.agregarListenerSalir(e -> procesarCerrarSesion());
    }

    /**
     * Procesa la creación de una nueva partida.
     * Busca primero 'nivel_prueba.json' automáticamente; si no existe, abre el JFileChooser.
     */
    private void procesarNuevaPartida() {
        File archivoNivel = new File("nivel_prueba.json");

        // Si no existe el archivo por defecto, se abre el explorador de archivos en la raíz del proyecto
        if (!archivoNivel.exists()) {
            JFileChooser selectorArchivo = new JFileChooser(new File("."));
            selectorArchivo.setDialogTitle("Seleccionar archivo de nivel (.json)");

            FileNameExtensionFilter filtro =
                    new FileNameExtensionFilter("Archivos JSON de Nivel (*.json)", "json");
            selectorArchivo.setFileFilter(filtro);

            int resultado = selectorArchivo.showOpenDialog(vista);

            if (resultado == JFileChooser.APPROVE_OPTION) {
                archivoNivel = selectorArchivo.getSelectedFile();
            } else {
                return; // Si el usuario cancela la selección
            }
        }

        try {
            LectorNivelJSON lector = new LectorNivelJSON();
            ConfiguracionNivel nivelCargado = lector.cargarNivel(archivoNivel);

            Dificultad dificultadSeleccionada = solicitarDificultad();

            if (dificultadSeleccionada != null) {
                ControladorJardin controladorJardin =
                        new ControladorJardin(nivelCargado, dificultadSeleccionada, nicknameJugador);
                controladorJardin.iniciarJuego();
            }

        } catch (Exception ex) {
            vista.mostrarMensaje("Error al cargar el archivo de nivel:\n" + ex.getMessage());
        }
    }

    /**
     * Solicita la dificultad de la partida mediante un diálogo emergent.
     */
    private Dificultad solicitarDificultad() {
        Dificultad[] opciones = Dificultad.values();
        return (Dificultad) JOptionPane.showInputDialog(
                vista,
                "Selecciona la dificultad de la partida:",
                "Dificultad de Juego",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                Dificultad.MEDIO
        );
    }

    /**
     * Reanuda una partida previamente guardada desde el archivo binario 'partida.dat'.
     */
    private void procesarReanudarPartida() {
        EstadoPartida estadoGuardado = GestorPartidas.cargarPartida(nicknameJugador);

        if (estadoGuardado == null) {
            vista.mostrarMensaje("No se encontró ninguna partida guardada para " + nicknameJugador + ".");
            return;
        }

        ControladorJardin controladorJardin = new ControladorJardin(estadoGuardado);
        controladorJardin.iniciarJuego();
    }

    /**
     * Lee y despliega las estadísticas acumuladas desde el archivo de texto plano.
     */
    private void procesarVerEstadisticas() {
        String reporte = GestorEstadisticas.obtenerEstadisticasFormateadas(nicknameJugador);
        vista.mostrarMensaje(reporte);
    }

    /**
     * Cierra la ventana del menú principal para retornar a la pantalla de inicio o login.
     */
    private void procesarCerrarSesion() {
        vista.dispose();
    }
}