package com.example.fpoebatallanaval.models;

public class GameExceptions {

    public static class CantPlaceShip extends Exception {
        public CantPlaceShip(String message) {
            super(message);
        }
    }

    public static class InvalidZoneException extends Exception {
        public InvalidZoneException(String message) {
            super(message);
        }
    }
}
