package com.example.fpoebatallanaval.controller;

import com.example.fpoebatallanaval.model.GameDataManager;
import com.example.fpoebatallanaval.view.GameBattleView;
import com.example.fpoebatallanaval.view.MenuView;
import com.example.fpoebatallanaval.view.NickNameView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class NicknameController {
    @FXML
    private AnchorPane continuePane;
    @FXML
    private TextField nicknameInput;


    @FXML
    void OnActionContinuarButton(ActionEvent event) {
        String nickname = nicknameInput.getText();
        GameDataManager.saveNickname(nickname);
        GameDataManager.updateBarcosHundidos(nickname, 0);
        GameDataManager.savePlayerStats();
        continuePane.setVisible(true);




    }
    @FXML
    void OnActionContinueGame(ActionEvent event) {

    }

    @FXML
    void OnActionNewGame(ActionEvent event) {
        //close current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        Stage stage = new Stage();
        GameBattleView.getInstance().show(stage);


    }
}
