package org.borghisales.salessysten.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

//TODOS LOS BOTONES DE MANAGEMENT BARRA DE ARRIBA Y RECUADROS
public class ManagementController extends MenuController implements Initializable {

    private static int lastTab ; //GUARDA LA PAGINA DONDE ESTABA EL USUARIO ANTES DE ENTRAR A OTRA PAGINA

    @FXML
    private Button sellerButton;

    @FXML
    private TabPane tabPaneManage;

    @FXML
    void openSeller(ActionEvent actionEvent){
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(SELLER_VIEW_FXML,"Vendedor");
        closeCurrentStage(sellerButton);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabPaneManage.getSelectionModel().select(lastTab);
    }

    public void openCustomer(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(CUSTOMER_VIEW_FXML, "Cliente");
        closeCurrentStage(sellerButton);

    }

    //Management ---- > product
    public void openProduct(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(PRODUCT_VIEW_FXML,"Productos");
        closeCurrentStage(sellerButton);

    }

    //Management----->generate sale
    public void openGenerateSale(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(GENERATE_SALE_VIEW_FXML,"Generador de Ventas");
        closeCurrentStage(sellerButton);
    }
    public void openSalesReport(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(REPORT_VIEW_FXML,"Ventas");
        closeCurrentStage(sellerButton);

    }

    //YA FUNCIONA
    public void help(ActionEvent actionEvent) {
        MenuController.setAlert(Alert.AlertType.INFORMATION, "Para cualquier duda, leer el manual del software en: https://github.com/Borghii/Sales-System");
    }


    public void exit(ActionEvent actionEvent) {
        openNewStage(MAIN_VIEW_FXML,"Registro");
        closeCurrentStage(sellerButton);
    }


}
