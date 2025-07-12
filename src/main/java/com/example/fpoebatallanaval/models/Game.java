package com.example.fpoebatallanaval.models;

import java.util.List;

import java.io.Serializable;

/**
 * La clase Game representa el estado completo de una partida de Batalla Naval.
 * Sirve como estructura para guardar o cargar una partida,
 * incluyendo los barcos de ambos jugadores, los disparos realizados y la fase actual del juego.
 */
public class Game implements Serializable {

    // Identificador de versión para asegurar compatibilidad durante la serialización
    private static final long serialVersionUID = 1L;

    // Lista de barcos del jugador humano
    private List<Ship> playerShips;

    // Lista de barcos de la computadora (IA)
    private List<Ship> computerShips;

    // Matriz que indica las posiciones donde el jugador ha disparado
    private boolean[][] playerShots;

    // Matriz que indica las posiciones donde la computadora ha disparado
    private boolean[][] computerShots;

    // Nombre del jugador (puede mostrarse en la interfaz o en los resultados)
    private String playerName;

    // Fase acutal del juego (por ejemplo: colocación de barcos, turno del jugador, eyc.)
    private com.example.fpoebatallanaval.controller.GameController.GamePhase gamePhase;

    // --- Getters y Setters ---

    /**
     * @return Lista de barcos del jugador
     */
    public List<Ship> getPlayerShips() { return playerShips; }

    /**
     * Asigna la lista de barcos del jugador
     * @param playerShips Lista de barcos colocados por el jugador
     */
    public void setPlayerShips(List<Ship> playerShips) { this.playerShips = playerShips; }

    /**
     * @return Lista de barcos del oponente (IA)
     */
    public List<Ship> getComputerShips() { return computerShips; }

    /**
     * Asigna la lista de barcos de la computadora.
     * @param computerShips Lista de barcos colocados por la IA
     */
    public void setComputerShips(List<Ship> computerShips) { this.computerShips = computerShips; }

    /**
     * @return Matriz que indica las posiciones donde el jugador ha disparado
     */
    public boolean[][] getPlayerShots() { return playerShots; }

    /**
     * Asigna la matriz de disparos realizados por el jugddor.
     * @param playerShots Matriz booleana con marcas de disparos
     */
    public void setPlayerShots(boolean[][] playerShots) { this.playerShots = playerShots; }

    /**
     * @return Matriz que indica las posiciones donde la computadora ha disparado
     */
    public boolean[][] getComputerShots() { return computerShots; }

    /**
     * Asigna la matriz de disparos realizados por la computadora.
     * @param computerShots Matriz booleana con marcas de disparos de la IA
     */
    public void setComputerShots(boolean[][] computerShots) { this.computerShots = computerShots; }

    /**
     * @return Nombre del jugador
     */
    public String getPlayerName() { return playerName; }

    /**
     * Asigna el nombre del jugador.
     * @param playerName Nombre personalizado ingresado por el usuario
     */
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    /**
     * @return Fase actual del juego
     */
    public com.example.fpoebatallanaval.controller.GameController.GamePhase getGamePhase() { return gamePhase; }

    /**
     * Asigna la fase actual del juego.
     * @param gamePhase Enum que representa la etapa del juego
     */
    public void setGamePhase(com.example.fpoebatallanaval.controller.GameController.GamePhase gamePhase) { this.gamePhase = gamePhase; }

}