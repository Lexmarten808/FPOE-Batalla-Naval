package com.example.fpoebatallanaval.model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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

    public static void saveNickname(String nickname) {
        currentNickname = nickname;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NICKNAME_FILE))) {
            writer.write(nickname);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadNickname() {
        File file = new File(NICKNAME_FILE);
        if (!file.exists()) {
            currentNickname = "Jugador";
            return currentNickname;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            currentNickname = reader.readLine();
        } catch (IOException e) {
            currentNickname = "Jugador";
        }

        return currentNickname;
    }

    public static String getCurrentNickname() {
        return currentNickname;
    }

    // ----- Barcos Hundidos -----

    public static int getBarcosHundidos(String nickname) {
        return playerStats.getOrDefault(nickname, 0);
    }

    public static void updateBarcosHundidos(String nickname, int nuevos) {
        int actuales = getBarcosHundidos(nickname);
        playerStats.put(nickname, actuales + nuevos);
        savePlayerStats();
    }

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
}
