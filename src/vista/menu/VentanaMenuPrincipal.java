package vista.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Vista del Menú Principal del juego.
 * Expone las cuatro opciones requeridas:
 * Nueva Partida, Reanudar, Estadísticas y Salir.
 */
public class VentanaMenuPrincipal extends JFrame {
    private JButton botonNuevaPartida;
    private JButton botonReanudarPartida;
    private JButton botonEstadisticas;
    private JButton botonSalir;
    private Image imagenFondo;
    private JLabel labelBienvenida;

    public VentanaMenuPrincipal(String nicknameJugador) {
        // Tu código actual de inicialización gráfica va aquí
        cargarImagenFondo();
        configurarVentana();
        inicializarComponentes();
        // Opcional: Puedes usar el nombre para el título de la ventana
        setTitle("Plants vs Zombies - Jugador: " + nicknameJugador);
    }

    private void cargarImagenFondo() {
        ImageIcon icono = new ImageIcon("src/recursos/fondo_menu.jpg");
        this.imagenFondo = icono.getImage();
    }

    private void configurarVentana() {
        setTitle("Zombis vs. Plantas - Menú Principal");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

        labelBienvenida = new JLabel("Bienvenido Jugador", SwingConstants.CENTER);
        labelBienvenida.setForeground(Color.WHITE);
        labelBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        labelBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);

        botonNuevaPartida = crearBotonMenu("Nueva Partida");
        botonReanudarPartida = crearBotonMenu("Reanudar Partida Guardada");
        botonEstadisticas = crearBotonMenu("Estadísticas");
        botonSalir = crearBotonMenu("Salir");

        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));
        panelPrincipal.add(labelBienvenida);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));
        panelPrincipal.add(botonNuevaPartida);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
        panelPrincipal.add(botonReanudarPartida);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
        panelPrincipal.add(botonEstadisticas);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
        panelPrincipal.add(botonSalir);

        setContentPane(panelPrincipal);
    }

    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(250, 40));
        return boton;
    }

    // --- Métodos de exposición para el Controlador ---

    public void establecerNombreUsuario(String nickname) {
        labelBienvenida.setText("Bienvenido, " + nickname);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void agregarListenerNuevaPartida(ActionListener listener) { botonNuevaPartida.addActionListener(listener); }
    public void agregarListenerReanudarPartida(ActionListener listener) { botonReanudarPartida.addActionListener(listener); }
    public void agregarListenerEstadisticas(ActionListener listener) { botonEstadisticas.addActionListener(listener); }
    public void agregarListenerSalir(ActionListener listener) { botonSalir.addActionListener(listener); }



}