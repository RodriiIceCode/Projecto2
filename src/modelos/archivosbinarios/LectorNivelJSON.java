package modelos.archivosbinarios;

import modelos.estructura.ArregloDinamico;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Parsea manualmente el archivo JSON del jardín sin librerías externas.
 * Algoritmo: Lee el archivo como texto, limpia espacios/saltos de línea y
 * procesa mediante búsqueda de claves y delimitadores '[' y ']'.
 */
public class LectorNivelJSON {

    /**
     * Lee y valida un archivo JSON de nivel.
     * @param archivo El archivo seleccionado por el usuario.
     * @return Objeto ConfiguracionNivel con los datos cargados.
     * @throws Exception Si el archivo no cumple el formato o las dimensiones son inconsistentes.
     */
    public ConfiguracionNivel cargarNivel(File archivo) throws Exception {
        String contenido = leerArchivoTexto(archivo);

        int duracion = extraerNumero(contenido, "duracionSegundos");
        int cerebros = extraerNumero(contenido, "cerebrosIniciales");
        String[][] matrizJardin = extraerMatrizJardin(contenido);

        // Validación estricta del enunciado: Todos los carriles deben tener las mismas columnas
        validarDimensionesJardin(matrizJardin);

        return new ConfiguracionNivel(duracion, cerebros, matrizJardin);
    }

    /**
     * Lee todo el contenido del archivo de texto en una sola cadena.
     */
    private String leerArchivoTexto(File archivo) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                sb.append(linea.trim());
            }
        }
        return sb.toString();
    }

    /**
     * Extrae un valor numérico entero asociado a una clave en formato JSON.
     * Ejemplo: "duracionSegundos": 180
     */
    private int extraerNumero(String texto, String clave) throws Exception {
        String patron = "\"" + clave + "\"";
        int inicioClave = texto.indexOf(patron);
        if (inicioClave == -1) {
            throw new Exception("Propiedad faltante en JSON: " + clave);
        }

        int inicioDosPuntos = texto.indexOf(":", inicioClave + patron.length());
        int inicioValor = inicioDosPuntos + 1;

        // Avanzar hasta encontrar el primer dígito
        while (inicioValor < texto.length() && !Character.isDigit(texto.charAt(inicioValor))) {
            inicioValor++;
        }

        int finValor = inicioValor;
        while (finValor < texto.length() && Character.isDigit(texto.charAt(finValor))) {
            finValor++;
        }

        String valorStr = texto.substring(inicioValor, finValor);
        return Integer.parseInt(valorStr);
    }

    /**
     * Extrae la matriz bidimensional del arreglo 'jardin' en el JSON.
     */
    private String[][] extraerMatrizJardin(String texto) throws Exception {
        int inicioJardin = texto.indexOf("\"jardin\"");
        if (inicioJardin == -1) {
            throw new Exception("No se encontró la propiedad 'jardin' en el archivo.");
        }

        int inicioArregloPrincipal = texto.indexOf("[", inicioJardin);
        int finArregloPrincipal = texto.lastIndexOf("]");

        if (inicioArregloPrincipal == -1 || finArregloPrincipal == -1) {
            throw new Exception("Formato de matriz 'jardin' inválido.");
        }

        String bloqueJardin = texto.substring(inicioArregloPrincipal + 1, finArregloPrincipal).trim();

        // Extraer sub-arreglos que corresponden a los carriles
        ArregloDinamico<String[]> listaFilas = new ArregloDinamico<>();
        int cursor = 0;

        while (cursor < bloqueJardin.length()) {
            int inicioFila = bloqueJardin.indexOf("[", cursor);
            if (inicioFila == -1) break;

            int finFila = bloqueJardin.indexOf("]", inicioFila);
            if (finFila == -1) break;

            String contenidoFila = bloqueJardin.substring(inicioFila + 1, finFila);
            String[] casillas = parsearElementosFila(contenidoFila);
            listaFilas.agregar(casillas);

            cursor = finFila + 1;
        }

        if (listaFilas.obtenerTamano() == 0) {
            throw new Exception("El jardín debe contener al menos un carril.");
        }

        // Convertir nuestro ArregloDinamico a un arreglo bidimensional primitivo
        String[][] matriz = new String[listaFilas.obtenerTamano()][];
        for (int i = 0; i < listaFilas.obtenerTamano(); i++) {
            matriz[i] = listaFilas.obtener(i);
        }

        return matriz;
    }

    /**
     * Parsea una fila individual de casillas separadas por comas ("VACIO", "NUEZ", etc.).
     */
    private String[] parsearElementosFila(String textoFila) {
        String[] elementosTemporales = textoFila.split(",");
        ArregloDinamico<String> casillas = new ArregloDinamico<>();

        for (String el : elementosTemporales) {
            // Limpiar comillas, espacios y comas
            String limpiado = el.replace("\"", "").replace("[", "").replace("]", "").trim();
            if (!limpiado.isEmpty()) {
                casillas.agregar(limpiado);
            }
        }

        String[] resultado = new String[casillas.obtenerTamano()];
        for (int i = 0; i < casillas.obtenerTamano(); i++) {
            resultado[i] = casillas.obtener(i);
        }
        return resultado;
    }

    /**
     * Valida que todas las filas tengan la misma longitud de columnas.
     */
    private void validarDimensionesJardin(String[][] matriz) throws Exception {
        if (matriz.length == 0) {
            throw new Exception("El jardín está vacío.");
        }

        int columnasEsperadas = matriz[0].length;
        for (int i = 1; i < matriz.length; i++) {
            if (matriz[i].length != columnasEsperadas) {
                throw new Exception("Archivo inválido: El carril " + (i + 1) +
                        " tiene " + matriz[i].length + " columnas, pero el carril 1 tiene " + columnasEsperadas + ".");
            }
        }
    }
}