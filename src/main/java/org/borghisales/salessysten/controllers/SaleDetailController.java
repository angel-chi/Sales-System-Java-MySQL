package org.borghisales.salessysten.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.borghisales.salessysten.model.SalesDAO;
import org.borghisales.salessysten.model.ShoppingCart;
import org.borghisales.salessysten.model.Sales;
import java.awt.event.ActionEvent;

import org.borghisales.salessysten.model.DiscountSrategy;
import org.borghisales.salessysten.model.PercentageDiscount;
import org.borghisales.salessysten.model.BulkDiscount;

import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

public class SaleDetailController extends ReportsController implements Initializable {

    private ReportsController parent;
    private static final SalesDAO salesDAO = new SalesDAO();
    private static ObservableList<ShoppingCart> productsDetails;
    private static int idSale;

    @FXML
    private Button desactivar;
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
            // Tomamos el precio tal cual viene de la BD
            double unitPrice = sc.price();
            int quantity = sc.quantity();

            // Creamos el item SIN tocar el precio
            ShoppingCart item = new ShoppingCart(
                    sc.nr(),
                    sc.cod(),
                    sc.product(),
                    quantity,
                    unitPrice
            );

            discountedList.add(item);
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
    //private void finalizar(){};


    public void finalizar(javafx.event.ActionEvent actionEvent) {
        salesDAO.desactivar(idSale, Sales.State.FINALIZADA);
        if (parent != null) {
            parent.refreshReports();
        }
        closeCurrentStage(desactivar);
    }
    public void devolver(javafx.event.ActionEvent actionEvent){
        salesDAO.regresar(idSale, Sales.State.DEVUELTA,productsDetails);
        if (parent != null) {
            parent.refreshReports();
        }
        closeCurrentStage(desactivar);
    }
    public void cancelar(javafx.event.ActionEvent actionEvent){
        salesDAO.regresar(idSale, Sales.State.CANCELADA,productsDetails);
        if (parent != null) {
            parent.refreshReports();
        }
        closeCurrentStage(desactivar);
    }

    private void displayProductsDetails() {
        tableSale.setItems(productsDetails);
    }


    public static void setIdSale(int idSale) {
        SaleDetailController.idSale = idSale;
    }

    public void setParentController(ReportsController parent) {
        this.parent = parent;
    }


}