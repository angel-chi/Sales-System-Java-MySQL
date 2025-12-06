package org.borghisales.salessysten.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

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


    public void openNewStage(String fxmlFileName, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource(fxmlFileName));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            configureStageCloseEvent(stage, fxmlFileName, title);
            stage.show();
            stage.setWidth(700);
            stage.setHeight(650);
            stage.setAlwaysOnTop(true);
        } catch (IOException | NullPointerException e) {
            setAlert(Alert.AlertType.WARNING, "Error loading the view: "+ e.getMessage());
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

    static public void setAlert(Alert.AlertType alertType, String argument) {
        // SOLUCION -> Alertas siempre encima
        //Se espera a que el hilo este disponible para evitar problemas de interfaz
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.getButtonTypes().setAll(acceptButton);
            alert.setContentText(argument);

            //Trata de buscar una ventana padre (que este activa)
            Optional<Window> owner = Window.getWindows().stream()
                    .filter(Window::isFocused)
                    .findFirst();

            //Si no hay ventana activa busca la primera que se este mostrando
            if (!owner.isPresent()) {
                owner = Window.getWindows().stream()
                        .filter(Window::isShowing)
                        .findFirst();
            }
            //Asignar ventana padre a la alerta
            owner.ifPresent(alert::initOwner);
            //Activa el modal para bloquear
            alert.initModality(Modality.APPLICATION_MODAL);

            //Cast con stage para aplicar alwaysOnTop
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.setAlwaysOnTop(true);

            // Mostrar y esperar al cierre, al cierre el stage es eliminado
            alert.showAndWait();

            // Cualquier cosa tratar de quitar el alwaysOntTop
            try {
                alertStage.setAlwaysOnTop(false);
            } catch (Exception ignored) {}
        });
    }


    static public void cleanCells(TextField...cells){
        for (TextField e:cells)
            e.clear();
    }


}
