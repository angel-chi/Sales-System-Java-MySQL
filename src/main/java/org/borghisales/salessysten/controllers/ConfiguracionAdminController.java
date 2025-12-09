package org.borghisales.salessysten.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.borghisales.salessysten.model.CodigoDescuento;
import org.borghisales.salessysten.model.CodigoDescuentoDAO;
import org.borghisales.salessysten.model.ConfiguracionDAO;

import java.net.URL;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConfiguracionAdminController extends MenuController implements Initializable {

    // IVA Tab
    @FXML
    private TextField campoPorcentajeIva;

    // Discount Tab
    @FXML
    private TableView<CodigoDescuento> tablaDescuentos;
    @FXML
    private TableColumn<CodigoDescuento, String> columnaCodigo;
    @FXML
    private TableColumn<CodigoDescuento, Double> columnaPorcentaje;
    @FXML
    private TableColumn<CodigoDescuento, CodigoDescuento.Estado> columnaEstado;
    @FXML
    private TableColumn<CodigoDescuento, LocalDate> columnaExpiracion;
    @FXML
    private TextField campoCodigo;
    @FXML
    private TextField campoPorcentaje;
    @FXML
    private RadioButton radioActivo;
    @FXML
    private RadioButton radioInactivo;
    @FXML
    private ToggleGroup grupoEstado;
    @FXML
    private DatePicker pickerFechaExpiracion;

    private final ConfiguracionDAO configuracionDAO = new ConfiguracionDAO();
    private final CodigoDescuentoDAO codigoDescuentoDAO = new CodigoDescuentoDAO();

    private ObservableList<CodigoDescuento> listaDescuentos;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // IVA Tab
        cargarPorcentajeIva();

        // Discount Tab
        configurarTablaDescuentos();
        cargarDescuentos();
    }

    // IVA Logic
    private void cargarPorcentajeIva() {
        try {
            String ivaStr = configuracionDAO.getValor("IVA");
            if (ivaStr != null) {
                campoPorcentajeIva.setText(String.format(Locale.US, "%.2f", Double.parseDouble(ivaStr) * 100));
            } else {
                campoPorcentajeIva.setText("0.00");
            }
        } catch (NumberFormatException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al cargar el IVA: formato inválido.");
            campoPorcentajeIva.setText("0.00");
        }
    }

    @FXML
    public void guardarIva(ActionEvent actionEvent) {
        try {
            double ivaPercentage = Double.parseDouble(campoPorcentajeIva.getText()) / 100;
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

    // Discount Logic
    private void configurarTablaDescuentos() {
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        columnaPorcentaje.setCellValueFactory(new PropertyValueFactory<>("porcentaje"));
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        columnaExpiracion.setCellValueFactory(new PropertyValueFactory<>("fechaExpiracion"));
        listaDescuentos = FXCollections.observableArrayList();
        tablaDescuentos.setItems(listaDescuentos);
    }

    private void cargarDescuentos() {
        listaDescuentos.setAll(codigoDescuentoDAO.obtenerTodosLosCodigos());
    }

    @FXML
    public void agregarDescuento(ActionEvent actionEvent) {
        CodigoDescuento nuevoDescuento = new CodigoDescuento(
                0,
                campoCodigo.getText(),
                Double.parseDouble(campoPorcentaje.getText()),
                radioActivo.isSelected() ? CodigoDescuento.Estado.ACTIVO : CodigoDescuento.Estado.INACTIVO,
                pickerFechaExpiracion.getValue()
        );
        if (codigoDescuentoDAO.guardarCodigo(nuevoDescuento)) {
            MenuController.setAlert(Alert.AlertType.INFORMATION, "Código de descuento agregado exitosamente.");
            cargarDescuentos();
            limpiarCampos();
        }
    }

    @FXML
    public void actualizarDescuento(ActionEvent actionEvent) {
        CodigoDescuento seleccionado = tablaDescuentos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            MenuController.setAlert(Alert.AlertType.WARNING, "Por favor, seleccione un código de descuento de la tabla.");
            return;
        }

        seleccionado.setCodigo(campoCodigo.getText());
        seleccionado.setPorcentaje(Double.parseDouble(campoPorcentaje.getText()));
        seleccionado.setEstado(radioActivo.isSelected() ? CodigoDescuento.Estado.ACTIVO : CodigoDescuento.Estado.INACTIVO);
        seleccionado.setFechaExpiracion(pickerFechaExpiracion.getValue());

        if (codigoDescuentoDAO.actualizarCodigo(seleccionado)) {
            MenuController.setAlert(Alert.AlertType.INFORMATION, "Código de descuento actualizado exitosamente.");
            cargarDescuentos();
            limpiarCampos();
        }
    }

    @FXML
    public void eliminarDescuento(ActionEvent actionEvent) {
        CodigoDescuento seleccionado = tablaDescuentos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            MenuController.setAlert(Alert.AlertType.WARNING, "Por favor, seleccione un código de descuento de la tabla.");
            return;
        }

        if (codigoDescuentoDAO.eliminarCodigo(seleccionado.getId())) {
            MenuController.setAlert(Alert.AlertType.INFORMATION, "Código de descuento eliminado exitosamente.");
            cargarDescuentos();
            limpiarCampos();
        }
    }

    @FXML
    public void limpiarCampos() {
        campoCodigo.clear();
        campoPorcentaje.clear();
        radioActivo.setSelected(true);
        pickerFechaExpiracion.setValue(null);
        tablaDescuentos.getSelectionModel().clearSelection();
    }
    
    @FXML
    public void volverAtras(ActionEvent actionEvent) {
        openNewStage(MANAGEMENT_VIEW_FXML, "Management");
        closeCurrentStage(campoPorcentajeIva);
    }
}
