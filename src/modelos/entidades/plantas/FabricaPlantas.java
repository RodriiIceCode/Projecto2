package modelos.entidades.plantas;

public class FabricaPlantas {

    /**
     * Recibe el texto que viene del JSON o del usuario y devuelve el objeto correcto.
     * Es 'static' para que puedas usarlo desde cualquier parte sin crear un new FabricaPlantas().
     */
    public static Planta crearPlanta(String tipoPlanta, int fila, int columna) {

        // Convertimos a mayúsculas por si en el JSON viene como "Lanzaguisantes" o "lanzaguisantes"
        switch (tipoPlanta.toUpperCase().trim()) {
            case "LANZAGUISANTES":
                return new Lanzaguisantes(fila, columna);
            case "NUEZ":
                return new Nuez(fila, columna);
            case "PATATAPUM":
                return new Patatapum(fila, columna);
            case "REPETIDORA":
                return new Repetidora(fila, columna);
            case "HIELAGUISANTES":
                return new Hielaguisantes(fila, columna);
            case "NUEZ_CASCARARABIAS":
            case "CASCARARABIAS":
                return new NuezCascaraRabias(fila, columna);
            case "CARRONIVORA":
                return new Carronivora(fila, columna);
            case "PINCHOHIERBA":
                return new Pinchohierba(fila, columna);
            case "VACIO":
            default:
                return null;
        }
    }
}