package com.example.fpoebatallanaval.models;

/**
 * Clase contenedora de excepciones personalizadas utilizadas en el juego Batalla Naval.
 */
public class GameExceptions {

    /**
     * Excepción lanzada cuando no se puede colocar un barco en el tablero.
     */
    public static class CantPlaceShip extends Exception {
        public CantPlaceShip(String message) {
            super(message);
        }
    }

    /**
     * Excepción lanzada cuando se intenta acceder o utilizar una zona no válida del tablero.
     */
    public static class InvalidZoneException extends Exception {
        public InvalidZoneException(String message) {
            super(message);
        }
    }
}
