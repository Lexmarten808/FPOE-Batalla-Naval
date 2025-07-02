package com.example.fpoebatallanaval.controller;

import com.example.fpoebatallanaval.model.AlertHelper;
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

        //obtiene el string del nombre
        String nickname = nicknameInput.getText().toLowerCase().trim();
        if (nickname.isEmpty()) {
            AlertHelper.showErrorAlert("nombre no valido!","","por favor ingresa un nombre valido");
            return;}

        //guarda el nombre en nickname.txt
        GameDataManager.saveNickname(nickname);

        //declara el booleano para saber si el jugador existe o es nuevo
        boolean yaRegistrado = GameDataManager.playerExists(nickname);

        //declara por defecto el numero de barcos hundidos si el jugador es nuevo
        if(!yaRegistrado){GameDataManager.updateBarcosHundidos(nickname, 0);

            //close current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            //carga la vista del juego nuevo
            Stage stage = new Stage();
            GameBattleView.getInstance().show(stage);}

        //guarda el nickname y el numero de barcos hundidos en players.txt
        GameDataManager.savePlayerStats();

        /*muestra el dialogo para preguntar si se desea continuar la ultima partida
          en caso de que el nombre tenga una partida guardada
        */
        if (yaRegistrado) {continuePane.setVisible(true);}




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
