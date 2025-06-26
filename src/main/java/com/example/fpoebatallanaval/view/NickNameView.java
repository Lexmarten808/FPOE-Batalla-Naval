package com.example.fpoebatallanaval.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NickNameView {





    private static NickNameView instance;
    private Parent root;
    private Scene scene;

    private NickNameView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fpoebatallanaval/views/nick-name-view.fxml"));
            root = loader.load();
            scene = new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NickNameView getInstance() {
        if (instance == null) {
            instance = new NickNameView();
        }
        return instance;
    }

    public void show(Stage stage) {
        stage.setTitle("Batalla Naval - Menú");
        stage.setScene(scene);
        stage.show();
    }

}
