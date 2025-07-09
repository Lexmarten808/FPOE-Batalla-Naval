package com.example.fpoebatallanaval.views;

import com.example.fpoebatallanaval.Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuView extends Stage {

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
        this.getIcons().add(new Image(Main.class.getResourceAsStream("/com/example/fpoebatallanaval/images/batalla-naval-logo.png")));
    }

    public static MenuView getInstance() throws IOException {
        if (MenuViewHolder.INSTANCE == null) {
            MenuViewHolder.INSTANCE = new MenuView();
        }
        return MenuViewHolder.INSTANCE;
    }

    private static class MenuViewHolder {
        private static MenuView INSTANCE;
    }

}