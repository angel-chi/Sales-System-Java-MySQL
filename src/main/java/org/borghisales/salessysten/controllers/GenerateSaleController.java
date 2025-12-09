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

public class GenerateSaleController extends MenuController implements Initializable,validacionEntrada{
    // Variables
    private final SalesDAO salesDAO = new SalesDAO();
    private int idSale;

    private static int contProducts =1;
    public static final double IVA = 0.16;
    private static ObservableList<ShoppingCart> products;

    private static String sellerName;
    private static int idVendedor;

    private final LocalDate now = LocalDate.now();

    private Scene scene = null;
    private Stage stage;

    private final Alert alertCustomer = new Alert(Alert.AlertType.WARNING);
    private final Alert alertProduct = new Alert(Alert.AlertType.WARNING);
    private final ButtonType buttonTypeAccept = new ButtonType("SI");
    private final ButtonType buttonTypeCancel = new ButtonType("NO");

    private final CustomerDAO customerDAO = new CustomerDAO();

    private Customer cliente;
    private final ProductDAO productDAO = new ProductDAO();
    private double subtotalActual =0;

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
    private TextField total1;   // IVA
    @FXML
    private TextField total11;  // TOTAL con IVA
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
    // Métodos de configuración
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeUIElements();
        configureAlerts();
        configureTable();
    }
    // Inicializar elementos de la interfaz de usuario.
    private void initializeUIElements() {
        total.setText("0.0");    // Subtotal
        total1.setText("0.0");   // IVA
        total11.setText("0.0");  // Total con IVA
        setSerial();
        seller.setText(sellerName);
        date.setText(String.valueOf(now));
    }

    // Dos alertas. Se usan cuando no se encuentra cliente o producto.
    private void configureAlerts() {
        configureAlert(alertCustomer, "Nuevo cliente.", "El cliente no existe.", "¿Desea agregarlo?");
        configureAlert(alertProduct, "Nuevo producto.", "El producto no existe.", "¿Desea agregarlo?");
    }
    // Vacía la tabla y mapea las columnas con los datos de Shopping Cart.
    private void configureTable() {
        configureTableColumns();
        tableSale.getItems().clear();
        products = FXCollections.observableArrayList();
    }
    // Configura una alerta.
    private void configureAlert(Alert alert, String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getButtonTypes().setAll(buttonTypeAccept, buttonTypeCancel);
    }
    // Configura tableSale.
    private void configureTableColumns() {
        colNro.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().nr()).asObject());
        colCod.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().cod()));
        colProduct.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().product()));
        colQuantity.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().quantity()).asObject());
        colPrice.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().price()).asObject());
        colTotal.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().total() * (1 + IVA)).asObject()
        );

    }
    public void searchCustomer(ActionEvent actionEvent) {

        // Campo vacío
        if (campoVacio(codCustomer)) {
            mostrarAdvertencia("Debes ingresar una identificación de cliente.");
            return;
        }

        // No numérico
        int customerId;
        try {
            customerId = Integer.parseInt(codCustomer.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAdvertencia("La identificación del cliente debe ser un número válido.");
            return;
        }

        cliente = customerDAO.searchCustomer(customerId);

        if (cliente != null) {
            setAlert(Alert.AlertType.CONFIRMATION,
                    "Cliente encontrado: " + cliente.name());
            customerName.setText(cliente.name());
        } else {
            handleCustomerNotFound();
        }
    }

    // Si no existe el cliente, recomienda agregarlo.
    private void handleCustomerNotFound() {
        alertCustomer.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeAccept) {
                openCustomerManagementView();
            }
        });
    }
    // Agregando dicho cliente.
    private void openCustomerManagementView() {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource(CUSTOMER_VIEW_FXML));

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage = new Stage();
        stage.setTitle("Administrar Cliente");
        stage.setScene(scene);
        stage.show();
    }
    // Buscar un producto.
    public void searchProduct(ActionEvent actionEvent) {

        if (campoVacio(codProduct)) {
            mostrarAdvertencia("Debes ingresar un código de producto.");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(codProduct.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAdvertencia("El código de producto debe ser un número válido.");
            return;
        }

        Product product = productDAO.searchProduct(productId);

        if (product != null) {
            updateProductFields(product);
        } else {
            handleProductNotFound();
        }
    }

    // Actualizar la información de un producto, si existe.
    private void updateProductFields(Product product) {
        setAlert(Alert.AlertType.CONFIRMATION, "Producto Encontrado: " + product.name());
        productName.setText(product.name());
        stock.setText(String.valueOf(product.stock()));
        price.setText(String.valueOf(product.price()));

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, product.stock(), 0);
        quantity.setValueFactory(valueFactory);
    }
    // Si no existe, recomienda agregarlo.
    private void handleProductNotFound() {
        alertProduct.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeAccept) {
                openProductManagementView();
            }
        });
    }
    // Agregando dciho producto.
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
    // Cancelar una acción.
    public void cancel(ActionEvent actionEvent) {
        if (products.isEmpty())return;
        MenuController.cleanCells(codCustomer,codProduct,customerName,productName,price,stock);
        quantity.getValueFactory().setValue(null);
        tableSale.getItems().clear();
        MenuController.setAlert(Alert.AlertType.INFORMATION,"Venta cancelada");
        //Se elimina el TotalClear();
        products.clear();
        total.setText("0.0");
        total1.setText("0.0");
        total11.setText("0.0");
        subtotalActual = 0;
    }
    // Este es el proceso de generar una venta.
    public void generateSale(ActionEvent actionEvent) {
        if (products.isEmpty()) {
            return;
        }
        Sales sales = createSalesObject();
        if (saveSaleAndDetails(sales)) {
            productDAO.subtractStock(products);
            cleanFieldsAndTable();
            setSerial();

            subtotalActual = 0;
            total.setText("0.0");
            total1.setText("0.0");
            total11.setText("0.0");
            products.clear();
            updateReportsController();
        }

    }

    private Sales createSalesObject() {
        double ivaActual = subtotalActual * IVA;
        double totalConIva = subtotalActual + ivaActual;
        return new Sales(
                cliente.idCustomer(),
                idVendedor,
                serial.getText(),
                LocalDate.parse(date.getText()),
                totalConIva,              // ← aquí guardas total con IVA
                Sales.State.ACTIVE
        );
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

    public static void setIdVendedor(int idVendedor) {
        GenerateSaleController.idVendedor = idVendedor;
    }

    // Agrega un producto a la tabla de venta.
    public void addShoppingCart(ActionEvent actionEvent) {
        String errorMessage = validateInputs();

        if (errorMessage != null) {
            MenuController.setAlert(Alert.AlertType.ERROR, errorMessage);
            return;
        }

        ShoppingCart product = createShoppingCartObject();

        if (isProductAlreadyInCart(product)) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Este producto ya está en tu carrito de compra.");
            return;
        }

        addToCartAndUpdateTotal(product);
    }
    // Creando un objeto ShoppingCart
    private ShoppingCart createShoppingCartObject() {
        return new ShoppingCart(contProducts++, codProduct.getText(),
                productName.getText(), quantity.getValue(),
                Double.parseDouble(price.getText()));
    }
    // ¿El producto ya está en el carrito?
    private boolean isProductAlreadyInCart(ShoppingCart product) {
        return products.stream().anyMatch(e -> Objects.equals(e.cod(), product.cod()));
    }
    // Agregar al carrito y actualizar el total.
    private void addToCartAndUpdateTotal(ShoppingCart product) {
        products.add(product);
        tableSale.setItems(products);
        // subtotalActual acumula el total de los productos sin IVA
        subtotalActual += product.total();
        double ivaActual = subtotalActual * IVA;
        double totalConIva = subtotalActual + ivaActual;
        // Subtotal
        total.setText(String.format("%.2f", subtotalActual));
        // IVA
        total1.setText(String.format("%.2f", ivaActual));
        // Total final con IVA
        total11.setText(String.format("%.2f", totalConIva));
    }

    // Validando datos.
    private String validateInputs() {
        if (productName.getText().isEmpty() || customerName.getText().isEmpty()) {
            return "Nombre de cliente/producto faltantes.";
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
