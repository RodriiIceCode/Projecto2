package modelos.dominio;

import java.io.Serializable;

/**
 * Representa la cuenta de un jugador.
 * Implementa Serializable para permitir su guardado nativo en archivos binarios.
 */
public class CuentaJugador implements Serializable {
    // Es buena práctica definir el serialVersionUID al implementar Serializable
    private static final long serialVersionUID = 1L;

    private String nickname;
    private int partidasJugadas;
    private int partidasGanadas;
    private int partidasPerdidas;
    private int plantasDestruidas;
    private int zombisInvocados;
    private int cerebrosGastados;
    private int mejorTiempoVictoria; // En segundos. Valor por defecto indicará que no hay victorias.
    private int podadorasActivadas;

    /**
     * Constructor principal de la cuenta.
     * @param nickname Nombre de usuario ya validado.
     */
    public CuentaJugador(String nickname) {
        if (!esNicknameValido(nickname)) {
            throw new IllegalArgumentException("Nickname inválido. Debe ser alfanumérico (1-12 caracteres).");
        }
        this.nickname = nickname;
        this.partidasJugadas = 0;
        this.partidasGanadas = 0;
        this.partidasPerdidas = 0;
        this.plantasDestruidas = 0;
        this.zombisInvocados = 0;
        this.cerebrosGastados = 0;
        this.mejorTiempoVictoria = Integer.MAX_VALUE; // Representa que aún no ha ganado
        this.podadorasActivadas = 0;
    }

    /**
     * Valida la regla de negocio del enunciado para el nickname.
     * Alfanumérico, entre 1 y 12 caracteres.
     */
    private boolean esNicknameValido(String nick) {
        if (nick == null || nick.length() < 1 || nick.length() > 12) {
            return false;
        }
        // Validación manual carácter por carácter (Evitamos RegEx complejos por rendimiento académico)
        for (int i = 0; i < nick.length(); i++) {
            char c = nick.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

    // Getters
    public String obtenerNickname() { return nickname; }
    public int obtenerPartidasJugadas() { return partidasJugadas; }
    // ... (restantes getters generados explícitamente según reglas, sin Lombok)

    /**
     * Método para actualizar estadísticas al finalizar una partida.
     */
    public void registrarFinDePartida(boolean victoria, int tiempoSegundos, int plantasDestruidasPartida,
                                      int zombisInvocadosPartida, int podadorasUsadas, int cerebrosUsados) {
        this.partidasJugadas++;
        this.plantasDestruidas += plantasDestruidasPartida;
        this.zombisInvocados += zombisInvocadosPartida;
        this.podadorasActivadas += podadorasUsadas;
        this.cerebrosGastados += cerebrosUsados;

        if (victoria) {
            this.partidasGanadas++;
            if (tiempoSegundos < this.mejorTiempoVictoria) {
                this.mejorTiempoVictoria = tiempoSegundos;
            }
        } else {
            this.partidasPerdidas++;
        }
    }
}