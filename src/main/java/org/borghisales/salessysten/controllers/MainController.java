package org.borghisales.salessysten.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.borghisales.salessysten.model.Vendedor; // Seller -> Vendedor
import org.borghisales.salessysten.model.VendedorDAO; // SellerDAO -> VendedorDAO

//Para usar el metodo Platform.exit() que cierra la aplicación de forma controlada.
import javafx.application.Platform;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends MenuController implements Initializable {
    @FXML
    private  TextField user;
    @FXML
    private TextField password;

    public static Vendedor vendedorLogeado; // Seller -> Vendedor, sellerLog -> vendedorLogeado


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        user.setText("44994806");
//        password.setText("chimu");

        //En caso de que se cambie de usuario, se deben actualizar los reportes de ese usuario.
        ReportsController.setVentas(null);
        ReportsController.setDatosGraficoCircular(null);
        ReportsController.setDatosGraficoLinea(null);

    }

    @FXML
    private void signIn(){
        if (VendedorDAO.login(user.getText(),password.getText())) { // SellerDAO -> VendedorDAO
            openNewStage(MANAGEMENT_VIEW_FXML, "Gestión");
            closeCurrentStage(user);
        }
    }

    @FXML
    private void closeApp() {
        Platform.exit();
    }

}
