package com.example.fpoebatallanaval.models;

import java.io.Serializable;

/**
 * La clase Position representa una coordenada (x, y) en el tablero del juego.
 * Implementa Serializable para permitir su serialización y uso en procesos como guardado o envío por red.
 */
public class Position implements Serializable {

    private static final long serialVersionUID = 1L;

    public int x; // Coordenada horizontal
    public int y; // Coordenada vertical

    /**
     * Constructor principal que inicializa una posición con coordenadas específicas.
     * @param x Coordenada horizontal
     * @param y Coordenada vertical
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor que copia las coordenadas desde otra instancia de Position.
     * @param posToCopy
     */
    public Position(Position posToCopy) {
        this.x = posToCopy.x;
        this.y = posToCopy.y;
    }

    /**
     * Compara esta posición con otra para verificar si tienen las mismas coordenadas.
     * @param obj Objeto con el que se va a comparar
     * @return true si ambas poisiciones tienen los mismos valores de x e y, false en caso contario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Mismo objeto
        if (obj == null || getClass() != obj.getClass()) return false; // Distinto tipo
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    /**
     * Devuelve una representación en cadena de la posición con el formato (x, y).
     * @return String con las coordenadas
     */
    @Override
    public String toString() { return "(" + x + ", " + y + ")"; }

}