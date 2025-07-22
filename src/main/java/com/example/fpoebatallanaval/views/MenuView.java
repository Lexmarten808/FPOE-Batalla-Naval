package com.example.fpoebatallanaval.views;

import com.example.fpoebatallanaval.Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase que representa la ventana del menú principal del juego.
 * Carga el diseño definido en el archivo FXML y confugra la escena.
 * Implementa el patrón Singleton para que solo exista una instancia de esta vista.
 */
public class MenuView extends Stage {

    /**
     * Constructor de MenuView. Carga la interfaz desde el archivo FXML,
     * configura la escena, el título y el ícono de la ventana.
     * @throws IOException si ocurre un error al cargar el archivo FXML
     */
    public MenuView() throws IOException {
        // Load the FXML file for the menu layout
        FXMLLoader fxmlLoader = new FXMLLoader(
                Main.class.getResource("/com/example/fpoebatallanaval/views/menu.fxml")
        );
        Parent root = fxmlLoader.load();

        // Create and set the scene
        Scene scene = new Scene(root);
        this.setTitle("Batalla Naval - Menu");
        this.setScene(scene);
        this.setResizable(false);

        // Set the window icon from resources
        this.getIcons().add(
                new Image(Main.class.getResourceAsStream("/com/example/fpoebatallanaval/images/batalla-naval-logo.png")));
    }

    /**
     * Devuelve una única instancia de la vista del menú utilizando el patrón Singleton.
     * Si no existe, la crea.
     * @return Instancia única de MenuView.
     * @throws IOException si ocurre un error al cargar la vista por primera vez
     */
    public static MenuView getInstance() throws IOException {
        if (MenuViewHolder.INSTANCE == null) {
            MenuViewHolder.INSTANCE = new MenuView();
        }
        return MenuViewHolder.INSTANCE;
    }

    /**
     * Clase interna para almacenar la instancia única de MenuView.
     * Applica el patrón Holder para garantizar un Singleton perezoso y seguro.
     */
    private static class MenuViewHolder {
        private static MenuView INSTANCE;
    }

}