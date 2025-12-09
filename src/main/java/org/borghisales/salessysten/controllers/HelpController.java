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

    private static final String REPO_URL =
            "https://github.com/angel-chi/Sales-System-Java-MySQL";

    private static final String README_URL =
            "https://github.com/angel-chi/Sales-System-Java-MySQL/blob/main/README.md";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (titleLabel != null) {
            titleLabel.setText("Ayuda del sistema");
        }
    }

    @FXML
    private void openRepository(ActionEvent event) {
        openUrl(REPO_URL);
    }
    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }
    //Permite abrir la URL al accionar el botón. Se hace una verificación para cada sistema operativo,
    // pues ejecutan rutinas distintas para abrir una ventana
    private void openUrl(String url) {
        new Thread(() -> {
            try {
                String os = System.getProperty("os.name").toLowerCase();

                if (os.contains("win")) {
                    Runtime.getRuntime().exec(
                            new String[]{"rundll32", "url.dll,FileProtocolHandler", url}
                    );
                } else if (os.contains("nux") || os.contains("nix") || os.contains("aix")) {
                    Runtime.getRuntime().exec(new String[]{"xdg-open", url});
                } else if (os.contains("mac")) {
                    Runtime.getRuntime().exec(new String[]{"open", url});
                } else {
                    System.out.println("Sistema operativo no soportado.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() ->
                        setAlert(Alert.AlertType.ERROR, "No se pudo abrir la URL.")
                );
            }
        }).start();
    }
}
