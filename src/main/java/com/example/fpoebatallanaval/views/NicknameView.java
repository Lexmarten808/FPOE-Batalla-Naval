package com.example.fpoebatallanaval.views;

import com.example.fpoebatallanaval.Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase que represnta la vista para ingresar el apodo del jugador antes de comenzar la partida.
 * Carga la interfaz gráfica desde el archivo FXML y configura la ventana.
 * Aplica el patrón Singleton para asegurar que solo haya una instancia activa.
 */
public class NicknameView extends Stage {

    /**
     * Constructor de NicknameView.
     * Carga la interfaz desde el archivo FXMLy configura los atributos de la ventana.
     * @throws IOException si ocurre un error al cargar el archivo nickname.fxml
     */
    public NicknameView() throws IOException {
        // Load the FXML file for the menu layout
        FXMLLoader fxmlLoader = new FXMLLoader(
                Main.class.getResource("/com/example/fpoebatallanaval/views/nickname.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());

        // Set up the stage properties
        this.setTitle("Batalla Naval - Nickname");
        this.setScene(scene);
        this.setResizable(false);

        // Set the window icon from resources
        this.getIcons().add(new Image(Main.class.getResourceAsStream("/com/example/fpoebatallanaval/images/batalla-naval-logo.png")));
    }

    /**
     * Retorna una instancia única de NicknameView utilizando el patrón Singleton.
     * Si aún no existe, la instancia se crea.
     * @return Instancia única de NicknameView
     * @throws IOException si ocurre un error al inicializar la vista
     */
    public static NicknameView getInstance() throws IOException {
        if (NicknameViewHolder.INSTANCE == null) {
            NicknameViewHolder.INSTANCE = new NicknameView();
        }
        return NicknameViewHolder.INSTANCE;
    }

    /**
     * Clase interna que mantiene la instancia única de NicknameView.
     * Implementa el patrón Holder para inicialización perezosa.
     */
    private static class NicknameViewHolder {
        private static NicknameView INSTANCE;
    }

}