package com.example.fpoebatallanaval;

import com.example.fpoebatallanaval.views.MenuView;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application  {

    @Override
    public void start(Stage stage) throws IOException {
        // Obtain the singleton instance of the MenuView and display it
        MenuView menuView = MenuView.getInstance();
        menuView.show();
    }

    public static void main(String[] args) { launch(); }

}