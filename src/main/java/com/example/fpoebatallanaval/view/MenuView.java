package com.example.fpoebatallanaval.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuView {



    private static MenuView instance;
    private Parent root;
    private Scene scene;

    private MenuView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fpoebatallanaval/views/game-menu.fxml"));
            root = loader.load();
            scene = new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MenuView getInstance() {
        if (instance == null) {
            instance = new MenuView();
        }
        return instance;
    }

    public void show(Stage stage) {
        stage.setTitle("Batalla Naval - Menú");
        stage.setScene(scene);
        stage.show();
    }


}




