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
    public static HashMap<String, String > rutaArchivos = new HashMap<>();
    public static HashMap<String, String> titulosFxml = new HashMap<>();

    public MenuController(){
        titulosFxml.put(MAIN_VIEW_FXML,"Iniciar Sesión");
        titulosFxml.put(MANAGEMENT_VIEW_FXML,"Gestión");
        titulosFxml.put(SELLER_VIEW_FXML,"Vendedor");
        titulosFxml.put(PRODUCT_VIEW_FXML,"Productos");
        titulosFxml.put(CUSTOMER_VIEW_FXML,"Cliente");
        titulosFxml.put(GENERATE_SALE_VIEW_FXML,"Carrito de compras");
        titulosFxml.put(REPORT_VIEW_FXML,"Ventas");
        titulosFxml.put(SALE_DETAIL_VIEW_FXML,"Detalle de venta");
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
            //Mantener el tamaño al cambiar de pestaña.
            stage.setMinWidth(400);
            stage.setMinHeight(600);
            // Mantiene el tamaño dinámico.
            stage.setResizable(false);
            // Centrar ventana.
            stage.centerOnScreen();
            stage.setTitle(title);
            stage.setScene(scene);
            configureStageCloseEvent(stage, fxmlFileName);
            stage.show();

        } catch (IOException | NullPointerException e) {
            setAlert(Alert.AlertType.WARNING, "Error cargando la vista: "+ e.getMessage());
        }
    }
    //Configura el evento de cambio de pestaña.
    //Ahora se hace una relación entre pestaña padre e hija, permitiendo que cada que se abra una ventana se mantenga el título.
    private void configureStageCloseEvent(Stage stage, String fxmlFileName) {
        if (!fxmlFileName.equals(MAIN_VIEW_FXML)) {
            stage.setOnCloseRequest(e -> {
                String padreFxml = getFxmlFather(fxmlFileName);
                String tituloPadre = titulosFxml.getOrDefault(padreFxml, "Ventana");
                openNewStage(padreFxml, tituloPadre);
            });
        }
    }

    String getFxmlFather(String fxml){
        return rutaArchivos.get(fxml);
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