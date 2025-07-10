package com.example.fpoebatallanaval.models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ship implements Serializable {

    private static final long serialVersionUID = 1L;
    public enum ShipPlacementColour implements Serializable {
        Valid, Invalid, Placed
    }
    private Position gridPosition;
    private Position drawPosition;
    private ShipPlacementColour shipPlacementColour;
    private int segments;
    private boolean isSideWays;
    private int destroyedSections;

    public Ship(Position gridPos, Position drawPos, int segments, boolean isSideWays) {
        this.gridPosition = gridPos;
        this.drawPosition = drawPos;
        this.segments = segments;
        this.isSideWays = isSideWays;
        this.destroyedSections = 0;
        shipPlacementColour = ShipPlacementColour.Placed;
    }

    public void paint(GraphicsContext gc) {
        if (shipPlacementColour == ShipPlacementColour.Placed) {
            gc.setFill(isDestroyed() ? Color.BLUE : Color.DARKGRAY);
        } else {
            gc.setFill(shipPlacementColour == ShipPlacementColour.Valid ? Color.GREEN : Color.RED);
        }

        if (isSideWays) {
            paintHorizontal(gc);
        } else {
            paintVertical(gc);
        }
    }

    public void paintHorizontal(GraphicsContext gc) {
        int cellSize = Grid.CELL_SIZE;
        int boatWidth = (int) (cellSize * 0.8);
        int boatTopY = drawPosition.y + cellSize / 2 - boatWidth / 2;

        // Proa triangular
        gc.fillPolygon(
                new double[]{drawPosition.x + cellSize / 4, drawPosition.x + cellSize, drawPosition.x + cellSize},
                new double[]{drawPosition.y + cellSize / 2, boatTopY, boatTopY + boatWidth},
                3);

        // Cuerpo rectangular
        gc.fillRect(drawPosition.x + cellSize, boatTopY, cellSize * (segments - 1.2), boatWidth);
    }

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

    public void setShipPlacementColour(ShipPlacementColour shipPlacementColour) {
        this.shipPlacementColour = shipPlacementColour;
    }

    public int getSegments() { return segments; }

    public void toggleSideways() { isSideWays = !isSideWays; }

    public void destroySection() { destroyedSections++; }

    public boolean isDestroyed() { return destroyedSections >= segments; }

    public void setDrawPosition(Position gridPos, Position drawPos) {
        this.gridPosition = gridPos;
        this.drawPosition = drawPos;
    }

    public boolean isSideWays() { return isSideWays; }

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
