package org.borghisales.salessysten.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class MenuController {

    public static final String VIEWS_DIRECTORY = "/org/borghisales/salessysten/Views/";
    public static final String MAIN_VIEW_FXML = VIEWS_DIRECTORY + "MainView.fxml";
    public static final String MANAGEMENT_VIEW_FXML = VIEWS_DIRECTORY + "ManagementView.fxml";
    public static final String SELLER_VIEW_FXML = VIEWS_DIRECTORY + "SellerView.fxml";
    public static final String PRODUCT_VIEW_FXML = VIEWS_DIRECTORY + "ProductView.fxml";
    public static final String CUSTOMER_VIEW_FXML = VIEWS_DIRECTORY + "CustomerView.fxml";
    public static final String GENERATE_SALE_VIEW_FXML = VIEWS_DIRECTORY + "GenerateSaleView.fxml";
    public static final String REPORT_VIEW_FXML = VIEWS_DIRECTORY + "ReportsView.fxml";
    public static final String HELP_VIEW_FXML = VIEWS_DIRECTORY + "HelpView.fxml";
    public static final String SALE_DETAIL_VIEW_FXML = VIEWS_DIRECTORY + "SaleDetailView.fxml";
    public static final String PROVEEDOR_VIEW_FXML = VIEWS_DIRECTORY + "ProveedorView.fxml";
    public static final String GENERAR_COMPRA_VIEW_FXML = VIEWS_DIRECTORY + "GenerarCompraView.fxml";
    public static final String COMPRAS_REPORT_VIEW_FXML = VIEWS_DIRECTORY + "ComprasReportView.fxml";


    static Alert defaultAlert;
    static ButtonType acceptButton = new ButtonType("Aceptar");
    public static HashMap<String, String > filePaths = new HashMap<>();

    void closeCurrentStage(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }


    public void openNewStage(String fxmlFileName, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource(fxmlFileName));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600); //Estas dos lineas se agregaron para que al abrir una pestaña nueva se abra con un tamaño fijo
            configureStageCloseEvent(stage, fxmlFileName, title);
            stage.show();

        } catch (IOException | NullPointerException e) {
            setAlert(Alert.AlertType.WARNING, "Error al cargar la vista: "+ e.getMessage());
        }
    }
//Se cambió esto para la salida de la ventana help
    //Se volvió a modificar para la salida desde la ventana de Gestión
    private void configureStageCloseEvent(Stage stage, String fxmlFileName, String title) {
        if (!fxmlFileName.equals(MAIN_VIEW_FXML)) {
            stage.setOnCloseRequest(e -> {
                String parentFxml = getFxmlFather(fxmlFileName);
                if (parentFxml != null) {
                    // Determinar el título correcto según la ventana padre
                    String parentTitle = parentFxml.equals(MAIN_VIEW_FXML) ? "Inicio de Sesión" : "Gestión";
                    openNewStage(parentFxml, parentTitle);
                }
            });
        }
    }

    String getFxmlFather(String fxml){
        return filePaths.get(fxml);
    }

    static public void setAlert(Alert.AlertType alertType,String argument){
        defaultAlert = new Alert(alertType);
        defaultAlert.setTitle("Información");
        defaultAlert.setHeaderText(null);
        defaultAlert.getButtonTypes().setAll(acceptButton);
        defaultAlert.setContentText(argument);
        defaultAlert.showAndWait();
    }

    static public void cleanCells(TextField...cells){
        for (TextField e:cells)
            e.clear();
    }


}
