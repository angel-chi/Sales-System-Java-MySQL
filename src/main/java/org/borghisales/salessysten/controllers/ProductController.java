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
import javafx.util.Callback;
import org.borghisales.salessysten.model.Brand;
import org.borghisales.salessysten.model.Product;
import org.borghisales.salessysten.model.ProductDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductController extends MenuController implements Initializable {

    private final ProductDAO productDAO = new ProductDAO();
    private final ObservableList<Product.State> stateList = FXCollections.observableArrayList(Product.State.ACTIVE, Product.State.DISACTIVE);
    private final ObservableList<Brand> brandList = FXCollections.observableArrayList();

    private static ObservableList<Product> products = null;

    @FXML private ComboBox<Product.State> cbState;
    @FXML private ComboBox<Brand> cbBrand;
    @FXML private TextField name;
    @FXML private TextField price;
    @FXML private TextField stock;

    // Botones
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private Button btnReturn;

    @FXML private TableView<Product> tableProducts;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colBrand;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;
    @FXML private TableColumn<Product, Product.State> colState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeProductData();

        // implementamos UIEfectos****************
        UIEfectos.styleButtonAdd(btnAdd);
        UIEfectos.styleButtonUpdate(btnUpdate);
        UIEfectos.styleButtonDelete(btnDelete);
        UIEfectos.styleButtonGray(btnClear);
        UIEfectos.styleButtonReturn(btnReturn);
    }

    private void initializeComboBox() {
        cbState.setValue(Product.State.ACTIVE);
        cbState.setItems(stateList);

        productDAO.getBrands(brandList);
        cbBrand.setItems(brandList);

        styleComboBox(cbState);
        styleComboBox(cbBrand);
    }

    private <T> void styleComboBox(ComboBox<T> comboBox) {
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setStyle("-fx-text-fill: white;");
                }
            }
        });
        comboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<T> call(ListView<T> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setStyle("-fx-background-color: #333333;");
                        } else {
                            setText(item.toString());
                            setStyle("-fx-background-color: #333333; -fx-text-fill: white;");
                        }
                    }
                };
            }
        });
    }

    private void initializeTable() {
        tableProducts.setPlaceholder(new Label("No hay productos registrados"));

        tableProducts.setOnMouseClicked(mouseEvent -> {
            if (!tableProducts.getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                Product product = tableProducts.getSelectionModel().getSelectedItem();
                setCells(product);
            }
        });

        colId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idProduct()).asObject());
        colName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().name()));
        colBrand.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().brandName()));

        // Formato de precio con color y símbolo
        colPrice.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().price()).asObject());
        colPrice.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                    setStyle("-fx-text-fill: #a5d6a7;");
                }
            }
        });

        colStock.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().stock()).asObject());
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().state()));
    }

    private void initializeProductData() {
        tableProducts.getItems().clear();
        if (products == null) {
            products = FXCollections.observableArrayList();
            productDAO.setTable(products);
        }
        /*Actualiza la tabla de products cada que se abre sin tener que cerrar el programa*/
        products.clear();
        productDAO.setTable(products);
        tableProducts.setItems(products);
    }

    @FXML
    public void returnToMenu(ActionEvent event) {
        openNewStage(MANAGEMENT_VIEW_FXML, "Management");
        closeCurrentStage(btnReturn);
    }

    public void addProduct(ActionEvent actionEvent) {
        Brand selectedBrand = cbBrand.getValue();
        if (selectedBrand == null || name.getText().isEmpty()) return;

        try {
            Product product = new Product(
                    name.getText(),
                    selectedBrand.idBrand(),
                    selectedBrand.name(),
                    Double.parseDouble(price.getText()),
                    Integer.parseInt(stock.getText()),
                    cbState.getValue()
            );

            if (productDAO.create(product)) {
                cleanCellsScreen(null);
                updateTable();
            }
        } catch (NumberFormatException e) {
            System.out.println("Error formato número");
        }
    }

    public void updateProduct(ActionEvent actionEvent) {
        Brand selectedBrand = cbBrand.getValue();
        Product selectedProduct = tableProducts.getSelectionModel().getSelectedItem();

        if (selectedBrand == null || selectedProduct == null) return;

        try {
            Product product = new Product(
                    selectedProduct.idProduct(),
                    name.getText(),
                    selectedBrand.idBrand(),
                    selectedBrand.name(),
                    Double.parseDouble(price.getText()),
                    Integer.parseInt(stock.getText()),
                    cbState.getValue()
            );

            if (productDAO.update(product)) {
                cleanCellsScreen(null);
                updateTable();
            }
        } catch (NumberFormatException e) {
            System.out.println("Error formato número");
        }
    }

    public void deleteProduct(ActionEvent actionEvent) {
        if (!name.getText().isEmpty() && productDAO.delete(name.getText())) {
            cleanCellsScreen(null);
            updateTable();
        }
    }

    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(name, price, stock);
        cbBrand.getSelectionModel().clearSelection();
        cbState.setValue(Product.State.ACTIVE);
        tableProducts.getSelectionModel().clearSelection();
    }

    private void setCells(Product product){
        name.setText(product.name());
        price.setText(String.valueOf(product.price()));
        stock.setText(String.valueOf(product.stock()));
        cbState.setValue(product.state());

        for(Brand b : cbBrand.getItems()){
            if(b.idBrand() == product.idBrand()){
                cbBrand.setValue(b);
                break;
            }
        }
    }

    private void updateTable() {
        tableProducts.getItems().clear();
        productDAO.setTable(products);
        tableProducts.setItems(products);
    }
}