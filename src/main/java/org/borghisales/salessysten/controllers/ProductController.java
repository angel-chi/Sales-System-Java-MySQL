package org.borghisales.salessysten.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.borghisales.salessysten.model.Product;
import org.borghisales.salessysten.model.ProductDAO;

import org.borghisales.salessysten.controllers.SelectionListener;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    private final ProductDAO productDAO = new ProductDAO();

    private final ObservableList<Product.State> stateList = FXCollections.observableArrayList(Product.State.ACTIVE, Product.State.DISACTIVE);

    private static ObservableList<Product> products =null;
    private FilteredList<Product> filteredData;        // Lista FILTRADA

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
    @FXML
    private VBox searchContainer;
    @FXML
    private TextField txtSearch;
    @FXML
    private ToggleGroup searchOptions;

    @FXML
    private RadioButton rbId, rbNombre, rbPrice;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeProductData();
        // CAMBIO: Aplicamos la configuración para poder deseleccionar los botones
        makeDeselectable(rbId);
        makeDeselectable(rbNombre);
        makeDeselectable(rbPrice);

        rbNombre.setOnAction(e -> applyFilter(txtSearch.getText()));
        rbPrice.setOnAction(e -> applyFilter(txtSearch.getText()));
        rbId.setOnAction(e -> applyFilter(txtSearch.getText()));

        //esta linea se movió por que se generaban estos egventos listeners cada vez que el metodo searchCostumer ocurra
        txtSearch.textProperty().addListener((n, un, txt) -> {
            applyFilter(txt);
        });
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
        if (products == null) {
            products = FXCollections.observableArrayList();
        } else {
            products.clear(); //  Limpiamos la lista por si traía basura de antes
        }
        productDAO.setTable(products); //actualizamos la customers con la db
        filteredData = new FilteredList<>(products, p -> true);
        tableProducts.setItems(filteredData);      //usamos la mascara sobre la original
    }

    public void addProduct(ActionEvent actionEvent) {
        Product product = new Product(name.getText(),Double.parseDouble(price.getText()),
                          Integer.parseInt(stock.getText()), cbState.getValue());
        if (productDAO.create(product)) {
            MenuController.cleanCells(name,price,stock);
            updateTable();
        }
    }

    public void updateProduct(ActionEvent actionEvent) {
        Product product = new Product(name.getText(),Double.parseDouble(price.getText()),
                Integer.parseInt(stock.getText()), cbState.getValue());
        if (productDAO.update(product)) {
            MenuController.cleanCells(name,price,stock);
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

        // Notificar al listener si existe
        if (selectionListener != null) {
            selectionListener.onItemSelected(product);
            // Cerrar la ventana modal
            tableProducts.getScene().getWindow().hide();
        }
    }


    private void updateTable() {
        products.clear(); //  Vaciamos el contenido antes de volver a llenarlo para evitar duplicados
        productDAO.setTable(products);
    }
    @FXML
    private void searchProduct() {
        boolean isVisible = searchContainer.isVisible();
        searchContainer.setVisible(!isVisible);
        searchContainer.setManaged(!isVisible);
    }
    // ******************************************************************************************
        // Botones de Generate en Product
    //  Nuevo metodo para permitir la deselección al hacer click
    private void makeDeselectable(RadioButton rb) {
        rb.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (rb.isSelected()) {
                searchOptions.selectToggle(null);
                applyFilter(txtSearch.getText());
                // Consumimos el evento para evitar que JavaFX lo vuelva a seleccionar automáticamente
                event.consume();
            }
        });
    }

    private void applyFilter(String txt) {
        filteredData.setPredicate(product -> {

            if (txt == null || txt.isEmpty()) {
                return true; // mostrar todos
            }

            String lowerCaseFilter = txt.toLowerCase();

            if (rbNombre.isSelected()) {
                return product.name().toLowerCase().startsWith(lowerCaseFilter);
            } else if (rbPrice.isSelected()) {
                return String.valueOf(product.price()).startsWith(lowerCaseFilter);
            } else if (rbId.isSelected()) {
                return String.valueOf(product.idProduct()).startsWith(lowerCaseFilter);
            }

            // Si no hay ningún radio seleccionado, muestra todo
            return true;
        });
    }


    private SelectionListener<Product> selectionListener;

    public void setSelectionListener(SelectionListener<Product> listener) {
        this.selectionListener = listener;
    }


}
