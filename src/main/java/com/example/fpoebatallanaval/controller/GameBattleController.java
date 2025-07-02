package com.example.fpoebatallanaval.controller;

import com.example.fpoebatallanaval.model.GameDataManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameBattleController {
    @FXML
    private Label BarcoshundidosId;

    @FXML
    private Label NicknameId;

    @FXML
    private Label totalBarcosHundidosId;

    //Id's de los grid encargados del los tableros de la maquina y el humano
    @FXML
    private VBox PlayerGridId;

    @FXML
    private VBox MachineGridId;

    @FXML
    public void mostrarDatosJugador() {
        String nickname = GameDataManager.getCurrentNickname();
        int totalHundidos = GameDataManager.getBarcosHundidos(nickname);

        NicknameId.setText("Jugador: " + nickname);
        totalBarcosHundidosId.setText("Total hundidos: " + totalHundidos);

        // Por ahora, dejamos este en 0 hasta que tengas la lógica de disparos
        BarcoshundidosId.setText("Hundidos en esta partida: 0");

        // Crear y mostrar los tableros
        GridPane playerBoard = createLabeledBoard(true);
        GridPane machineBoard = createLabeledBoard(false);

        PlayerGridId.getChildren().clear();
        MachineGridId.getChildren().clear();

        PlayerGridId.getChildren().add(playerBoard);
        MachineGridId.getChildren().add(machineBoard);

    }
    private GridPane createLabeledBoard(boolean isPlayerBoard) {
        GridPane grid = new GridPane();
        grid.setHgap(0);
        grid.setVgap(0);

        for (int row = 0; row <= 10; row++) {
            for (int col = 0; col <= 10; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(30, 30);
                cell.setStyle("-fx-background-color: grey;-fx-border-color: white; -fx-border-width: 1px;");
                Label label = new Label();

                // Esquina (0,0)
                if (row == 0 && col == 0) {
                    cell.setStyle("-fx-background-color: transparent;");
                }

                // Letras A-J
                else if (row == 0) {
                    label.setText(String.valueOf((char) ('A' + col - 1)));
                    label.setStyle("-fx-background-color: grey;-fx-text-fill: white; -fx-font-weight: bold;");
                }

                // Números 1-10
                else if (col == 0) {
                    label.setText(String.valueOf(row));
                    label.setStyle("-fx-background-color: transparent;-fx-text-fill: white; -fx-font-weight: bold;");
                }

                // Celdas del tablero
                else {
                    cell.setStyle("-fx-background-color: aqua; -fx-border-color: grey; -fx-border-width: 1;");
                    if (!isPlayerBoard) {
                        int finalRow = row - 1;
                        int finalCol = col - 1;
                        cell.setOnMouseClicked(e -> {
                            System.out.println("Disparo en: " + finalRow + "," + finalCol);
                            // lógica futura
                        });
                    }
                }

                // Centrar contenido
                cell.setAlignment(Pos.CENTER);
                cell.getChildren().add(label);
                grid.add(cell, col, row);
            }
        }

        return grid;
    }
}
