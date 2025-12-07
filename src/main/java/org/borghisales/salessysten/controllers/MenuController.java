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
    public static final String SALE_DETAIL_VIEW_FXML = VIEWS_DIRECTORY + "SaleDetailView.fxml";

    static Alert defaultAlert;
    static ButtonType acceptButton = new ButtonType("Aceptar");
    public static HashMap<String, String > filePaths = new HashMap<>();

    //Hashmap que guarda los titulos de cada ventana.
    public static HashMap<String, String> titles = new HashMap<>();
    static {
        titles.put(MANAGEMENT_VIEW_FXML, "Gestión");
        titles.put(SELLER_VIEW_FXML, "Vendedores");
        titles.put(CUSTOMER_VIEW_FXML, "Clientes");
        titles.put(PRODUCT_VIEW_FXML, "Productos");
        titles.put(GENERATE_SALE_VIEW_FXML, "Venta");
        titles.put(REPORT_VIEW_FXML, "Reportes");
        titles.put(SALE_DETAIL_VIEW_FXML, "Detalles de Venta");
    }
    //Metodo para obtener cada título perteneciente al hashmap.
    String getTitleForView(String fxml) {
        return titles.getOrDefault(fxml, "Ventana");
    }

    void closeCurrentStage(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void openNewStage(String fxmlFileName, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource(fxmlFileName));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            //Mantiene el nombre de la ventana actual.
            stage.getProperties().put("currentView",fxmlFileName);
            stage.getProperties().put("currentTitle", title);
            // Mantener el mismo tamaño al cambiar ventanas.
            stage.setMinWidth(400);
            stage.setMinHeight(600);
            // Mantiene el tamaño dinámico.
            stage.setResizable(false);
            // Contrar ventana.
            stage.centerOnScreen();
            configureStageCloseEvent(stage);
            stage.show();
        } catch (IOException | NullPointerException e) {
            setAlert(Alert.AlertType.WARNING, "Error al cargar la ventana: " + e.getMessage());
        }
    }

    /*Cambio en la clase que cierra una ventana. Ahora en lugar de mantener el fxmlFileName guardado al abrir la ventana
    se mantiene el nombre de la ventana actual, corrigiendo el error identificado
     */

    private void configureStageCloseEvent(Stage stage) {
        stage.setOnCloseRequest(e -> {
            String vistaActual = (String) stage.getProperties().get("currentView");

            if (!vistaActual.equals(MAIN_VIEW_FXML)) {
                String vistaPadre = getFxmlFather(vistaActual);
                String tituloPadre = getTitleForView(vistaPadre);
                openNewStage(vistaPadre,tituloPadre);
            }
        });
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
