package org.borghisales.salessysten.controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;

import javafx.collections.ObservableList;

import java.io.IOException;
import java.time.LocalDate;





import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.borghisales.salessysten.model.*;


import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
// Objetivo: Visualización y filtrado de los reportes de ventas.
public class ReportsController implements Initializable {
    // Variables
    private static final String[] mesesMostrados =
            {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    private static int idxMonth = LocalDate.now().getMonth().getValue()-1;
    private static int yearsShowed = LocalDate.now().getYear();


    private static HashMap<Integer,HashMap<Integer,XYChart.Series<String,Integer>>> cacheReportLineChart= new HashMap<>();

    private final ObservableList<String> exportList = FXCollections.observableArrayList(".PDF",".XLSX",".CSV");


    private final SalesDAO salesDAO = new SalesDAO();
    private static ObservableList<Sales> sales = null;
    private static ObservableList<PieChart.Data> pieChartData = null;
    private static XYChart.Series<String,Integer> lineChartData = null;

    @FXML
    private TextField year;
    @FXML
    public TextField month;
    @FXML
    private LineChart<String,Integer> salesLineChart;
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

    public static void removeCacheLineChart(int year, int month) {
        if (cacheReportLineChart != null && cacheReportLineChart.containsKey(year)) {
            HashMap<Integer, XYChart.Series<String, Integer>> yearData = cacheReportLineChart.get(year);
            if (yearData != null && yearData.containsKey(month)) {
                yearData.remove(month);
                lineChartData = null;
            }
        }
    }

    @Override // Configuración de la UI
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupLineChart();
        setupPieChart();
        setupTableView();
        setupEventHandlers();
        setupComboBox();
    }

    private void setupLineChart() {
        x_time.setAutoRanging(true);

        List<String> categories = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            categories.add(String.valueOf(i));
        }
         // X: Días del mes, Y: Cantidad de Ventas
        x_time.setAutoRanging(false);
        x_time.setCategories(FXCollections.observableArrayList(categories));
        x_time.setLabel("Días del mes");
        y_amountSales.setLabel("Cantidad de ventas");

        year.setText(String.valueOf(yearsShowed));
        month.setText(mesesMostrados[idxMonth]);
        // Carga de datos
        if (lineChartData == null) {
            lineChartData = new XYChart.Series<>();
            salesDAO.setLineChart(lineChartData, Integer.parseInt(year.getText()), idxMonth + 1);
        }
        // Añadir 0 a un día del mes sin ventas
        fillMissingDays(lineChartData);
        salesLineChart.getData().add(lineChartData);
    }
    // Configurar gráfica de pastel.
    private void setupPieChart() {
        if (pieChartData == null) {
            pieChartData = FXCollections.observableArrayList();
            ProductDAO.setPieChart(pieChartData);

            pieChartData.forEach(data ->
                    data.nameProperty().bind(
                            Bindings.concat(
                                    data.getName(), " Cantidad: ", (int) data.pieValueProperty().doubleValue()
                            )
                    )
            );
        }
        pieChartProducts.getData().addAll(pieChartData);
    }
    // Mapea los datos del objeto Sales en la tabla.
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
    // Configura el evento del doble clic en la tabla de ventas.
    private void setupEventHandlers() {
        tableReport.setOnMouseClicked(mouseEvent -> {
            if (!tableReport.getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                int idSales = tableReport.getSelectionModel().getSelectedItem().idSales();
                SaleDetailController.setIdSale(idSales);

                FXMLLoader fxmlLoaderSaleDetails = new FXMLLoader(MenuController.class.getResource(MainController.SALE_DETAIL_VIEW_FXML));

                try {
                    Scene scene = new Scene(fxmlLoaderSaleDetails.load());
                    Stage stage = new Stage();
                    stage.setTitle("Detalle de Venta");
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

    // Filtrado
    public void onFilter(ActionEvent actionEvent) {
        if (sales.isEmpty()){
            MenuController.setAlert(Alert.AlertType.WARNING,"No existen ventas.");
            return;
        }

        try {
            double minAmount = this.minAmount.getText().isEmpty() ? Double.MIN_VALUE: Double.parseDouble(this.minAmount.getText());
            double maxAmount = this.maxAmount.getText().isEmpty() ? Double.MAX_VALUE: Double.parseDouble(this.maxAmount.getText());


            LocalDate minDate = this.minDate.getValue() == null ? LocalDate.MIN : this.minDate.getValue();
            LocalDate maxDate = this.maxDate.getValue() == null ? LocalDate.MAX : this.maxDate.getValue();


            if (minAmount >= maxAmount) {
                MenuController.setAlert(Alert.AlertType.ERROR,"Configura los intervalos de cantidad correctamente.");
                return;
            }
            if (minDate.isAfter(maxDate)) {
                MenuController.setAlert(Alert.AlertType.ERROR,"Configura los intervalos de fecha correctamente.");
                return;
            }

            Predicate<Sales> amountFilter = p -> p.amount() >= minAmount && p.amount() <= maxAmount;
            Predicate<Sales> dateFilter = p -> p.saleDate().isAfter(minDate) && p.saleDate().isBefore(maxDate);

            tableReport.setItems(FXCollections.observableArrayList(sales.stream()
                    .filter(amountFilter)
                    .filter(dateFilter)
                    .toList()));

        } catch (NumberFormatException e) {
            MenuController.setAlert(Alert.AlertType.WARNING,e.getMessage());
        }
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


    // Método para agregar los valores al HashMap.
    public static void setCacheReportLineChart(int year, int month, XYChart.Series<String, Integer> lineChartData) {
        if (lineChartData != null) {
            // Verificando si el año ya está en el HashMap.
            if (!cacheReportLineChart.containsKey(year)) {
                cacheReportLineChart.put(year, new HashMap<>());
            }
            //  Dado un año, obtener un mapa de los meses que corresponden.
            HashMap<Integer, XYChart.Series<String, Integer>> yearData = cacheReportLineChart.get(year);
            // Verificar si ya existen series para dicho mes.
            if (!yearData.containsKey(month)) {
                // Agregar series para ese mes.
                XYChart.Series<String, Integer> series = new XYChart.Series<>();
                series.getData().addAll(lineChartData.getData());
                series.setName(mesesMostrados[idxMonth]);
                yearData.put(month, series);
            } else {
                System.out.println("Ya existen series para el año: " + year + " y el mes: " + month + ". No se añadirán nuevas series.");
            }
        } else {
            System.out.println("Error: lineChartData está vacío. No se puede añadir al mapa de caché.");
        }

        recorrerHashMap();
    }

    // Exportar en uno de los formatos disponibles.
    public void onExport(ActionEvent actionEvent) {
        switch (cbTypeExport.getValue()){
            case ".PDF"  -> SalesReportGenerator.generatePDFReport(tableReport.getItems(),"src/main/resources/reports/sales_report.pdf");
            case ".XLSX" -> SalesReportGenerator.generateExcelReport(tableReport.getItems(),"src/main/resources/reports/sales_report.xlsx");
            case ".CSV" -> SalesReportGenerator.generateCSVReport(tableReport.getItems(),"src/main/resources/reports/sales_report.csv");
        }
    }
    // Sumar un año.
    public void sumYear(ActionEvent actionEvent) {
        yearsShowed++;
        lineChartData.getData().clear();

        if (checkCache()){
            salesDAO.setLineChart(lineChartData,yearsShowed, idxMonth+1);
        } else {
            lineChartData.getData().addAll(cacheReportLineChart.get(yearsShowed).get(idxMonth+1).getData());
        }

        fillMissingDays(lineChartData);
        salesLineChart.getData().clear();
        salesLineChart.getData().add(lineChartData);
        year.setText(String.valueOf(yearsShowed));
    }
    // Restar un año.
    public void subtractYear(ActionEvent actionEvent) {
        yearsShowed--;
        lineChartData.getData().clear();

        if (checkCache()){
            salesDAO.setLineChart(lineChartData,yearsShowed, idxMonth+1);
        } else {
            lineChartData.getData().addAll(cacheReportLineChart.get(yearsShowed).get(idxMonth+1).getData());
        }

        fillMissingDays(lineChartData);
        salesLineChart.getData().clear();
        salesLineChart.getData().add(lineChartData);
        year.setText(String.valueOf(yearsShowed));
    }
    // Sumar un mes.
    public void sumMonth(ActionEvent actionEvent) {
        if (idxMonth==11) return;

        idxMonth++;
        lineChartData.getData().clear();

        if (checkCache()){
            salesDAO.setLineChart(lineChartData,Integer.parseInt(year.getText()), idxMonth+1);
        } else {
            lineChartData.getData().addAll(cacheReportLineChart.get(yearsShowed).get(idxMonth+1).getData());
        }

        fillMissingDays(lineChartData);
        salesLineChart.getData().clear();
        salesLineChart.getData().add(lineChartData);
        month.setText(mesesMostrados[idxMonth]);
    }
    // Restar un mes.
    public void subtractMonth(ActionEvent actionEvent) {
        if (idxMonth==0) return;
        idxMonth--;

        salesLineChart.getData().remove(lineChartData);
        lineChartData.getData().clear();

        if (checkCache()){
            salesDAO.setLineChart(lineChartData,Integer.parseInt(year.getText()), idxMonth+1);
        } else {
            lineChartData.getData().addAll(cacheReportLineChart.get(yearsShowed).get(idxMonth+1).getData());
        }

        fillMissingDays(lineChartData);
        salesLineChart.getData().add(lineChartData);
        month.setText(mesesMostrados[idxMonth]);
    }

    // Recorrer el Hash Map.
    public static void recorrerHashMap() {
        for (Integer year : cacheReportLineChart.keySet()) {
            System.out.println("Año: " + year);
            HashMap<Integer, XYChart.Series<String, Integer>> yearData = cacheReportLineChart.get(year);
            for (Integer month : yearData.keySet()) {
                System.out.println("  Mes: " + month);
                XYChart.Series<String, Integer> series = yearData.get(month);
                if (series != null) {
                    System.out.println("    Series: " + series.getName());
                    for (XYChart.Data<String, Integer> data : series.getData()) {
                        System.out.println("      Datos: " + data.getXValue() + ", " + data.getYValue());
                    }
                } else {
                    System.out.println("    No hay una serie asociada para este mes.");
                }
            }
        }
    }
    // Revisar caché.
    private static boolean checkCache() {
        // First, check if the year is not null
        if (cacheReportLineChart.get(yearsShowed) != null) {
            // If the year is not null, then you can check if the data for the month is null or not
            return cacheReportLineChart.get(yearsShowed).get(idxMonth + 1) == null;
        } else {
            // If the year is null, you can return false or any value that is appropriate for your logic
            return true;
        }
    }

    public void fillMissingDays(XYChart.Series<String, Integer> lineChartData) {
        // Creando un mapa para guardar ventas por día.
        Map<Integer, Integer> salesByDay = new HashMap<>();

        // Consiguiendo información de las series existentes y agregándola al mapa.
        for (XYChart.Data<String, Integer> data : lineChartData.getData()) {
            int day = Integer.parseInt(data.getXValue());
            int sales = data.getYValue();
            salesByDay.put(day, sales);
        }

        // Iterar sobre todos los días de un mes y agregar series faltantes.
        for (int i = 1; i <= 31; i++) { // Máximo de 31 días en un mes.
            if (!salesByDay.containsKey(i)) {
                // Si no hay datos, agrega uno con 0.
                XYChart.Data<String, Integer> data = new XYChart.Data<>(String.valueOf(i), 0);
                lineChartData.getData().add(data);
            }
        }

        // Ordenar por día.
        lineChartData.getData().sort(Comparator.comparingInt(data -> Integer.parseInt(data.getXValue())));
    }

}
