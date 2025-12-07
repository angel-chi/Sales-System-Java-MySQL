package org.borghisales.salessysten.controllers;

import org.borghisales.salessysten.model.Sales;
import org.borghisales.salessysten.model.payment.Payment;
import org.borghisales.salessysten.model.payment.CashPayment;
import org.borghisales.salessysten.model.payment.CardPayment;
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
    private final ButtonType buttonTypeAccept = new ButtonType("YES");
    private final ButtonType buttonTypeCancel = new ButtonType("NO");


    private final CustomerDAO customerDAO = new CustomerDAO();

    private Customer customer;
    private final ProductDAO productDAO = new ProductDAO();

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
    @FXML
    private ComboBox<String> paymentMethod;

    @FXML
    private TextField cardLast4;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeUIElements();
        configureAlerts();
        configureTable();
    }

    private void initializeUIElements() {
        total.setText("0.0");
        setSerial();
        seller.setText(sellerName);
        date.setText(String.valueOf(now));

        if (paymentMethod != null) {
            paymentMethod.setItems(FXCollections.observableArrayList("CASH", "CARD"));
            paymentMethod.setValue("CASH");
        }
    }


    private void configureAlerts() {
        configureAlert(alertCustomer, "New customer", "The customer doesn't exist", "Do you want to add it?");
        configureAlert(alertProduct, "New Product", "The product doesn't exist", "Do you want to add it?");
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
            setAlert(Alert.AlertType.CONFIRMATION, "Customer found: " + customer.name());
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
        stage.setTitle("Manage Customer");
        stage.setScene(scene);
        stage.show();
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
        setAlert(Alert.AlertType.CONFIRMATION, "Product found: " + product.name());
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
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource(PRODUCT_VIEW_FXML));

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage = new Stage();
        stage.setTitle("Manage Product");
        stage.setScene(scene);
        stage.show();
    }


    public void cancel(ActionEvent actionEvent) {
        if (products.isEmpty())return;
        MenuController.cleanCells(codCustomer,codProduct,customerName,productName,price,stock);
        quantity.getValueFactory().setValue(null);
        tableSale.getItems().clear();
        MenuController.setAlert(Alert.AlertType.INFORMATION,"Sale Canceled");
        total.clear();
    }

    public void generateSale(ActionEvent actionEvent) {
        if (products.isEmpty()) {
            return;
        }

        double totalAmount = Double.parseDouble(total.getText().replace(",", "."));

        // 1. Crear Payment y validar
        Payment payment = createPayment(totalAmount);
        if (payment == null) {
            return;
        }

        if (!payment.authorize()) {
            MenuController.setAlert(Alert.AlertType.ERROR,
                    "Payment not authorized.\n" + payment.getDescription());
            return;
        }

        // 2. Determinar PaymentType para la BD
        Sales.PaymentType paymentType;
        String method = paymentMethod.getValue();
        if ("CASH".equals(method)) {
            paymentType = Sales.PaymentType.CASH;
        } else {
            paymentType = Sales.PaymentType.CARD;
        }

        // 3. Crear Sales con paymentType
        Sales sales = createSalesObject(totalAmount, paymentType);

        // 4. Guardar venta y detalles
        if (saveSaleAndDetails(sales)) {
            productDAO.subtractStock(products);
            cleanFieldsAndTable();
            setSerial();
            total.setText("0.0");
            products.clear();
            updateReportsController();

            MenuController.setAlert(Alert.AlertType.INFORMATION,
                    "Sale completed successfully.\n" + payment.getDescription());
        }
    }


    private Sales createSalesObject(double totalAmount, Sales.PaymentType paymentType) {
        return new Sales(
                customer.idCustomer(),
                idSeller,
                serial.getText(),
                LocalDate.parse(date.getText()),
                totalAmount,
                Sales.State.ACTIVE,
                paymentType
        );
    }


    private Payment createPayment(double totalAmount) {
        if (paymentMethod == null) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Payment method combo is not initialized.");
            return null;
        }

        String method = paymentMethod.getValue();

        if (method == null) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Select a payment method.");
            return null;
        }

        if ("CASH".equals(method)) {
            return new CashPayment(totalAmount);
        } else if ("CARD".equals(method)) {
            String last4 = cardLast4.getText();
            if (last4 == null || last4.isBlank()) {
                MenuController.setAlert(Alert.AlertType.ERROR,
                        "Enter the last 4 digits of the card.");
                return null;
            }
            return new CardPayment(totalAmount, last4);
        } else {
            MenuController.setAlert(Alert.AlertType.ERROR, "Unknown payment method: " + method);
            return null;
        }
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
            MenuController.setAlert(Alert.AlertType.ERROR, "This product is already in your shopping cart");
            return;
        }

        addToCartAndUpdateTotal(product);
    }

    private ShoppingCart createShoppingCartObject() {
        return new ShoppingCart(contProducts++, codProduct.getText(),
                productName.getText(), quantity.getValue(),
                Double.parseDouble(price.getText()));
    }

    private boolean isProductAlreadyInCart(ShoppingCart product) {
        return products.stream().anyMatch(e -> Objects.equals(e.cod(), product.cod()));
    }

    private void addToCartAndUpdateTotal(ShoppingCart product) {
        products.add(product);
        tableSale.setItems(products);
        double currentTotal = Double.parseDouble(total.getText()) + product.total();
        total.setText(String.format("%.2f", currentTotal));
    }


    private String validateInputs() {
        if (productName.getText().isEmpty() || customerName.getText().isEmpty()) {
            return "Missing customer name or product name.";
        } else if (quantity.getValue() == 0) {
            return "Quantity can't be 0.";
        }
        return null;
    }

    private void setSerial(){
        idSale = 1+salesDAO.IdSale();
        String formattedId= String.format("%04d", idSale);
        serial.setText(formattedId);
    }


}
