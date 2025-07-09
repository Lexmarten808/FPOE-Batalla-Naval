package com.example.fpoebatallanaval.views;

import com.example.fpoebatallanaval.Main;
import com.example.fpoebatallanaval.controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class GameView extends Stage {

    private final GameController gameController;

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

    public GameController getController() { return gameController; }

    public static GameView getInstance() throws IOException {
        if (GameViewHolder.INSTANCE == null) {
            GameViewHolder.INSTANCE = new GameView();
        }
        return GameViewHolder.INSTANCE;
    }

    private static class GameViewHolder {
        private static GameView INSTANCE;
    }
}
