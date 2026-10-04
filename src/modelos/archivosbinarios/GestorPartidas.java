package modelos.archivosbinarios;

import modelos.dominio.EstadoPartida;
import java.io.*;

/**
 * Administra el guardado y la carga de partidas guardadas (.dat).
 */
public class GestorPartidas {

    public static boolean guardarPartida(EstadoPartida estado, String nickname) {
        File directorio = new File("usuarios/" + nickname);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        File archivo = new File(directorio, "partida.dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(estado);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static EstadoPartida cargarPartida(String nickname) {
        File archivo = new File("usuarios/" + nickname + "/partida.dat");
        if (!archivo.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (EstadoPartida) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}