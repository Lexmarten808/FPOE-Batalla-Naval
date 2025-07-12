package com.example.fpoebatallanaval.controller;

import com.example.fpoebatallanaval.models.AlertHelper;
import com.example.fpoebatallanaval.views.NicknameView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador para la vista del menú principal del juego.
 * Maneja los eventos de los botones: Nueva Partida, Instrucciones, Cargar Partida, Modo Maestro y Salir.
 */
public class MenuController {

    /**
     * Muestra una alerta con las instrucciones del juego al usuario.
     * Este método se activa al hacer click en el botón "Instrucciones".
     * @param event Evento generado por le botón
     */
    @FXML
    void onActionButtonInstrucciones(ActionEvent event) {
        System.out.println("Mostrando instrucciones...");
        AlertHelper.showInfoAlert("","","\n" +
                "Objetivo del juego:\n" +
                "Hundir todos los barcos del oponente antes de que él hunda los tuyos.\n" +
                "\n" +
                "Preparación:\n" +
                "Cada jugador (tú y la máquina) tiene un tablero de 10x10.\n" +
                "Ambos colocan sus barcos sin que el otro los vea.\n" +
                "\n" +
                "Tipos de barcos:\n" +
                "- 1 Portaaviones (5 casillas)\n" +
                "- 1 Acorazado (4 casillas)\n" +
                "- 2 Submarinos (3 casillas)\n" +
                "- 1 Destructor (2 casillas)\n" +
                "(Pueden colocarse horizontal o verticalmente, sin superponerse)\n" +
                "\n" +
                "Turnos:\n" +
                "Cada jugador dispara en una coordenada del tablero enemigo.\n" +
                "La máquina hace lo mismo en su turno.\n" +
                "\n" +
                "Resultados de un disparo:\n" +
                "- Agua: no hay barco en esa casilla.\n" +
                "- Tocado: diste en parte de un barco.\n" +
                "- Hundido: acertaste todas las partes de un barco.\n" +
                "\n" +
                "Fin del juego:\n" +
                "Gana quien hunda todos los barcos del oponente.");
    }

    /**
     * Abre la vista de ingreso de nickname para comenzar una nueva partida.
     * Este método se activa al hacer click en el botón "Nueva Partida".
     * @param event Evento generado por el botón
     * @throws IOException si ocurre un error al cargar la vista
     */
    @FXML
    void onActionButtonNuevaPartida(ActionEvent event) throws IOException {
        System.out.println("Iniciando juego...");

        // Load the nickname view (singleton)
        NicknameView nicknameView = NicknameView.getInstance();
        nicknameView.show();

        // Close the current menun window
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    /**
     * (En desarrollo) Manejador para cargar una partida existente.
     * Actualmebte cierra la ventana pero no implementa la carga.
     * @param event Evento generado por el botón
     */
    @FXML
    void onActionButtonCargarPartida(ActionEvent event) {
        System.out.println("Iniciando juego...");
        //close current stage
        // Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // currentStage.close();
        //loads the nickname stage
        // Stage stage = new Stage();
        // NicknameView.getInstance().show(stage);

    }

    /**
     * (En desarrollo) Manejador para activar un modo especial (debug o desarrollador).
     * Actualmente solo imprime un mensaje en consola.
     * @param event Evento generado por el botón
     */
    @FXML
    void onActionButtonModoMaestro(ActionEvent event) {
        System.out.println("iniciando modo debug ...");
    }

    /**
     * Cierra la aplicación al hacer clic en el botón "Salir".
     * @param event Evento generado por el botón
     */
    @FXML
    void onActionButtonSalir(ActionEvent event) {
        System.out.println("Saliendo del juego...");
        //close current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

}