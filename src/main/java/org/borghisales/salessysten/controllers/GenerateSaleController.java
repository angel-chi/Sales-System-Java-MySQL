package org.borghisales.salessysten.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.borghisales.salessysten.model.*;

import org.borghisales.salessysten.model.DiscountSrategy;
import org.borghisales.salessysten.model.PercentageDiscount;
import org.borghisales.salessysten.model.BulkDiscount;
import javafx.scene.control.Alert;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class GenerateSaleController extends MenuController implements Initializable {

    private final SalesDAO salesDAO = new SalesDAO();
    private int idSale;

    private static int contProducts =1;
    private static ObservableList<ShoppingCart> products;

    private static String sellerName;
    private static int idSeller;

    private final LocalDate now = LocalDate.now();


    private Scene scene = null;
    private Stage stage;

    private final Alert alertCustomer = new Alert(Alert.AlertType.WARNING);
    private final Alert alertProduct = new Alert(Alert.AlertType.WARNING);
    private final ButtonType buttonTypeAccept = new ButtonType("SI");
    private final ButtonType buttonTypeCancel = new ButtonType("NO");


    private final CustomerDAO customerDAO = new CustomerDAO();

    private Customer customer;
    private final ProductDAO productDAO = new ProductDAO();

    private Product currentProduct;

    @FXML
    private TextField cbDiscountType;
    @FXML
    private Button back;
    @FXML
    private TextField serial;
    @FXML
    private TextField codCustomer;
    @FXML
    private TextField codProduct;
    @FXML
    private TextField price;
    @FXML
    private TextField customerName;
    @FXML
    private TextField productName;
    @FXML
    private TextField stock;
    @FXML
    private TextField seller;
    @FXML
    private TextField total;
    @FXML
    private TextField date;

    @FXML
    private Spinner<Integer> quantity;
    @FXML
    private TableView<ShoppingCart> tableSale;
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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeUIElements();
        configureAlerts();
        configureTable();
        cbDiscountType.setDisable(true);   // solo visor
    }

    private void initializeUIElements() {
        total.setText("0.0");
        setSerial();
        seller.setText(sellerName);
        date.setText(String.valueOf(now));
    }

    private void configureAlerts() {
        configureAlert(alertCustomer, "Nuevo cliente", "Este cliente no existe", "¿Quieres agregarlo?");
        configureAlert(alertProduct, "Nuevo producto", "Este producto no existe", "¿Quieres agregarlo?");
    }

    private void configureTable() {
        configureTableColumns();
        tableSale.getItems().clear();
        products = FXCollections.observableArrayList();
    }


    private void configureAlert(Alert alert, String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getButtonTypes().setAll(buttonTypeAccept, buttonTypeCancel);
    }

    private void configureTableColumns() {
        colNro.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().nr()).asObject());
        colCod.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().cod()));
        colProduct.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().product()));
        colQuantity.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().quantity()).asObject());
        colPrice.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().price()).asObject());
        colTotal.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().total()).asObject());

        //2 Decimales
        colPrice.setCellFactory(column -> new TableCell<ShoppingCart, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", value));
                }
            }
        });
    }

    public void searchCustomer(ActionEvent actionEvent) {
        int customerId = Integer.parseInt(codCustomer.getText());
        customer = customerDAO.searchCustomer(customerId);

        if (customer != null) {
            setAlert(Alert.AlertType.CONFIRMATION, "Cliente seleccionado: " + customer.name());
            customerName.setText(customer.name());
        } else {
            handleCustomerNotFound();
        }
    }

    private void handleCustomerNotFound() {
        alertCustomer.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeAccept) {
                openCustomerManagementView();
            }
        });
    }

    private void openCustomerManagementView() {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource(CUSTOMER_VIEW_FXML));

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage = new Stage();
        stage.setTitle("Gestión de clientes");
        stage.setScene(scene);
        stage.show();
    }


    public void searchProduct(ActionEvent actionEvent) {

        int productId = Integer.parseInt(codProduct.getText());
        Product product = productDAO.searchProduct(productId);

        if (product != null) {
            currentProduct = product;
            updateProductFields(product);
        } else {
            currentProduct = null;
            handleProductNotFound();
        }
    }

    private void updateProductFields(Product product) {
        setAlert(Alert.AlertType.CONFIRMATION, "Producto seleccionado: " + product.name());

        productName.setText(product.name());
        stock.setText(String.valueOf(product.stock()));
        price.setText(String.valueOf(product.price()));

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, product.stock(), 1);
        quantity.setValueFactory(valueFactory);

        double discountPercent = product.discountPercentage();      // ej. 0.10
        int minQty = product.discountMinQuantity();                 // ej. 1 o 5

        // Habilitamos el ComboBox pero solo como “visor”
        cbDiscountType.setDisable(false);

        if (discountPercent <= 0) {
            // Sin descuento
            cbDiscountType.setText("Sin descuento");
        } else if (minQty <= 1) {
            // Descuento por unidad
            cbDiscountType.setText(
                    String.format("Descuento %.0f%% por unidad", discountPercent)
            );
        } else {
            // Descuento por volumen
            cbDiscountType.setText(
                    String.format("Descuento por volumen (%.0f%% si compra %d+)",
                            discountPercent,
                            minQty)
            );
        }
    }

    private void handleProductNotFound() {
        alertProduct.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeAccept) {
                openProductManagementView();
            }
        });
    }

    private void openProductManagementView() {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource(PRODUCT_VIEW_FXML));

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage = new Stage();
        stage.setTitle("Administración de productos");
        stage.setScene(scene);
        stage.show();
    }


    public void cancel(ActionEvent actionEvent) {
        if (products.isEmpty()) return;
        cleanFieldsAndTable();
        MenuController.setAlert(Alert.AlertType.INFORMATION, "Venta cancelada");
        total.clear();
        products.clear();
    }

    public void generateSale(ActionEvent actionEvent) {
        if (products.isEmpty()) {
            return;
        }

        Sales sales = createSalesObject();

        if (saveSaleAndDetails(sales)) {
            productDAO.subtractStock(products);
            cleanFieldsAndTable();
            setSerial();
            total.setText("0.0");
            products.clear();
            updateReportsController();
        }
    }

    private Sales createSalesObject() {
        return new Sales(customer.idCustomer(), idSeller, serial.getText(),
                LocalDate.parse(date.getText()), Double.parseDouble((total.getText()).replace(',','.')),
                Sales.State.ACTIVE);
    }

    private boolean saveSaleAndDetails(Sales sales) {
        boolean saleSaved = salesDAO.SaveSale(sales);
        boolean detailsSaved = salesDAO.SaveDetailsSale(products, idSale);
        return saleSaved && detailsSaved;
    }

    private void cleanFieldsAndTable() {
        MenuController.cleanCells(codCustomer, codProduct, customerName, productName, price, stock, cbDiscountType);
        quantity.getValueFactory().setValue(null);
        tableSale.getItems().clear();
    }

    private void updateReportsController() {
        ReportsController.setSales(null);
        ReportsController.setPieChartData(null);
        ReportsController.setLineChartData(null);
        ReportsController.removeCacheLineChart(now.getYear(), now.getMonth().getValue());
    }


    public static void setSellerName(String sellerName) {
        GenerateSaleController.sellerName = sellerName;
    }

    public static void setIdSeller(int idSeller) {
        GenerateSaleController.idSeller = idSeller;
    }

    public void addShoppingCart(ActionEvent actionEvent) {
        String errorMessage = validateInputs();

        if (errorMessage != null) {
            MenuController.setAlert(Alert.AlertType.ERROR, errorMessage);
            return;
        }

        ShoppingCart product = createShoppingCartObject();

        if (isProductAlreadyInCart(product)) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Ya tienes este producto en tu carrito de compras");
            return;
        }

        addToCartAndUpdateTotal(product);
    }

    private ShoppingCart createShoppingCartObject() {
        double originalUnitPrice = Double.parseDouble(price.getText());
        int qty = quantity.getValue();

        double finalUnitPrice = originalUnitPrice;

        // ================================
        // 1. Leer datos del producto
        // ================================
        double discountPercent = 0.0;   // en DECIMAL (0.10, 0.20, etc.)
        int minQty = 0;

        if (currentProduct != null) {
            discountPercent = currentProduct.discountPercentage();
            minQty = currentProduct.discountMinQuantity();
        }

        DiscountSrategy strategy = null;

        // ================================
        // 2. Decidir la estrategia automáticamente
        // ================================
        if (discountPercent > 0) {
            double discountFraction = discountPercent / 100.0;

            // Descuento por UNIDAD (minQty <= 1)
            if (minQty <= 1) {
                strategy = new PercentageDiscount(discountFraction);

                // Solo VISOR del tipo de descuento
                cbDiscountType.setText(
                        String.format("Descuento %.0f%% por unidad", discountPercent)
                );

            } else { // Descuento por VOLUMEN

                // Si la cantidad comprada alcanza el mínimo, se aplica
                if (qty >= minQty) {
                    strategy = new BulkDiscount(minQty, discountFraction);

                    cbDiscountType.setText(
                            String.format("Descuento por volumen (%.0f%% si compra %d+)",
                                    discountPercent, minQty)
                    );
                } else {
                    // No se aplica, pero mostramos que EXISTE un descuento por volumen
                    cbDiscountType.setText(
                            String.format("Descuento por volumen (%.0f%% si compra %d+) - no aplicado",
                                    discountPercent, minQty)
                    );
                }
            }

        } else {
            // Sin descuento configurado para este producto
            cbDiscountType.setText("Sin descuento");
        }

        // ================================
        // 3. Aplicar el descuento (si corresponde)
        // ================================
        if (strategy != null) {
            double discounted = strategy.apply(originalUnitPrice, qty);

            // Por seguridad, solo usamos si realmente baja el precio
            if (discounted < originalUnitPrice) {
                finalUnitPrice = discounted;
            }

            // Mostrar el mensaje como ya lo tenías
            showDiscountAppliedAlert(
                    productName.getText(), // nombre del producto
                    originalUnitPrice,     // precio antes
                    finalUnitPrice,        // precio con descuento (unitario)
                    qty                    // cantidad
            );
        }

        // ================================
        // 4. Crear el objeto ShoppingCart
        // ================================
        return new ShoppingCart(
                Integer.parseInt(serial.getText()), // nr
                codProduct.getText(),               // cod
                productName.getText(),              // product
                qty,                                // quantity
                finalUnitPrice,                     // precio unitario (ya con descuento)
                finalUnitPrice * qty                // total
        );
    }

    private boolean isProductAlreadyInCart(ShoppingCart product) {
        return products.stream().anyMatch(e -> Objects.equals(e.cod(), product.cod()));
    }

    private void addToCartAndUpdateTotal(ShoppingCart product) {
        products.add(product);
        tableSale.setItems(products);
        double currentTotal = Double.parseDouble((total.getText()).replace(',','.')) + product.total();
        total.setText(String.format("%.2f", currentTotal));
    }


    private String validateInputs() {
        if (productName.getText().isEmpty() || customerName.getText().isEmpty()) {
            return "Falta el nombre del cliente o del producto";
        } else if (quantity.getValue() == 0) {
            return "Elige una cantidad lógica";
        }
        return null;
    }

    private void setSerial(){
        idSale = 1+salesDAO.IdSale();
        String formattedId= String.format("%04d", idSale);
        serial.setText(formattedId);
    }

    public void back(ActionEvent actionEvent) {
        openNewStage(MANAGEMENT_VIEW_FXML,"Administración");
        closeCurrentStage(back);
    }

    private void configureDiscountField() {
        cbDiscountType.setText("Sin descuento");   // texto inicial
    }

    private void showDiscountAppliedAlert(String productName,
                                          double originalPrice,
                                          double discountedPrice,
                                          int quantity) {

        double originalSubtotal = originalPrice * quantity;
        double discountedSubtotal = discountedPrice * quantity;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Descuento aplicado");
        alert.setHeaderText(null);
        alert.setContentText(String.format(
                "Se aplicó un descuento al producto \"%s\".\n\n" +
                        "Precio unitario antes: %.2f\n" +
                        "Precio unitario después: %.2f\n\n" +
                        "Subtotal antes: %.2f\n" +
                        "Subtotal con descuento: %.2f",
                productName,
                originalPrice,
                discountedPrice,
                originalSubtotal,
                discountedSubtotal
        ));

        alert.showAndWait();
    }

}
