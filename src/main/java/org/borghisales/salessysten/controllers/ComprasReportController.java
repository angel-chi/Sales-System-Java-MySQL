package org.borghisales.salessysten.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import org.borghisales.salessysten.model.dao.ComprasDAO;
import org.borghisales.salessysten.model.entities.Compras;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ComprasReportController extends MenuController implements Initializable {

    // DAO
    private final ComprasDAO comprasDAO = new ComprasDAO();

    // Filtros
    @FXML private TextField minAmount;
    @FXML private TextField maxAmount;
    @FXML private DatePicker minDate;
    @FXML private DatePicker maxDate;
    @FXML private ComboBox<String> cbTypeExport;

    // Tabla
    @FXML private TableView<Compras> tableReport;
    @FXML private TableColumn<Compras, Integer> colIdCompra;
    @FXML private TableColumn<Compras, Integer> colIdProveedor;
    @FXML private TableColumn<Compras, String> colNombreProveedor;
    @FXML private TableColumn<Compras, Integer> colIdVendedor;
    @FXML private TableColumn<Compras, String> colFechaCompra;
    @FXML private TableColumn<Compras, Double> colMonto;
    @FXML private TableColumn<Compras, String> colEstado;
    @FXML private TableColumn<Compras, String> colAcciones;

    // Gráficas
    @FXML private PieChart pieChartProveedores;
    @FXML private LineChart<String, Number> comprasLineChart;
    @FXML private CategoryAxis x_time;
    @FXML private NumberAxis y_amountCompras;

    // Controles de fecha
    @FXML private TextField year;
    @FXML private TextField month;

    private ObservableList<Compras> compras = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTable();
        configureComboBox();
        loadCompras();
        initializeCharts();
        tableReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configureTable() {
        colIdCompra.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().idCompra()).asObject());
        colIdProveedor.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().idProveedor()).asObject());
        colNombreProveedor.setCellValueFactory(c -> new SimpleStringProperty("Proveedor " + c.getValue().idProveedor()));
        colIdVendedor.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().idVendedor()).asObject());
        colFechaCompra.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().fechaCompra().toString()));
        colMonto.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().subtotal()).asObject());
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().estado().toString()));

        // Columna de acciones con botón "Ver Detalle"
        colAcciones.setCellFactory(col -> new TableCell<Compras, String>() {
            private final Button btnDetalle = new Button("Ver Detalle");

            {
                btnDetalle.setOnAction(e -> {
                    Compras compra = getTableView().getItems().get(getIndex());
                    verDetalle(compra);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDetalle);
                }
            }
        });
    }

    private void configureComboBox() {
        ObservableList<String> exportFormats = FXCollections.observableArrayList("PDF", "Excel", "CSV");
        cbTypeExport.setItems(exportFormats);
        cbTypeExport.setValue("PDF");
    }

    private void loadCompras() {
        compras.clear();
        comprasDAO.setTable(compras);
        tableReport.setItems(compras);
    }

    private void initializeCharts() {
        // Inicializar año y mes actuales
        LocalDate now = LocalDate.now();
        year.setText(String.valueOf(now.getYear()));
        month.setText(String.valueOf(now.getMonthValue()));

        loadPieChartData();
        loadLineChartData();
    }

    private void loadPieChartData() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Proveedor A", 91),
                new PieChart.Data("Proveedor B", 3),
                new PieChart.Data("Proveedor C", 3),
                new PieChart.Data("Otros", 3)
        );
        pieChartProveedores.setData(pieData);
    }

    private void loadLineChartData() {
    }

    @FXML
    private void onFilter(ActionEvent e) {
        // Implementar filtrado por fecha y monto
        setAlert(Alert.AlertType.INFORMATION, "Filtros aplicados");
    }

    @FXML
    private void onExport(ActionEvent e) {
        String format = cbTypeExport.getValue();
        // Implementar exportación según formato
        setAlert(Alert.AlertType.INFORMATION, "Exportando a " + format + "...");
    }

    @FXML
    private void sumYear(ActionEvent e) {
        int currentYear = Integer.parseInt(year.getText());
        year.setText(String.valueOf(currentYear + 1));
        loadLineChartData();
    }

    @FXML
    private void subtractYear(ActionEvent e) {
        int currentYear = Integer.parseInt(year.getText());
        year.setText(String.valueOf(currentYear - 1));
        loadLineChartData();
    }

    @FXML
    private void sumMonth(ActionEvent e) {
        int currentMonth = Integer.parseInt(month.getText());
        if (currentMonth < 12) {
            month.setText(String.valueOf(currentMonth + 1));
        } else {
            month.setText("1");
            sumYear(e);
        }
        loadLineChartData();
    }

    @FXML
    private void subtractMonth(ActionEvent e) {
        int currentMonth = Integer.parseInt(month.getText());
        if (currentMonth > 1) {
            month.setText(String.valueOf(currentMonth - 1));
        } else {
            month.setText("12");
            subtractYear(e);
        }
        loadLineChartData();
    }

    private void verDetalle(Compras compra) {
        // Abrir ventana con detalle de la compra
        setAlert(Alert.AlertType.INFORMATION, "Ver detalle de compra ID: " + compra.idCompra());
    }
}