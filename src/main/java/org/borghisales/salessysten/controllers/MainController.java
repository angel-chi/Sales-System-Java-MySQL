package org.borghisales.salessysten.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.borghisales.salessysten.model.Seller;
import org.borghisales.salessysten.model.SellerDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends MenuController implements Initializable {

    //Atributos conectados a la interfaz de usuario
    @FXML
    private  TextField user;
    @FXML
    private TextField password;

    public static Seller sellerLog; //Variable global que representa a la persona con sesion activa


    //Métodos que se tienen que implementar a fuerza por la interfaz Initializable (viene de JavaFX)
    //Limpia los datos que se haya modificado el logIn anterior
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { //Se activa cada vez que se cierra la sesion
//        user.setText("44994806");
//        password.setText("chimu");

        //In case the user is changed, the reports of that user should be updated.
        ReportsController.setSales(null);
        ReportsController.setPieChartData(null);
        ReportsController.setLineChartData(null);
    }

    //Es el que esta conectado con los botones de JavaFX
    @FXML // Conectado con algun onAction="#signIn"
    private void signIn(){
        if (SellerDAO.login(user.getText(),password.getText())) { //Llama a SellerDAO para revisar la existencia
                                                                  // de las credenciales en la base de datos

            openNewStage(MANAGEMENT_VIEW_FXML, "Administrar"); //Llama a la interfaz "hija" y
                                                                    // le pone como titulo "management"
            closeCurrentStage(user); //Al iniciar sesión correctamente cierra la ventana actual

        }
    }

}
