package controlador.inicio;

import controlador.menu.ControladorMenuPrincipal;
import modelos.gestor.GestorCuentas;
import vista.inicio.VentanaInicio;

/**
 * Controlador para la pantalla de bienvenida.
 * Maneja la creación/selección de usuarios y navega al Menú Principal.
 */
public class ControladorInicio {
    private final VentanaInicio vista;
    private final GestorCuentas gestorCuentas;

    public ControladorInicio(VentanaInicio vista, GestorCuentas gestorCuentas) {
        this.vista = vista;
        this.gestorCuentas = gestorCuentas;

        inicializarEventos();
        actualizarListaCombo();
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    private void inicializarEventos() {
        vista.agregarListenerBotonCrear(e -> procesarCreacionCuenta());
        vista.agregarListenerBotonSeleccionar(e -> procesarSeleccionCuenta());
    }

    private void actualizarListaCombo() {
        String[] nombres = gestorCuentas.obtenerNombresCuentas();
        vista.actualizarListaUsuarios(nombres);
    }

    private void procesarCreacionCuenta() {
        String nick = vista.obtenerTextoNuevoUsuario();

        if (nick.isEmpty()) {
            vista.mostrarMensaje("El nombre de usuario no puede estar vacío.", true);
            return;
        }

        boolean exito = gestorCuentas.crearCuenta(nick);
        if (exito) {
            vista.mostrarMensaje("Cuenta '" + nick + "' creada exitosamente.", false);
            vista.limpiarCampoNuevoUsuario();
            actualizarListaCombo();

            // TRANSICIÓN AL MENÚ PRINCIPAL
            navegarAMenuPrincipal(nick);
        } else {
            vista.mostrarMensaje("No se pudo crear la cuenta (ya existe o formato inválido).", true);
        }
    }

    private void procesarSeleccionCuenta() {
        String seleccionado = vista.obtenerUsuarioSeleccionado();
        if (seleccionado == null || seleccionado.trim().isEmpty()) {
            vista.mostrarMensaje("Debe seleccionar una cuenta existente.", true);
            return;
        }

        // TRANSICIÓN AL MENÚ PRINCIPAL
        navegarAMenuPrincipal(seleccionado);
    }

    /**
     * Oculta la ventana de inicio y pasa el control al ControladorMenuPrincipal.
     */
    private void navegarAMenuPrincipal(String nickname) {
        // 1. Ocultar y destruir la ventana de inicio para liberar memoria
        vista.dispose();

        // 2. Instanciar el Controlador del Menú Principal
        // (El controlador ahora crea automáticamente su propia ventana con el nickname)
        ControladorMenuPrincipal controladorMenu = new ControladorMenuPrincipal(nickname);

        // 3. Mostrar la nueva ventana
        controladorMenu.mostrarVentana();
    }
}