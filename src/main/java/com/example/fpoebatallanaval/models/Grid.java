package com.example.fpoebatallanaval.models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.Serializable;

/**
 * Representa la cuadrícula (tablero) del juego de Batalla Naval.
 * Hereda de Rectangle para aprovechar posición, ancho y alto.
 * Contiene celdas (markers), barcos (ships) y funcionalidades para pintar, reiniciar y marcar posiciones.
 */
public class Grid extends Rectangle implements Serializable {

    // Identificador de versión para asegurar compatibilidad durante la serialización
    private static final long serialVersionUID = 1L;

    // Constantes de configuración del tablero
    public static final int CELL_SIZE = 40;   // Tamaño de cada celda (px)
    public static final int GRID_WIDTH = 10;  // Número de columnas
    public static final int GRID_HEIGHT = 10; // Número de filas
    public static final int[] BOAT_SIZES = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1}; // Tamaños de los barcos a generar

    // Matriz de celdas (marcadores) que representan el estado del tablero
    private Marker[][] markers = new Marker[GRID_WIDTH][GRID_HEIGHT];

    // Lista de barcos presentes en el tablero
    private List<Ship> ships;

    // Generador de aleatoriedad para colocar barcos
    private Random random;

    // Controla si se deben mostrar visualmente los barcos
    private boolean showShips;

    // Indica si todos los barcos han sido destruidos
    private boolean allShipsDestroyed;

    /**
     * Constructor que inicializa la cuadrícula y crea los marcadores.
     * @param x Posición horizontal inicial de la cuadrícula (en píxeles)
     * @param y Posición vertical inicial de la cuadrícula (en píxeles)
     */
    public Grid(int x, int y) {
        super(x, y, CELL_SIZE * GRID_WIDTH, CELL_SIZE * GRID_HEIGHT);
        createMarkerGrid();
        ships = new ArrayList<>();
        random = new Random();
        showShips = false;
    }

    /**
     * Inicializa la matriz de marcadores para cada celda del tablero.
     */
    private void createMarkerGrid() {
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                markers[x][y] = new Marker(
                        position.x + x * CELL_SIZE,
                        position.y + y * CELL_SIZE,
                        CELL_SIZE,
                        CELL_SIZE
                );
            }
        }
    }

    /**
     * Dibuja el contenido del tablero, incluyendo barcos (si corresponde), marcadores y rejilla.
     * @param gc Contexto gráfico para pintar en el canvas.
     */
    public void paint(GraphicsContext gc) {
        drawMarkers(gc);
        drawGrid(gc);

        for (Ship ship : ships) {


            if (showShips || ship.isDestroyed()) {
                System.out.println("Dibujando barcos del grid en: " + position.x + "," + position.y);
                ship.paint(gc);
            }
        }

    }

    public void setShowShips(boolean showShips) { this.showShips = showShips; }

    public boolean isShowingShips() { return showShips; }

    /**
     * Restaura el tablero a su estado inicial, eliminando marcas y barcos.
     */
    public void reset() {
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                markers[x][y].reset();
            }
        }
        ships.clear();
        showShips = false;
        allShipsDestroyed = false;
    }

    /**
     * Marca una posición del tablero. Evalúa si fue un impaco a un barco.
     * @param pos Posición a marcar.
     * @return true si la posición contenía un barco, false si fue agua o ya estaba marcada.
     */
    public boolean markPosition(Position pos) {
        Marker marker = markers[pos.x][pos.y];
        if (marker.isMarked()) {
            return false; // Ya está marcado
        }
        marker.mark();

        allShipsDestroyed = true;
        for (Ship ship : ships) {
            if (!ship.isDestroyed()) {
                allShipsDestroyed = false;
                break;
            }
        }

        return marker.isShip();
    }

    /**
     * Verifica si todos los barcos en el tablero han sido destruidos.
     * @return true si no quedan barcos activos, false en caso contrario.
     */
    public boolean areAllShipsDestroyed() {
        for (Ship ship : ships) {
            if (!ship.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    public boolean isPositionMarked(Position pos) { return markers[pos.x][pos.y].isMarked(); }

    public Marker getMarkerAtPosition(Position pos) { return markers[pos.x][pos.y]; }

    /**
     * Convierte coordenadas de mouse (píxeles) a posición lógica dentro de la cuadrícula.
     * Si está fuera de los límites, retorna (-1, -1).
     * @param mouseX Coordenada X del mouse
     * @param mouseY Coordenada Y del mouse
     * @return Posición lógica en la cuadrícula
     */
    public Position getPositionInGrid(int mouseX, int mouseY) {
        if (!isPositionInside(new Position(mouseX, mouseY))) {
            return new Position(-1, -1);
        }

        int gridX = Math.max(0, Math.min((mouseX - position.x) / CELL_SIZE, GRID_WIDTH - 1));
        int gridY = Math.max(0, Math.min((mouseY - position.x) / CELL_SIZE, GRID_HEIGHT - 1));

        return new Position(gridX, gridY);
    }

    /**
     * Determina si un barco puede colocarse en una posición dada sin chocar con otros ni salirse del tablero.
     * @param gridX    Coordenada X inicial
     * @param gridY    Coordenada Y inicial
     * @param segments Cantidad de segmentos del barco
     * @param sideways Orientación: true para horizontal, false para vertical
     * @return true si puede colocarse, false si hay conflicto
     */
    public boolean canPlaceShipAt(int gridX, int gridY, int segments, boolean sideways) {
        // Generar una lista de todas las celdas que el barco ocuparía
        List<Position> occupiedCells = new ArrayList<>();

        for (int i = 0; i < segments; i++) {
            if (sideways) {
                // Barco horizontal: Aumentar X en cada segmento.
                occupiedCells.add(new Position(gridX + i, gridY));
            } else {
                // Barco vertical: Aumentar Y en cada segmento.
                occupiedCells.add(new Position(gridX, gridY + i));
            }
        }

        // Validar cada celda ocupada
        for (Position cell : occupiedCells) {
            // Verificar si está fuera de los límites del tablero.
            if (cell.x < 0 || cell.x >= GRID_WIDTH || cell.y < 0 || cell.y >= GRID_HEIGHT) {
                return false; // Celda fuera del tablero.
            }

            // Verificar si la celda ya está ocupada por otro barco.
            if (markers[cell.x][cell.y].isShip()) {
                return false; // Celda ya ocupada.
            }
        }

        // Si todas las celdas son válidas, el barco puede colocarse.
        return true;
    }

    /**
     * Dibuja los marcadores en el tablero.
     * @param gc Contexto gráfico para pintar en el canvas.
     */
    private void drawMarkers(GraphicsContext gc) {
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                markers[x][y].paint(gc);
            }
        }
    }

    /**
     * Dibuja la cuadrícula del tablero como líneas negras sobre las celdas.
     * @param gc Contexto gráfico para pintar en el canvas.
     */
    private void drawGrid(GraphicsContext gc) {
        // Pinta el fondo de cada celda (relleno)
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                double xPos = position.x + x * CELL_SIZE;
                double yPos = position.y + y * CELL_SIZE;

                gc.setFill(Color.web("#08303B")); // Color de fondo de celda
                gc.fillRect(xPos, yPos, CELL_SIZE, CELL_SIZE);
            }
        }

        gc.setStroke(Color.BLACK);

        double y2 = position.y;
        double y1 = position.y + height;

        for (int x = 0; x <= GRID_WIDTH; x++) {
            double xPos = position.x + x * CELL_SIZE;
            gc.strokeLine(xPos, y1, xPos, y2);
        }

        double x2 = position.x;
        double x1 = position.x + width;

        for (int y = 0; y <= GRID_HEIGHT; y++) {
            double yPos = position.y + y * CELL_SIZE;
            gc.strokeLine(x1, yPos, x2, yPos);
        }
    }

    /**
     * Coloca aleatoriamente los barcos definidos por el arreglo BOAT_SIZES.
     */
    public void populateShips() {
        ships.clear();
        for (int i = 0; i < BOAT_SIZES.length; i++) {
            boolean sideways = random.nextBoolean();
            int gridX, gridY;
            do {
                gridX = random.nextInt(sideways ? GRID_WIDTH - BOAT_SIZES[i] : GRID_WIDTH);
                gridY = random.nextInt(sideways ? GRID_HEIGHT : GRID_HEIGHT - BOAT_SIZES[i]);
            } while (!canPlaceShipAt(gridX, gridY, BOAT_SIZES[i], sideways));
            placeShip(gridX, gridY, BOAT_SIZES[i], sideways);
        }
    }

    /**
     * Crea un nuevo barco y lo coloca físicamente y lógicamente en el tablero.
     * @param gridX    Coordenada X inicial
     * @param gridY    Coordenada Y inicial
     * @param segments Cantidad de segmentos del barco
     * @param sideways Orientación: true para horizontal, false para vertical
     */
    private void placeShip(int gridX, int gridY, int segments, boolean sideways) {
        placeShip(new Ship(new Position(gridX, gridY), new Position(position.x + gridX * CELL_SIZE, position.y + gridY * CELL_SIZE), segments, sideways), gridX, gridY);
    }

    /**
     * Añade el barco a la lista y marca sus posiciones en el tablero.
     * @param ship
     * @param gridX
     * @param gridY
     */
    public void placeShip(Ship ship, int gridX, int gridY) {
        ships.add(ship);
        if (ship.isSideWays()) {
            for (int x = 0; x < ship.getSegments(); x++) {
                markers[gridX + x][gridY].setAsShip(ships.get(ships.size() - 1));
            }
        } else {
            for (int y = 0; y < ship.getSegments(); y++) {
                markers[gridX][gridY + y].setAsShip(ships.get(ships.size() - 1));
            }
        }
    }

    /**
     * @return La lista de barcos colocados en el tablero.
     */
    public List<Ship> getShips() { return ships; }

    /**
     * Establece los barcos del tablero y actualiza los marcadores con su referencia.
     * @param ships
     */
    public void setShips(List<Ship> ships) {
        this.ships = ships;
        for (Ship ship : ships) {
            for (Position pos : ship.getOccupiedCoordinates()) {
                markers[pos.x][pos.y].setAsShip(ship);
            }
        }
    }

    /**
     * @return El estado de cada celca como una matriz booleana de marcados.
     */
    public boolean[][] getMarkers() {
        boolean[][] markerStates = new boolean[GRID_WIDTH][GRID_HEIGHT];
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                markerStates[x][y] = markers[x][y].isMarked();
            }
        }
        return markerStates;
    }

    /**
     * Restaura los marcadores a partir de una matriz booleana, y repinta el estado en el canvas.
     * @param markerStates
     * @param gc
     */
    public void setMarkers(boolean[][] markerStates, GraphicsContext gc) {
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                markers[x][y].reset();
                if (markerStates[x][y]) {
                    markers[x][y].mark();
                    // Aquí se debe asociar el marcador al barco correspondiente
                    for (Ship ship : ships) {
                        if (ship.getOccupiedCoordinates().contains(new Position(x, y))) {
                            markers[x][y].setAsShip(ship);
                        }
                    }
                    markers[x][y].paint(gc);
                }
            }
        }
    }

}