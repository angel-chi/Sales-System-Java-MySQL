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

import org.borghisales.salessysten.model.DiscountSrategy;
import org.borghisales.salessysten.model.PercentageDiscount;
import org.borghisales.salessysten.model.BulkDiscount;

import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

public class SaleDetailController implements Initializable {



    private static final SalesDAO salesDAO = new SalesDAO();
    private static ObservableList<ShoppingCart> productsDetails;
    private static int idSale;

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


    @Override
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

    private void clearTableItems() {
        tableSale.getItems().clear();
    }

    private void loadProductsDetails() {
        productsDetails = FXCollections.observableArrayList();
        salesDAO.setTableDetails(productsDetails, idSale);
        // Nueva lista con precios ya descontados
        var discountedList = FXCollections.<ShoppingCart>observableArrayList();

        for (ShoppingCart sc : productsDetails) {

            double unitPrice = sc.price();   // precio unitario original
            int quantity = sc.quantity();    // cantidad comprada
            double discountedUnitPrice = unitPrice;

            //Elegir estrategia según cantidad
            DiscountSrategy strategy;

            if (quantity >= 5) {
                strategy = new BulkDiscount(5, 0.20);  // 20% por comprar 5 o más
            } else {
                strategy = new PercentageDiscount(0.10); // 10% fijo
            }

            //Aplicar descuento
            double newUnitPrice = strategy.apply(unitPrice, quantity);

            if (newUnitPrice < unitPrice) {
                discountedUnitPrice = newUnitPrice;

                //Mostrar mensaje al usuario
                showDiscountAppliedAlert(
                        sc.product(),
                        unitPrice,
                        discountedUnitPrice,
                        quantity
                );
            }

            //Crear un nuevo ShoppingCart con precio YA descontado
            ShoppingCart discountedItem = new ShoppingCart(
                    sc.nr(),
                    sc.cod(),
                    sc.product(),
                    sc.quantity(),
                    discountedUnitPrice
            );

            discountedList.add(discountedItem);
        }

        // Reemplazar lista original
        productsDetails = discountedList;
    }

    private void displayTotal() {
        double sumTotal = productsDetails.stream()
                .mapToDouble(ShoppingCart::total)
                .sum();
        totalSale.setText(String.format("%.2f", sumTotal));
    }

    private void displayProductsDetails() {
        tableSale.setItems(productsDetails);
    }


    public static void setIdSale(int idSale) {
        SaleDetailController.idSale = idSale;
    }

    private void showDiscountAppliedAlert(String productName,
                                          double originalPrice,
                                          double discountedPrice,
                                          int quantity) {

        double originalSubtotal = originalPrice * quantity;
        double discountedSubtotal = discountedPrice * quantity;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Descuento aplicado");
        alert.setHeaderText(null);
        alert.setContentText(String.format(
                "Se aplicó un descuento al producto \"%s\".\n\n" +
                        "Precio unitario antes: %.2f\n" +
                        "Precio unitario después: %.2f\n\n" +
                        "Subtotal antes: %.2f\n" +
                        "Subtotal con descuento: %.2f",
                productName,
                originalPrice,
                discountedPrice,
                originalSubtotal,
                discountedSubtotal
        ));

        alert.showAndWait();
    }


}