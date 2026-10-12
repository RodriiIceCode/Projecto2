package modelos.entidades.zombis;

public class FabricaZombis {

    public static Zombi crearZombi(String tipoZombi, int fila, int columna) {
        switch (tipoZombi.toUpperCase()) {
            case "ZOMBI_BASICO":
            case "ZOMBI":
                return new ZombiBasico(fila, columna);
            case "ZOMBI_CARACONO":
            case "CARACONO":
                return new ZombiCaracono(fila, columna);
            case "ZOMBI_SALTADOR":
            case "SALTADOR":
                return new ZombiSaltador(fila, columna);
            case "ZOMBI_DEPORTISTA":
            case "DEPORTISTA":
                return new ZombiDeportista(fila, columna);
            case "ZOMBI_BAILON":
            case "BAILON":
                return new ZombiBailon(fila, columna);
            case "ZOMBISTEIN":
                return new Zombistein(fila, columna);
            default:
                return null;
        }
    }
}