package org.borghisales.salessysten.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.borghisales.salessysten.model.*;

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
    private final ButtonType buttonTypeAccept = new ButtonType("SIMON");
    private final ButtonType buttonTypeCancel = new ButtonType("NO");


    private final CustomerDAO customerDAO = new CustomerDAO();

    private Customer customer;
    private final ProductDAO productDAO = new ProductDAO();

    private double currentSubtotal = 0.0;
    private double currentDiscount = 0.0;
    @FXML
    private TextField subtotal;
    @FXML
    private TextField discountAmount;
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
    private Spinner<Integer> discountSpinner;

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
        initializeDiscountSpinner();
    }

    private void initializeUIElements() {
        total.setText("0.0");
        subtotal.setText("0.00");
        discountAmount.setText("0.00");
        setSerial();
        seller.setText(sellerName);
        date.setText(String.valueOf(now));
    }
    private void initializeDiscountSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        discountSpinner.setValueFactory(valueFactory);
    }
    private void configureAlerts() {
        configureAlert(alertCustomer, "NUEVO CLIENTE", "EL CLIENTE NO EXISTE", "¿QUIERES AÑADIR UNO?");
        configureAlert(alertProduct, "NUEVO PRODUCTO", "EL PRODUCTO NO EXISTE", "¿QUIERES AÑADIR UNO?");
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
    }

    public void searchCustomer(ActionEvent actionEvent) {
        int customerId = Integer.parseInt(codCustomer.getText());
        customer = customerDAO.searchCustomer(customerId);

        if (customer != null) {
            setAlert(Alert.AlertType.CONFIRMATION, "Cliente encontrado: " + customer.name());
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
        MenuController.openNewStage(MenuController.CUSTOMER_VIEW_FXML, "Gestión de Clientes");
    }


    public void searchProduct(ActionEvent actionEvent) {
        int productId = Integer.parseInt(codProduct.getText());
        Product product = productDAO.searchProduct(productId);

        if (product != null) {
            updateProductFields(product);
        } else {
            handleProductNotFound();
        }
    }

    private void updateProductFields(Product product) {
        setAlert(Alert.AlertType.CONFIRMATION, "Producto encontrado: " + product.name());
        productName.setText(product.name());
        stock.setText(String.valueOf(product.stock()));
        price.setText(String.valueOf(product.price()));

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, product.stock(), 0);
        quantity.setValueFactory(valueFactory);
    }

    private void handleProductNotFound() {
        alertProduct.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeAccept) {
                openProductManagementView();
            }
        });
    }

    private void openProductManagementView() {
        MenuController.openNewStage(MenuController.PRODUCT_VIEW_FXML, "Gestión de Productos");
    }

    @FXML
    public void applyDiscount(ActionEvent actionEvent) {
        if (currentSubtotal <= 0) {
            MenuController.setAlert(Alert.AlertType.WARNING,
                    "No hay productos en el carrito para aplicar descuento");
            return;
        }

        int discountPercentage = discountSpinner.getValue();

        if (discountPercentage < 0 || discountPercentage > 100) {
            MenuController.setAlert(Alert.AlertType.WARNING,
                    "El descuento debe estar entre 0% y 100%");
            return;
        }

        currentDiscount = currentSubtotal * (discountPercentage / 100.0);


        double finalTotal = currentSubtotal - currentDiscount;


        discountAmount.setText(String.format("%.2f", currentDiscount));
        total.setText(String.format("%.2f", finalTotal));

        MenuController.setAlert(Alert.AlertType.INFORMATION,
                String.format("Descuento del %d%% aplicado: $%.2f",
                        discountPercentage, currentDiscount));
    }
    private void calculateTotal() {
        currentSubtotal = 0.0;

        for (ShoppingCart item : tableSale.getItems()) {
            currentSubtotal += item.total();
        }

        subtotal.setText(String.format("%.2f", currentSubtotal));

        applyCurrentDiscount();
    }

    private void applyCurrentDiscount() {
        double finalTotal = currentSubtotal - currentDiscount;
        total.setText(String.format("%.2f", finalTotal));
    }

    private void clearDiscount() {
        discountSpinner.getValueFactory().setValue(0);
        currentDiscount = 0.0;
        currentSubtotal = 0.0;
        discountAmount.setText("0.00");
        subtotal.setText("0.00");
    }

    public void cancel(ActionEvent actionEvent) {
        if (products.isEmpty())return;
        MenuController.cleanCells(codCustomer,codProduct,customerName,productName,price,stock);
        quantity.getValueFactory().setValue(null);
        tableSale.getItems().clear();
        clearDiscount();
        total.setText("0.00");
        MenuController.setAlert(Alert.AlertType.INFORMATION,"Venta Cancelada");
    }

    public void generateSale(ActionEvent actionEvent) {
        if (products.isEmpty()) {
            MenuController.setAlert(Alert.AlertType.WARNING, "No hay productos en el carrito para generar la venta");
            return;
        }
        if (customer == null) {
            MenuController.setAlert(Alert.AlertType.WARNING, "Debe buscar un cliente primero");
            return;
        }
        Sales sales = createSalesObject();

        if (saveSaleAndDetails(sales)) {
            productDAO.subtractStock(products);
            cleanFieldsAndTable();
            setSerial();
            clearDiscount();
            total.setText("0.0");
            products.clear();
            updateReportsController();
            MenuController.setAlert(Alert.AlertType.INFORMATION, "¡Venta generada exitosamente!");
        } else {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al guardar la venta en la base de datos");
        }
    }

    private Sales createSalesObject() {
        //se crea la variable totalText para poder evitar errores con la , al momento de generar la venta
        String totalText = total.getText().replace(",", ".");
        return new Sales(
                customer.idCustomer(),
                idSeller,
                serial.getText(),
                LocalDate.parse(date.getText()),
                Double.parseDouble(totalText),
                Sales.State.ACTIVO);
    }

    private boolean saveSaleAndDetails(Sales sales) {
        boolean saleSaved = salesDAO.SaveSale(sales);
        boolean detailsSaved = salesDAO.SaveDetailsSale(products, idSale);
        return saleSaved && detailsSaved;
    }

    private void cleanFieldsAndTable() {
        MenuController.cleanCells(codCustomer, codProduct, customerName, productName, price, stock);
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
            MenuController.setAlert(Alert.AlertType.ERROR, "Este producto ya esta en tu carrito");
            return;
        }

        addToCartAndUpdateTotal(product);
    }

    private ShoppingCart createShoppingCartObject() {
        String priceText = price.getText().replace(",", ".");
        return new ShoppingCart(
                contProducts++,
                codProduct.getText(),
                productName.getText(),
                quantity.getValue(),
                Double.parseDouble(priceText));
    }

    private boolean isProductAlreadyInCart(ShoppingCart product) {
        return products.stream().anyMatch(e -> Objects.equals(e.cod(), product.cod()));
    }

    private void addToCartAndUpdateTotal(ShoppingCart product) {
        products.add(product);
        tableSale.setItems(products);
        calculateTotal();
    }


    private String validateInputs() {
        if (productName.getText().isEmpty() || customerName.getText().isEmpty()) {
            return "Nombre de prodcuto o cliente no encontrado.";
        } else if (quantity.getValue() == 0) {
            return "La cantidad no puede ser 0.";
        }
        return null;
    }

    private void setSerial(){
        idSale = 1+salesDAO.IdSale();
        String formattedId= String.format("%04d", idSale);
        serial.setText(formattedId);
    }


}
