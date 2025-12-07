package org.borghisales.salessysten.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;

//importar la clase vendedor para usarla
import org.borghisales.salessysten.model.Vendedor;

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
    private Button productButton;

    @FXML
    void openSeller(ActionEvent actionEvent){
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(SELLER_VIEW_FXML,"Vendedor");
        closeCurrentStage(sellerButton);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabPaneManage.getSelectionModel().select(lastTab);

        // Ocultar botón de productos si NO es administrador
        if (MainController.vendedorLogeado != null &&
                MainController.vendedorLogeado.getRol() == Vendedor.Rol.VENDEDOR) {

            productButton.setDisable(true);
            productButton.setVisible(false);
        }
    }

    public void openCustomer(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(CUSTOMER_VIEW_FXML, "Cliente");
        closeCurrentStage(sellerButton);

    }

    public void openProduct(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(PRODUCT_VIEW_FXML,"Producto");
        closeCurrentStage(sellerButton);
    }

    public void openGenerateSale(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(GENERATE_SALE_VIEW_FXML,"Carrito de Compras");
        closeCurrentStage(sellerButton);
    }
    public void openSalesReport(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(REPORT_VIEW_FXML,"Ventas");
        closeCurrentStage(sellerButton);

    }

    public void help(ActionEvent actionEvent) {
        //Se usa Thread para no bloquear la interfaz gráfica
        new Thread(() -> {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI("https://github.com/angel-chi/Sales-System-Java-MySQL/blob/Basulto-Maga%C3%B1a/README.md"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() ->
                        setAlert(Alert.AlertType.ERROR, "No se pudo abrir la URL.")
                );
            }
        }).start();
    }

    public void openConfiguracionAdmin(ActionEvent actionEvent) {
        if (MainController.vendedorLogeado != null && MainController.vendedorLogeado.getRol() == Vendedor.Rol.ADMINISTRADOR) {
            lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
            openNewStage(CONFIGURACION_ADMIN_VIEW_FXML, "Configuraciones de Administrador");
            closeCurrentStage(sellerButton);
        } else {
            setAlert(Alert.AlertType.WARNING, "Acceso denegado. Solo los administradores pueden acceder a esta configuración.");
        }
    }

    public void exit(ActionEvent actionEvent) {
        openNewStage(MAIN_VIEW_FXML,"Iniciar Sesión");
        closeCurrentStage(sellerButton);
    }
}
