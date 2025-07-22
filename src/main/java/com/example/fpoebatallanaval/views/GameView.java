package com.example.fpoebatallanaval.views;

import com.example.fpoebatallanaval.Main;
import com.example.fpoebatallanaval.controller.GameController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase que representa la ventana principal del juego.
 * Carga la vista desde el archivo FXML, obtiene su controlador y configura los atributos visuales.
 * Aplica el patrón Singleton para garantizar una sola instancia activa.
 */
public class GameView extends Stage {

    // Controlador asociado a la vista del juego
    private final GameController gameController;

    /**
     * Constructor privado. Carga el archivo FXML, inicializa la escena y configura la ventana.
     * @throws IOException si ocurre un error al cargar el archivo game.fxml
     */
    private GameView() throws IOException {
        // Load the game.fxml file and create the scene
        FXMLLoader fxmlLoader = new FXMLLoader(
                Main.class.getResource("/com/example/fpoebatallanaval/views/game.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());

        // Retrieve the controller instance from the FXMLLoader
        this.gameController = fxmlLoader.getController();

        // Set up the stage properties
        this.setTitle("Game Battle");
        this.setScene(scene);
        this.setResizable(false);

        // Set the window icon from resources
        this.getIcons().add(new Image(Main.class.getResourceAsStream("/com/example/fpoebatallanaval/images/batalla-naval-logo.png")));
    }

    /**
     * Devuelve el controlador asociado a esta vista.
     * Permite acceder a la lógica del juego desde otras clases.
     * @return Instancia del controlador GameController
     */
    public GameController getController() { return gameController; }

    /**
     * Devuelve una instancia única de GameView utilizando el patrón Singleton.
     * @return Instancia única de GameView
     * @throws IOException si ocurre un error al inicializar la vista
     */
    public static GameView getInstance() throws IOException {
        if (GameViewHolder.INSTANCE == null) {
            GameViewHolder.INSTANCE = new GameView();
        }
        return GameViewHolder.INSTANCE;
    }

    /**
     * Clase interna que almacena la instancia única de GameView.
     * Aplica el patrón Holder para inicialización segura y perezosa.
     */
    private static class GameViewHolder {
        private static GameView INSTANCE;
    }

}