package com.example.fpoebatallanaval;

import com.example.fpoebatallanaval.views.MenuView;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal del juego.
 * Es el punto de entrada de la aplicación JavaFX.
 */
public class Main extends Application  {

    /**
     * Método llamado automáticamente al iniciar la aplicación JavaFX.
     * Muestra la vista principal del menú.
     * @param stage La ventana principal (Stage) proporcionada por JavaFx.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Obtener la instancia única del menú y mostrarla
        MenuView menuView = MenuView.getInstance();
        menuView.show();
    }

    /**
     * Método estático que lanza la aplicación.
     * Se ejecuta al correr el programa.
     * @param args Argumetnos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        launch(); // Inicia la aplicación JavaFX
    }

}