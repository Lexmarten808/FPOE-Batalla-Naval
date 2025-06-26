package com.example.fpoebatallanaval.model;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class AlertHelper {


        /**
         * Displays an informational alert with a custom title, header, and message.
         *
         * @param title   the title of the alert window.
         * @param header  the header text of the alert.
         * @param message the message shown in the alert content.
         */
        public static void showInfoAlert(String title,String header, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(message);

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setMinHeight(Region.USE_PREF_SIZE);
            dialogPane.setMinWidth(520);

            // Get the Alert window and set the application icon
            Stage stage = (Stage) dialogPane.getScene().getWindow();
            //stage.getIcons().add(new Image(Main.class.getResourceAsStream("images/icons/uno-logo.png")));

            alert.showAndWait();
        }

        /**
         * Displays an error alert with a custom title, header, and message.
         *
         * @param title   the title of the alert window.
         * @param header  the header text of the alert.
         * @param message the message displayed in the alert content.
         */
        public static void showErrorAlert(String title,String header, String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(message);

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setMinHeight(Region.USE_PREF_SIZE);
            dialogPane.setMinWidth(520);

            // Set the window icon fot he alert
            Stage stage = (Stage) dialogPane.getScene().getWindow();
           // stage.getIcons().add(new Image(Main.class.getResourceAsStream("images/icons/uno-logo.png")));

            alert.showAndWait();
        }
}
