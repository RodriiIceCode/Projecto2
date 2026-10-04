package modelos.archivosbinarios;

import modelos.dominio.CuentaJugador;
import modelos.estructura.ArregloDinamico;

import java.io.*;

/**
 * GestorArchivos administra la lectura y escritura en el sistema de archivos del SO.
 * Implementa serialización binaria nativa (ObjectInputStream/ObjectOutputStream)
 * y flujo de archivos de texto plano sin librerías externas.
 */
public class GestorArchivos {
    private static final String RUTA_RAIZ_DATOS = "datos";
    private static final String RUTA_CUENTAS = RUTA_RAIZ_DATOS + File.separator + "cuentas";

    public GestorArchivos() {
        asegurarDirectorioRaiz();
    }

    /**
     * Garantiza la existencia de la carpeta 'datos/cuentas/' al iniciar el programa.
     * Justificación técnica: Previene 'FileNotFoundException' al intentar guardar datos.
     */
    private void asegurarDirectorioRaiz() {
        File carpetaRaiz = new File(RUTA_CUENTAS);
        if (!carpetaRaiz.exists()) {
            carpetaRaiz.mkdirs(); // Crea carpetas padres e hijas si no existen
        }
    }

    /**
     * Crea la estructura física de carpetas y archivos para un nuevo usuario:
     * - datos/cuentas/<nickname>/
     * - datos/cuentas/<nickname>/partidas/
     * - datos/cuentas/<nickname>/estadisticas.txt
     * - datos/cuentas/<nickname>/cuenta.dat
     *
     * @param cuenta Instancia del nuevo usuario a guardar.
     * @return true si la estructura se creó exitosamente.
     */
    public boolean crearEstructuraNuevaCuenta(CuentaJugador cuenta) {
        String nick = cuenta.obtenerNickname();
        File carpetaUsuario = new File(RUTA_CUENTAS + File.separator + nick);
        File carpetaPartidas = new File(carpetaUsuario, "partidas");

        // 1. Crear directorios físicos
        if (!carpetaUsuario.exists() && !carpetaUsuario.mkdirs()) {
            return false;
        }
        if (!carpetaPartidas.exists()) {
            carpetaPartidas.mkdirs();
        }

        // 2. Crear archivo inicial de estadísticas (.txt plano)
        crearArchivoEstadisticasInicial(nick);

        // 3. Serializar el objeto de la cuenta (.dat binario)
        return guardarCuenta(cuenta);
    }

    /**
     * Guarda (serializa) el objeto CuentaJugador en su respectivo archivo binario.
     * Uso de try-with-resources: Garantiza el cierre automático de los flujos de memoria
     * previniendo fugas de recursos (Memory Leaks).
     */
    public boolean guardarCuenta(CuentaJugador cuenta) {
        String rutaArchivo = RUTA_CUENTAS + File.separator + cuenta.obtenerNickname() + File.separator + "cuenta.dat";
        File archivo = new File(rutaArchivo);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(cuenta);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Escanea el directorio 'datos/cuentas/', busca subcarpetas de usuarios y
     * deserializa cada 'cuenta.dat' existente para restaurarlas en memoria al iniciar la App.
     *
     * @return Un ArregloDinamico personalizado con todas las cuentas encontradas.
     */
    public ArregloDinamico<CuentaJugador> cargarTodasLasCuentas() {
        ArregloDinamico<CuentaJugador> cuentasCargadas = new ArregloDinamico<>();
        File carpetaCuentas = new File(RUTA_CUENTAS);
        File[] carpetasUsuarios = carpetaCuentas.listFiles(File::isDirectory);

        if (carpetasUsuarios != null) {
            for (File carpeta : carpetasUsuarios) {
                File archivoCuenta = new File(carpeta, "cuenta.dat");
                if (archivoCuenta.exists()) {
                    // Deserialización binaria nativa
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoCuenta))) {
                        CuentaJugador cuenta = (CuentaJugador) ois.readObject();
                        cuentasCargadas.agregar(cuenta);
                    } catch (IOException | ClassNotFoundException e) {
                        System.err.println("Error al cargar la cuenta en: " + carpeta.getName());
                    }
                }
            }
        }
        return cuentasCargadas;
    }

    /**
     * Escribe el archivo de texto plano para las estadísticas de la cuenta.
     * Se usa FileWriter / BufferedWriter nativo para formato legible (.txt).
     */
    private void crearArchivoEstadisticasInicial(String nickname) {
        String rutaEstadisticas = RUTA_CUENTAS + File.separator + nickname + File.separator + "estadisticas.txt";
        File archivo = new File(rutaEstadisticas);

        if (!archivo.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                writer.write("=== ESTADÍSTICAS DE JUGADOR: " + nickname + " ===");
                writer.newLine();
                writer.write("Partidas Jugadas: 0");
                writer.newLine();
                writer.write("Partidas Ganadas: 0");
                writer.newLine();
                writer.write("Partidas Perdidas: 0");
                writer.newLine();
                writer.write("Plantas Destruidas: 0");
                writer.newLine();
                writer.write("Zombis Invocados: 0");
                writer.newLine();
                writer.write("Cerebros Gastados: 0");
                writer.newLine();
                writer.write("Mejor Tiempo Victoria (s): N/A");
                writer.newLine();
                writer.write("Podadoras Activadas: 0");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
