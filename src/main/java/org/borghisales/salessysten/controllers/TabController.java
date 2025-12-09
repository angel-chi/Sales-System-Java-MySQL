package org.borghisales.salessysten.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.borghisales.salessysten.util.ViewFiles;

import java.io.IOException;


//clase que controla las ventanas que se sobreponen y no regresan hacia atras, ejemplos detalles de venta
// y crear un nuevo producto si no existe

public class TabController extends ViewFiles {

    //funcion para abrir un tab
    public static void openNewTab(String fxmlFileName){
        FXMLLoader fxmlLoaderSaleDetails = new FXMLLoader(MenuController.class.getResource(fxmlFileName));
        try {
            Scene scene = new Scene(fxmlLoaderSaleDetails.load());
            Stage stage = new Stage();
            String title = getTitle(fxmlFileName);
            stage.setTitle(title);
            stage.setScene(scene);
            configureSize(fxmlFileName, stage);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // switch para seleccionar el tamaño minimo de la ventana
    private static void configureSize (String fxmlFileName, Stage stage){
        switch (fxmlFileName){
            case MANAGEMENT_VIEW_FXML:
                stage.setMinWidth(320);
                stage.setMinHeight(440);
                break;
            case PRODUCT_VIEW_FXML:
                stage.setMinWidth(663);
                stage.setMinHeight(504);
                break;
            case REPORT_VIEW_FXML:
                stage.setMinWidth(1465);
                stage.setMinHeight(1048);
                break;
            case SALE_DETAIL_VIEW_FXML:
                stage.setMinWidth(799);
                stage.setMinHeight(560);
                break;
            case SELLER_VIEW_FXML:
                stage.setMinWidth(653);
                stage.setMinHeight(494);
                break;
            case GENERATE_SALE_VIEW_FXML:
                stage.setMinWidth(590);
                stage.setMinHeight(636);
                break;
            case CUSTOMER_VIEW_FXML:
                stage.setMinWidth(653);
                stage.setMinHeight(504);
                break;
            case HELP_VIEW_FXML:
                stage.setMinWidth(800);
                stage.setMinHeight(600);
        }
    }

    private static String getTitle (String fxmlFileName){
        switch (fxmlFileName){
            case MANAGEMENT_VIEW_FXML:
                return "Menú principal";
            case PRODUCT_VIEW_FXML:
                return "Producto";
            case REPORT_VIEW_FXML:
                return "Generar reporte";
            case SALE_DETAIL_VIEW_FXML:
                return "Detalles de venta";
            case SELLER_VIEW_FXML:
                return "Menú de vendedor";
            case GENERATE_SALE_VIEW_FXML:
                return "Generar venta";
            case CUSTOMER_VIEW_FXML:
                return "Menú de cliente";
            case HELP_VIEW_FXML:
                return "Panel de Ayuda";
            default:
                return "Inicio de Sesión";
        }
    }
}
