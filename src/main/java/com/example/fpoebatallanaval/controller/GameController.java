package com.example.fpoebatallanaval.controller;

import com.example.fpoebatallanaval.models.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    // ========== FXML ==========
    @FXML private Label NicknameId;
    @FXML private Label totalBarcosHundidosId;
    @FXML private Label BarcoshundidosId;
    @FXML private AnchorPane GameCanvasContainer;

    // ========== Lógica de juego ==========
    public enum GamePhase {PlacingShips, FiringShots, GameOver}

    private GamePhase gamePhase;
    private Grid playerGrid;
    private Grid computerGrid;
    private Ship placingShip;
    private Position tempPlacingPosition;
    private int placingShipIndex;
    private AI ai;
    private Canvas canvas;
    private String playerName;
    public static boolean masterMode;

    // ========== Constructor ==========
    public GameController() throws IOException {
        computerGrid = new Grid(Grid.CELL_SIZE * Grid.GRID_WIDTH + 40, 0);
        playerGrid = new Grid(0,0);
        gamePhase = GamePhase.PlacingShips;

        canvas = new Canvas(Grid.CELL_SIZE * Grid.GRID_WIDTH * 2 + 50, Grid.CELL_SIZE * Grid.GRID_HEIGHT);

        restart();
        draw();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mostrarDatosJugador();
        GameCanvasContainer.getChildren().add(canvas);

        // === REGISTRAR EVENTOS DE MOUSE ===
        canvas.setOnMouseReleased(new MouseReleasedHandler());
        canvas.setOnMouseMoved(new MouseMovedHandler());

        // Capturar teclas del teclado
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(event -> handleInput(event.getCode()));
    }

    @FXML
    public void mostrarDatosJugador() {
        String nickname = GameDataManager.getCurrentNickname();
        int totalHundidos = GameDataManager.getBarcosHundidos(nickname);

        NicknameId.setText(nickname);
        totalBarcosHundidosId.setText("Total hundidos: " + totalHundidos);
        BarcoshundidosId.setText("Hundidos en esta partida: 0");

    }

    // ========== Eventos de mouse ==========
    private class MouseReleasedHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            System.out.println("Mouse released at: (" + event.getX() + ", " + event.getY() + ")");

            Position mousePosition = new Position((int) event.getX(), (int) event.getY());
            if (gamePhase == GamePhase.PlacingShips && playerGrid.isPositionInside(mousePosition)) {
                tryPlaceShip(mousePosition);
            } else if (gamePhase == GamePhase.FiringShots && computerGrid.isPositionInside(mousePosition)) {
                tryFireAtComputer(mousePosition);
            }
            draw();
        }
    }

    private class MouseMovedHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            if (gamePhase != GamePhase.PlacingShips) return;
            tryMovePlacingShip(new Position((int) event.getX(), (int) event.getY()));
            draw();
        }
    }

    // ========== Entrada por teclado ==========
    public void handleInput(KeyCode keyCode) {
        if (keyCode == KeyCode.ESCAPE) {
            System.exit(1);
        } else if (keyCode == KeyCode.R) {
            restart();
        } else if (gamePhase == GamePhase.PlacingShips && keyCode == KeyCode.X) {
            placingShip.toggleSideways();
            // Reajustar posición para que no se salga del grid tras rotar
            Position adjustedPos = new Position(tempPlacingPosition);

            if (placingShip.isSideWays()) {
                adjustedPos.x = Math.min(adjustedPos.x, Grid.GRID_WIDTH - placingShip.getSegments());
            } else {
                adjustedPos.y = Math.min(adjustedPos.y, Grid.GRID_HEIGHT - placingShip.getSegments());
            }

            updateShipPlacement(adjustedPos);
        } else if (keyCode == KeyCode.D) {
            masterMode = true;
        } else if (keyCode == KeyCode.S && gamePhase == GamePhase.PlacingShips) {
            computerGrid.setShowShips(!computerGrid.isShowingShips());
        }
        draw();
    }

    // ========== Ciclo del juego ==========
    public void restart() {
        computerGrid.reset();
        playerGrid.reset();
        ai = new AI(playerGrid);
        ai.reset();

        playerGrid.setShowShips(true);
        computerGrid.setShowShips(false);

        placingShipIndex = 0;
        tempPlacingPosition = new Position(0, 0);
        placingShip = new Ship(new Position(0, 0), new Position(playerGrid.getPosition().x, playerGrid.getPosition().y), Grid.BOAT_SIZES[0], true);
        updateShipPlacement(tempPlacingPosition);

        computerGrid.populateShips();
        masterMode = false;
        gamePhase = GamePhase.PlacingShips;

        draw();
    }

    private void tryPlaceShip(Position mousePosition) {
        Position targetPos = playerGrid.getPositionInGrid(mousePosition.x, mousePosition.y);
        updateShipPlacement(targetPos);
        if (playerGrid.canPlaceShipAt(targetPos.x, targetPos.y, Grid.BOAT_SIZES[placingShipIndex], placingShip.isSideWays())) {
            placeShip(targetPos);
        }
    }

    private void placeShip(Position targetPos) {
        System.out.println("Barco #" + placingShipIndex + " colocado en: "
                + targetPos.x + "," + targetPos.y + " | Sideways: " + placingShip.isSideWays());
        placingShip.setShipPlacementColour(Ship.ShipPlacementColour.Placed);

        // Actualizar la posición de dibujo del barco ya colocado
        placingShip.setDrawPosition(
                new Position(targetPos),
                new Position(
                        playerGrid.getPosition().x + targetPos.x * Grid.CELL_SIZE,
                        playerGrid.getPosition().y + targetPos.y * Grid.CELL_SIZE
                )
        );

        playerGrid.placeShip(placingShip, targetPos.x, targetPos.y);
        placingShipIndex++;

        if (placingShipIndex < Grid.BOAT_SIZES.length) {
            placingShip = new Ship(
                    new Position(targetPos.x, targetPos.y),
                    new Position(
                            playerGrid.getPosition().x + targetPos.x * Grid.CELL_SIZE,
                            playerGrid.getPosition().y + targetPos.y * Grid.CELL_SIZE
                    ),
                    Grid.BOAT_SIZES[placingShipIndex],
                    placingShip.isSideWays()
            );
            updateShipPlacement(targetPos);
        } else {
            gamePhase = GamePhase.FiringShots;
            // Status panel
        }
        draw();
        saveGame("defaultPlayerName");
    }

    private void tryFireAtComputer(Position mousePosition) {
        Position targetPos = computerGrid.getPositionInGrid(mousePosition.x, mousePosition.y);
        if (!computerGrid.isPositionMarked(targetPos)) {
            doPlayerTurn(targetPos);
            if (!computerGrid.areAllShipsDestroyed()) doAiTurn();

            if (computerGrid.areAllShipsDestroyed()) {
                gamePhase = GamePhase.GameOver;
                // Status panel
            }
        }
        draw();
    }

    private void doPlayerTurn(Position targetPos) {
        boolean hit = computerGrid.markPosition(targetPos);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";
        if (hit && computerGrid.getMarkerAtPosition(targetPos).getAssociatedShip().isDestroyed()) {
            destroyed = "(Destroyed)";
        }
        draw();
        saveGame(playerName); // Guardado automático del estado
        if (computerGrid.areAllShipsDestroyed()) {
            gamePhase = GamePhase.GameOver;
            // Status panel
        }
    }

    private void doAiTurn() {
        Position aiMove = ai.selectMove();
        boolean hit = playerGrid.markPosition(aiMove);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";
        if (hit && playerGrid.getMarkerAtPosition(aiMove).getAssociatedShip().isDestroyed()) {
            destroyed = "(Destroyed)";
        }
        // status panel
        draw();
        saveGame(playerName); // Guardado automático del estado
        if (playerGrid.areAllShipsDestroyed()) {
            gamePhase = GamePhase.GameOver;
            // Status panel
        }
    }

    private void tryMovePlacingShip(Position mousePosition) {
        if (playerGrid.isPositionInside(mousePosition)) {
            Position targetPos = playerGrid.getPositionInGrid(mousePosition.x, mousePosition.y);

            // Ajuste la posición para asegurar que el barco no salga del tablero
            if (placingShip.isSideWays()) {
                targetPos.x = Math.max(0, Math.min(targetPos.x, Grid.GRID_WIDTH - placingShip.getSegments()));
            } else {
                targetPos.y = Math.max(0, Math.min(targetPos.y, Grid.GRID_HEIGHT - placingShip.getSegments()));
            }

            updateShipPlacement(targetPos);
        }
        draw();
    }

    private void updateShipPlacement(Position targetPos) {
        if (placingShip.isSideWays()) {
            targetPos.x = Math.min(targetPos.x, Grid.GRID_WIDTH - placingShip.getSegments());
        } else {
            targetPos.y = Math.min(targetPos.y, Grid.GRID_HEIGHT - placingShip.getSegments());
        }

        placingShip.setDrawPosition(
                new Position(targetPos),
                new Position(playerGrid.getPosition().x + targetPos.x * Grid.CELL_SIZE,
                        playerGrid.getPosition().y + targetPos.y * Grid.CELL_SIZE)
        );

        tempPlacingPosition = targetPos;

        if (playerGrid.canPlaceShipAt(tempPlacingPosition.x, tempPlacingPosition.y, placingShip.getSegments(), placingShip.isSideWays())) {
            placingShip.setShipPlacementColour(Ship.ShipPlacementColour.Valid);
        } else {
            placingShip.setShipPlacementColour(Ship.ShipPlacementColour.Invalid);
        }
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Dibujar los grids y elementos
        computerGrid.paint(gc);
        playerGrid.paint(gc);

        if (gamePhase == GamePhase.PlacingShips) {
            placingShip.paint(gc);
        }

        // statusPanel.paint(gc);
    }

    // ========== Guardar / Cargar ==========
    public void saveGame(String playerName) {
        this.playerName = playerName;
        Game game = new Game();
        game.setPlayerShips(playerGrid.getShips());
        game.setComputerShips(computerGrid.getShips());
        game.setPlayerShots(playerGrid.getMarkers());
        game.setComputerShots(computerGrid.getMarkers());
        game.setPlayerName(playerName);
        game.setGamePhase(gamePhase);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saved_game.dat"))) {
            oos.writeObject(game);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("player_name.txt"))) {
            writer.println(playerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saved_game.dat"))) {
            Game game = (Game) ois.readObject();

            // Cargar barcos y marcadores desde el estado guardado
            playerGrid.setShips(game.getPlayerShips());
            computerGrid.setShips(game.getComputerShips());
            playerGrid.setMarkers(game.getPlayerShots(), canvas.getGraphicsContext2D());
            computerGrid.setMarkers(game.getComputerShots(), canvas.getGraphicsContext2D());

            // Reasociar marcadores a barcos para el jugador
            for (Ship ship : playerGrid.getShips()) {
                for (Position pos : ship.getOccupiedCoordinates()) {
                    playerGrid.getMarkerAtPosition(pos).setAsShip(ship);
                }
            }

            // Reasociar marcadores a barcos para la computadora
            for (Ship ship : computerGrid.getShips()) {
                for (Position pos : ship.getOccupiedCoordinates()) {
                    computerGrid.getMarkerAtPosition(pos).setAsShip(ship);
                }
            }

            // Restaurar el resto del estado del juego
            gamePhase = game.getGamePhase();
            this.playerName = game.getPlayerName();
            // statusPanel.setTopLine("Bienvenido de nuevo, " + playerName + "!");
            draw();  // Redibujar el tablero con el estado restaurado

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerName() {
        return playerName; // Método para obtener el nombre del jugador
    }

    // Métodos vacíos (placeholder para interfaces si es necesario))
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}