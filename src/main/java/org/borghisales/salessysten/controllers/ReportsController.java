package org.borghisales.salessysten.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.borghisales.salessysten.model.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.function.Predicate;

public class ReportsController implements Initializable {

    private static final String[] monthsShowed = {
            "Enero","Febrero","Marzo","Abril","Mayo","Junio",
            "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"
    };
    private static int idxMonth = LocalDate.now().getMonthValue() - 1;
    private static int yearsShowed = LocalDate.now().getYear();

    private static final HashMap<Integer, HashMap<Integer, XYChart.Series<String, Integer>>> cacheReportLineChart = new HashMap<>();

    private final ObservableList<String> exportList = FXCollections.observableArrayList(".PDF", ".XLSX", ".CSV");

    private final SalesDAO salesDAO = new SalesDAO();
    private static ObservableList<Sales> sales = null;
    private static ObservableList<PieChart.Data> pieChartData = null;
    private static XYChart.Series<String, Integer> lineChartData = null;

    @FXML
    private TextField year;
    @FXML
    private TextField month;
    @FXML
    private LineChart<String, Integer> salesLineChart;
    @FXML
    private CategoryAxis x_time;
    @FXML
    private NumberAxis y_amountSales;
    @FXML
    private PieChart pieChartProducts;
    @FXML
    private DatePicker minDate;
    @FXML
    private DatePicker maxDate;
    @FXML
    private TextField minAmount;
    @FXML
    private TextField maxAmount;
    @FXML
    private ComboBox<String> cbTypeExport;
    @FXML
    private TableView<Sales> tableReport;
    @FXML
    private TableColumn<Sales,Integer> colIdSales;
    @FXML
    private TableColumn<Sales,Integer> colIdCustomer;
    @FXML
    private TableColumn<Sales,Integer> colIdSeller;
    @FXML
    private TableColumn<Sales,String> colNumberSales;
    @FXML
    private TableColumn<Sales, LocalDate> colSaleDate;
    @FXML
    private TableColumn<Sales,Double> colAmount;
    @FXML
    private TableColumn<Sales, Sales.State> colState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupLineChart();
        setupPieChart();
        setupTableView();
        setupEventHandlers();
        setupComboBox();
    }

    private void setupLineChart() {
        x_time.setAutoRanging(false);
        x_time.setLabel("Días del Mes");
        y_amountSales.setLabel("Monto de Ventas");

        year.setText(String.valueOf(yearsShowed));
        month.setText(monthsShowed[idxMonth]);

        if (lineChartData == null) {
            lineChartData = new XYChart.Series<>();
            lineChartData.setName(monthsShowed[idxMonth]);
            salesDAO.setLineChart(lineChartData, yearsShowed, idxMonth + 1);
        }

        fillMissingDays(lineChartData);
        salesLineChart.getData().clear();
        salesLineChart.getData().add(lineChartData);
    }

    private void setupPieChart() {
        if (pieChartData == null) {
            pieChartData = FXCollections.observableArrayList();
            ProductDAO.setPieChart(pieChartData);

            pieChartData.forEach(data ->
                    data.nameProperty().bind(
                            Bindings.concat(data.getName(), " cantidad: ", (int) data.pieValueProperty().doubleValue())
                    )
            );
        }
        pieChartProducts.getData().clear();
        pieChartProducts.getData().addAll(pieChartData);
    }

    private void setupTableView() {
        colIdSales.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idSales()).asObject());
        colIdCustomer.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idCustomer()).asObject());
        colIdSeller.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idSeller()).asObject());
        colNumberSales.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().numberSales()));
        colSaleDate.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().saleDate()));
        colAmount.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().amount()).asObject());
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().state()));

        if (sales == null) {
            sales = FXCollections.observableArrayList();
            salesDAO.setTable(sales);
        }

        tableReport.setItems(sales);
    }

    private void setupEventHandlers() {
        tableReport.setOnMouseClicked(mouseEvent -> {
            if (!tableReport.getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                Sales selectedSale = tableReport.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Acción requerida");
                alert.setHeaderText("¿Qué deseas hacer con la venta seleccionada?");
                alert.setContentText("Número de venta: " + selectedSale.numberSales());

                ButtonType btnDetalles = new ButtonType("Ver detalles");
                ButtonType btnEliminar = new ButtonType("Eliminar");
                ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(btnDetalles, btnEliminar, btnCancelar);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isEmpty()) return;

                if (result.get() == btnDetalles) {
                    openSaleDetails(selectedSale.idSales());
                } else if (result.get() == btnEliminar) {
                    deleteSale(selectedSale);
                }
            }
        });
    }

    private void deleteSale(Sales sale) {
        Alert confirm = new Alert(Alert.AlertType.WARNING);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText("¿Seguro que deseas eliminar esta venta?");
        confirm.setContentText("Esta acción no se puede deshacer.");

        ButtonType btnYes = new ButtonType("Eliminar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnNo = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirm.getButtonTypes().setAll(btnYes, btnNo);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == btnYes) {
            boolean success = salesDAO.deleteSale(sale.idSales());
            if (success) {
                sales.remove(sale);
                tableReport.refresh();
                MenuController.setAlert(Alert.AlertType.INFORMATION, "Venta eliminada correctamente.");
            } else {
                MenuController.setAlert(Alert.AlertType.ERROR, "No se pudo eliminar la venta.");
            }
        }
    }

    private void openSaleDetails(int idSales) {
        try {
            FXMLLoader loader = new FXMLLoader(MenuController.class.getResource(MainController.SALE_DETAIL_VIEW_FXML));
            Scene scene = new Scene(loader.load());

            SaleDetailController controller = loader.getController();
            controller.setIdSale(idSales);
            controller.loadData();

            Stage stage = new Stage();
            stage.setTitle("Detalles de venta");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            MenuController.setAlert(Alert.AlertType.ERROR, "No se pudo abrir los detalles de la venta.");
        }
    }

    private void setupComboBox() {
        cbTypeExport.setItems(exportList);
        cbTypeExport.setValue(".PDF");
    }

    public void onFilter(ActionEvent actionEvent) {
        if (sales.isEmpty()) {
            MenuController.setAlert(Alert.AlertType.WARNING, "Ventas no existentes");
            return;
        }

        try {
            double min = minAmount.getText().isEmpty() ? Double.MIN_VALUE : Double.parseDouble(minAmount.getText());
            double max = maxAmount.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxAmount.getText());

            LocalDate minD = minDate.getValue() == null ? LocalDate.MIN : minDate.getValue();
            LocalDate maxD = maxDate.getValue() == null ? LocalDate.MAX : maxDate.getValue();

            if (min >= max) {
                MenuController.setAlert(Alert.AlertType.ERROR, "Establece los intervalos de cantidad de manera correcta");
                return;
            }
            if (minD.isAfter(maxD)) {
                MenuController.setAlert(Alert.AlertType.ERROR, "Establece los intervalos de fecha de manera correcta");
                return;
            }

            Predicate<Sales> amountFilter = p -> p.amount() >= min && p.amount() <= max;
            Predicate<Sales> dateFilter = p -> !p.saleDate().isBefore(minD) && !p.saleDate().isAfter(maxD);

            tableReport.setItems(FXCollections.observableArrayList(
                    sales.stream().filter(amountFilter).filter(dateFilter).toList()
            ));

        } catch (NumberFormatException e) {
            MenuController.setAlert(Alert.AlertType.WARNING, e.getMessage());
        }
    }
    public static void removeCacheLineChart(int year, int month) {
        if (cacheReportLineChart.containsKey(year)) {
            HashMap<Integer, XYChart.Series<String, Integer>> yearData = cacheReportLineChart.get(year);
            if (yearData != null) {
                yearData.remove(month);
            }
        }
        lineChartData = null; // Por si se desea resetear de manera global es útil
    }

    public static void setSales(ObservableList<Sales> sales) {
        ReportsController.sales = sales;
    }

    public static void setPieChartData(ObservableList<PieChart.Data> pieChartData) {
        ReportsController.pieChartData = pieChartData;
    }

    public static void setLineChartData(XYChart.Series<String, Integer> lineChartData) {
        ReportsController.lineChartData = lineChartData;
    }

    public void onExport(ActionEvent actionEvent) {
        switch (cbTypeExport.getValue()) {
            case ".PDF" -> SalesReportGenerator.generatePDFReport(tableReport.getItems(), "src/main/resources/reports/sales_report.pdf");
            case ".XLSX" -> SalesReportGenerator.generateExcelReport(tableReport.getItems(), "src/main/resources/reports/sales_report.xlsx");
            case ".CSV" -> SalesReportGenerator.generateCSVReport(tableReport.getItems(), "src/main/resources/reports/sales_report.csv");
        }
    }

    // --- Navegación de meses y años ---

    public void sumYear(ActionEvent actionEvent) { changeYear(1); }
    public void subtractYear(ActionEvent actionEvent) { changeYear(-1); }
    public void sumMonth(ActionEvent actionEvent) { changeMonth(1); }
    public void subtractMonth(ActionEvent actionEvent) { changeMonth(-1); }

    private void changeYear(int delta) {
        yearsShowed += delta;
        updateLineChart();
        year.setText(String.valueOf(yearsShowed));
    }

    private void changeMonth(int delta) {
        idxMonth += delta;
        if (idxMonth < 0) idxMonth = 0;
        if (idxMonth > 11) idxMonth = 11;
        updateLineChart();
        month.setText(monthsShowed[idxMonth]);
    }

    private void updateLineChart() {
        lineChartData.getData().clear();

        XYChart.Series<String, Integer> cached = getCachedSeries(yearsShowed, idxMonth + 1);
        if (cached != null) {
            lineChartData.getData().addAll(cached.getData());
        } else {
            salesDAO.setLineChart(lineChartData, yearsShowed, idxMonth + 1);
            addCacheSeries(yearsShowed, idxMonth + 1, lineChartData);
        }

        fillMissingDays(lineChartData);
        salesLineChart.getData().clear();
        salesLineChart.getData().add(lineChartData);
    }

    private XYChart.Series<String, Integer> getCachedSeries(int year, int month) {
        if (cacheReportLineChart.containsKey(year)) {
            return cacheReportLineChart.get(year).get(month);
        }
        return null;
    }

    private void addCacheSeries(int year, int month, XYChart.Series<String, Integer> series) {
        cacheReportLineChart.putIfAbsent(year, new HashMap<>());
        XYChart.Series<String, Integer> copy = new XYChart.Series<>();
        copy.getData().addAll(series.getData());
        copy.setName(monthsShowed[month - 1]);
        cacheReportLineChart.get(year).put(month, copy);
    }

    public void fillMissingDays(XYChart.Series<String, Integer> series) {
        Map<Integer, Integer> salesByDay = new HashMap<>();
        for (XYChart.Data<String, Integer> d : series.getData()) {
            salesByDay.put(Integer.parseInt(d.getXValue()), d.getYValue());
        }

        int daysInMonth = Month.of(idxMonth + 1).length(Year.isLeap(yearsShowed));
        for (int i = 1; i <= daysInMonth; i++) {
            if (!salesByDay.containsKey(i)) {
                series.getData().add(new XYChart.Data<>(String.valueOf(i), 0));
            }
        }

        series.getData().sort(Comparator.comparingInt(d -> Integer.parseInt(d.getXValue())));
    }
}
