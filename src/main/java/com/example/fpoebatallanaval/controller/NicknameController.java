package com.example.fpoebatallanaval.controller;

import com.example.fpoebatallanaval.views.GameView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class NicknameController {

    @FXML private TextField textFieldNickname;


    @FXML
    void onActionButtonContinuar(ActionEvent event) throws IOException {
        String nickname = textFieldNickname.getText();
        if (nickname.isBlank()) {
            nickname = "Jugador"; // Default nickname
        }

        // Load the game view (singleton)
        GameView gameView = GameView.getInstance();
        gameView.show();

        // Pass game state to the controller

        // Close the current menu window
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}