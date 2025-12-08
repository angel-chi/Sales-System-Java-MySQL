package org.borghisales.salessysten.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagementController extends MenuController implements Initializable {

    private static int lastTab;

    @FXML
    private Button sellerButton;

    @FXML
    private TabPane tabPaneManage;

    @FXML
    void openSeller(ActionEvent actionEvent){
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openAutoSizeStage(SELLER_VIEW_FXML,"Vendedor");
        closeCurrentStage(sellerButton);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabPaneManage.getSelectionModel().select(lastTab);
    }

    public void openCustomer(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openAutoSizeStage(CUSTOMER_VIEW_FXML, "Cliente");
        closeCurrentStage(sellerButton);
    }

    public void openProduct(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openAutoSizeStage(PRODUCT_VIEW_FXML,"Productos");
        closeCurrentStage(sellerButton);
    }

    public void openGenerateSale(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openAutoSizeStage(GENERATE_SALE_VIEW_FXML,"Carrito de compras");
        closeCurrentStage(sellerButton);
    }

    public void openSalesReport(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openAutoSizeStage(REPORT_VIEW_FXML,"Ventas");
        closeCurrentStage(sellerButton);
    }

    public void help(ActionEvent actionEvent) {
        try {
            new ProcessBuilder("xdg-open", "https://github.com/Borghii/Sales-System")
                    .inheritIO()
                    .start();
        } catch (Exception e) {
            e.printStackTrace();
            setAlert(Alert.AlertType.ERROR, "No se pudo abrir el navegador.");
        }
    }

    public void exit(ActionEvent actionEvent) {
        openAutoSizeStage(MAIN_VIEW_FXML,"Inicio de sesión");
        closeCurrentStage(sellerButton);
    }
}
