package org.borghisales.salessysten.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import org.borghisales.salessysten.model.Seller;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import static org.borghisales.salessysten.Main.hostServices;

public class ManagementController extends MenuController implements Initializable {

    private static int lastTab ;

    @FXML
    private Button sellerButton;

    @FXML
    private TabPane tabPaneManage;

    @FXML
    void openSeller(ActionEvent actionEvent){
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        MenuController.openNewStage(SELLER_VIEW_FXML,"Vendedor");
        closeCurrentStage(sellerButton);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabPaneManage.getSelectionModel().select(lastTab);
    }

    public void openCustomer(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        MenuController.openNewStage(CUSTOMER_VIEW_FXML, "Cliente");
        closeCurrentStage(sellerButton);

    }

    public void openProduct(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        MenuController.openNewStage(PRODUCT_VIEW_FXML,"Productos");
        closeCurrentStage(sellerButton);

    }

    public void openGenerateSale(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        MenuController.openNewStage(GENERATE_SALE_VIEW_FXML,"Carrito de Compras");
        closeCurrentStage(sellerButton);
    }
    public void openSalesReport(ActionEvent actionEvent) {
        if (MainController.sellerLog == null ||
                MainController.sellerLog.role() != Seller.Role.ADMIN) {

            setAlert(Alert.AlertType.WARNING,
                    "Acceso Denegado, Solo los administradores pueden acceder a los reportes");
            return;
        }
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        MenuController.openNewStage(REPORT_VIEW_FXML,"Ventas");
        closeCurrentStage(sellerButton);

    }

    public void help(ActionEvent actionEvent) {
        try {
            hostServices.showDocument("https://github.com/angel-chi/Sales-System-Java-MySQL/branches");
        } catch (Exception e) {
            e.printStackTrace();
            setAlert(Alert.AlertType.ERROR,"La URL no puede ser abierta, comprueba tu conexión a internet");
        }

    }

    public void exit(ActionEvent actionEvent) {
        openNewStage(MAIN_VIEW_FXML,"Inicio de sesión");
        closeCurrentStage(sellerButton);
    }
}