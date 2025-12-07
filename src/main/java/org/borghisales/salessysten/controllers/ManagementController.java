package org.borghisales.salessysten.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ManagementController extends MenuController implements Initializable {

    //private static int lastTab ;

    @FXML
    private StackPane mainpage;

//    @FXML
//    private Button sellerButton;

//    @FXML
//    private TabPane tabPaneManage;

//    @FXML
//    void openSeller(ActionEvent actionEvent){
//        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
//        openNewStage(SELLER_VIEW_FXML,"Vendedor");
//        closeCurrentStage(sellerButton);
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPage(WELCOME_MENU_VIEW_FXML,"Bienvenido");
    }

    public void openGenerateSale(ActionEvent actionEvent) {
        loadPage(GENERATE_SALE_VIEW_FXML,"Carrito de compras");
    }

    public void openCustomer(ActionEvent actionEvent) {
        loadPage(CUSTOMER_VIEW_FXML,"Clientes");
    }

    public void openProduct(ActionEvent actionEvent) {
        loadPage(PRODUCT_VIEW_FXML,"Productos");
    }

    public void openSeller(ActionEvent actionEvent) {
        loadPage(SELLER_VIEW_FXML,"Vendedores");
    }

    public void openSalesReport(ActionEvent actionEvent) {
        loadPage(REPORT_VIEW_FXML,"Ventas");
    }

    public void loadPage(String fxmlFileName, String title){
        try{

            Parent page = FXMLLoader.load(getClass().getResource(fxmlFileName));
            mainpage.getChildren().setAll(page);
        } catch (IOException | NullPointerException e) {
            setAlert(Alert.AlertType.WARNING, "Error cargando la vista: "+ e.getMessage());
        }
    }
//    public void openCustomer(ActionEvent actionEvent) {
//        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
//        openNewStage(CUSTOMER_VIEW_FXML, "Cliente");
//        closeCurrentStage(sellerButton);
//
//    }
//
//    public void openProduct(ActionEvent actionEvent) {
//        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
//        openNewStage(PRODUCT_VIEW_FXML,"Productos");
//        closeCurrentStage(sellerButton);
//
//    }
//
//    public void openGenerateSale(ActionEvent actionEvent) {
//        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
//        openNewStage(GENERATE_SALE_VIEW_FXML,"Carrito de compras");
//        closeCurrentStage(sellerButton);
//    }
//    public void openSalesReport(ActionEvent actionEvent) {
//        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
//        openNewStage(REPORT_VIEW_FXML,"Ventas");
//        closeCurrentStage(sellerButton);
//
//    }

    public void help(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/Borghii/Sales-System"));
        } catch (Exception e) {
            e.printStackTrace();
            setAlert(Alert.AlertType.ERROR,"La liga no puede ser abierta. Revisa tu conexion de internet.");
        }

    }

    public void exit(ActionEvent actionEvent) {
        openNewStage(MAIN_VIEW_FXML,"Inicio de sesion");
        closeCurrentStage(mainpage);
    }



}
