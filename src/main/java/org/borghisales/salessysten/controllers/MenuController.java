package org.borghisales.salessysten.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;


//Clase papá de las vistas
public class MenuController {

    //RUTAS A LOS ARCHIVOS .FXML (Vista del programa) //Permiten abrir la vista sin escribir toda la direccion
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
    public static HashMap<String, String > filePaths = new HashMap<>();  //Indica qué ventana abrir
                                                                         //cuando se cierra otra
                                                                         //(relaciones entre vistas)

    //Cierra ventanas emergentes
    void closeCurrentStage(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    //despues de presionar un boton abre otra ventana
    public void openNewStage(String fxmlFileName, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource(fxmlFileName));
            Parent root = fxmlLoader.load();

            Scene scene;
            Stage stage = new Stage();

            switch (fxmlFileName) {
                case MAIN_VIEW_FXML -> {
                    scene = new Scene(root, 420, 400);
                    stage.setResizable(false);
                }
                case MANAGEMENT_VIEW_FXML -> {
                    scene = new Scene(root, 315, 450);
                    stage.setResizable(false);
                }
                case CUSTOMER_VIEW_FXML -> {
                    scene = new Scene(root, 665, 510);
                    stage.setResizable(false);
                }
                case PRODUCT_VIEW_FXML, SELLER_VIEW_FXML -> {
                    scene = new Scene(root, 650, 500);
                    stage.setResizable(false);
                }
                case REPORT_VIEW_FXML -> {
                    scene = new Scene(root, 1470, 1030);
                    stage.setResizable(false);
                }
                case GENERATE_SALE_VIEW_FXML -> {
                    scene = new Scene(root, 590, 600);
                    stage.setResizable(false);
                }
                default -> {
                    // tamaño por defecto si no coincide con ninguno
                    scene = new Scene(root, 600, 400);
                }
            }

            stage.setTitle(title);
            stage.setScene(scene);
            configureStageCloseEvent(stage, fxmlFileName, title); //Configura que pasa cuando esta ventana se ciera
                                                                    // a donde va?
            stage.show();

        } catch (IOException | NullPointerException e) {
            setAlert(Alert.AlertType.WARNING, "Error al cargar la ventana: "+ e.getMessage());
        }
    }

    //Configura que pasa cuando una pestaña se cierra (redirige al usuario)
    private void configureStageCloseEvent(Stage stage, String fxmlFileName, String title) {
        if (!fxmlFileName.equals(MAIN_VIEW_FXML)) {
            stage.setOnCloseRequest(e -> {
                openNewStage(getFxmlFather(fxmlFileName),title);
            });
        }
    }

    //Encuentra a las vistas papa de una clase
    String getFxmlFather(String fxml){
        return filePaths.get(fxml);
    }

    //Mensajes emergentes al usuario
    static public void setAlert(Alert.AlertType alertType,String argument){
        defaultAlert = new Alert(alertType);
        defaultAlert.setTitle("Información");
        defaultAlert.setHeaderText(null);
        defaultAlert.getButtonTypes().setAll(acceptButton);
        defaultAlert.setContentText(argument);
        defaultAlert.showAndWait();
    }

    //Limpia campos de texto
    static public void cleanCells(TextField...cells){
        for (TextField e:cells)
            e.clear();
    }


}
