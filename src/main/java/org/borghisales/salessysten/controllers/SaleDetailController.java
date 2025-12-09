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
import org.borghisales.salessysten.model.SalesDAO;
import org.borghisales.salessysten.model.ShoppingCart;

import java.net.URL;
import java.util.ResourceBundle;

public class SaleDetailController implements Initializable {

    private static final SalesDAO salesDAO = new SalesDAO();
    private static ObservableList<ShoppingCart> detallesProducto;
    private static int idSale;

    // salesDAO: Interactúa con la base de datos.
    // detallesProducto: almacena los objetos ShoppingCart
    // idSale: ID de la venta de la que se van a mostrar los detalles.

    // Variables
    @FXML
    private   TableView<ShoppingCart> tableSale;
    @FXML
    private TableColumn<ShoppingCart,Integer> colNro;
    @FXML
    private TableColumn<ShoppingCart,String> colCod;
    @FXML
    private TableColumn<ShoppingCart,String> colProduct;
    @FXML
    private TableColumn<ShoppingCart, Integer> colQuantity;
    @FXML
    private TableColumn<ShoppingCart, Double> colPrice;
    @FXML
    private TableColumn<ShoppingCart,Double> colTotal;
    @FXML
    private TextField totalSale;

    @Override // Configurar UI
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTableColumns();
        clearTableItems();
        loadProductsDetails();
        displayTotal();
        displayProductsDetails();
    }

    private void configureTableColumns() {
        colNro.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().nr()).asObject());
        colCod.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().cod()));
        colProduct.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().product()));
        colQuantity.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().quantity()).asObject());
        colPrice.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().price()).asObject());
        colTotal.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().total()).asObject());
    }
    // Asegura que la tabla esté vacía antes de cargar nuevos datos.
    private void clearTableItems() {
        tableSale.getItems().clear();
    }
    // Ejecuta la consulta a la base de datos.
    private void loadProductsDetails() {
        detallesProducto = FXCollections.observableArrayList();
        salesDAO.setTableDetails(detallesProducto, idSale);
    }

    private void displayTotal() {
        double sumTotal = detallesProducto.stream()
                .mapToDouble(ShoppingCart::total)
                .sum();
        totalSale.setText(String.format("%.2f", sumTotal));
    }

    private void displayProductsDetails() {
        tableSale.setItems(detallesProducto);
    }

    public static void setIdSale(int idSale) {
        SaleDetailController.idSale = idSale;
    }

}
