package com.example.fpoebatallanaval.models;

import java.io.*;
import java.util.List;

/**
 * La clase Game representa el estado completo de una partida de Batalla Naval.
 * Sirve como estructura para guardar o cargar una partida,
 * incluyendo los barcos de ambos jugadores, los disparos realizados y la fase actual del juego.
 */
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    // 1. Instancia única (Singleton)
    private static Game instance;

    // 2. Constructor privado
    private Game() {}

    // 3. Método para obtener la instancia
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    private String playerName;
    private List<Ship> playerShips;
    private boolean[][] playerShots;

    private List<Ship> computerShips;
    private boolean[][] computerShots;

    private String phase;
    private boolean isMasterMode = false;

    // --- Getters y Setters ---

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String name) { playerName = name; }

    public List<Ship> getPlayerShips() { return playerShips; }
    public void setPlayerShips(List<Ship> playerShips) { this.playerShips = playerShips; }

    public boolean[][] getPlayerShots() { return playerShots; }
    public void setPlayerShots(boolean[][] playerShots) { this.playerShots = playerShots; }

    public List<Ship> getComputerShips() { return computerShips; }
    public void setComputerShips(List<Ship> computerShips) { this.computerShips = computerShips; }

    public boolean[][] getComputerShots() { return computerShots; }
    public void setComputerShots(boolean[][] computerShots) { this.computerShots = computerShots; }

    public String getPhase() { return phase; }
    public void setPhase(String phase) { this.phase = phase; }

    public boolean isMasterMode() { return isMasterMode; }
    public void setMasterMode(Boolean isMasterMode) { this.isMasterMode = isMasterMode; }

    @Override
    public String toString() {
        return "Game{" +
                "playerName='" + playerName + '\'' +
                ", phase=" + phase +
                ", isMasterMode=" + isMasterMode +
                ", playerShips=" + (playerShips != null ? playerShips.size() : 0) +
                ", computerShips=" + (computerShips != null ? computerShips.size() : 0) +
                '}';
    }

}