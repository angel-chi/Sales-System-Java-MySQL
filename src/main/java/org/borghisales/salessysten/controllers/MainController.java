package org.borghisales.salessysten.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.borghisales.salessysten.model.Seller;
import org.borghisales.salessysten.model.SellerDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends MenuController implements Initializable {

    // Componentes FXML
    @FXML private TextField user;
    @FXML private PasswordField password;
    @FXML private TextField passwordText;
    @FXML private VBox loginContainer;
    @FXML private Button btnLogin;

    // Componentes para el Ojo
    @FXML private ToggleButton btnTogglePass;
    @FXML private ImageView imgEye;

    public static Seller sellerLog;

    private final String PATH_ojoAbierto = "/images/ojoAbiertoDos.png";
    private final String PATH_ojoCerrado = "/images/ojoCerraDos.png";

    // Estilos del botón
    private final String STYLE_NORMAL = "-fx-background-color: #3a3a3a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #555; -fx-border-radius: 8;";
    private final String STYLE_HOVER = "-fx-background-color: #505050; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: white; -fx-border-radius: 8;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ReportsController.setSales(null);
        ReportsController.setPieChartData(null);
        ReportsController.setLineChartData(null);

        setupButtonHover();

        setupPasswordVisibility();
        playEntryAnimation();
    }

    private void setupButtonHover() {
        btnLogin.setCursor(Cursor.HAND);

        btnLogin.setOnMouseEntered(e -> {
            btnLogin.setStyle(STYLE_HOVER);
            btnLogin.setScaleX(1.05);
            btnLogin.setScaleY(1.05);
        });

        btnLogin.setOnMouseExited(e -> {
            btnLogin.setStyle(STYLE_NORMAL);
            btnLogin.setScaleX(1.0);
            btnLogin.setScaleY(1.0);
        });
    }

    private void playEntryAnimation() {
        FadeTransition fade = new FadeTransition(Duration.millis(1000), loginContainer);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition translate = new TranslateTransition(Duration.millis(1000), loginContainer);
        translate.setFromY(30);
        translate.setToY(0);

        fade.play();
        translate.play();
    }

    private void setupPasswordVisibility() {
        passwordText.textProperty().bindBidirectional(password.textProperty());

        password.setVisible(true);
        password.setManaged(true);
        passwordText.setVisible(false);
        passwordText.setManaged(false);
    }

    @FXML
    private void togglePasswordVisibility() {
        if (btnTogglePass.isSelected()) {
            passwordText.setVisible(true);
            passwordText.setManaged(true);
            password.setVisible(false);
            password.setManaged(false);

            updateEyeIcon(PATH_ojoAbierto);
        } else {
            passwordText.setVisible(false);
            passwordText.setManaged(false);
            password.setVisible(true);
            password.setManaged(true);

            updateEyeIcon(PATH_ojoCerrado);
        }
    }

    private void updateEyeIcon(String path) {
        try {
            // Carga la imagen desde recursos
            imgEye.setImage(new Image(getClass().getResourceAsStream(path)));
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen del ojo: " + path);
        }
    }

    @FXML
    private void signIn(){
        if (SellerDAO.login(user.getText(), password.getText())) {
            openNewStage(MANAGEMENT_VIEW_FXML, "Management");
            closeCurrentStage(user);
        } else {
            shakeTextField(user);

            if (passwordText.isVisible()) {
                shakeTextField(passwordText);
            } else {
                shakeTextField(password);
            }
        }
    }

    private void shakeTextField(javafx.scene.Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(60), node);
        tt.setByX(6f);
        tt.setCycleCount(4);
        tt.setAutoReverse(true);
        tt.play();
    }
}