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

        // Implementamos UIEfectos (Animaciones de botones)
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
        if (products == null) {
            products = FXCollections.observableArrayList();
        }
        products.clear();
        productDAO.setTable(products);
        tableProducts.setItems(products);
    }

    // validaciones*********************
    private boolean validarEntradas() {
        // verifica que no esten vacios los campos
        if (name.getText().isEmpty() || price.getText().isEmpty() || stock.getText().isEmpty() || cbBrand.getSelectionModel().getSelectedItem() == null) {
            setAlert(Alert.AlertType.WARNING, "Todos los campos son obligatorios.");
            return false;
        }
        // Validamos el Precio
        try {
            double valorPrecio = Double.parseDouble(price.getText());
            if (valorPrecio < 0) {
                setAlert(Alert.AlertType.WARNING, "El precio debe ser un numero positivo.");
                return false;
            }
        } catch (NumberFormatException e) {
            setAlert(Alert.AlertType.WARNING, "El precio debe ser un número válido (Ej: 10.50).");
            return false;
        }

        // validamos el stock**********
        try {
            int valorStock = Integer.parseInt(stock.getText());
            if (valorStock < 0) {
                setAlert(Alert.AlertType.WARNING, "El stock no puede ser negativo.");
                return false;
            }
        } catch (NumberFormatException e) {
            setAlert(Alert.AlertType.WARNING, "El stock debe ser un número entero (Ej: 10).");
            return false;
        }
        return true;
    }

    @FXML
    public void addProduct(ActionEvent actionEvent) {
        // se validan las entradas con los criterios previamente establecidos
        if (!validarEntradas()) return;
        /*continua si ya se valido lo anterior*/
        Product product = new Product(
                name.getText(),
                cbBrand.getValue().idBrand(),
                cbBrand.getValue().name(),
                Double.parseDouble(price.getText()),
                Integer.parseInt(stock.getText()),
                cbState.getValue()
        );

        if (productDAO.create(product)) {
            cleanCellsScreen(null);
            updateTable(); // Recarga la tabla
        }
    }

    @FXML
    public void updateProduct(ActionEvent actionEvent) {
        Product selectedProduct = tableProducts.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            setAlert(Alert.AlertType.WARNING, "Seleccione un producto de la tabla para actualizar.");
            return;
        }

        if (!validarEntradas()) return;

        // Mantenemos el ID del producto original
        Product product = new Product(
                selectedProduct.idProduct(),
                name.getText(),
                cbBrand.getValue().idBrand(),
                cbBrand.getValue().name(),
                Double.parseDouble(price.getText()),
                Integer.parseInt(stock.getText()),
                cbState.getValue()
        );

        if (productDAO.update(product)) {
            cleanCellsScreen(null);
            updateTable();
        }
    }

    @FXML
    public void deleteProduct(ActionEvent actionEvent) {
        if (name.getText().isEmpty()) {
            setAlert(Alert.AlertType.WARNING, "Seleccione un producto para eliminar.");
            return;
        }
        if (productDAO.delete(name.getText())) {
            cleanCellsScreen(null);
            updateTable();
        }
    }

    @FXML
    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(name, price, stock);
        cbBrand.getSelectionModel().clearSelection();
        cbState.setValue(Product.State.ACTIVE);
        tableProducts.getSelectionModel().clearSelection();
    }

    @FXML
    public void returnToMenu(ActionEvent event) {
        openNewStage(MANAGEMENT_VIEW_FXML, "Management");
        closeCurrentStage(btnReturn);
    }

    private void setCells(Product product){
        name.setText(product.name());
        price.setText(String.valueOf(product.price()));
        stock.setText(String.valueOf(product.stock()));
        cbState.setValue(product.state());

        // Seleccionar la marca correcta
        for(Brand b : cbBrand.getItems()){
            if(b.idBrand() == product.idBrand()){
                cbBrand.setValue(b);
                break;
            }
        }
    }

    private void updateTable() {
        products.clear();
        productDAO.setTable(products);
        tableProducts.setItems(products);
    }
}