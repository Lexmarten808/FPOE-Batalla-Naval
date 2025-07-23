package com.example.fpoebatallanaval.models;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * La clase {@code GameDataManager} se encarga de gestionar los datos persistentes del jugador,
 * como el nickname y la cantidad de barcos hundidos. Utiliza archivos de texto para almacenar y cargar
 * esta información de forma local.
 *
 * <p>Los datos se guardan en la carpeta {@code data/}, que se crea automáticamente si no existe.</p>
 */
public class GameDataManager {
    private static final String NICKNAME_FILE = "data/nickname.txt";
    private static final String PLAYER_STATS_FILE = "data/players.txt";
    private static final Map<String, Integer> playerStats = new HashMap<>();
    private static String currentNickname = "Jugador";

    static {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs(); // crea la carpeta si no existe
        }

        loadPlayerStats();
        loadNickname();
    }

    // ----- Nickname -----

    /**
     * Guarda el nickname del jugador en el archivo correspondiente.
     *
     * @param nickname El nombre que se desea guardar.
     */
    public static void saveNickname(String nickname) {
        currentNickname = nickname;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NICKNAME_FILE))) {
            writer.write(nickname);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga el nickname almacenado desde el archivo. Si el archivo no existe, se asigna "Guest".
     *
     * @return El nickname cargado o "Guest" si no hay ninguno guardado.
     */
    public static String loadNickname() {
        File file = new File(NICKNAME_FILE);
        if (!file.exists()) {
            currentNickname = "Guest";
            return currentNickname;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            currentNickname = reader.readLine();
        } catch (IOException e) {
            currentNickname = "Guest";
        }

        return currentNickname;
    }

    /**
     * Obtiene el nickname actual del jugador.
     *
     * @return El nickname actual.
     */
    public static String getCurrentNickname() {
        return currentNickname;
    }

    // ----- Barcos Hundidos -----

    /**
     * Devuelve la cantidad de barcos hundidos registrados para un jugador.
     *
     * @param nickname El nombre del jugador.
     * @return El número de barcos hundidos registrados, o 0 si no hay datos.
     */
    public static int getBarcosHundidos(String nickname) {
        return playerStats.getOrDefault(nickname, 0);
    }

    /**
     * Actualiza el total de barcos hundidos para un jugador sumando los nuevos hundimientos,
     * y guarda los cambios en el archivo.
     *
     * @param nickname El nombre del jugador.
     * @param nuevos   La cantidad de barcos hundidos que se deben agregar.
     */
    public static void updateBarcosHundidos(String nickname, int nuevos) {
        int actuales = getBarcosHundidos(nickname);
        playerStats.put(nickname, actuales + nuevos);
        savePlayerStats();
    }

    /**
     * Carga desde archivo las estadísticas de barcos hundidos por jugador y las almacena en memoria.
     * El archivo debe tener el formato: {@code nombre cantidad} por línea.
     */
    private static void loadPlayerStats() {
        File file = new File(PLAYER_STATS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(" ");
                if (parts.length == 2) {
                    String name = parts[0];
                    int count = Integer.parseInt(parts[1]);
                    playerStats.put(name, count);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda las estadísticas actuales de barcos hundidos por jugador en el archivo correspondiente.
     */
    public static void savePlayerStats() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PLAYER_STATS_FILE))) {
            for (Map.Entry<String, Integer> entry : playerStats.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifica si un jugador ya existe en las estadísticas guardadas.
     *
     * @param nickname El nombre del jugador.
     * @return {@code true} si el jugador existe, {@code false} en caso contrario.
     */
    public static boolean playerExists(String nickname) {
        return playerStats.containsKey(nickname);
    }

}
