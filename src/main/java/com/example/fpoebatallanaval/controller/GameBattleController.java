package com.example.fpoebatallanaval.controller;

import com.example.fpoebatallanaval.model.GameDataManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameBattleController {
    @FXML
    private Label BarcoshundidosId;

    @FXML
    private Label NicknameId;

    @FXML
    private Label totalBarcosHundidosId;

    @FXML
    public void mostrarDatosJugador() {
        String nickname = GameDataManager.getCurrentNickname();
        int totalHundidos = GameDataManager.getBarcosHundidos(nickname);

        NicknameId.setText("Jugador: " + nickname);
        totalBarcosHundidosId.setText("Total hundidos: " + totalHundidos);

        // Por ahora, dejamos este en 0 hasta que tengas la lógica de disparos
        BarcoshundidosId.setText("Hundidos en esta partida: 0");
    }
}
