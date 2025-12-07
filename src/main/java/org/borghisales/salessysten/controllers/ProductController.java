package org.borghisales.salessysten.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.borghisales.salessysten.model.Product;
import org.borghisales.salessysten.model.ProductDAO;
import javafx.scene.control.TextField;


import java.net.URL;
import java.util.ResourceBundle;

public class ProductController extends MenuController implements Initializable {

    private final ProductDAO productDAO = new ProductDAO();

    private final ObservableList<Product.State> stateList = FXCollections.observableArrayList(Product.State.ACTIVE, Product.State.DISACTIVE);

    private static ObservableList<Product> products =null;
    @FXML
    private Button back;
    @FXML
    private ComboBox<Product.State> cbState;
    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextField stock;
    @FXML
    private TextField txtDiscountPercentage;
    @FXML
    private TextField txtDiscountMinQty;
    @FXML
    private TableView<Product> tableProducts;
    @FXML
    private TableColumn<Product,Integer> colId;
    @FXML
    private TableColumn<Product,String> colName;
    @FXML
    private TableColumn<Product,Double> colPrice;
    @FXML
    private TableColumn<Product,Integer> colStock;
    @FXML
    private TableColumn<Product,Product.State> colState;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeProductData();
    }

    private void initializeTable() {
        tableProducts.setOnMouseClicked(mouseEvent -> {
            if (!tableProducts.getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                Product product = tableProducts.getSelectionModel().getSelectedItem();
                setCells(product);
            }
        });

        colId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idProduct()).asObject());
        colName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().name()));
        colPrice.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().price()).asObject());
        colStock.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().stock()).asObject());
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().state()));
    }

    private void initializeComboBox() {
        cbState.setValue(Product.State.ACTIVE);
        cbState.setItems(stateList);
    }

    private void initializeProductData() {
        tableProducts.getItems().clear();
        if (products == null) {
            products = FXCollections.observableArrayList();
            productDAO.setTable(products);
        }
        tableProducts.setItems(products);
    }

    public void addProduct(ActionEvent actionEvent) {
        // ============================
        //  LEER Y VALIDAR DESCUENTO (%)
        // ============================
        double discountPercentage = 0.0;

        if (!txtDiscountPercentage.getText().trim().isEmpty()) {
            try {
                discountPercentage = Double.parseDouble(txtDiscountPercentage.getText().trim());

                // Validación 1: Rango permitido
                if (discountPercentage < 0 || discountPercentage > 100) {
                    MenuController.setAlert(Alert.AlertType.ERROR,
                            "El descuento debe estar entre 0% y 100%.");
                    return; // Cancelar registro
                }

            } catch (NumberFormatException e) {
                MenuController.setAlert(Alert.AlertType.ERROR,
                        "Ingrese un valor numérico válido para el descuento.");
                return;
            }
        }

        // ============================
        //  LEER Y VALIDAR CANTIDAD MÍNIMA
        // ============================
        int discountMinQty = 0;

        if (!txtDiscountMinQty.getText().trim().isEmpty()) {
            try {
                discountMinQty = Integer.parseInt(txtDiscountMinQty.getText().trim());

                // Validación 2: No negativos
                if (discountMinQty < 0) {
                    MenuController.setAlert(Alert.AlertType.ERROR,
                            "La cantidad mínima no puede ser negativa.");
                    return;
                }

            } catch (NumberFormatException e) {
                MenuController.setAlert(Alert.AlertType.ERROR,
                        "Ingrese un valor numérico válido para la cantidad mínima.");
                return;
            }
        }

        // ============================
        //  CREAR PRODUCTO
        // ============================
        Product product = new Product(
                name.getText(),
                Double.parseDouble(price.getText()),
                Integer.parseInt(stock.getText()),
                cbState.getValue(),
                discountPercentage,     // 👈 SE GUARDA TAL CUAL (ej: 50)
                discountMinQty
        );

        // ============================
        //  GUARDAR EN BD
        // ============================
        if (productDAO.create(product)) {
            MenuController.cleanCells(name, price, stock);
            txtDiscountPercentage.clear();
            txtDiscountMinQty.clear();
            updateTable();
        }
    }

    public void updateProduct(ActionEvent actionEvent) {
        // ==============================
        // 1. Leer y validar DESCUENTO (%)
        // ==============================
        double discountPercentage = 0.0;
        String discountText = txtDiscountPercentage.getText().trim();

        if (!discountText.isEmpty()) {
            try {
                discountPercentage = Double.parseDouble(discountText);

                // rango permitido: 0–100
                if (discountPercentage < 0 || discountPercentage > 100) {
                    MenuController.setAlert(Alert.AlertType.ERROR,
                            "El descuento debe estar entre 0 y 100.");
                    return;
                }
            } catch (NumberFormatException e) {
                MenuController.setAlert(Alert.AlertType.ERROR,
                        "El descuento debe ser un número válido.");
                return;
            }
        }

        // ==================================
        // 2. Leer y validar CANTIDAD MÍNIMA
        // ==================================
        int discountMinQty = 0;
        String minQtyText = txtDiscountMinQty.getText().trim();

        if (!minQtyText.isEmpty()) {
            try {
                discountMinQty = Integer.parseInt(minQtyText);

                if (discountMinQty < 0) {
                    MenuController.setAlert(Alert.AlertType.ERROR,
                            "La cantidad mínima no puede ser negativa.");
                    return;
                }
            } catch (NumberFormatException e) {
                MenuController.setAlert(Alert.AlertType.ERROR,
                        "La cantidad mínima debe ser un número entero válido.");
                return;
            }
        }

        // Si el descuento es 0, forzamos cantidad mínima en 0
        if (discountPercentage == 0) {
            discountMinQty = 0;
        }

        // ==============================
        // 3. Crear producto y actualizar
        // ==============================
        Product product = new Product(
                name.getText(),
                Double.parseDouble(price.getText()),
                Integer.parseInt(stock.getText()),
                cbState.getValue(),
                discountPercentage,   // 0–100
                discountMinQty
        );

        if (productDAO.update(product)) {
            MenuController.cleanCells(name, price, stock);
            txtDiscountPercentage.clear();
            txtDiscountMinQty.clear();
            updateTable();
        }
    }

    public void deleteProduct(ActionEvent actionEvent) {
        if (productDAO.delete(name.getText())) {
            MenuController.cleanCells(name,price,stock);
            updateTable();
        }
    }

    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(name,price,stock);
    }

    private void setCells(Product product){
        name.setText(product.name());
        price.setText(String.valueOf(product.price()));
        stock.setText(String.valueOf(product.stock()));
        cbState.setValue(product.state());

        txtDiscountPercentage.setText(String.valueOf(product.discountPercentage()));
        txtDiscountMinQty.setText(String.valueOf(product.discountMinQuantity()));
    }

    private void updateTable() {
        tableProducts.getItems().clear();
        productDAO.setTable(products);
        tableProducts.setItems(products);
    }

    public void back(ActionEvent actionEvent) {
        openNewStage(MANAGEMENT_VIEW_FXML,"Administración");
        closeCurrentStage(back);
    }

}
