package com.example.fpoebatallanaval.controller;

import com.example.fpoebatallanaval.models.GameDataManager;
import com.example.fpoebatallanaval.views.GameView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador de la vista de ingreso de apodo (nickname).
 * Se encarga de procesar la entrada del usuario y configuar los datos iniciales del jugador.
 */
public class NicknameController {

    // Campo de texto donde el jugador escribe su nickname
    @FXML
    private TextField textFieldNickname;

    /**
     * Método invocado cuando se presiona el botón "Continuar".
     * Valida el apodo ingresado, lo guarda, y lanza la vista principal del juego.
     * @param event Evento de acción asociado al botón
     * @throws IOException si ocurre un error al cargar la vista del juego
     */
    @FXML
    void onActionButtonContinuar(ActionEvent event) throws IOException {
        // Obtener el texto ingresado por el jugador
        String nickname = textFieldNickname.getText();

        // Si el campo está vacío, se una un nombre por defecto
        if (nickname.isBlank()) {
            nickname = "Jugador"; // Default nickname
        }

        /*
        // Guarda el apodo en memoria (uso interno)
        GameDataManager.saveNickname(nickname);

        // Verificar si el jugador ya existe en los registros
        boolean yaRegistrado = GameDataManager.playerExists(nickname);

        // Si es nuevo, inicializa sus estadísticas con 0 barcos hundidos
        if (!yaRegistrado) {
            GameDataManager.updateBarcosHundidos(nickname, 0);
        }

        // Guarda el apodo y estadísticas en el archivo players.txt
        GameDataManager.savePlayerStats();
         */

        // Cargar y mostrar la ventana del juego (patrón Singleton)
        GameView gameView = GameView.getInstance();
        gameView.show();

        // Pass game state to the controller

        // Cerrar la ventana actual (vista del nickname)
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

}