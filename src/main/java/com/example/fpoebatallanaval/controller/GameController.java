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

/**
 * Controlador principal del juego.
 * Gestiona la lógica de juego, la interacción con el usuario y la actualización visual.
 */
public class GameController implements Initializable {

    // ========== FXML ==========
    @FXML private Label NicknameId;
    @FXML private Label totalBarcosHundidosId;
    @FXML private Label BarcoshundidosId;
    @FXML private AnchorPane GameCanvasContainer;
    @FXML private Label StatusLabel;

    // ========== Lógica de juego ==========
    public enum GamePhase {PlacingShips, FiringShots, GameOver}

    private GamePhase gamePhase;
    private Grid playerGrid;              // Tablero del jugador
    private Grid computerGrid;            // Tablero de la computadora
    private Ship placingShip;             // Barco que se está colocando actualmente
    private Position tempPlacingPosition; // Posición temporal para pintar el barco en movimiento
    private int placingShipIndex;         // Índice del barco que se está colocando
    private AI ai;                        // Componente de la IA enemiga
    private Canvas canvas;                // Área gráfica del juego
    private String playerName;            // Nombre del jugador
    public static boolean masterMode;     // Modo maestro (debug)
    private int barcosHundidos = 0;       //contador de barcos hundidos en la partida
    private int totalBarcosHundidos = 0;  //contador del total de barcos undidos


    // ========== Constructor ==========
    public GameController() throws IOException {
        // Crear tableros para el jugador y la máquina
        computerGrid = new Grid(Grid.CELL_SIZE * Grid.GRID_WIDTH + 40, 0);
        playerGrid = new Grid(0,0);
        gamePhase = GamePhase.PlacingShips;

        // Crear lienzo (canvas) para pintar el juego
        canvas = new Canvas(
                Grid.CELL_SIZE * Grid.GRID_WIDTH * 2 + 50,
                Grid.CELL_SIZE * Grid.GRID_HEIGHT
        );

        // Reiniciar juego e iniciar dibujo
        restart();
        draw();
    }

    /**
     * Método llamado automáticamente al cargar la vista.
     * Inicializa la interfaz, muestra los datos del jugador y configura los eventos.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mostrarDatosJugador();
        GameCanvasContainer.getChildren().add(canvas);

        // Registrar manejadores para eventos de mouse
        canvas.setOnMouseReleased(new MouseReleasedHandler());
        canvas.setOnMouseMoved(new MouseMovedHandler());

        // Habilitar captuas de teclas
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(event -> handleInput(event.getCode()));
        if(gamePhase != GamePhase.PlacingShips) {StatusLabel.setVisible(false);}
    }

    /**
     * Muestra en pantalla el nickname del jugador, los barcos hundidos totales
     * y los hundidos durante la partida actual
     */
    @FXML
    public void mostrarDatosJugador() {
        String nickname = GameDataManager.getCurrentNickname();
        int totalHundidos = GameDataManager.getBarcosHundidos(nickname);

        NicknameId.setText(nickname);
        totalBarcosHundidosId.setText("Total hundidos: " + totalHundidos);
        BarcoshundidosId.setText("Hundidos en esta partida: 0");

    }

    // ======================================
    // ========== EVENTOS DE MOUSE ==========
    // ======================================

    /**
     * Manejador para eventos de clic del mouse.
     * Dependiendo de la fase del juego, intenta colocar un barco o disparar.
     */
    private class MouseReleasedHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            System.out.println("Mouse released at: (" + event.getX() + ", " + event.getY() + ")");
            System.out.println("game state: " + gamePhase);
            Position mousePosition = new Position((int) event.getX(), (int) event.getY());

            try {
                if (gamePhase == GamePhase.PlacingShips && event.getX() >= 440 && event.getX() <= 840) {
                    throw new GameExceptions.InvalidZoneException("No puedes colocar tus barcos en territorio enemigo.");
                }
                if (gamePhase == GamePhase.FiringShots && event.getX() >= 0 && event.getX() <= 400) {
                    throw new GameExceptions.InvalidZoneException("No puedes atacar tus barcos en territorio aliado.");
                }
            } catch (GameExceptions.InvalidZoneException e) {
                System.out.println("Error: " + e.getMessage());
                AlertHelper.showErrorAlert("Zona inválida", "No puedes realizar esta acción", e.getMessage());
                return;
            }


            if (gamePhase == GamePhase.PlacingShips && playerGrid.isPositionInside(mousePosition)) {
                tryPlaceShip(mousePosition);
            } else if (gamePhase == GamePhase.FiringShots && computerGrid.isPositionInside(mousePosition)) {
                tryFireAtComputer(mousePosition);
            }
            draw();
            if(gamePhase != GamePhase.PlacingShips) {StatusLabel.setVisible(false);}
        }

    }

    /**
     * Manejador de eventos de movimiento del mouse.
     * Se utiliza para actualizar visualmente la posición del barco en colocación.
     */
    private class MouseMovedHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            if (gamePhase != GamePhase.PlacingShips) return;

            tryMovePlacingShip(new Position((int) event.getX(), (int) event.getY()));
            draw();
        }
    }

    // ========== Entrada por teclado ==========

    /**
     * Maneja las entradas del teclado durante el juego.
     * Teclas disponibles:
     * - ESCAPE: sale del juego.
     * - R: reinicia la partida.
     * - X: rota el barco en colocación.
     * - D: activa el modo maestro (debug).
     * - S: muestra u oculta los barcos enemigos (modo trampa).
     * @param keyCode Tecla presionada
     */
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

    /**
     * Reinicia la partida, incluyendo tableros, barcos y estado del juego.
     */
    public void restart() {
        computerGrid.reset();
        playerGrid.reset();
        ai = new AI(playerGrid);
        ai.reset();

        playerGrid.setShowShips(true);
        computerGrid.setShowShips(false);

        placingShipIndex = 0;
        tempPlacingPosition = new Position(0, 0);
        placingShip = new Ship(
                new Position(0, 0),
                new Position(playerGrid.getPosition().x, playerGrid.getPosition().y),
                Grid.BOAT_SIZES[0],
                true
        );
        updateShipPlacement(tempPlacingPosition);

        computerGrid.populateShips();
        masterMode = false;
        gamePhase = GamePhase.PlacingShips;

        draw();
    }

    /**
     * Intenta colocar el barco en la posición dada.
     * Si es válida, se llama a {@link #placeShip(Position)}.
     */
    private void tryPlaceShip(Position mousePosition) {
        Position targetPos = playerGrid.getPositionInGrid(mousePosition.x, mousePosition.y);
        updateShipPlacement(targetPos);

        if (playerGrid.canPlaceShipAt(targetPos.x, targetPos.y, Grid.BOAT_SIZES[placingShipIndex], placingShip.isSideWays())) {
            placeShip(targetPos);
        }
        else {
            try {
                if (!playerGrid.canPlaceShipAt(targetPos.x, targetPos.y, Grid.BOAT_SIZES[placingShipIndex], placingShip.isSideWays())) {
                    throw new GameExceptions.CantPlaceShip("no puedes colocar el barco aqui, elije una posicion valida");
                }

            } catch (GameExceptions.CantPlaceShip e) {
                System.out.println(e.getMessage());
                AlertHelper.showErrorAlert("Movimiento inválido", "No puedes realizar esta acción", e.getMessage());
            }
        }
    }

    /**
     * Coloca un barco en el tablero del jugador.
     * Si todos los barcos han sido colocados, se pasa a la fase de disparos.
     */
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

    /**
     * Intenta disparar a una posición del tablero enemigo.
     * Si es un disparo válido, se ejecuta el turno del jugador y luego el de la IA.
     */
    private void tryFireAtComputer(Position mousePosition) {
        Position targetPos = computerGrid.getPositionInGrid(mousePosition.x, mousePosition.y);
        if (!computerGrid.isPositionMarked(targetPos)) {
            doPlayerTurn(targetPos);

            if (!computerGrid.areAllShipsDestroyed()) doAiTurn();

            if (computerGrid.areAllShipsDestroyed()) {
                gamePhase = GamePhase.GameOver;
                String ganador= GameDataManager.getCurrentNickname();
                AlertHelper.showInfoAlert("the game has ended","ENDGAME!","the game has ended the winner is: "+ganador);
                // Status panel
            }
        }

        draw();
    }

    /**
     * Ejecuta el turno del jugador. Marca la posición y verifica si hubo acierto o destrucción.
     * @param targetPos
     */
    private void doPlayerTurn(Position targetPos) {

        boolean hit = computerGrid.markPosition(targetPos);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";

        //debug
        if (hit) {System.out.println("Barco atacado en: " + targetPos.x + ", " + targetPos.y);}
        if(!hit){System.out.println("tiro realizado fallido en: " + targetPos.x + ", " + targetPos.y);}


        if (hit && computerGrid.getMarkerAtPosition(targetPos).getAssociatedShip().isDestroyed()) {
            destroyed = "(Destroyed)";
            barcosHundidos+=1;
            actualizarLabelBarcosHundidos();

            //añadir los barcos destruidos por el jugador a su historial
            GameDataManager.updateBarcosHundidos(GameDataManager.getCurrentNickname(),1);
            actualizarLabelTotalBarcosHundidos();
        }
        draw();
        saveGame(playerName); // Guardado automático del estado
        if (computerGrid.areAllShipsDestroyed()) {
            gamePhase = GamePhase.GameOver;


            // Status panel
        }
    }

    /**
     * Ejecuta el turno de la IA. Selecciona una posición y marca el tablero del jugador.
     */
    private void doAiTurn() {
        Position aiMove = ai.selectMove();
        boolean hit = playerGrid.markPosition(aiMove);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";

        //debug
        if(hit){System.out.println("barco del jugador atacado en: " + aiMove.x + ", " + aiMove.y);}
        if(!hit){System.out.println("tiro de la maquina fallido");}

        if (hit && playerGrid.getMarkerAtPosition(aiMove).getAssociatedShip().isDestroyed()) {
            destroyed = "(Destroyed)";
            //añadir los barcos destruidos por el jugador a su historial
            GameDataManager.updateBarcosHundidos(GameDataManager.getCurrentNickname(),1);
            actualizarLabelTotalBarcosHundidos();

        }
        // status panel
        draw();
        saveGame(playerName); // Guardado automático del estado
        if (playerGrid.areAllShipsDestroyed()) {
            gamePhase = GamePhase.GameOver;


            // Status panel
        }
    }

    /**
     * Intenta mover visualmente el barco en colocación.
     * Se asegura de que no se salga del tablero.
     */
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

    /**
     * Actualiza la posición de dibujo del barco en colocación y define si es válida.
     */
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

    /**
     * Redibuja completamente el estado actual del juego en el canvas.
     */
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

    /**
     * Guarda el estado actual del juego (barcos, disparos fase y nombre del jugador)
     * en un archivo binario 'saved_game.dat' y el nombre del jugador en 'player_name.txt'.
     * @param playerName Nombre del jugador actual.
     */
    public void saveGame(String playerName) {
        this.playerName = playerName;

        // Crear objeto Game con el estado actual
        Game game = new Game();
        game.setPlayerShips(playerGrid.getShips());
        game.setComputerShips(computerGrid.getShips());
        game.setPlayerShots(playerGrid.getMarkers());
        game.setComputerShots(computerGrid.getMarkers());
        game.setPlayerName(playerName);
        game.setGamePhase(gamePhase);


        // Guardar estado del juego como objeto serializado
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saved_game.dat"))) {
            oos.writeObject(game);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Guardar el nombre del jugador por separado
        try (PrintWriter writer = new PrintWriter(new FileWriter("player_name.txt"))) {
            writer.println(playerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga el estado del juego desde 'saved_game.dat' y restaura todos los elementos visuales y lógicos.
     * También reasocia los marcadores a sus barcos correspondientes.
     */
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
            if(gamePhase != GamePhase.PlacingShips) {StatusLabel.setVisible(false);}

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve el nombre actual del jugador.
     * @return Nombre del jugador.
     */
    public String getPlayerName() {
        return playerName;
    }

    //metodo para actualizar los barcos hundidos en la partida
    private void actualizarLabelBarcosHundidos() {
        BarcoshundidosId.setText("Hundidos esta partida: " + barcosHundidos);
    }

    //metodo para actualizar el total de barcos hundidos por el jugador
    private void actualizarLabelTotalBarcosHundidos() {
        totalBarcosHundidosId.setText("Total Barcos Hundidos: " + GameDataManager.getBarcosHundidos(GameDataManager.getCurrentNickname()));
    }
    public void activarModoMaestro() {
        computerGrid.setShowShips(true);
        draw(); // Redibuja el canvas con los barcos visibles
    }


    // ========== Placeholder para eventos del mouse (no utilizados actualmente) ==========

    // Métodos vacíos requeridos para compatibilidad con interfaces o futuros usos.

    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}