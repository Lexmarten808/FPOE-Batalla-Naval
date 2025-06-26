package com.example.fpoebatallanaval.controller;

import com.example.fpoebatallanaval.view.GameBattleView;
import com.example.fpoebatallanaval.view.MenuView;
import com.example.fpoebatallanaval.view.NickNameView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NicknameController {



    @FXML
    void OnActionContinuarButton(ActionEvent event) {

        //close current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        Stage stage = new Stage();
        GameBattleView.getInstance().show(stage);


    }
}
