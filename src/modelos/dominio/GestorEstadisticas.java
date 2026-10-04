package modelos.dominio;

import java.io.*;

/**
 * Gestor de persistencia en texto plano para el historial de métricas del usuario.
 */
public class GestorEstadisticas {

    public static void registrarResultado(String nickname, boolean victoria, int zombisInvocados, int cerebrosGastados) {
        File directorio = new File("usuarios/" + nickname);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        File archivo = new File(directorio, "estadisticas.txt");
        int partidas = 0, ganadas = 0, perdidas = 0, zombisTotales = 0, cerebrosTotales = 0;

        // Leer datos anteriores si existen
        if (archivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                partidas = Integer.parseInt(reader.readLine().split(":")[1].trim());
                ganadas = Integer.parseInt(reader.readLine().split(":")[1].trim());
                perdidas = Integer.parseInt(reader.readLine().split(":")[1].trim());
                zombisTotales = Integer.parseInt(reader.readLine().split(":")[1].trim());
                cerebrosTotales = Integer.parseInt(reader.readLine().split(":")[1].trim());
            } catch (Exception ignored) {}
        }

        // Acumular métricas
        partidas++;
        if (victoria) ganadas++; else perdidas++;
        zombisTotales += zombisInvocados;
        cerebrosTotales += cerebrosGastados;

        // Sobreescribir archivo actualizado
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            writer.write("Partidas Jugadas: " + partidas + "\n");
            writer.write("Partidas Ganadas: " + ganadas + "\n");
            writer.write("Partidas Perdidas: " + perdidas + "\n");
            writer.write("Zombis Invocados: " + zombisTotales + "\n");
            writer.write("Cerebros Gastados: " + cerebrosTotales + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String obtenerEstadisticasFormateadas(String nickname) {
        File archivo = new File("usuarios/" + nickname + "/estadisticas.txt");
        if (!archivo.exists()) {
            return "No hay datos de estadísticas registrados para " + nickname + ".";
        }

        StringBuilder sb = new StringBuilder("=== ESTADÍSTICAS DE " + nickname.toUpperCase() + " ===\n\n");
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                sb.append(linea).append("\n");
            }
        } catch (IOException e) {
            return "Error al leer el archivo de estadísticas.";
        }
        return sb.toString();
    }
}