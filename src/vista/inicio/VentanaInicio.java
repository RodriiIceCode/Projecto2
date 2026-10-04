package vista.inicio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Interfaz gráfica inicial para la selección y creación de cuentas.
 * Solo contiene componentes nativos de Java Swing.
 */
public class VentanaInicio extends JFrame {
    // Componentes para crear cuenta
    private JTextField campoNuevoUsuario;
    private JButton botonCrearCuenta;

    // Componentes para seleccionar cuenta
    private JComboBox<String> comboUsuarios;
    private JButton botonSeleccionarCuenta;

    // Objeto para almacenar la imagen en memoria
    private Image imagenFondo;

    public VentanaInicio() {
        cargarImagenFondo();
        configurarVentana();
        inicializarComponentes();
    }

    /**
     * Carga la imagen desde el sistema de archivos al arrancar la vista.
     */
    private void cargarImagenFondo() {
        // Utilizamos ImageIcon como herramienta nativa para leer la imagen desde la carpeta src/recursos
        // Asegúrate de que el nombre y la extensión coincidan exactamente.
        ImageIcon icono = new ImageIcon("src/recursos/Title Screen.png");
        this.imagenFondo = icono.getImage();
    }

    private void configurarVentana() {
        setTitle("Zombis vs. Plantas - Inicio");
        setSize(700, 550); // Hice la ventana un poco más grande para apreciar el fondo
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    private void inicializarComponentes() {
        // 1. Crear un Panel Personalizado que dibuje la imagen de fondo
        // Justificación académica: Al crear una clase anónima de JPanel y sobrescribir paintComponent,
        // interceptamos el renderizado de Swing para pintar los píxeles de la imagen antes de colocar los botones.
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Si la imagen cargó correctamente, la dibujamos del tamaño exacto del panel
                if (imagenFondo != null) {
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        // Configuramos el layout del panel principal
        panelPrincipal.setLayout(new GridLayout(2, 1, 10, 10));

        // 2. Panel superior: Creación de cuenta
        JPanel panelCrear = new JPanel(new FlowLayout());
        panelCrear.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Crear Nueva Cuenta",
                0, 0, null, Color.BLACK)); // Borde y texto blanco para que resalte
        panelCrear.setOpaque(false); // CRÍTICO: Hace el panel transparente para ver el fondo

        campoNuevoUsuario = new JTextField(12);
        botonCrearCuenta = new JButton("Crear y Jugar");

        JLabel labelNick = new JLabel("Nickname:");
        labelNick.setForeground(Color.BLACK); // Letra blanca

        panelCrear.add(labelNick);
        panelCrear.add(campoNuevoUsuario);
        panelCrear.add(botonCrearCuenta);

        // 3. Panel inferior: Selección de cuenta
        JPanel panelSeleccionar = new JPanel(new FlowLayout());
        panelSeleccionar.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                "Seleccionar Cuenta Existente",
                0, 0, null, Color.BLACK));
        panelSeleccionar.setOpaque(false); // CRÍTICO: Transparencia

        comboUsuarios = new JComboBox<>();
        botonSeleccionarCuenta = new JButton("Continuar");

        JLabel labelCuentas = new JLabel("Cuentas:");
        labelCuentas.setForeground(Color.BLACK); // Letra blanca

        panelSeleccionar.add(labelCuentas);
        panelSeleccionar.add(comboUsuarios);
        panelSeleccionar.add(botonSeleccionarCuenta);

        // Agregar los sub-paneles transparentes al panel principal con fondo
        panelPrincipal.add(panelCrear);
        panelPrincipal.add(panelSeleccionar);

        // Establecer nuestro panel personalizado como el contenido central de la ventana
        setContentPane(panelPrincipal);
    }

    // --- Métodos de exposición para el Controlador (se mantienen igual) ---

    public String obtenerTextoNuevoUsuario() {
        return campoNuevoUsuario.getText().trim();
    }

    public String obtenerUsuarioSeleccionado() {
        return (String) comboUsuarios.getSelectedItem();
    }

    public void actualizarListaUsuarios(String[] nombres) {
        comboUsuarios.removeAllItems();
        for (String nombre : nombres) {
            comboUsuarios.addItem(nombre);
        }
    }

    public void mostrarMensaje(String mensaje, boolean esError) {
        int tipo = esError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
        JOptionPane.showMessageDialog(this, mensaje, "Información", tipo);
    }

    public void limpiarCampoNuevoUsuario() {
        campoNuevoUsuario.setText("");
    }

    public void agregarListenerBotonCrear(ActionListener listener) {
        botonCrearCuenta.addActionListener(listener);
    }

    public void agregarListenerBotonSeleccionar(ActionListener listener) {
        botonSeleccionarCuenta.addActionListener(listener);
    }
}