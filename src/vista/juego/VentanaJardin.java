package vista.juego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Vista principal del jardín con panel de selección de zombis y tablero dinámico.
 */
public class VentanaJardin extends JFrame {
    private final int filas;
    private final int columnas;

    private JLabel labelCerebros;
    private JLabel labelTiempo;
    private JLabel labelUsuario;
    private JLabel labelZombiSeleccionado;

    private JButton[][] matrizCasillas;
    private JButton botonPausarGuardar;
    private JButton botonRendirse;

    // Botones de selección de Zombis
    private JButton botonZombiBasico;
    private JButton botonZombiCono;
    private JButton botonZombiCubeta;

    public VentanaJardin(int filas, int columnas, String nickname) {
        this.filas = filas;
        this.columnas = columnas;
        this.matrizCasillas = new JButton[filas][columnas];

        configurarVentana();
        inicializarComponentes(nickname);
    }

    private void configurarVentana() {
        setTitle("Zombis vs. Plantas - Batalla en el Jardín");
        setSize(1050, 720);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes(String nickname) {
        setLayout(new BorderLayout(10, 10));

        // --- PANEL SUPERIOR: Marcador e Información ---
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelSuperior.setBackground(new Color(40, 40, 40));

        labelUsuario = new JLabel("Jugador: " + nickname);
        labelUsuario.setForeground(Color.WHITE);
        labelUsuario.setFont(new Font("Arial", Font.BOLD, 14));

        labelCerebros = new JLabel("Cerebros: 0");
        labelCerebros.setForeground(Color.YELLOW);
        labelCerebros.setFont(new Font("Arial", Font.BOLD, 16));

        labelTiempo = new JLabel("Tiempo: 0s");
        labelTiempo.setForeground(Color.CYAN);
        labelTiempo.setFont(new Font("Arial", Font.BOLD, 16));

        botonPausarGuardar = new JButton("Pausar / Guardar");
        botonRendirse = new JButton("Rendirse");

        panelSuperior.add(labelUsuario);
        panelSuperior.add(labelCerebros);
        panelSuperior.add(labelTiempo);
        panelSuperior.add(botonPausarGuardar);
        panelSuperior.add(botonRendirse);

        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL IZQUIERDO: Selector de Zombis ---
        JPanel panelSelector = new JPanel();
        panelSelector.setLayout(new BoxLayout(panelSelector, BoxLayout.Y_AXIS));
        panelSelector.setBorder(BorderFactory.createTitledBorder("Invocación de Zombis"));
        panelSelector.setPreferredSize(new Dimension(200, 0));

        botonZombiBasico = new JButton("Zombi Común (50)");
        botonZombiCono = new JButton("Zombi Cono (100)");
        botonZombiCubeta = new JButton("Zombi Cubeta (150)");

        labelZombiSeleccionado = new JLabel("Seleccionado: Ninguno");
        labelZombiSeleccionado.setFont(new Font("Arial", Font.ITALIC, 12));

        panelSelector.add(Box.createRigidArea(new Dimension(0, 15)));
        panelSelector.add(labelZombiSeleccionado);
        panelSelector.add(Box.createRigidArea(new Dimension(0, 20)));
        panelSelector.add(botonZombiBasico);
        panelSelector.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSelector.add(botonZombiCono);
        panelSelector.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSelector.add(botonZombiCubeta);

        add(panelSelector, BorderLayout.WEST);

        // --- PANEL CENTRAL: Grilla del Jardín ---
        JPanel panelGrilla = new JPanel(new GridLayout(filas, columnas, 2, 2));
        panelGrilla.setBackground(new Color(34, 139, 34)); // Verde Césped

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                JButton casilla = new JButton();
                casilla.setFocusable(false);
                casilla.setFont(new Font("Arial", Font.PLAIN, 10));
                casilla.setBackground(new Color(124, 252, 0)); // Verde claro

                matrizCasillas[f][c] = casilla;
                panelGrilla.add(casilla);
            }
        }

        add(panelGrilla, BorderLayout.CENTER);
    }

    // --- Métodos de actualización UI ---

    public void actualizarCerebros(int cerebros) {
        labelCerebros.setText("Cerebros: " + cerebros);
    }

    public void actualizarTiempo(int segundos) {
        labelTiempo.setText("Tiempo: " + segundos + "s");
    }

    public void actualizarZombiSeleccionadoTexto(String nombre) {
        labelZombiSeleccionado.setText("Seleccionado: " + nombre);
    }

    public void actualizarCasilla(int fila, int columna, String texto, Color colorFondo) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            matrizCasillas[fila][columna].setText(texto);
            if (colorFondo != null) {
                matrizCasillas[fila][columna].setBackground(colorFondo);
            }
        }
    }

    public void agregarListenerCasilla(int fila, int columna, ActionListener listener) {
        matrizCasillas[fila][columna].addActionListener(listener);
    }

    public void agregarListenerZombiBasico(ActionListener listener) { botonZombiBasico.addActionListener(listener); }
    public void agregarListenerZombiCono(ActionListener listener) { botonZombiCono.addActionListener(listener); }
    public void agregarListenerZombiCubeta(ActionListener listener) { botonZombiCubeta.addActionListener(listener); }

    public void agregarListenerPausaGuardar(ActionListener listener) { botonPausarGuardar.addActionListener(listener); }
    public void agregarListenerRendirse(ActionListener listener) { botonRendirse.addActionListener(listener); }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}