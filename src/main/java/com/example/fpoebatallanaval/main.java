package com.example.fpoebatallanaval;

import com.example.fpoebatallanaval.view.MenuView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class main extends Application  {

    @Override
    public void start(Stage stage) {
        MenuView.getInstance().show(stage);
    }

    public static void main(String[] args) {
        launch();
    }
    }