package org.borghisales.salessysten.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import org.borghisales.salessysten.model.Sales;
import org.borghisales.salessysten.model.SalesDAO;
import org.borghisales.salessysten.model.SalesReportGenerator;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

public class ReportsController extends MenuController implements Initializable {

    private final SalesDAO salesDAO = new SalesDAO();

    private static final String[] monthsShowed = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static int idxMonth = LocalDate.now().getMonth().getValue() - 1;
    private static int yearsShowed = LocalDate.now().getYear();

    private static HashMap<Integer, HashMap<Integer, XYChart.Series<String, Integer>>> cacheReportLineChart = new HashMap<>();
    private final ObservableList<String> exportList = FXCollections.observableArrayList(".PDF", ".XLSX", ".CSV");

    private static ObservableList<Sales> sales = null;
    private static ObservableList<PieChart.Data> pieChartData = null;
    private static XYChart.Series<String, Integer> lineChartData = null;

    @FXML private TextField year;
    @FXML public TextField month;

    @FXML private LineChart<String, Integer> salesLineChart;
    @FXML private CategoryAxis x_time;
    @FXML private NumberAxis y_amountSales;
    @FXML private PieChart pieChartProducts;

    @FXML private DatePicker minDate;
    @FXML private DatePicker maxDate;
    @FXML private TextField minAmount;
    @FXML private TextField maxAmount;
    @FXML private ComboBox<String> cbTypeExport;

    @FXML private TableView<Sales> tableReport;
    @FXML private TableColumn<Sales, Integer> colIdSales;
    @FXML private TableColumn<Sales, Integer> colIdCustomer;
    @FXML private TableColumn<Sales, Integer> colIdSeller;
    @FXML private TableColumn<Sales, String> colNumberSales;
    @FXML private TableColumn<Sales, LocalDate> colSaleDate;
    @FXML private TableColumn<Sales, Double> colAmount;
    @FXML private TableColumn<Sales, Sales.State> colState;

    // --- BOTONES ---
    @FXML private Button btnFilter;
    @FXML private Button btnExport;
    @FXML private Button btnSubYear;
    @FXML private Button btnSumYear;
    @FXML private Button btnSubMonth;
    @FXML private Button btnSumMonth;
    @FXML private Button btnReturn;

    public static void removeCacheLineChart(int year, int month) {
        if (cacheReportLineChart != null && cacheReportLineChart.containsKey(year)) {
            HashMap<Integer, XYChart.Series<String, Integer>> yearData = cacheReportLineChart.get(year);
            if (yearData != null && yearData.containsKey(month)) {
                yearData.remove(month);
                lineChartData = null;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupLineChart();
        setupPieChart();
        setupTableView();
        setupEventHandlers();
        setupComboBox();

        UIEfectos.styleButtonUpdate(btnFilter); // Azul
        UIEfectos.styleButtonAdd(btnExport);    // Verde
        UIEfectos.styleButtonReturn(btnReturn); // VOLVER

        UIEfectos.styleButtonGray(btnSubYear);
        UIEfectos.styleButtonGray(btnSumYear);
        UIEfectos.styleButtonGray(btnSubMonth);
        UIEfectos.styleButtonGray(btnSumMonth);
    }

    @FXML
    public void returnToMenu(ActionEvent event) {
        openNewStage(MANAGEMENT_VIEW_FXML, "Management");
        closeCurrentStage(btnReturn);
    }


    private void setupLineChart() {
        x_time.setAutoRanging(true);
        List<String> categories = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            categories.add(String.valueOf(i));
        }
        x_time.setAutoRanging(false);
        x_time.setCategories(FXCollections.observableArrayList(categories));
        x_time.setLabel("Days of the month");
        y_amountSales.setLabel("Sales amount");

        x_time.setTickLabelFill(javafx.scene.paint.Color.WHITE);
        y_amountSales.setTickLabelFill(javafx.scene.paint.Color.WHITE);

        year.setText(String.valueOf(yearsShowed));
        month.setText(monthsShowed[idxMonth]);

        if (lineChartData == null) {
            lineChartData = new XYChart.Series<>();
            salesDAO.setLineChart(lineChartData, Integer.parseInt(year.getText()), idxMonth + 1);
        }

        fillMissingDays(lineChartData);
        salesLineChart.getData().add(lineChartData);
    }

    private void setupPieChart() {
        if (pieChartData == null) {
            pieChartData = FXCollections.observableArrayList();
            salesDAO.setPieChart(pieChartData);

            pieChartData.forEach(data ->
                    data.nameProperty().bind(
                            Bindings.concat(
                                    data.getName(), " amount: ", (int) data.pieValueProperty().doubleValue()
                            )
                    )
            );
        }
        pieChartProducts.getData().addAll(pieChartData);
    }

    private void setupTableView() {
        colIdSales.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idSales()).asObject());
        colIdCustomer.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idCustomer()).asObject());
        colIdSeller.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idSeller()).asObject());
        colNumberSales.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().numberSales()));
        colSaleDate.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().saleDate()));
        colAmount.setCellValueFactory(p -> new SimpleDoubleProperty((p.getValue().amount())).asObject());
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().state()));

        tableReport.getItems().clear();

        if (sales == null) {
            sales = FXCollections.observableArrayList();
            salesDAO.setTable(sales);
        }
        tableReport.setItems(sales);
    }

    private void setupEventHandlers() {
        tableReport.setOnMouseClicked(mouseEvent -> {
            if (!tableReport.getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                int idSales = tableReport.getSelectionModel().getSelectedItem().idSales();
                SaleDetailController.setIdSale(idSales);

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource(MainController.SALE_DETAIL_VIEW_FXML));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setTitle("Sale detail");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void setupComboBox() {
        cbTypeExport.setValue(".PDF");
        cbTypeExport.setItems(exportList);
    }

    public void onFilter(ActionEvent actionEvent) {
        if (sales.isEmpty()){
            MenuController.setAlert(Alert.AlertType.WARNING,"There are no sales");
            return;
        }
        try {
            double minA = minAmount.getText().isEmpty() ? Double.MIN_VALUE: Double.parseDouble(minAmount.getText());
            double maxA = maxAmount.getText().isEmpty() ? Double.MAX_VALUE: Double.parseDouble(maxAmount.getText());
            LocalDate minD = minDate.getValue() == null ? LocalDate.MIN : minDate.getValue();
            LocalDate maxD = maxDate.getValue() == null ? LocalDate.MAX : maxDate.getValue();

            if (minA >= maxA) {
                MenuController.setAlert(Alert.AlertType.ERROR,"Set correct amount intervals");
                return;
            }
            if (minD.isAfter(maxD)) {
                MenuController.setAlert(Alert.AlertType.ERROR,"Set correct date intervals");
                return;
            }

            Predicate<Sales> amountFilter = p -> p.amount() >= minA && p.amount() <= maxA;
            Predicate<Sales> dateFilter = p -> p.saleDate().isAfter(minD) && p.saleDate().isBefore(maxD);

            tableReport.setItems(FXCollections.observableArrayList(sales.stream()
                    .filter(amountFilter)
                    .filter(dateFilter)
                    .toList()));

        } catch (NumberFormatException e) {
            MenuController.setAlert(Alert.AlertType.WARNING,e.getMessage());
        }
    }

    public void onExport(ActionEvent actionEvent) {
        String path = "src/main/resources/reports/sales_report" + cbTypeExport.getValue().toLowerCase();
        switch (cbTypeExport.getValue()){
            case ".PDF"  -> SalesReportGenerator.generatePDFReport(tableReport.getItems(), path);
            case ".XLSX" -> SalesReportGenerator.generateExcelReport(tableReport.getItems(), path);
            case ".CSV" -> SalesReportGenerator.generateCSVReport(tableReport.getItems(), path);
        }
        MenuController.setAlert(Alert.AlertType.INFORMATION, "Reporte guardado en: " + path);
    }

    // --- NAVEGACIÓN GRÁFICOS ---

    public void sumYear(ActionEvent actionEvent) {
        yearsShowed++;
        updateLineChart();
        year.setText(String.valueOf(yearsShowed));
    }

    public void subtractYear(ActionEvent actionEvent) {
        yearsShowed--;
        updateLineChart();
        year.setText(String.valueOf(yearsShowed));
    }

    public void sumMonth(ActionEvent actionEvent) {
        if (idxMonth==11) return;
        idxMonth++;
        updateLineChart();
        month.setText(monthsShowed[idxMonth]);
    }

    public void subtractMonth(ActionEvent actionEvent) {
        if (idxMonth==0) return;
        idxMonth--;
        updateLineChart();
        month.setText(monthsShowed[idxMonth]);
    }

    private void updateLineChart() {
        lineChartData.getData().clear();
        if (checkCache()){
            salesDAO.setLineChart(lineChartData,yearsShowed, idxMonth+1);
        }else {
            lineChartData.getData().addAll(cacheReportLineChart.get(yearsShowed).get(idxMonth+1).getData());
        }
        fillMissingDays(lineChartData);
        salesLineChart.getData().clear();
        salesLineChart.getData().add(lineChartData);
    }

    // --- MÉTODOS AUXILIARES ---

    public static void setSales(ObservableList<Sales> sales) { ReportsController.sales = sales; }
    public static void setPieChartData(ObservableList<PieChart.Data> pieChartData) { ReportsController.pieChartData = pieChartData; }
    public static void setLineChartData(XYChart.Series<String, Integer> lineChartData) { ReportsController.lineChartData = lineChartData; }

    public static void setCacheReportLineChart(int year, int month, XYChart.Series<String, Integer> lineChartData) {
        if (lineChartData != null) {
            if (!cacheReportLineChart.containsKey(year)) {
                cacheReportLineChart.put(year, new HashMap<>());
            }
            HashMap<Integer, XYChart.Series<String, Integer>> yearData = cacheReportLineChart.get(year);
            if (!yearData.containsKey(month)) {
                XYChart.Series<String, Integer> series = new XYChart.Series<>();
                series.getData().addAll(lineChartData.getData());
                series.setName(monthsShowed[idxMonth]);
                yearData.put(month, series);
            }
        }
        recorrerHashMap();
    }

    public static void recorrerHashMap() {
        for (Integer year : cacheReportLineChart.keySet()) {
            System.out.println("Year: " + year);
        }
    }

    private static boolean checkCache() {
        if (cacheReportLineChart.get(yearsShowed) != null) {
            return cacheReportLineChart.get(yearsShowed).get(idxMonth + 1) == null;
        } else {
            return true;
        }
    }

    public void fillMissingDays(XYChart.Series<String, Integer> lineChartData) {
        Map<Integer, Integer> salesByDay = new HashMap<>();
        for (XYChart.Data<String, Integer> data : lineChartData.getData()) {
            int day = Integer.parseInt(data.getXValue());
            int sales = data.getYValue();
            salesByDay.put(day, sales);
        }
        for (int i = 1; i <= 31; i++) {
            if (!salesByDay.containsKey(i)) {
                XYChart.Data<String, Integer> data = new XYChart.Data<>(String.valueOf(i), 0);
                lineChartData.getData().add(data);
            }
        }
        lineChartData.getData().sort(Comparator.comparingInt(data -> Integer.parseInt(data.getXValue())));
    }
}