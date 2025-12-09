package org.borghisales.salessysten.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
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
    public static final String CONFIGURACION_ADMIN_VIEW_FXML = VIEWS_DIRECTORY + "ConfiguracionAdminView.fxml";


    static Alert defaultAlert;
    static ButtonType acceptButton = new ButtonType("Aceptar");
    public static HashMap<String, String > filePaths = new HashMap<>();

    static {
        filePaths.put(MANAGEMENT_VIEW_FXML, MAIN_VIEW_FXML);
        filePaths.put(SELLER_VIEW_FXML, MANAGEMENT_VIEW_FXML);
        filePaths.put(PRODUCT_VIEW_FXML, MANAGEMENT_VIEW_FXML);
        filePaths.put(CUSTOMER_VIEW_FXML, MANAGEMENT_VIEW_FXML);
        filePaths.put(GENERATE_SALE_VIEW_FXML, MANAGEMENT_VIEW_FXML);
        filePaths.put(REPORT_VIEW_FXML, MANAGEMENT_VIEW_FXML);
        filePaths.put(CONFIGURACION_ADMIN_VIEW_FXML, MANAGEMENT_VIEW_FXML);
    }

    void closeCurrentStage(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }


    //Abrir una nueva ventana
    public void openNewStage(String fxmlFileName, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource(fxmlFileName));
            Parent root = fxmlLoader.load();

            Scene scene;
            Stage stage = new Stage();

            switch (fxmlFileName) {
                case MAIN_VIEW_FXML, MANAGEMENT_VIEW_FXML, GENERATE_SALE_VIEW_FXML, CONFIGURACION_ADMIN_VIEW_FXML:
                    scene = new Scene(root, 800, 600);
                    stage.setResizable(false);
                    break;

                case REPORT_VIEW_FXML:
                    scene = new Scene(root, 1470, 1040);
                    stage.setResizable(false);
                    break;

                case PRODUCT_VIEW_FXML:
                    scene = new Scene(root, 650, 500);
                    stage.setResizable(false);
                    break;

                case SELLER_VIEW_FXML:
                    scene = new Scene(root, 700, 500);
                    stage.setResizable(false);
                    break;

                case CUSTOMER_VIEW_FXML:
                    scene = new Scene(root, 665, 510);
                    stage.setResizable(false);
                    break;

                // Si ningún título coincide, usa tamaño genérico
                default:
                    scene = new Scene(root, 800, 600);
            }

            stage.setTitle(title);
            stage.setScene(scene);
            configureStageCloseEvent(stage, fxmlFileName, title);
            stage.show();

        } catch (IOException | NullPointerException e) {
            setAlert(Alert.AlertType.WARNING, "Error: cargando la vista: "+ e.getMessage());
        }
    }

    private void configureStageCloseEvent(Stage stage, String fxmlFileName, String title) {
        if (!fxmlFileName.equals(MAIN_VIEW_FXML)) {
            stage.setOnCloseRequest(e -> {
                //Se obtiene la ventana padre
                String parentFxml = getFxmlFather(fxmlFileName);
                if (parentFxml != null) {
                    //Aseguramos que el título también se pase correctamente
                    openNewStage(parentFxml, getParentTittle(parentFxml));
                }
            });
        }
    }

    //Método para determinar el nombre de la ventana padre
    private String getParentTittle(String fxmlFileName) {
        //Nombre de las ventanas padres (ventanas que se ven al cerrar otras)
        switch (fxmlFileName) {
            case MAIN_VIEW_FXML:
                return "Inicio de Sesión";
            case MANAGEMENT_VIEW_FXML:
                return "Gestión";
            case CONFIGURACION_ADMIN_VIEW_FXML:
                return "Configuraciones de Administrador";
            default:
                return "Ventana desconocida";
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
