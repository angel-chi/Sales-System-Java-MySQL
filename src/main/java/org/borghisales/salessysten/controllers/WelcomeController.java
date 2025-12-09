package org.borghisales.salessysten.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    @FXML
    private Text nombreVendedor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nombreVendedor.setText(MainController.sellerLog.name());
    }
}
