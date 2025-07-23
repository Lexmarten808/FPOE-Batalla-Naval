package com.example.fpoebatallanaval.models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

/**
 * La clase Ship representa un barco dentro del juego de Batalla Naval.
 * Contiene información sobre su posición, orientación, cantidad de segmentos, estado visual y destrucción.
 */
public class Ship implements Serializable {

    // Identificador de versión para asegurar compatibilidad durante la serialización
    private static final long serialVersionUID = 1L;

    /**
     * Enum que representa el estado visual del barco durante su colocación.
     */
    public enum ShipPlacementColour implements Serializable {
        Valid,   // Posición válida para colocar el barco (verde)
        Invalid, // Posición inválida (rojo)
        Placed   // Ya colocado en el tablero (gris o azul si destruido)
    }

    private Position gridPosition; // Posición en la cuadrícula lógica (índices de la celda)
    private Position drawPosition; // Posición en el canvas donde se dibuja el barco (en píxeles)
    private ShipPlacementColour shipPlacementColour; // Estado visual del barco (solo usado al colocar)
    private int segments; // Número de segmentos que tiene el barco (tamaño)
    private boolean isSideWays; // Indica si el barco está colocado en orientación horizontal
    private int destroyedSections; // Contador de secciones que han sido impactadas

    /**
     * Constructor del barco.
     * @param gridPos    Posición lógica del barco en la cuadrícula (índices)
     * @param drawPos    Posición gráfica en píxeles para dibujarlo
     * @param segments   Número de segmentos que conforman el barco
     * @param isSideWays Orientación del barco (true = horizontal, false = vertical)
     */
    public Ship(Position gridPos, Position drawPos, int segments, boolean isSideWays) {
        this.gridPosition = gridPos;
        this.drawPosition = drawPos;
        this.segments = segments;
        this.isSideWays = isSideWays;
        this.destroyedSections = 0;
        shipPlacementColour = ShipPlacementColour.Placed;
    }

    /**
     * Dibuja el barco en el canvas, según su estado (colocado, válido, inválido o destruido).
     * @param gc Contexto gráfico donde se realiza le dibujo
     */
    public void paint(GraphicsContext gc) {
        // Determina el color según el estado del barco
        if (shipPlacementColour == ShipPlacementColour.Placed) {
            gc.setFill(isDestroyed() ? Paint.valueOf("#041257") : Paint.valueOf("#595d70"));
        } else {
            gc.setFill(Paint.valueOf(shipPlacementColour == ShipPlacementColour.Valid ? "#138537" : "#851326"));
        }

        // Dibuja según la orientación
        if (isSideWays) {
            paintHorizontal(gc);
        } else {
            paintVertical(gc);
        }
    }

    /**
     * Dibuja un barco horizontal (triángulo a la izquierda y cuerpo rectangular).
     */
    public void paintHorizontal(GraphicsContext gc) {
        int cellSize = Grid.CELL_SIZE;
        int boatWidth = (int) (cellSize * 0.8);
        int boatTopY = drawPosition.y + cellSize / 2 - boatWidth / 2;

        // Dibuja la proa triangular (inicio del barco)
        gc.fillPolygon(
                new double[]{drawPosition.x + cellSize / 4, drawPosition.x + cellSize, drawPosition.x + cellSize},
                new double[]{drawPosition.y + cellSize / 2, boatTopY, boatTopY + boatWidth},
                3);

        // Dibuja el cuerpo del barco (rectángulo)
        gc.fillRect(drawPosition.x + cellSize, boatTopY, cellSize * (segments - 1.2), boatWidth);

    }

    /**
     * Dibuja un barco vertical (triángulo en la parte superior y cuerpo rectangular).
     */
    public void paintVertical(GraphicsContext gc) {
        int cellSize = Grid.CELL_SIZE;
        int boatWidth = (int) (cellSize * 0.8);
        int boatLeftX = drawPosition.x + cellSize / 2 - boatWidth / 2;

        // Proa triangular
        gc.fillPolygon(
                new double[]{drawPosition.x + cellSize / 2, boatLeftX, boatLeftX + boatWidth},
                new double[]{drawPosition.y + cellSize / 4, drawPosition.y + cellSize, drawPosition.y + cellSize},
                3);

        // Cuerpo rectangular
        gc.fillRect(boatLeftX, drawPosition.y + cellSize, boatWidth, cellSize * (segments - 1.2));
    }

    /**
     * Establece el estado visual del barco (para colorearlo al colocar).
     * @param shipPlacementColour Estado visual (válido, inválido o colocado)
     */
    public void setShipPlacementColour(ShipPlacementColour shipPlacementColour) {
        this.shipPlacementColour = shipPlacementColour;
    }

    /**
     * @return Número total de segmentos que tiene el barco
     */
    public int getSegments() { return segments; }

    /**
     * Invierte la orientación actual del barco (horizontal a vertical o viceversa).
     */
    public void toggleSideways() { isSideWays = !isSideWays; }

    /**
     * Marca una sección del barco como destruida.
     */
    public void destroySection() { destroyedSections++; }

    /**
     * @return true si todos los segmentos fueron impactados, false en caso contrario
     */
    public boolean isDestroyed() { return destroyedSections >= segments; }

    /**
     * Establece la posición lógica (en la cuadrícula) y la posición de dibujo del barco.
     * @param gridPos Nueva posición lógica (índices de celda)
     * @param drawPos Nueva posición gráfica (coordenadas en píxeles)
     */
    public void setDrawPosition(Position gridPos, Position drawPos) {
        this.gridPosition = gridPos;
        this.drawPosition = drawPos;
    }

    /**
     * @return true si el barco está en orientación horizontal, false si es vertical
     */
    public boolean isSideWays() { return isSideWays; }

    /**
     * Devuelve una lista de las coordenadas que ocupa el barco en la cuadrícula.
     * Esto se usa para marcar el tablero y detectar impactos.
     * @return Lista de posiciones que ocupa el barco
     */
    public List<Position> getOccupiedCoordinates() {
        List<Position> coordinates = new ArrayList<>();
        if (isSideWays) {
            for (int x = 0; x < segments; x++) {
                coordinates.add(new Position(gridPosition.x + x, gridPosition.y));
            }
        } else {
            for (int y = 0; y < segments; y++) {
                coordinates.add(new Position(gridPosition.x, gridPosition.y + y));
            }
        }
        return coordinates;
    }

}