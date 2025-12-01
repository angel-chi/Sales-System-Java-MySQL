package org.borghisales.salessysten.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.borghisales.salessysten.model.Seller;
import org.borghisales.salessysten.model.SellerDAO;

//Para usar el metodo Platform.exit() que cierra la aplicación de forma controlada.
import javafx.application.Platform;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends MenuController implements Initializable {
    @FXML
    private  TextField user;
    @FXML
    private TextField password;

    public static Seller sellerLog;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        user.setText("44994806");
//        password.setText("chimu");

        //In case the user is changed, the reports of that user should be updated.
        ReportsController.setSales(null);
        ReportsController.setPieChartData(null);
        ReportsController.setLineChartData(null);

    }

    @FXML
    private void signIn(){
        if (SellerDAO.login(user.getText(),password.getText())) {
            openNewStage(MANAGEMENT_VIEW_FXML, "Management", 800, 600);
            closeCurrentStage(user);
        }
    }

    @FXML
    private void closeApp() {
        Platform.exit();
    }

}
