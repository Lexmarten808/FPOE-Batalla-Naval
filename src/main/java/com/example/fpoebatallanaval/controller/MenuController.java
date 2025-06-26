package com.example.fpoebatallanaval.controller;

import com.example.fpoebatallanaval.model.AlertHelper;
import com.example.fpoebatallanaval.view.NickNameView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MenuController {
    @FXML
    private Label welcomeText;


    @FXML
    void OnActionInstruccionesButton(ActionEvent event) {
        System.out.println("Mostrando instrucciones...");
        AlertHelper.showInfoAlert("","","");
    }

    @FXML
    void OnActionJugarButton(ActionEvent event) {
        System.out.println("Iniciando juego...");
        //close current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        //loads the nickname stage
        Stage stage = new Stage();
        NickNameView.getInstance().show(stage);

    }
    @FXML
    void OnActionDebugButton(ActionEvent event) {
        System.out.println("iniciando modo debug ...");


    }

    @FXML
    void OnActionSalirButton(ActionEvent event) {
        System.out.println("Saliendo del juego...");
        //close current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

    }
}