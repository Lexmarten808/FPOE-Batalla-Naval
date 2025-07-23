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

    // ========== Lógica de juego ==========

    private Grid playerGrid;
    private Grid computerGrid;
    private Ship placingShip;             // Barco que se está colocando actualmente
    private Position tempPlacingPosition; // Posición temporal para pintar el barco en movimiento
    private int placingShipIndex;         // Índice del barco que se está colocando
    private AI ai;                        // Componente de la IA enemiga
    private Canvas canvas;                // Área gráfica del juego
    private int barcosHundidos = 0;       // contador de barcos hundidos en la partida
    private int totalBarcosHundidos = 0;  // contador del total de barcos undidos

    Game game = Game.getInstance();


    // ========== Constructor ==========
    public GameController() throws IOException {
        // Crear tableros para el jugador y la máquina
        playerGrid = new Grid(0, 0);
        computerGrid = (new Grid(Grid.CELL_SIZE * Grid.GRID_WIDTH + 40, 0));
        game.setPhase("PlacingShips");

        // Crear lienzo (canvas) para pintar el juego
        canvas = new Canvas(
                Grid.CELL_SIZE * Grid.GRID_WIDTH * 2 + 50,
                Grid.CELL_SIZE * Grid.GRID_HEIGHT
        );

        // Reiniciar juego e iniciar dibujo
        restart();
        if (game.isMasterMode() == true) {
            computerGrid.setShowShips(true);
        }
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
    }

    /**
     * Muestra en pantalla el nickname del jugador, los barcos hundidos totales
     * y los hundidos durante la partida actual
     */
    @FXML
    public void mostrarDatosJugador() {
        String nickname = game.getPlayerName();
        // int totalHundidos = GameDataManager.getBarcosHundidos(nickname);

        NicknameId.setText(nickname);
        totalBarcosHundidosId.setText("Total hundidos: -");
        BarcoshundidosId.setText("Hundidos en esta partida: -");

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
            // System.out.println("Mouse released at: (" + event.getX() + ", " + event.getY() + ")");
            // System.out.println("game state: " + game.getPhase());
            Position mousePosition = new Position((int) event.getX(), (int) event.getY());

            if (game.getPhase() == "PlacingShips" && playerGrid.isPositionInside(mousePosition)) {
                tryPlaceShip(mousePosition);
            } else if (game.getPhase() == "FiringShots" && computerGrid.isPositionInside(mousePosition)) {
                tryFireAtComputer(mousePosition);
            }
            draw();
        }
    }

    /**
     * Manejador de eventos de movimiento del mouse.
     * Se utiliza para actualizar visualmente la posición del barco en colocación.
     */
    private class MouseMovedHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            if (game.getPhase() != "PlacingShips") return;

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
     * - S: muestra u oculta los barcos enemigos (modo trampa).
     * @param keyCode Tecla presionada
     */
    public void handleInput(KeyCode keyCode) {
        if (keyCode == KeyCode.ESCAPE) {
            System.exit(1);
        } else if (keyCode == KeyCode.R) {
            restart();
        } else if (game.getPhase() == "PlacingShips" && keyCode == KeyCode.X) {
            placingShip.toggleSideways();

            // Reajustar posición para que no se salga del grid tras rotar
            Position adjustedPos = new Position(tempPlacingPosition);
            if (placingShip.isSideWays()) {
                adjustedPos.x = Math.min(adjustedPos.x, Grid.GRID_WIDTH - placingShip.getSegments());
            } else {
                adjustedPos.y = Math.min(adjustedPos.y, Grid.GRID_HEIGHT - placingShip.getSegments());
            }

            updateShipPlacement(adjustedPos);
        } else if (keyCode == KeyCode.S && game.getPhase() == "PlacingShips") {
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
        game.setMasterMode(false);
        game.setPhase("PlacingShips");

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
            game.setPhase("FiringShots");
        }
        draw();
        saveGame();
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
                game.setPhase("GameOver");
                String winner = game.getPlayerName();
                AlertHelper.showInfoAlert("The game has ended","ENDGAME!","The winner is: " + winner);
                System.exit(1);
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
        // if (hit) {System.out.println("Barco atacado en: " + targetPos.x + ", " + targetPos.y);}
        // if(!hit){System.out.println("tiro realizado fallido en: " + targetPos.x + ", " + targetPos.y);}


        if (hit && computerGrid.getMarkerAtPosition(targetPos).getAssociatedShip().isDestroyed()) {
            destroyed = "(Destroyed)";
            barcosHundidos+=1;
            // actualizarLabelBarcosHundidos();

            //añadir los barcos destruidos por el jugador a su historial
            // GameDataManager.updateBarcosHundidos(GameDataManager.getCurrentNickname(),1);
            // actualizarLabelTotalBarcosHundidos();
        }
        draw();
        saveGame();
        if (computerGrid.areAllShipsDestroyed()) {
            game.setPhase("GameOver");
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
        // if(hit){System.out.println("barco del jugador atacado en: " + aiMove.x + ", " + aiMove.y);}
        // if(!hit){System.out.println("tiro de la maquina fallido");}

        if (hit && playerGrid.getMarkerAtPosition(aiMove).getAssociatedShip().isDestroyed()) {
            destroyed = "(Destroyed)";
            //añadir los barcos destruidos por el jugador a su historial
            //GameDataManager.updateBarcosHundidos(GameDataManager.getCurrentNickname(),1);
            //actualizarLabelTotalBarcosHundidos();

        }
        draw();
        saveGame();
        if (playerGrid.areAllShipsDestroyed()) {
            game.setPhase("GameOver");
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

        if (game.getPhase() == "PlacingShips") {
            placingShip.paint(gc);
        }
    }

    // ========== Guardar / Cargar ==========

    /**
     * Guarda el estado actual del juego (barcos, disparos fase y nombre del jugador)
     * en un archivo binario 'saved_game.dat' y el nombre del jugador en 'player_name.txt'.
     */
    public void saveGame() {
        if (!game.isMasterMode()) { return; }
        // System.out.println("Inicio de guardado...");
        game.setPlayerShips(playerGrid.getShips());
        game.setComputerShips(computerGrid.getShips());

        if (game == null || playerGrid == null || computerGrid == null) {
            System.out.println("No se puede guardar el juego: componentes nulos.");
            return;
        }

        // Guardar estado del juego como objeto serializado
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saved_game.dat"))) {
            System.out.println(game);
            oos.writeObject(game);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Guardar el nombre del jugador por separado
        try (PrintWriter writer = new PrintWriter(new FileWriter("player_name.txt"))) {
            writer.println(game.getPlayerName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // System.out.println("Guardado exitosamente.");
    }


    /**
     * Carga el estado del juego desde 'saved_game.dat' y restaura todos los elementos visuales y lógicos.
     * También reasocia los marcadores a sus barcos correspondientes.
     */
    public void loadGame() {
        System.out.println("Función loadGame ejecutada...");

        // Cargar el estado del juego desde el archivo serializado
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saved_game.dat"))) {
            System.out.println("1");
            Game loadedGame = (Game) ois.readObject();
            System.out.println("2");
            this.game = loadedGame; // Reasignar la instancia del juego
            System.out.println("3");
            System.out.println(loadedGame);

            // Restaurar los datos en la interfaz
            playerGrid.setShips(loadedGame.getPlayerShips());
            playerGrid.setMarkers(loadedGame.getPlayerShots(), canvas.getGraphicsContext2D());

            System.out.println("4");
            computerGrid.setShips(loadedGame.getComputerShips());
            computerGrid.setMarkers(loadedGame.getComputerShots(), canvas.getGraphicsContext2D());
            System.out.println("5");
            // Reasociar marcadores a barcos para el jugador
            for (Ship ship : loadedGame.getPlayerShips()) {
                for (Position pos : ship.getOccupiedCoordinates()) {
                    playerGrid.getMarkerAtPosition(pos).setAsShip(ship);
                }
            }

            // Reasociar marcadores a barcos para la computadora
            for (Ship ship : loadedGame.getComputerShips()) {
                for (Position pos : ship.getOccupiedCoordinates()) {
                    computerGrid.getMarkerAtPosition(pos).setAsShip(ship);
                }
            }


            // Restaurar nombre del jugador desde archivo externo
            try (BufferedReader reader = new BufferedReader(new FileReader("player_name.txt"))) {
                String nameFromFile = reader.readLine();
                if (nameFromFile != null && !nameFromFile.isEmpty()) {
                    loadedGame.setPlayerName(nameFromFile);
                }
            } catch (IOException e) {
                System.out.println("No se pudo cargar el nombre del jugador.");
            }

            draw();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    //metodo para actualizar los barcos hundidos en la partida
    private void actualizarLabelBarcosHundidos() {
        BarcoshundidosId.setText("Hundidos esta partida: " + barcosHundidos);
    }

    //metodo para actualizar el total de barcos hundidos por el jugador
    private void actualizarLabelTotalBarcosHundidos() {
        totalBarcosHundidosId.setText("Total Barcos Hundidos: " + GameDataManager.getBarcosHundidos(GameDataManager.getCurrentNickname()));
    }


    // ========== Placeholder para eventos del mouse (no utilizados actualmente) ==========

    // Métodos vacíos requeridos para compatibilidad con interfaces o futuros usos.

    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}