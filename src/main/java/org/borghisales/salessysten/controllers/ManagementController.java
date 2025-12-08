package org.borghisales.salessysten.controllers;

import javafx.application.Platform;
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

public class ManagementController extends MenuController implements Initializable {

    private static int lastTab ;

    @FXML
    private Button sellerButton;

    @FXML
    private TabPane tabPaneManage;

    @FXML
    void openSeller(ActionEvent actionEvent){
        // Verificar si es MANAGER
        if (MainController.sellerLog.role() != Seller.Role.MANAGER) {
            MenuController.showAccessDenied(
                    "Solo los gerentes pueden gestionar vendedores",
                    MainController.sellerLog.role()
            );
            return;
        }
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
        // Verificar si es MANAGER
        if (MainController.sellerLog.role() != Seller.Role.MANAGER) {
            MenuController.showAccessDenied(
                    "Solo los gerentes pueden gestionar productos",
                    MainController.sellerLog.role()
            );
            return;
        }
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
//corrigiendo el problema de que se traba al darle en help
    public void help(ActionEvent actionEvent) {
        new Thread(() -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/Borghii/Sales-System"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        setAlert(Alert.AlertType.ERROR, "No se pudo abrir la URL. Verifica tu conexión o configuración del sistema.");
                    });
                }
            } else {
                Platform.runLater(() -> {
                    setAlert(Alert.AlertType.ERROR, "La funcionalidad de escritorio (abrir navegador) no está disponible en este sistema.");
                });
            }
        }).start();
    }

    public void exit(ActionEvent actionEvent) {
        openNewStage(MAIN_VIEW_FXML,"Ingresar");
        closeCurrentStage(sellerButton);
    }


}
