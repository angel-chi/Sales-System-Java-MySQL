package org.borghisales.salessysten.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
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
    public static final String HELP_VIEW_FXML = VIEWS_DIRECTORY + "HelpView.fxml";
    public static final String MAIN_PASSWORD_VIEW_FXML = VIEWS_DIRECTORY + "mainPasswordView.fxml";

    //Contraseña para menús y acciones que requieran un extra de seguridad
    private static final String ADMIN_PASSWORD = "main";


    static Alert defaultAlert;
    static ButtonType acceptButton = new ButtonType("Aceptar");
    public static HashMap<String, String > filePaths = new HashMap<>();

    public static void closeCurrentStage(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    //Esto se encarga de cada nueva ventana creada
    public static void openNewStage(String fxmlPath, String title, double width, double height, boolean modal) {
        try {
            FXMLLoader loader = new FXMLLoader(MenuController.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setMinWidth(width);
            stage.setMinHeight(height);

            //modal y ShowAndWait para que ciertos menus puedan recibir cambios y actualizarse (para la contraseña)
            if (modal) {
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } else {
                stage.show();
            }

        } catch (IOException e) {
            setAlert(Alert.AlertType.ERROR, "Error al abrir ventana: " + e.getMessage());
        }
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

    //boton de Cerrar ventana
    @FXML
    public void exitWindow(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        closeCurrentStage(button);
        openNewStage(MANAGEMENT_VIEW_FXML, "Control del punto de venta", 600, 800, false);
    }


    @FXML
    private ImageView img;
    //Todo esto es para pedir contraseña antes de acceder a ciertas funcionalidades
    @FXML
    private PasswordField password;

    public static boolean correctPassword = false;

    @FXML
    public void acceptPassword(){
        if (ADMIN_PASSWORD.equals(password.getText())){
            correctPassword = true;
            closeCurrentStage(password);
        } else {
            correctPassword = false;
        }
    }

    @FXML
    public static void requestPassword(){
        openNewStage(MAIN_PASSWORD_VIEW_FXML, "Aviso", 600, 50, true);
    }

}
