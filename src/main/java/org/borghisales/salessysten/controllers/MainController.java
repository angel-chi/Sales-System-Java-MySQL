package org.borghisales.salessysten.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.borghisales.salessysten.model.Seller;
import org.borghisales.salessysten.model.SellerDAO;
import javafx.scene.control.Alert;

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
        // Presionar Enter en el campo de usuario brinca al campo de contraseña
        user.setOnAction(e -> password.requestFocus());
        // Presionar Enter en el campo de contraseña inicia sesión
        password.setOnAction(e -> signIn());

    }

    @FXML
    private void signIn(){
        String dni = user.getText();
        String userPass = password.getText();

        if (dni.isEmpty() || userPass.isEmpty() ){
            setAlert(Alert.AlertType.ERROR,"Usuario o contraseña vacíos");
            return;
        }
        Seller loggedSeller = SellerDAO.login(dni, userPass);
        if (loggedSeller != null) {
            sellerLog = loggedSeller;
            GenerateSaleController.setSellerName(loggedSeller.name());
            GenerateSaleController.setIdSeller(loggedSeller.idSeller());
            openNewStage(MANAGEMENT_VIEW_FXML, "Gestión");
            closeCurrentStage(user);
        } else {
            setAlert(Alert.AlertType.ERROR, "Usuario no encontrado o error en la base de datos") ;
        }
    }

}
