package org.borghisales.salessysten.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagementController extends MenuController implements Initializable {

    @FXML
    private  ImageView uadyLogo;
    @FXML
    private ImageView reportes;
    @FXML
    private ImageView tuerca;
    @FXML
    private ImageView carritoShop;
    @FXML
    private ImageView cinta;

    @FXML
    private Button sellerButton;

    @FXML
    void openSeller(ActionEvent actionEvent){
        openNewStage(SELLER_VIEW_FXML,"Vendedor", 650, 600);
        closeCurrentStage((Button) actionEvent.getSource());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Image img = new Image(getClass().getResource("/images/cinta.jpg").toString());
        cinta.setImage(img);

        Image img2 = new Image(getClass().getResource("/images/carritoshop.png").toString());
        carritoShop.setImage(img2);

        Image img3 = new Image(getClass().getResource("/images/tuerca.png").toString());
        tuerca.setImage(img3);

        Image img4 = new Image(getClass().getResource("/images/reportes.png").toString());
        reportes.setImage(img4);

        Image img5 = new Image(getClass().getResource("/images/uady.png").toString());
        uadyLogo.setImage(img5);


    }



    public void openCustomer(ActionEvent actionEvent) {
        openNewStage(CUSTOMER_VIEW_FXML, "Clientes", 1000, 800);
        closeCurrentStage((Button) actionEvent.getSource());
    }

    public void openProduct(ActionEvent actionEvent) {
        openNewStage(PRODUCT_VIEW_FXML,"Productos", 800, 800);
        closeCurrentStage((Button) actionEvent.getSource());
    }

    public void openGenerateSale(ActionEvent actionEvent) {

        openNewStage(GENERATE_SALE_VIEW_FXML,"Carrito de compra", 700, 700);
        closeCurrentStage((Button) actionEvent.getSource());
    }

    public void openSalesReport(ActionEvent actionEvent) {
        openNewStage(REPORT_VIEW_FXML,"Ventas", 1500, 1025);
        closeCurrentStage((Button) actionEvent.getSource());

    }

    public void help(ActionEvent actionEvent) {
        setAlert(Alert.AlertType.INFORMATION, "SE ENCUENTRA EN MANTENIMIENTO");
    }

    public void exit(ActionEvent actionEvent) {

        closeCurrentStage((Button) actionEvent.getSource());
        openNewStage(MAIN_VIEW_FXML, "Inicio de sesión", 400, 500);
    }


}
