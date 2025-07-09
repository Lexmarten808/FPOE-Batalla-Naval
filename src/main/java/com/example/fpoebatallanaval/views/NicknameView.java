package com.example.fpoebatallanaval.views;

import com.example.fpoebatallanaval.Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class NicknameView extends Stage {

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

    public static NicknameView getInstance() throws IOException {
        if (NicknameViewHolder.INSTANCE == null) {
            NicknameViewHolder.INSTANCE = new NicknameView();
        }
        return NicknameViewHolder.INSTANCE;
    }

    private static class NicknameViewHolder {
        private static NicknameView INSTANCE;
    }

}