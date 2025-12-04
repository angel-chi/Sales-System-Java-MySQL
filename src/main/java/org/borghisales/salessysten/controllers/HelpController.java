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
        String url = "https://github.com/Borghii/Sales-System";
        try {
            if (!Desktop.isDesktopSupported()) {
                setAlert(Alert.AlertType.ERROR, "No se puede abrir un link");
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.BROWSE)) {
                setAlert(Alert.AlertType.ERROR,"No se pueden abrir links");
                return;
            }
            desktop.browse(new URI(url));
        }
        catch (URISyntaxException e) {setAlert(Alert.AlertType.ERROR,"The URL is invalid: " + url);}
        catch (IOException e) {setAlert(Alert.AlertType.ERROR,"Could not open the URL. Check your internet connection.");}
        catch (Exception e) {setAlert(Alert.AlertType.ERROR,"An unexpected error occurred while opening the help page.");}

    }

}
