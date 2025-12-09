package org.borghisales.salessysten.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagementController extends MenuController implements Initializable {

    private static int lastTab ;

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

    public void openProduct(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(PRODUCT_VIEW_FXML,"Productos");
        closeCurrentStage(sellerButton);

    }

    public void openGenerateSale(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(GENERATE_SALE_VIEW_FXML,"Carrito de compras");
        closeCurrentStage(sellerButton);
    }
    public void openSalesReport(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(REPORT_VIEW_FXML,"Ventas");
        closeCurrentStage(sellerButton);

    }

    public void help(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(HELP_DETAIL_VIEW_FXML,"Ayuda");
        closeCurrentStage(sellerButton);
    }

    public void exit(ActionEvent actionEvent) {
        openNewStage(MAIN_VIEW_FXML,"Iniciar sesión");
        closeCurrentStage(sellerButton);
    }


}
