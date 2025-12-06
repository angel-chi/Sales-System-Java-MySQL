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
        openNewStage(GENERATE_SALE_VIEW_FXML,"Carrito de compra");
        closeCurrentStage(sellerButton);
    }
    public void openSalesReport(ActionEvent actionEvent) {
        lastTab = tabPaneManage.getSelectionModel().getSelectedIndex();
        openNewStage(REPORT_VIEW_FXML,"Ventas");
        closeCurrentStage(sellerButton);

    }
//Haciendo uso de la función Thread, se maneja el error del botón HELP sin bloquear el programa.
public void help(ActionEvent actionEvent) {
    new Thread(() -> {
        try {
            //URL ACTUALIZADA
            String url = "https://github.com/angel-chi/Sales-System-Java-MySQL.git";
            //Se revisa el nombre de cada sistema operativo, pues cada uno de ellos ejecuta una rutina de navegación distinta
            //Para windows
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", url});
           //Para linux y algunas de sus distribuciones
            } else if (System.getProperty("os.name").toLowerCase().contains("nux")||System.getProperty("os.name").toLowerCase().contains("nix")
            ||System.getProperty("os.name").toLowerCase().contains("aix")) {
                Runtime.getRuntime().exec(new String[]{"xdg-open", url});
            //Para MacOS
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"open", url});
            //POR SI ACASO
            } else {
                System.out.println("Sistema operativo no soportado");
            }
        } catch (Exception E) {
            E.printStackTrace();
            javafx.application.Platform.runLater(() ->
                    setAlert(Alert.AlertType.ERROR, "No se pudo abrir la URL.")
            );
        }
    }).start();
}

    public void exit(ActionEvent actionEvent) {
        openNewStage(MAIN_VIEW_FXML,"Iniciar Sesión");
        closeCurrentStage(sellerButton);
    }


}
