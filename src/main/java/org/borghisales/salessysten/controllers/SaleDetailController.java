package org.borghisales.salessysten.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.borghisales.salessysten.model.VentaDAO; // SalesDAO -> VentaDAO
import org.borghisales.salessysten.model.CarritoCompra; // ShoppingCart -> CarritoCompra

import java.net.URL;
import java.util.ResourceBundle;

public class SaleDetailController implements Initializable {



    private static final VentaDAO ventaDAO = new VentaDAO(); // SalesDAO -> VentaDAO
    private static ObservableList<CarritoCompra> detallesProductos; // ShoppingCart -> CarritoCompra, productsDetails -> detallesProductos
    private static int idVenta; // idSale -> idVenta

    @FXML
    private   TableView<CarritoCompra> tableSale; // ShoppingCart -> CarritoCompra
    @FXML
    private TableColumn<CarritoCompra,Integer> colNro; // ShoppingCart -> CarritoCompra
    @FXML
    private TableColumn<CarritoCompra,String> colCod; // ShoppingCart -> CarritoCompra
    @FXML
    private TableColumn<CarritoCompra,String> colProduct; // ShoppingCart -> CarritoCompra
    @FXML
    private TableColumn<CarritoCompra, Integer> colQuantity; // ShoppingCart -> CarritoCompra
    @FXML
    private TableColumn<CarritoCompra, Double> colPrice; // ShoppingCart -> CarritoCompra
    @FXML
    private TableColumn<CarritoCompra,Double> colTotal; // ShoppingCart -> CarritoCompra
    @FXML
    private TextField totalSale;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTableColumns();
        clearTableItems();
        loadProductsDetails();
        displayTotal();
        displayProductsDetails();
    }

    private void configureTableColumns() {
        colNro.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().numero()).asObject()); // nr() -> numero()
        colCod.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().codigo())); // cod() -> codigo()
        colProduct.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().producto())); // product() -> producto()
        colQuantity.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().cantidad()).asObject()); // quantity() -> cantidad()
        colPrice.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().precio()).asObject()); // price() -> precio()
        colTotal.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().total()).asObject());
    }

    private void clearTableItems() {
        tableSale.getItems().clear();
    }

    private void loadProductsDetails() {
        detallesProductos = FXCollections.observableArrayList(); // productsDetails -> detallesProductos
        ventaDAO.setTableDetails(detallesProductos, idVenta); // salesDAO -> ventaDAO, productsDetails -> detallesProductos, idSale -> idVenta
    }

    private void displayTotal() {
        double sumTotal = detallesProductos.stream() // productsDetails -> detallesProductos
                .mapToDouble(CarritoCompra::total) // ShoppingCart -> CarritoCompra
                .sum();
        totalSale.setText(String.format("%.2f", sumTotal));
    }

    private void displayProductsDetails() {
        tableSale.setItems(detallesProductos); // productsDetails -> detallesProductos
    }


    public static void setIdVenta(int idVenta) { // setIdSale -> setIdVenta, idSale -> idVenta
        SaleDetailController.idVenta = idVenta; // idSale -> idVenta
    }


}
