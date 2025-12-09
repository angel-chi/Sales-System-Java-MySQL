package org.borghisales.salessysten.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.borghisales.salessysten.model.Product;
import org.borghisales.salessysten.model.ProductDAO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProductSelectionController extends MenuController implements Initializable {

    private final ProductDAO productDAO = new ProductDAO();
    private ObservableList<Product> products;

    private GenerateSaleController parentController;

    @FXML
    private TableView<Product> tableProducts;
    @FXML
    private TableColumn<Product, Integer> colNro;
    @FXML
    private TableColumn<Product, String> colCod;
    @FXML
    private TableColumn<Product, String> colProduct;
    @FXML
    private TableColumn<Product, Double> colPrice;
    @FXML
    private TableColumn<Product, Integer> colExistencias;
    @FXML
    private TableColumn<Product, String> colGarantia;

    @FXML
    private TextField codProduct;
    @FXML
    private TextField productName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTableColumns();
        loadAllProducts();
        configureDoubleClick();
    }

    private void configureTableColumns() {
        colNro.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idProduct()).asObject());
        colCod.setCellValueFactory(p -> new SimpleStringProperty(String.valueOf(p.getValue().idProduct())));
        colProduct.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().name()));
        colPrice.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().price()).asObject());
        colExistencias.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().stock()).asObject());
        colGarantia.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().garantia().toString()));
    }

    private void loadAllProducts() {
        List<Product> list = productDAO.getAllProducts();
        products = FXCollections.observableArrayList(list);
        tableProducts.setItems(products);
    }

    private void configureDoubleClick() {
        tableProducts.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (! row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Product clicked = row.getItem();
                    acceptProductAndClose(clicked);
                }
            });
            return row;
        });
    }

    private void acceptProductAndClose(Product product) {
        if (parentController != null) {
            parentController.acceptSelectedProduct(product);
        }
        // cerrar la ventana actual
        Stage stage = (Stage) tableProducts.getScene().getWindow();
        stage.close();
    }

    public void setParentController(GenerateSaleController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void searchProduct(ActionEvent actionEvent) {
        String codText = codProduct.getText() == null ? "" : codProduct.getText().trim();
        String nameText = productName.getText() == null ? "" : productName.getText().trim();

        if (codText.isBlank() && nameText.isBlank()) {
            products = FXCollections.observableArrayList(productDAO.getAllProducts());
            tableProducts.setItems(products);
            return;
        }

        if (!codText.isBlank()) {
            try {
                int id = Integer.parseInt(codText);
                Product p = productDAO.searchProduct(id);
                if (p != null) {
                    tableProducts.setItems(FXCollections.observableArrayList(p));
                    return;
                } else {
                }
            } catch (NumberFormatException ignored) {
            }
        }

        String nameToSearch = !nameText.isBlank() ? nameText : codText;
        if (!nameToSearch.isBlank()) {
            List<Product> matched = productDAO.getAllProducts().stream()
                    .filter(prod -> prod.name().toLowerCase().contains(nameToSearch.toLowerCase()))
                    .collect(Collectors.toList());
            products = FXCollections.observableArrayList(matched);
            tableProducts.setItems(products);
        } else {
            loadAllProducts();
        }
    }
}
