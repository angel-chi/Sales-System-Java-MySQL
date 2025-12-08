package org.borghisales.salessysten.controllers;

import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.Duration;
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

    // Componentes FXML
    @FXML private ComboBox<Product.State> cbState;
    @FXML private ComboBox<Brand> cbBrand;
    @FXML private TextField name;
    @FXML private TextField price;
    @FXML private TextField stock;

    // Botones con fx:id para animaciones
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private Button btnReturn; // Nuevo Botón Volver

    // Tabla
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

        // Iniciamos estilos y animaciones UI/UX
        setupAnimations();
    }

    // --- SECCIÓN VISUAL Y UI/UX ---

    private void setupAnimations() {
        // Estilos base y hover
        setupButtonHover(btnAdd,
                "-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;",
                "-fx-background-color: #4caf50; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(76,175,80,0.6), 10, 0, 0, 0);");

        setupButtonHover(btnUpdate,
                "-fx-background-color: #1565c0; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;",
                "-fx-background-color: #42a5f5; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(33,150,243,0.6), 10, 0, 0, 0);");

        setupButtonHover(btnDelete,
                "-fx-background-color: #c62828; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;",
                "-fx-background-color: #ef5350; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(244,67,54,0.6), 10, 0, 0, 0);");

        setupButtonHover(btnClear,
                "-fx-background-color: #424242; -fx-text-fill: #aaa; -fx-background-radius: 5;",
                "-fx-background-color: #616161; -fx-text-fill: white; -fx-background-radius: 5;");

        setupButtonHover(btnReturn,
                "-fx-background-color: transparent; -fx-text-fill: #ffa726; -fx-border-color: #fb8c00; -fx-border-radius: 5; -fx-border-width: 1.5;",
                "-fx-background-color: #fb8c00; -fx-text-fill: white; -fx-border-color: #fb8c00; -fx-border-radius: 5; -fx-border-width: 1.5; -fx-effect: dropshadow(three-pass-box, rgba(251,140,0,0.5), 10, 0, 0, 0);");
    }

    private void setupButtonHover(Button btn, String normalStyle, String hoverStyle) {
        btn.setCursor(Cursor.HAND);
        btn.setStyle(normalStyle); // Aplicar inicial
        btn.setOnMouseEntered(e -> {
            btn.setStyle(hoverStyle);
            scaleButton(btn, 1.05);
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle(normalStyle);
            scaleButton(btn, 1.0);
        });
    }

    private void scaleButton(Button btn, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), btn);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }

    // --- LÓGICA DE DATOS ---

    private void initializeComboBox() {
        cbState.setValue(Product.State.ACTIVE);
        cbState.setItems(stateList);

        productDAO.getBrands(brandList);
        cbBrand.setItems(brandList);

        // Arreglar fondo blanco de los desplegables
        styleComboBox(cbState);
        styleComboBox(cbBrand);
    }

    // Método para arreglar el fondo blanco del ComboBox
    private <T> void styleComboBox(ComboBox<T> comboBox) {
        // Estilo del botón cerrado
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
        // Estilo de la lista desplegable
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
        tableProducts.setItems(products);
    }

    // --- ACCIONES DE BOTONES ---

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