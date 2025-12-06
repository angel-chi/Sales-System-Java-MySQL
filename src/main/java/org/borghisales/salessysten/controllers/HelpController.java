package org.borghisales.salessysten.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.awt.*;
import java.net.URL;
import java.net.URI;
import java.util.ResourceBundle;
import java.net.URISyntaxException;
import java.io.IOException;

public class HelpController extends MenuController implements Initializable {

    @FXML
    private Label titleLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleLabel.setText("No hay servicio");
    }
    @FXML
    private void onClose(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    private void onHelp(ActionEvent event){
        String url = "https://github.com/angel-chi/Sales-System-Java-MySQL";

        try {
            // Detectar el sistema operativo
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("linux")) {
                // Para Linux (Ubuntu)
                Runtime.getRuntime().exec(new String[]{"xdg-open", url});
            } else if (os.contains("mac")) {
                // Para macOS
                Runtime.getRuntime().exec(new String[]{"open", url});
            } else if (os.contains("win")) {
                // Para Windows
                Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", url});
            } else {
                // Fallback
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI(url));
                } else {
                    setAlert(Alert.AlertType.INFORMATION,
                            "No se pudo abrir el navegador automáticamente.");
                }
            }
        } catch (Exception e) {
            // Si falla
            setAlert(Alert.AlertType.INFORMATION,
                    "No se pudo abrir el navegador.\n\nCopia este link:\n" + url);
        }
    }

}
