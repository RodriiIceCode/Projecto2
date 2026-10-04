import modelos.gestor.GestorCuentas;
import vista.inicio.VentanaInicio;
import controlador.inicio.ControladorInicio;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // iniciamos la interfaz grafica en un hilo especial de Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 1. Instanciar el backend (package cotrolador)
                GestorCuentas modeloCuentas = new GestorCuentas();

                // 2. Instanciar el frontend (package vista)
                VentanaInicio ventanaInicio = new VentanaInicio();

                // 3. Conectar ambos con el controlador
                ControladorInicio controlador = new ControladorInicio(ventanaInicio, modeloCuentas);

                // 4. Iniciar la aplicación
                controlador.iniciar();
            }
        });
    }
}