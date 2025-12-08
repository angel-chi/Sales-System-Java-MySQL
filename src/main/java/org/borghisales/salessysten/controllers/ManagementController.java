package org.borghisales.salessysten.controllers;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagementController extends MenuController implements Initializable {

    @FXML private Button btnSale;
    @FXML private Button btnReport;
    @FXML private Button btnClient;
    @FXML private Button btnProduct;
    @FXML private Button btnSeller;
    @FXML private Button btnHelp;
    @FXML private Button btnExit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // GRUPO VENTAS
        String greenBase = "-fx-background-color: #2e3b2e; -fx-text-fill: #a5d6a7; -fx-background-radius: 10; -fx-border-color: #2e7d32; -fx-border-radius: 10;";
        String greenHover = "-fx-background-color: #388e3c; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #66bb6a; -fx-border-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(76,175,80,0.4), 10, 0, 0, 5);";
        addHoverEffect(btnSale, greenBase, greenHover);
        addHoverEffect(btnReport, greenBase, greenHover);

        // GRUPO GESTION
        String blueBase = "-fx-background-color: #2a313d; -fx-text-fill: #90caf9; -fx-background-radius: 10; -fx-border-color: #1565c0; -fx-border-radius: 10;";
        String blueHover = "-fx-background-color: #1976d2; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #42a5f5; -fx-border-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(33,150,243,0.4), 10, 0, 0, 5);";
        addHoverEffect(btnClient, blueBase, blueHover);
        addHoverEffect(btnProduct, blueBase, blueHover);
        addHoverEffect(btnSeller, blueBase, blueHover);

        // GRUPO SISTEMA
        String helpBase = "-fx-background-color: #3e2723; -fx-text-fill: #ffccbc; -fx-background-radius: 10; -fx-border-color: #d84315; -fx-border-radius: 10;";
        String helpHover = "-fx-background-color: #d84315; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #ff7043; -fx-border-radius: 10;";
        addHoverEffect(btnHelp, helpBase, helpHover);

        String exitBase = "-fx-background-color: #373737; -fx-text-fill: #ef9a9a; -fx-background-radius: 10; -fx-border-color: #c62828; -fx-border-radius: 10;";
        String exitHover = "-fx-background-color: #c62828; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #ff5252; -fx-border-radius: 10;";
        addHoverEffect(btnExit, exitBase, exitHover);
    }

    private void addHoverEffect(Button btn, String baseStyle, String hoverStyle) {
        btn.setCursor(Cursor.HAND);

        btn.setOnMouseEntered(e -> {
            btn.setStyle(hoverStyle);
            scaleButton(btn, 1.05); // Crece un 5%
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(baseStyle);
            scaleButton(btn, 1.0); // Vuelve a tamaño original
        });
    }

    private void scaleButton(Button btn, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }

    // --- MÉTODOS DE ACCIÓN (Lógica original mantenida) ---

    @FXML
    void openSeller(ActionEvent actionEvent){
        openNewStage(SELLER_VIEW_FXML,"Seller");
        closeCurrentStage(btnSeller);
    }

    public void openCustomer(ActionEvent actionEvent) {
        openNewStage(CUSTOMER_VIEW_FXML, "Customer");
        closeCurrentStage(btnClient);
    }

    public void openProduct(ActionEvent actionEvent) {
        openNewStage(PRODUCT_VIEW_FXML,"Products");
        closeCurrentStage(btnProduct);
    }

    public void openGenerateSale(ActionEvent actionEvent) {
        openNewStage(GENERATE_SALE_VIEW_FXML,"Shopping cart");
        closeCurrentStage(btnSale);
    }

    public void openSalesReport(ActionEvent actionEvent) {
        openNewStage(REPORT_VIEW_FXML,"Sales");
        closeCurrentStage(btnReport);
    }

    public void help(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/Borghii/Sales-System"));
        } catch (Exception e) {
            e.printStackTrace();
            setAlert(Alert.AlertType.ERROR,"The URL could not be opened. Check your internet connection.");
        }
    }

    public void exit(ActionEvent actionEvent) {
        openNewStage(MAIN_VIEW_FXML,"Login");
        closeCurrentStage(btnExit);
    }
}