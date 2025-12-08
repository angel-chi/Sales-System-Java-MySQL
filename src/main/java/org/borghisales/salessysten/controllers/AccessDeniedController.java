package org.borghisales.salessysten.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.borghisales.salessysten.model.Seller;

import java.net.URL;
import java.util.ResourceBundle;

public class AccessDeniedController implements Initializable {

    @FXML
    private Label messageLabel;

    @FXML
    private Label roleLabel;

    private static String customMessage;
    private static Seller.Role userRole;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (customMessage != null) {
            messageLabel.setText(customMessage);
        }

        if (userRole != null) {
            roleLabel.setText("Tu rol actual: " + userRole.name());
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
    }

    // Métodos estáticos para configurar el mensaje antes de abrir la ventana
    public static void setMessage(String message) {
        customMessage = message;
    }

    public static void setUserRole(Seller.Role role) {
        userRole = role;
    }
}