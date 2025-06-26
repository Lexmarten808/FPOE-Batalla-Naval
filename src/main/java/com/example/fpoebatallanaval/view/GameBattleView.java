package com.example.fpoebatallanaval.view;

import com.example.fpoebatallanaval.controller.NicknameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameBattleView {
    private static GameBattleView instance;
    private Parent root;
    private Scene scene;

    private GameBattleView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fpoebatallanaval/views/game-battle.fxml"));
            root = loader.load();
            scene = new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameBattleView getInstance() {
        if (instance == null) {
            instance = new GameBattleView();
        }
        return instance;
    }

    public void show(Stage stage) {
        stage.setTitle("Batalla Naval - Menú");
        stage.setScene(scene);
        stage.show();
    }
}
