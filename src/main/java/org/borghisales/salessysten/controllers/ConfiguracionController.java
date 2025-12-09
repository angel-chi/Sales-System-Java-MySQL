package org.borghisales.salessysten.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.borghisales.salessysten.model.ConfiguracionDAO;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConfiguracionController extends MenuController implements Initializable {

    @FXML
    private TextField ivaPercentageField;

    private final ConfiguracionDAO configuracionDAO = new ConfiguracionDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadIvaPercentage();
    }

    private void loadIvaPercentage() {
        try {
            String ivaStr = configuracionDAO.getValor("IVA");
            if (ivaStr != null) {
                ivaPercentageField.setText(String.format(Locale.US, "%.2f", Double.parseDouble(ivaStr) * 100));
            } else {
                ivaPercentageField.setText("0.00");
            }
        } catch (NumberFormatException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al cargar el IVA: formato inválido.");
            ivaPercentageField.setText("0.00");
        }
    }

    @FXML
    public void saveIvaPercentage(ActionEvent actionEvent) {
        try {
            double ivaPercentage = Double.parseDouble(ivaPercentageField.getText()) / 100;
            if (ivaPercentage < 0) {
                MenuController.setAlert(Alert.AlertType.WARNING, "El porcentaje de IVA no puede ser negativo.");
                return;
            }
            configuracionDAO.updateValor("IVA", String.valueOf(ivaPercentage));
            MenuController.setAlert(Alert.AlertType.INFORMATION, "Porcentaje de IVA guardado exitosamente.");
        } catch (NumberFormatException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Formato de IVA inválido. Por favor, introduzca un número válido.");
        }
    }

    @FXML
    public void backToManagement(ActionEvent actionEvent) {
        openNewStage(MANAGEMENT_VIEW_FXML, "Management");
        closeCurrentStage(ivaPercentageField);
    }
}
