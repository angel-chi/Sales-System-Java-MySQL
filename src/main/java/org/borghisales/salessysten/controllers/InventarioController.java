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
import org.borghisales.salessysten.model.dao.ProductDAO;
import org.borghisales.salessysten.model.entities.Product;
import org.borghisales.salessysten.model.entities.State;

import java.net.URL;
import java.util.ResourceBundle;

public class InventarioController extends MenuController implements Initializable {

    // DAO
    private final ProductDAO productDAO = new ProductDAO();
    private ObservableList<Product> productos = FXCollections.observableArrayList();

    // Campos de búsqueda
    @FXML private TextField searchField;

    // Labels de estadísticas
    @FXML private Label totalProductos;
    @FXML private Label productosActivos;
    @FXML private Label stockBajo;
    @FXML private Label sinStock;

    // Tabla
    @FXML private TableView<Product> tableInventario;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String> colNombre;
    @FXML private TableColumn<Product, Double> colPrecio;
    @FXML private TableColumn<Product, Integer> colStock;
    @FXML private TableColumn<Product, String> colEstado;
    @FXML private TableColumn<Product, String> colAlerta;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTable();
        loadProducts();
        updateStatistics();
        tableInventario.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configureTable() {
        colId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idProduct()).asObject());
        colNombre.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().name()));
        colPrecio.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().price()).asObject());
        colStock.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().stock()).asObject());
        colEstado.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().state().toString()));

        // Columna de alerta según stock
        colAlerta.setCellValueFactory(p -> {
            int stock = p.getValue().stock();
            if (stock == 0) {
                return new SimpleStringProperty("SIN STOCK");
            } else if (stock < 10) {
                return new SimpleStringProperty("STOCK BAJO");
            } else {
                return new SimpleStringProperty("OK");
            }
        });

        // Colorear filas según stock
        tableInventario.setRowFactory(tv -> new TableRow<Product>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                if (empty || product == null) {
                    setStyle("");
                } else {
                    if (product.stock() == 0) {
                        setStyle("-fx-background-color: #ffcdd2;"); // Rojo claro
                    } else if (product.stock() < 10) {
                        setStyle("-fx-background-color: #fff9c4;"); // Amarillo claro
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }

    private void loadProducts() {
        productos.clear();
        productDAO.setTable(productos);
        tableInventario.setItems(productos);
    }

    private void updateStatistics() {
        int total = productos.size();
        int activos = (int) productos.stream().filter(p -> p.state() == State.ACTIVE).count();
        int bajo = (int) productos.stream().filter(p -> p.stock() > 0 && p.stock() < 10).count();
        int sinStockCount = (int) productos.stream().filter(p -> p.stock() == 0).count();

        totalProductos.setText(String.valueOf(total));
        productosActivos.setText(String.valueOf(activos));
        stockBajo.setText(String.valueOf(bajo));
        sinStock.setText(String.valueOf(sinStockCount));
    }

    @FXML
    private void searchProduct(ActionEvent e) {
        String searchText = searchField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            showAll(e);
            return;
        }

        ObservableList<Product> filtered = productos.filtered(product ->
                product.name().toLowerCase().contains(searchText)
        );

        tableInventario.setItems(filtered);
    }

    @FXML
    private void showAll(ActionEvent e) {
        searchField.clear();
        tableInventario.setItems(productos);
    }

    @FXML
    private void exit(ActionEvent e) {
        openNewStage(MANAGEMENT_VIEW_FXML, "Gestión");
        closeCurrentStage(searchField);
    }
}