package org.borghisales.salessysten.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.borghisales.salessysten.model.entities.Nivel;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagementController extends MenuController implements Initializable {

    private static int lastTab ;

    @FXML private Button sellerButton;
    @FXML private Button proveedorButton;
    @FXML private TabPane tabPaneManage;
    @FXML private Tab ventasTab;
    @FXML private Tab gestionTab;
    @FXML private Tab reportesTab;

    @FXML
    void openSeller(ActionEvent actionEvent){
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(SELLER_VIEW_FXML,"Vendedor/a");
        closeCurrentStage(sellerButton);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabPaneManage.getSelectionModel().select(lastTab);
    }
    /*
    public void aplicarSeguridad(){
        Nivel nivel = MainController.sellerLog.nivel();
        if (nivel == null) {
            ventasTab.setDisable(false);
            reportesTab.setDisable(false);
            gestionTab.setDisable(false);
            return;
        }
        ventasTab.setDisable(!nivel.puedeVender());
        reportesTab.setDisable(!nivel.puedeRevisarReportes());
        gestionTab.setDisable(!nivel.puedeEditarPersona());
        }
    */

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
        openNewStage(GENERATE_SALE_VIEW_FXML,"Generar Venta");
        closeCurrentStage(sellerButton);
    }
    public void openSalesReport(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(REPORT_VIEW_FXML,"Ventas");
        closeCurrentStage(sellerButton);

    }

    @FXML
    private void help() {
        // Registrar que la ventana padre de Help es Management
        MenuController.filePaths.put(HELP_VIEW_FXML, MANAGEMENT_VIEW_FXML);

        openNewStage(HELP_VIEW_FXML, "Ayuda");
        closeCurrentStage(tabPaneManage);
    }

    public void exit(ActionEvent actionEvent) {
        openNewStage(MAIN_VIEW_FXML,"Inicio de Sesión");
        closeCurrentStage(sellerButton);
    }

    @FXML
    public void openProveedor(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        MenuController.filePaths.put(PROVEEDOR_VIEW_FXML, MANAGEMENT_VIEW_FXML);
        openNewStage(PROVEEDOR_VIEW_FXML, "Proveedores");
        closeCurrentStage(proveedorButton);
    }

    @FXML
    public void openGenerarCompra(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        MenuController.filePaths.put(GENERAR_COMPRA_VIEW_FXML, MANAGEMENT_VIEW_FXML);
        openNewStage(GENERAR_COMPRA_VIEW_FXML, "Generar Compra");
        closeCurrentStage(tabPaneManage);
    }

    @FXML
    public void openComprasReport(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        MenuController.filePaths.put(COMPRAS_REPORT_VIEW_FXML, MANAGEMENT_VIEW_FXML);
        openNewStage(COMPRAS_REPORT_VIEW_FXML, "Informe de Compras");
        closeCurrentStage(tabPaneManage);
    }

    @FXML
    public void openInventario(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        MenuController.filePaths.put(INVENTARIO_VIEW_FXML, MANAGEMENT_VIEW_FXML);
        openNewStage(INVENTARIO_VIEW_FXML, "Inventario");
        closeCurrentStage(tabPaneManage);
    }

}
