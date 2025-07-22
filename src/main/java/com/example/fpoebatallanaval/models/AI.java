package com.example.fpoebatallanaval.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * La clase AI representa al oponente controlado por la computadora.
 * Su funcipon es seleccionar posiciones de disparo aleatorias dentro del tablero del jugador.
 */
public class AI {

    // Referencia al tablero del jugador humano
    private Grid playerGrid;

    // Generador de números aleatorias para decidir los movimientos
    private Random random;

    // Lista de posiciones disponibles para disparar
    private List<Position> possibleMoves;

    /**
     * Constructor de la IA. Recibe el tablero del jugador y prepara los movimientos posibles.
     * @param playerGrid Tablero del jugador contra el cual disparará la IA
     */
    public AI(Grid playerGrid) {
        this.playerGrid = playerGrid;
        this.random = new Random();
        this.possibleMoves = generateAllPossibleMoves();
    }

    /**
     * Genera una lista con todas las posiciones posibles del tablero.
     * Esta lista se usará para que la IA elija movimientos sin repetir.
     * @return Lista de posiciones válidas dentro del tablero
     */
    private List<Position> generateAllPossibleMoves() {
        List<Position> moves = new ArrayList<>();
        for (int x = 0; x < Grid.GRID_WIDTH; x++) {
            for (int y = 0; y < Grid.GRID_HEIGHT; y++) {
                moves.add(new Position(x, y));
            }
        }
        return moves;
    }

    /**
     * Reinicia los movimientos posibles para una nueva partida.
     */
    public void reset() {
        this.possibleMoves = generateAllPossibleMoves();
    }

    /**
     * Selecciona aleatoriamente una posición para disparar.
     * La posición seleccionada se elimina de la lista para evitar repetir jugadas.
     * @return Posición elegida por la IA para disparar
     * @throws IllegalStateException si ya no quedan movimientos disponibles
     */
    public Position selectMove() {
        if (possibleMoves.isEmpty()) {
            throw new IllegalStateException("No more possible moves available");
        }
        int index = random.nextInt(possibleMoves.size());
        Position move = possibleMoves.remove(index);
        return move;
    }

}