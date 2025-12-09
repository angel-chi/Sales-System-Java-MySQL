package org.borghisales.salessysten.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.scene.text.Text;

public class ManagementController extends MenuController implements Initializable {

    //private static int lastTab ;

    @FXML
    private Text dateNow, idVendedor, usuarioVendedor;


    @FXML
    private StackPane mainpage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idVendedor.setText(String.valueOf(MainController.sellerLog.idSeller()));
        usuarioVendedor.setText(MainController.sellerLog.dni());
        loadPage(WELCOME_MENU_VIEW_FXML);
    }

    public void openGenerateSale(ActionEvent actionEvent) {
        loadPage(GENERATE_SALE_VIEW_FXML);
    }

    public void openCustomer(ActionEvent actionEvent) {
        loadPage(CUSTOMER_VIEW_FXML);
    }

    public void openProduct(ActionEvent actionEvent) {

        loadPage(PRODUCT_VIEW_FXML);
    }

    public void openSeller(ActionEvent actionEvent) {
        loadPage(SELLER_VIEW_FXML);
    }

    public void openSalesReport(ActionEvent actionEvent) {
        loadPage(REPORT_VIEW_FXML);
    }

    public void loadPage(String fxmlFileName) {
        try {
            dateNow.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            Parent page = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFileName)));
            mainpage.getChildren().setAll(page);
        } catch (IOException | NullPointerException e) {
            setAlert(Alert.AlertType.WARNING, "Error cargando la vista: " + e.getMessage());
        }
    }


    public void help(ActionEvent actionEvent) {
        new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/Borghii/Sales-System"));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    setAlert(Alert.AlertType.ERROR, "La URL no pudo ser abierta. Revisa tu conexion a internet.");
                });
            }
        }).start();
    }

    public void exit(ActionEvent actionEvent) {
        openNewStage(MAIN_VIEW_FXML,"Inicio de sesion");
        closeCurrentStage(mainpage);
    }



}
