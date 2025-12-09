package org.borghisales.salessysten.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.borghisales.salessysten.util.ViewFiles;

import java.io.IOException;
import java.util.HashMap;

public class MenuController extends ViewFiles {


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
            //fix: ventanas chicas
            stage.sizeToScene();
            stage.centerOnScreen();
            configureSize(fxmlFileName, stage);
            //
            if (fxmlFileName.equals(MAIN_VIEW_FXML) || fxmlFileName.equals(HELP_VIEW_FXML)){
                stage.setResizable(false);
            }
            //
            configureStageCloseEvent(stage, fxmlFileName);
            stage.show();


        } catch (IOException | NullPointerException e) {
            setAlert(Alert.AlertType.WARNING, "Error al cargar la vista: "+ e.getMessage());
        }
    }

// switch para seleccionar el tamaño minimo de la ventana
    private void configureSize (String fxmlFileName, Stage stage){
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
                stage.setMinHeight(554);
                break;
            case SELLER_VIEW_FXML:
                stage.setMinWidth(653);
                stage.setMinHeight(494);
                break;
            case GENERATE_SALE_VIEW_FXML:
                stage.setMinWidth(850);
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

    private void configureStageCloseEvent(Stage stage, String fxmlFileName) {
        if (!fxmlFileName.equals(MAIN_VIEW_FXML)) {
            stage.setOnCloseRequest(e -> {
                //obtener titulo de la ventana anterior
                String previousTitle = getPreviousTitle(fxmlFileName);
                openNewStage(getFxmlFather(fxmlFileName), previousTitle);
            });
        }
    }
    String getFxmlFather(String fxml){
        return filePaths.get(fxml);
    }

    // fix: Nombre de las ventanas no cambian
    private String getPreviousTitle (String fxmlFileName){
        fxmlFileName = getFxmlFather(fxmlFileName);
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
    //
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
