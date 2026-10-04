package modelos.gestor;

import modelos.dominio.CuentaJugador;
import modelos.estructura.ArregloDinamico;
import modelos.archivosbinarios.GestorArchivos;

/**
 * GestorCuentas administra la lógica de cuentas en memoria y
 * coordina la persistencia en disco con GestorArchivos.
 */
public class GestorCuentas {
    private ArregloDinamico<CuentaJugador> listaCuentas;
    private final GestorArchivos gestorArchivos;

    public GestorCuentas() {
        this.gestorArchivos = new GestorArchivos();
        // Carga automática desde disco al arrancar la aplicación
        this.listaCuentas = gestorArchivos.cargarTodasLasCuentas();
    }

    /**
     * Crea la cuenta en memoria y persiste la estructura física en disco.
     */
    public boolean crearCuenta(String nickname) {
        if (existeCuenta(nickname)) {
            return false; // Nickname duplicado
        }

        try {
            CuentaJugador nuevaCuenta = new CuentaJugador(nickname);

            // Intentar crear carpetas y archivo binario inicial
            boolean guardadoExitoso = gestorArchivos.crearEstructuraNuevaCuenta(nuevaCuenta);

            if (guardadoExitoso) {
                listaCuentas.agregar(nuevaCuenta);
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            return false; // Formato de nickname inválido
        }
    }

    private boolean existeCuenta(String nickname) {
        for (int i = 0; i < listaCuentas.obtenerTamano(); i++) {
            CuentaJugador cuenta = listaCuentas.obtener(i);
            if (cuenta.obtenerNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public String[] obtenerNombresCuentas() {
        int tamano = listaCuentas.obtenerTamano();
        String[] nombres = new String[tamano];
        for (int i = 0; i < tamano; i++) {
            nombres[i] = listaCuentas.obtener(i).obtenerNickname();
        }
        return nombres;
    }
}