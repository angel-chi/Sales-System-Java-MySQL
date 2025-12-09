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
import org.borghisales.salessysten.model.IValidable;
import org.borghisales.salessysten.model.CRUD;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable, IValidable {
    //usando la abstraccion
    private final CRUD<Product> productDAO = new ProductDAO();

    private final ObservableList<Product.State> stateList = FXCollections.observableArrayList(Product.State.ACTIVE, Product.State.DISACTIVE);

    private static ObservableList<Product> products =null;
    @FXML
    private ComboBox<Product.State> cbState;
    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextField stock;
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
    public String validarCampos() {
        String nameText = name.getText();
        String priceText = price.getText();
        String stockText = stock.getText();
        if (IValidable.esCampoVacio(nameText)) {
            return "El campo Nombre del producto es obligatorio.";
        }
        if (IValidable.esCampoVacio(priceText)) {
            return "El campo Precio es obligatorio.";
        }
        if (IValidable.esCampoVacio(stockText)) {
            return "El campo Stock es obligatorio.";
        }
        try {
            Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            return "El precio debe ser un número decimal válido.";
        }
        try {
            Integer.parseInt(stockText);
        } catch (NumberFormatException e) {
            return "El stock debe ser un número entero válido.";
        }

        return null;
    }

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
        String errorMessage = validarCampos();
        if (errorMessage != null) {
            MenuController.setAlert(Alert.AlertType.WARNING, errorMessage);
            return; // Detiene la ejecución si hay errores
        }
        Product product = new Product(name.getText(),Double.parseDouble(price.getText()),
                          Integer.parseInt(stock.getText()), cbState.getValue());
        if (productDAO.create(product)) {
            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Producto añadido con éxito");
            MenuController.cleanCells(name,price,stock);
            updateTable();
        } else {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error añadiendo el producto: Favor de checar la información");
        }
    }

    public void updateProduct(ActionEvent actionEvent) {
        String errorMessage = validarCampos();
        if (errorMessage != null) {
            MenuController.setAlert(Alert.AlertType.WARNING, errorMessage);
            return; // Detiene la ejecución si hay errores
        }
        Product product = new Product(name.getText(),Double.parseDouble(price.getText()),
                Integer.parseInt(stock.getText()), cbState.getValue());
        if (productDAO.update(product)) {
            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Producto actualizado con éxito");
            MenuController.cleanCells(name,price,stock);
            updateTable();
        } else {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error actualizando producto: Nombre no encontrado o error de información");
        }
    }

    public void deleteProduct(ActionEvent actionEvent) {
        if (productDAO.delete(name.getText())) {
            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Producto borrado con éxito");
            MenuController.cleanCells(name,price,stock);
            updateTable();
        } else {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error borrando el producto: Puede no tener ventas asociadas o el nombre es incorrecto");
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
    }

    private void updateTable() {
        tableProducts.getItems().clear();
        productDAO.setTable(products);
        tableProducts.setItems(products);
    }


}
