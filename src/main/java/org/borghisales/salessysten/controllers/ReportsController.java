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
import org.borghisales.salessysten.model.*; // Importación general para nuevas clases
import org.borghisales.salessysten.model.Venta; // Sales -> Venta
import org.borghisales.salessysten.model.VentaDAO; // SalesDAO -> VentaDAO
import org.borghisales.salessysten.model.ProductoDAO; // ProductDAO -> ProductoDAO
import org.borghisales.salessysten.model.GeneradorReportesVentas; // SalesReportGenerator -> GeneradorReportesVentas


import java.net.URL;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;

public class ReportsController extends MenuController implements Initializable {

    private static final String[] mesesMostrados = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"}; // monthsShowed -> mesesMostrados (traducido)
    private static int idxMes = LocalDate.now().getMonth().getValue()-1; // idxMonth -> idxMes
    private static int añosMostrados = LocalDate.now().getYear(); // yearsShowed -> añosMostrados


    private static HashMap<Integer,HashMap<Integer,XYChart.Series<String,Integer>>> cacheReporteGraficoLinea= new HashMap<>(); // cacheReportLineChart -> cacheReporteGraficoLinea

    private final ObservableList<String> exportList = FXCollections.observableArrayList(".PDF",".XLSX",".CSV");


    private final VentaDAO ventaDAO = new VentaDAO(); // SalesDAO -> VentaDAO
    private static ObservableList<Venta> ventas = null; // Sales -> Venta, sales -> ventas
    private static ObservableList<PieChart.Data> datosGraficoCircular = null; // pieChartData -> datosGraficoCircular
    private static XYChart.Series<String,Integer> datosGraficoLinea = null; // lineChartData -> datosGraficoLinea

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
    private TableView<Venta> tableReport; // Sales -> Venta
    @FXML
    private TableColumn<Venta,Integer> colIdSales; // Sales -> Venta
    @FXML
    private TableColumn<Venta,Integer> colIdCustomer; // Sales -> Venta
    @FXML
    private TableColumn<Venta,Integer> colIdSeller; // Sales -> Venta
    @FXML
    private TableColumn<Venta,String> colNumberSales; // Sales -> Venta
    @FXML
    private TableColumn<Venta, LocalDate> colSaleDate; // Sales -> Venta
    @FXML
    private TableColumn<Venta,Double> colAmount; // Sales -> Venta
    @FXML
    private TableColumn<Venta, Venta.Estado> colState; // Sales.State -> Venta.Estado
    @FXML
    private TableColumn<Venta, Venta.TipoPago> colTipoPago;
    @FXML
    private TableColumn<Venta, Venta.EntregaTicket> colEntregaTicket;

    public static void eliminarCacheGraficoLinea(int year, int month) { // Nombre de método y parámetros
        if (cacheReporteGraficoLinea != null && cacheReporteGraficoLinea.containsKey(year)) { // cacheReportLineChart -> cacheReporteGraficoLinea
            HashMap<Integer, XYChart.Series<String, Integer>> yearData = cacheReporteGraficoLinea.get(year); // cacheReportLineChart -> cacheReporteGraficoLinea
            if (yearData != null && yearData.containsKey(month)) {
                yearData.remove(month);
                datosGraficoLinea = null; // lineChartData -> datosGraficoLinea
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
    }

    private void setupLineChart() {
        x_time.setAutoRanging(true);

        List<String> categories = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            categories.add(String.valueOf(i));
        }

        x_time.setAutoRanging(false);
        x_time.setCategories(FXCollections.observableArrayList(categories));
        x_time.setLabel("Días del mes"); // Traducido
        y_amountSales.setLabel("Monto de ventas"); // Traducido

        year.setText(String.valueOf(añosMostrados)); // yearsShowed -> añosMostrados
        month.setText(mesesMostrados[idxMes]); // monthsShowed -> mesesMostrados, idxMonth -> idxMes

        if (datosGraficoLinea == null) { // lineChartData -> datosGraficoLinea
            datosGraficoLinea = new XYChart.Series<>(); // lineChartData -> datosGraficoLinea
            ventaDAO.setLineChart(datosGraficoLinea, Integer.parseInt(year.getText()), idxMes + 1); // salesDAO -> ventaDAO, lineChartData -> datosGraficoLinea, idxMonth -> idxMes
        }

        fillMissingDays(datosGraficoLinea); // lineChartData -> datosGraficoLinea
        salesLineChart.getData().add(datosGraficoLinea); // lineChartData -> datosGraficoLinea
    }

    private void setupPieChart() {
        if (datosGraficoCircular == null) { // pieChartData -> datosGraficoCircular
            datosGraficoCircular = FXCollections.observableArrayList(); // pieChartData -> datosGraficoCircular
            ProductoDAO.setPieChart(datosGraficoCircular); // ProductDAO -> ProductoDAO, pieChartData -> datosGraficoCircular

            datosGraficoCircular.forEach(data -> // pieChartData -> datosGraficoCircular
                    data.nameProperty().bind(
                            Bindings.concat(
                                    data.getName(), " cantidad: ", (int) data.pieValueProperty().doubleValue() // " amount: " -> " cantidad: " (traducido)
                            )
                    )
            );
        }
        pieChartProducts.getData().addAll(datosGraficoCircular); // pieChartData -> datosGraficoCircular
    }

    private void setupTableView() {
        colIdSales.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idVenta()).asObject()); // idSales() -> idVenta()
        colIdCustomer.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idCliente()).asObject()); // idCustomer() -> idCliente()
        colIdSeller.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idVendedor()).asObject()); // idSeller() -> idVendedor()
        colNumberSales.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().numeroDeVenta())); // numberSales() -> numeroDeVenta()
        colSaleDate.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().fechaDeVenta())); // saleDate() -> fechaDeVenta()
        colAmount.setCellValueFactory(p -> new SimpleDoubleProperty((p.getValue().total())).asObject()); // amount() -> total()
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().estado())); // state() -> estado()
        colTipoPago.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().tipo_pago()));
        colEntregaTicket.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().entrega_ticket()));

        tableReport.getItems().clear();

        if (ventas == null) { // sales -> ventas
            ventas = FXCollections.observableArrayList(); // sales -> ventas
            ventaDAO.setTable(ventas); // salesDAO -> ventaDAO, sales -> ventas
        }

        tableReport.setItems(ventas); // sales -> ventas
    }


    private void setupEventHandlers() {
        tableReport.setOnMouseClicked(mouseEvent -> {
            if (!tableReport.getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                int idVenta = tableReport.getSelectionModel().getSelectedItem().idVenta(); // idSales -> idVenta, idSales() -> idVenta()
                SaleDetailController.setIdVenta(idVenta); // SaleDetailController.setIdSale -> SaleDetailController.setIdVenta

                FXMLLoader fxmlLoaderSaleDetails = new FXMLLoader(MenuController.class.getResource(MainController.SALE_DETAIL_VIEW_FXML));

                try {
                    Scene scene = new Scene(fxmlLoaderSaleDetails.load());
                    Stage stage = new Stage();
                    stage.setTitle("Detalle de Venta"); // Título traducido
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
        if (ventas.isEmpty()){ // sales -> ventas
            MenuController.setAlert(Alert.AlertType.WARNING,"No hay ventas para filtrar"); // Mensaje traducido
            return;
        }

        try {
            double minAmount = this.minAmount.getText().isEmpty() ? Double.MIN_VALUE: Double.parseDouble(this.minAmount.getText());
            double maxAmount = this.maxAmount.getText().isEmpty() ? Double.MAX_VALUE: Double.parseDouble(this.maxAmount.getText());


            LocalDate minDate = this.minDate.getValue() == null ? LocalDate.MIN : this.minDate.getValue();
            LocalDate maxDate = this.maxDate.getValue() == null ? LocalDate.MAX : this.maxDate.getValue();


            if (minAmount >= maxAmount) {
                MenuController.setAlert(Alert.AlertType.ERROR,"Establece intervalos de monto correctos"); // Mensaje traducido
                return;
            }
            if (minDate.isAfter(maxDate)) {
                MenuController.setAlert(Alert.AlertType.ERROR,"Establece intervalos de fecha correctos"); // Mensaje traducido
                return;
            }

            Predicate<Venta> amountFilter = p -> p.total() >= minAmount && p.total() <= maxAmount; // Sales -> Venta, amount() -> total()
            Predicate<Venta> dateFilter = p -> p.fechaDeVenta().isAfter(minDate) && p.fechaDeVenta().isBefore(maxDate); // Sales -> Venta, saleDate() -> fechaDeVenta()

            tableReport.setItems(FXCollections.observableArrayList(ventas.stream() // sales -> ventas
                    .filter(amountFilter)
                    .filter(dateFilter)
                    .toList()));

        } catch (NumberFormatException e) {
            MenuController.setAlert(Alert.AlertType.WARNING,e.getMessage());
        }
    }

    public static void setVentas(ObservableList<Venta> listaVentas) { // setSales -> setVentas, sales -> listaVentas
        ReportsController.ventas = listaVentas; // sales -> ventas
    }

    public static void setDatosGraficoCircular(ObservableList<PieChart.Data> datos) { // setPieChartData -> setDatosGraficoCircular, pieChartData -> datos
        ReportsController.datosGraficoCircular = datos; // pieChartData -> datosGraficoCircular
    }

    public static void setDatosGraficoLinea(XYChart.Series<String, Integer> datos) { // setLineChartData -> setDatosGraficoLinea, lineChartData -> datos
        ReportsController.datosGraficoLinea = datos; // lineChartData -> datosGraficoLinea
    }


    // Method to add values to the HashMap
    public static void setCacheReporteGraficoLinea(int year, int month, XYChart.Series<String, Integer> datosGraficoLinea) { // setCacheReportLineChart -> setCacheReporteGraficoLinea
        if (datosGraficoLinea != null) { // lineChartData -> datosGraficoLinea
            // Check if the year is already in the HashMap
            if (!cacheReporteGraficoLinea.containsKey(year)) { // cacheReportLineChart -> cacheReporteGraficoLinea
                cacheReporteGraficoLinea.put(year, new HashMap<>()); // cacheReportLineChart -> cacheReporteGraficoLinea
            }
            //  Obtain the map of months for the given year
            HashMap<Integer, XYChart.Series<String, Integer>> yearData = cacheReporteGraficoLinea.get(year); // cacheReportLineChart -> cacheReporteGraficoLinea
            // Check if a series already exists for the given month
            if (!yearData.containsKey(month)) {
                // Add data series to the map for the given month
                XYChart.Series<String, Integer> series = new XYChart.Series<>();
                series.getData().addAll(datosGraficoLinea.getData()); // lineChartData -> datosGraficoLinea
                series.setName(mesesMostrados[idxMes]); // monthsShowed -> mesesMostrados, idxMonth -> idxMes
                yearData.put(month, series);
            } else {
                System.out.println("Ya existe una serie para el año " + year + " y el mes " + month + ". No se añadirá una nueva serie."); // Mensaje traducido
            }
        } else {
            System.out.println("Error: datosGraficoLinea es nulo. No se puede añadir al mapa de caché."); // Mensaje traducido
        }

        recorrerHashMap();
    }


    public void onExport(ActionEvent actionEvent) {
        switch (cbTypeExport.getValue()){
            case ".PDF"  -> GeneradorReportesVentas.generatePDFReport(tableReport.getItems(),"src/main/resources/reports/sales_report.pdf"); // SalesReportGenerator -> GeneradorReportesVentas
            case ".XLSX" -> GeneradorReportesVentas.generateExcelReport(tableReport.getItems(),"src/main/resources/reports/sales_report.xlsx"); // SalesReportGenerator -> GeneradorReportesVentas
            case ".CSV" -> GeneradorReportesVentas.generateCSVReport(tableReport.getItems(),"src/main/resources/reports/sales_report.csv"); // SalesReportGenerator -> GeneradorReportesVentas
        }
    }

    public void sumYear(ActionEvent actionEvent) {
        añosMostrados++; // yearsShowed -> añosMostrados


        datosGraficoLinea.getData().clear(); // lineChartData -> datosGraficoLinea


        if (checkCache()){
            ventaDAO.setLineChart(datosGraficoLinea,añosMostrados, idxMes+1); // salesDAO -> ventaDAO, lineChartData -> datosGraficoLinea, yearsShowed -> añosMostrados, idxMonth -> idxMes
        }else {
            datosGraficoLinea.getData().addAll(cacheReporteGraficoLinea.get(añosMostrados).get(idxMes+1).getData()); // lineChartData -> datosGraficoLinea, cacheReportLineChart -> cacheReporteGraficoLinea, yearsShowed -> añosMostrados, idxMonth -> idxMes
        }



        fillMissingDays(datosGraficoLinea); // lineChartData -> datosGraficoLinea


        salesLineChart.getData().clear();
        salesLineChart.getData().add(datosGraficoLinea); // lineChartData -> datosGraficoLinea


        year.setText(String.valueOf(añosMostrados)); // yearsShowed -> añosMostrados
    }

    public void subtractYear(ActionEvent actionEvent) {
        añosMostrados--; // yearsShowed -> añosMostrados


        datosGraficoLinea.getData().clear(); // lineChartData -> datosGraficoLinea


        if (checkCache()){
            ventaDAO.setLineChart(datosGraficoLinea,añosMostrados, idxMes+1); // salesDAO -> ventaDAO, lineChartData -> datosGraficoLinea, yearsShowed -> añosMostrados, idxMonth -> idxMes
        }else {
            datosGraficoLinea.getData().addAll(cacheReporteGraficoLinea.get(añosMostrados).get(idxMes+1).getData()); // lineChartData -> datosGraficoLinea, cacheReportLineChart -> cacheReporteGraficoLinea, yearsShowed -> añosMostrados, idxMonth -> idxMes
        }



        fillMissingDays(datosGraficoLinea); // lineChartData -> datosGraficoLinea


        salesLineChart.getData().clear();
        salesLineChart.getData().add(datosGraficoLinea); // lineChartData -> datosGraficoLinea





        year.setText(String.valueOf(añosMostrados)); // yearsShowed -> añosMostrados
    }

    public void sumMonth(ActionEvent actionEvent) {
        if (idxMes==11) return; // idxMonth -> idxMes

        idxMes++; // idxMonth -> idxMes

        datosGraficoLinea.getData().clear(); // lineChartData -> datosGraficoLinea


        if (checkCache()){
            ventaDAO.setLineChart(datosGraficoLinea,Integer.parseInt(year.getText()), idxMes+1); // salesDAO -> ventaDAO, lineChartData -> datosGraficoLinea, idxMonth -> idxMes
        }else {
            datosGraficoLinea.getData().addAll(cacheReporteGraficoLinea.get(añosMostrados).get(idxMes+1).getData()); // lineChartData -> datosGraficoLinea, cacheReportLineChart -> cacheReporteGraficoLinea, añosMostrados -> añosMostrados, idxMonth -> idxMes
        }



        fillMissingDays(datosGraficoLinea); // lineChartData -> datosGraficoLinea


        salesLineChart.getData().clear();
        salesLineChart.getData().add(datosGraficoLinea); // lineChartData -> datosGraficoLinea


        month.setText(mesesMostrados[idxMes]); // monthsShowed -> mesesMostrados, idxMonth -> idxMes

    }

    public void subtractMonth(ActionEvent actionEvent) {
        if (idxMes==0) return; // idxMonth -> idxMes
        idxMes--; // idxMonth -> idxMes


        salesLineChart.getData().remove(datosGraficoLinea); // lineChartData -> datosGraficoLinea



        datosGraficoLinea.getData().clear(); // lineChartData -> datosGraficoLinea


        if (checkCache()){
            ventaDAO.setLineChart(datosGraficoLinea,Integer.parseInt(year.getText()), idxMes+1); // salesDAO -> ventaDAO, lineChartData -> datosGraficoLinea, idxMonth -> idxMes
        }else {
            datosGraficoLinea.getData().addAll(cacheReporteGraficoLinea.get(añosMostrados).get(idxMes+1).getData()); // lineChartData -> datosGraficoLinea, cacheReportLineChart -> cacheReporteGraficoLinea, añosMostrados -> añosMostrados, idxMonth -> idxMes
        }

        fillMissingDays(datosGraficoLinea); // lineChartData -> datosGraficoLinea


        salesLineChart.getData().add(datosGraficoLinea); // lineChartData -> datosGraficoLinea

        month.setText(mesesMostrados[idxMes]); // monthsShowed -> mesesMostrados, idxMonth -> idxMes
    }


    public static void recorrerHashMap() {
        for (Integer year : cacheReporteGraficoLinea.keySet()) { // cacheReportLineChart -> cacheReporteGraficoLinea
            System.out.println("Año: " + year); // Mensaje traducido
            HashMap<Integer, XYChart.Series<String, Integer>> yearData = cacheReporteGraficoLinea.get(year); // cacheReportLineChart -> cacheReporteGraficoLinea
            for (Integer month : yearData.keySet()) {
                System.out.println("  Mes: " + month); // Mensaje traducido
                XYChart.Series<String, Integer> series = yearData.get(month);
                if (series != null) {
                    System.out.println("    Serie: " + series.getName()); // Mensaje traducido
                    for (XYChart.Data<String, Integer> data : series.getData()) {
                        System.out.println("      Dato: " + data.getXValue() + ", " + data.getYValue()); // Mensaje traducido
                    }
                } else {
                    System.out.println("    No hay serie asociada para este mes."); // Mensaje traducido
                }
            }
        }
    }

    private static boolean checkCache() {
        // First, check if the year is not null
        if (cacheReporteGraficoLinea.get(añosMostrados) != null) { // cacheReportLineChart -> cacheReporteGraficoLinea, yearsShowed -> añosMostrados
            // If the year is not null, then you can check if the data for the month is null or not
            return cacheReporteGraficoLinea.get(añosMostrados).get(idxMes + 1) == null; // cacheReportLineChart -> cacheReporteGraficoLinea, yearsShowed -> añosMostrados, idxMonth -> idxMes
        } else {
            // If the year is null, you can return false or any value that is appropriate for your logic
            return true;
        }
    }


    public void fillMissingDays(XYChart.Series<String, Integer> lineChartData) {
        // Create a map to store sales by day
        Map<Integer, Integer> salesByDay = new HashMap<>();

        // Get existing data from the series and store it in the map
        for (XYChart.Data<String, Integer> data : lineChartData.getData()) {
            int day = Integer.parseInt(data.getXValue());
            int sales = data.getYValue();
            salesByDay.put(day, sales);
        }

        // Iterate over all days of the month and add missing series
        for (int i = 1; i <= 31; i++) { // Assuming a maximum of 31 days in a month
            if (!salesByDay.containsKey(i)) {
                // If there is no data for this day, add a point with a value of zero
                XYChart.Data<String, Integer> data = new XYChart.Data<>(String.valueOf(i), 0);
                lineChartData.getData().add(data);
            }
        }

        // Sort the data in the series by day
        lineChartData.getData().sort(Comparator.comparingInt(data -> Integer.parseInt(data.getXValue())));
    }

    @FXML
    public void backToMenu(ActionEvent actionEvent) {
        openNewStage(MANAGEMENT_VIEW_FXML, "Management");
        closeCurrentStage(cbTypeExport);
    }
}
