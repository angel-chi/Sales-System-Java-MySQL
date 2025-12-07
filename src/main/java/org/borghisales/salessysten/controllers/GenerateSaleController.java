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
    }

    private void initializeUIElements() {
        //quantity.getValueFactory().setValue(0);
        total.setText("0.0");
        setSerial();
        seller.setText(sellerName);
        date.setText(String.valueOf(now));
    }

    private void configureAlerts() {
        configureAlert(alertCustomer, "Cliente Nuevo", "El cliente no existe", "¿Quieres agregarlo?");
        configureAlert(alertProduct, "Producto Nuevo", "El producto no existe", "¿Quieres agregarlo?");
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
        try{
            if(codCustomer.getText() != null && !codCustomer.getText().isBlank()){
                int customerId = Integer.parseInt(codCustomer.getText());
                customer = customerDAO.searchCustomer(customerId);

                if (customer != null) {
                    setAlert(Alert.AlertType.CONFIRMATION, "Ciente encontrado: " + customer.name());
                    customerName.setText(customer.name());
                } else {
                    handleCustomerNotFound();
                }
            }
            if(customerName.getText() != null && !customerName.getText().isBlank()){
                String customerName = this.customerName.getText();
                customer = customerDAO.searchCustomerName(customerName);

                if(customer != null){
                    setAlert(Alert.AlertType.CONFIRMATION, "Cliente encontrado: " + customer.idCustomer());
                    codCustomer.setText(String.valueOf(customer.idCustomer()));
                }else{
                    handleCustomerNotFound();
                }
            }

        }catch (NumberFormatException e){
            codCustomer.clear();
            customerName.clear();
            MenuController.setAlert(Alert.AlertType.ERROR,"El codigo del cliente debe ser un numero");
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
        stage.setTitle("Gestionar Cliente");
        stage.setScene(scene);
        stage.show();
    }


    public void searchProduct(ActionEvent actionEvent) {
        try {
            int productId = Integer.parseInt(codProduct.getText());
            Product product = productDAO.searchProduct(productId);

            if (product != null) {
                updateProductFields(product);
            } else {
                handleProductNotFound();
            }
        }catch (NumberFormatException e){
            MenuController.setAlert(Alert.AlertType.ERROR,"El codigo del producto debe ser un numero");
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
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource(PRODUCT_VIEW_FXML));

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage = new Stage();
        stage.setTitle("Gestionar Producto");
        stage.setScene(scene);
        stage.show();
    }


    public void cancel(ActionEvent actionEvent) {
        if (products.isEmpty())return;
        MenuController.cleanCells(codCustomer,codProduct,customerName,productName,price,stock);
        quantity.getValueFactory().setValue(null);
        tableSale.getItems().clear();
        MenuController.setAlert(Alert.AlertType.INFORMATION,"Venta Cancelada");
        total.clear();
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
                LocalDate.parse(date.getText()), Double.parseDouble(total.getText()),
                Sales.State.ACTIVE);
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
            MenuController.setAlert(Alert.AlertType.ERROR, "Este producto ya se encuentra en tu carrito de compras");
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
            return "Nombre de cliente o de producto faltante.";
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
