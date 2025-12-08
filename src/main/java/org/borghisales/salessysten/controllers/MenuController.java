package org.borghisales.salessysten.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;

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


    public static void openNewStage(String fxmlFileName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(MenuController.class.getResource(fxmlFileName));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);

            // Variables para dimensiones
            double width, height, minWidth, minHeight;

            // Switch case para determinar tamaños según la ventana que se abra
            switch (fxmlFileName) {
                case MAIN_VIEW_FXML:
                    width = 1200;
                    height = 900;
                    minWidth = 900;
                    minHeight = 500;
                    break;
                case MANAGEMENT_VIEW_FXML:
                    width = 1200;
                    height = 900;
                    minWidth = 900;
                    minHeight = 500;
                    break;
                case SELLER_VIEW_FXML:
                    width = 1200;
                    height = 800;
                    minWidth = 800;
                    minHeight = 500;
                    break;
                case PRODUCT_VIEW_FXML:
                    width = 1100;
                    height = 700;
                    minWidth = 850;
                    minHeight = 550;
                    break;
                case CUSTOMER_VIEW_FXML:
                    width = 1000;
                    height = 650;
                    minWidth = 800;
                    minHeight = 500;
                    break;
                case GENERATE_SALE_VIEW_FXML:
                    width = 1800;
                    height = 1200;
                    minWidth = 1000;
                    minHeight = 600;
                    break;
                case REPORT_VIEW_FXML:
                    width = 1400;
                    height = 800;
                    minWidth = 1100;
                    minHeight = 650;
                    break;
                case SALE_DETAIL_VIEW_FXML:
                    width = 900;
                    height = 600;
                    minWidth = 700;
                    minHeight = 500;
                    break;

                default:
                    width = 1200;
                    height = 700;
                    minWidth = 900;
                    minHeight = 500;
                    break;
            }
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setMinWidth(minWidth);
            stage.setMinHeight(minHeight);

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException | NullPointerException e) {
            setAlert(Alert.AlertType.WARNING, "Error en la carga de la ventana: " + e.getMessage());
        }
    }

    private void configureStageCloseEvent(Stage stage, String fxmlFileName, String title) {
        if (!fxmlFileName.equals(MAIN_VIEW_FXML)) {
            stage.setOnCloseRequest(e -> {
                openNewStage(getFxmlFather(fxmlFileName),title);
            });
        }
    }

    String getFxmlFather(String fxml){
        return filePaths.get(fxml);
    }

    static public void setAlert(Alert.AlertType alertType,String argument){
        defaultAlert = new Alert(alertType);
        defaultAlert.setTitle("Information");
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
