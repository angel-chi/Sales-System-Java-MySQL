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
    static ButtonType acceptButton = new ButtonType("Accept");
    
    public static HashMap<String, String > filePaths = new HashMap<>();

    void closeCurrentStage(Node node) {

        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }


    public void openAutoSizeStage(String fxmlFileName, String title) {
        try {
            // Abrir una nueva ventana (core del sistema)
            FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource(fxmlFileName));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);


            stage.setResizable(false); // Evite maximizar
            stage.setMaximized(true);


            configureStageCloseEvent(stage, fxmlFileName, title);
            stage.show();
            stage.centerOnScreen();

        } catch (IOException | NullPointerException e) {
            setAlert(Alert.AlertType.WARNING, "Error loading the view: "+ e.getMessage());
        }
    }

    private void configureStageCloseEvent(Stage stage, String fxmlFileName, String title) {
        /*
        Acción al cerrar una ventana.
        Si NO estás en la ventana principal
        Cuando el usuario cierra la ventana
        Automáticamente se abre su ventana padre
         */
        if (!fxmlFileName.equals(MAIN_VIEW_FXML)) {
            stage.setOnCloseRequest(e -> {
                openAutoSizeStage(getFxmlFather(fxmlFileName),title);
            });
        }
    }
    // Obtener la vista padre
    String getFxmlFather(String fxml){
        return filePaths.get(fxml);
    }
    // Alertas globales
    static public void setAlert(Alert.AlertType alertType,String argument){
        defaultAlert = new Alert(alertType);
        defaultAlert.setTitle("Information");
        defaultAlert.setHeaderText(null);
        defaultAlert.getButtonTypes().setAll(acceptButton);
        defaultAlert.setContentText(argument);
        defaultAlert.showAndWait();
    }
    // Limpia múltiples campos en una sola línea
    static public void cleanCells(TextField...cells){
        for (TextField e : cells)
            e.clear();
    }


}
