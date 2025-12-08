package org.borghisales.salessysten.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;


public class HelpController extends MenuController implements Initializable {

    @FXML
    private Label titleLabel;
    private static final String URL_REPOSITORIO="https://github.com/angel-chi/Sales-System-Java-MySQL.git";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleLabel.setText("Sistema");
    }
    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    public void openRepository(ActionEvent actionEvent) {
        new Thread(() -> {
            try {
                // Cada Sistema Operativo ejecuta una rutina de navegación distinta.
               String os= System.getProperty("os.name").toLowerCase();

                // Windows
                if (os.contains("win")) {
                    Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", URL_REPOSITORIO});
                    // Algunas distribuciones de Linux
                } else if (os.contains("nux")||os.contains("nix")||os.contains("aix")) {
                    Runtime.getRuntime().exec(new String[]{"xdg-open", URL_REPOSITORIO});
                    // MacOS
                } else if (os.contains("mac")) {
                    Runtime.getRuntime().exec(new String[]{"open", URL_REPOSITORIO});
                    // Algún otro Sistema Operativo...
                } else {
                    System.out.println("Sistema operativo no soportado.");
                }
            } catch (Exception E) {
                E.printStackTrace();
                javafx.application.Platform.runLater(() ->
                        setAlert(Alert.AlertType.ERROR, "No se pudo abrir la URL.")
                );
            }
        }).start();
    }
}