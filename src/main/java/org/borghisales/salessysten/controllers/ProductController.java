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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.borghisales.salessysten.model.Producto; // Product -> Producto
import org.borghisales.salessysten.model.ProductoDAO; // ProductDAO -> ProductoDAO

import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    private final ProductoDAO productoDAO = new ProductoDAO(); // ProductDAO -> ProductoDAO

    private final ObservableList<Producto.Estado> stateList = FXCollections.observableArrayList(Producto.Estado.ACTIVO, Producto.Estado.INACTIVO); // Product.State -> Producto.Estado

    private static ObservableList<Producto> productos =null; // Product -> Producto, products -> productos
    @FXML
    private ComboBox<Producto.Estado> cbState; // Product.State -> Producto.Estado
    @FXML
    private TextField nombre; // name -> nombre
    @FXML
    private TextField precio; // price -> precio (se mantiene el TextField, pero el acceso a la propiedad es precio)
    @FXML
    private TextField existencia; // stock -> existencia
    @FXML
    private TableView<Producto> tableProducts; // Product -> Producto
    @FXML
    private TableColumn<Producto,Integer> colId; // Product -> Producto
    @FXML
    private TableColumn<Producto,String> colName; // Product -> Producto, colName -> colNombre
    @FXML
    private TableColumn<Producto,Double> colPrice; // Product -> Producto, colPrice -> colPrecio
    @FXML
    private TableColumn<Producto,Integer> colStock; // Product -> Producto, colStock -> colExistencia
    @FXML
    private TableColumn<Producto,Producto.Estado> colState; // Product -> Producto, Product.State -> Producto.Estado, colState -> colEstado


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeProductData();
    }

    private void initializeTable() {
        tableProducts.setOnMouseClicked(mouseEvent -> {
            if (!tableProducts.getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                Producto producto = tableProducts.getSelectionModel().getSelectedItem();
                setCells(producto);
            }
        });

        colId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idProducto()).asObject());
        colName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().nombre()));
        colPrice.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().precio()).asObject());
        colStock.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().existencia()).asObject());
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().estado()));
    }

    private void initializeComboBox() {
        cbState.setValue(Producto.Estado.ACTIVO); // Product.State.ACTIVE -> Producto.Estado.ACTIVO
        cbState.setItems(stateList);
    }

    private void initializeProductData() {
        tableProducts.getItems().clear();
        if (productos == null) { // products -> productos
            productos = FXCollections.observableArrayList(); // products -> productos
            productoDAO.setTable(productos); // productDAO -> productoDAO, products -> productos
        }
        tableProducts.setItems(productos); // products -> productos
    }

    public void addProduct(ActionEvent actionEvent) {
        Producto producto = new Producto(nombre.getText(),Double.parseDouble(precio.getText()), // name -> nombre, price -> precio
                          Integer.parseInt(existencia.getText()), cbState.getValue()); // stock -> existencia
        if (productoDAO.create(producto)) { // productDAO -> productoDAO
            MenuController.cleanCells(nombre,precio,existencia); // name -> nombre, price -> precio, stock -> existencia
            updateTable();
        }
    }

    public void updateProduct(ActionEvent actionEvent) {
        Producto producto = new Producto(nombre.getText(),Double.parseDouble(precio.getText()), // name -> nombre, price -> precio
                Integer.parseInt(existencia.getText()), cbState.getValue()); // stock -> existencia
        if (productoDAO.update(producto)) { // productDAO -> productoDAO
            MenuController.cleanCells(nombre,precio,existencia); // name -> nombre, price -> precio, stock -> existencia
            updateTable();
        }
    }

    public void deleteProduct(ActionEvent actionEvent) {
        if (productoDAO.delete(nombre.getText())) { // productDAO -> productoDAO, name -> nombre (manteniendo la lógica original de borrar por nombre)
            MenuController.cleanCells(nombre,precio,existencia); // name -> nombre, price -> precio, stock -> existencia
            updateTable();
        }
    }

    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(nombre,precio,existencia); // name -> nombre, price -> precio, stock -> existencia
    }

    private void setCells(Producto producto){ // Product -> Producto
        nombre.setText(producto.nombre()); // name -> nombre, product.name() -> producto.nombre()
        precio.setText(String.valueOf(producto.precio())); // price -> precio, product.price() -> producto.precio()
        existencia.setText(String.valueOf(producto.existencia())); // stock -> existencia, product.stock() -> producto.existencia()
        cbState.setValue(producto.estado()); // product.state() -> producto.estado()
    }

    private void updateTable() {
        tableProducts.getItems().clear();
        productoDAO.setTable(productos); // productDAO -> productoDAO, products -> productos
        tableProducts.setItems(productos); // products -> productos
    }

}
