package org.borghisales.salessysten.controllers;

import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.borghisales.salessysten.model.*;
import org.borghisales.salessysten.model.payment.CardPayment;
import org.borghisales.salessysten.model.payment.CashPayment;
import org.borghisales.salessysten.model.payment.Payment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class GenerateSaleController extends MenuController implements Initializable {

    private final SalesDAO salesDAO = new SalesDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ProductDAO productDAO = new ProductDAO();

    // --- VARIABLES DE ESTADO (YA NO SON STATIC LA LISTA NI EL CONTADOR) ---
    /*metodo idSale que actualice cada que se hace una venta*/
    private int idSale;
    private int contProducts = 1;
    private ObservableList<ShoppingCart> products; // Instancia local, se limpia al salir

    // Estos sí pueden ser static si vienen del Login (Sesión)
    private static String sellerName;
    private static int idSeller;

    private final LocalDate now = LocalDate.now();
    private Scene scene = null;
    private Stage stage;

    // Alertas
    private final Alert alertCustomer = new Alert(Alert.AlertType.WARNING);
    private final Alert alertProduct = new Alert(Alert.AlertType.WARNING);
    private final ButtonType buttonTypeAccept = new ButtonType("SÍ");
    private final ButtonType buttonTypeCancel = new ButtonType("NO");

    private Customer customer;

    // --- FXML UI Elements ---
    @FXML private Label lblSerial; // Cambié TextField a Label para el serial
    @FXML private TextField codCustomer;
    @FXML private TextField codProduct;
    @FXML private TextField price;
    @FXML private TextField customerName;
    @FXML private TextField productName;
    @FXML private TextField stock;
    @FXML private Label lblSeller; // Cambié TextField a Label
    @FXML private TextField total;
    @FXML private Label lblDate;   // Cambié TextField a Label
    @FXML private Spinner<Integer> quantity;

    @FXML private ComboBox<String> paymentMethod;
    @FXML private TextField cardLast4;
    @FXML private VBox cardDetailsContainer; // Contenedor para ocultar lo de la tarjeta

    // Botones (Para animaciones)
    @FXML private Button btnSearchCust;
    @FXML private Button btnSearchProd;
    @FXML private Button btnAdd;
    @FXML private Button btnCancel;
    @FXML private Button btnPay;
    @FXML private Button btnReturn;

    // Tabla
    @FXML private TableView<ShoppingCart> tableSale;
    @FXML private TableColumn<ShoppingCart,Integer> colNro;
    @FXML private TableColumn<ShoppingCart,String> colCod;
    @FXML private TableColumn<ShoppingCart,String> colProduct;
    @FXML private TableColumn<ShoppingCart, Integer> colQuantity;
    @FXML private TableColumn<ShoppingCart, Double> colPrice;
    @FXML private TableColumn<ShoppingCart,Double> colTotal;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Inicializar Estado
        products = FXCollections.observableArrayList(); // Lista nueva cada vez
        contProducts = 1;

        initializeUIElements();
        configureAlerts();
        configureTable();
        setupAnimations();

        // Listener para ocultar/mostrar campo de tarjeta
        paymentMethod.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("CARD".equals(newVal)) {
                cardDetailsContainer.setVisible(true);
                cardDetailsContainer.setManaged(true);
            } else {
                cardDetailsContainer.setVisible(false);
                cardDetailsContainer.setManaged(false);
                cardLast4.clear();
            }
        });
    }

    private void setupAnimations() {
        // Estilos para botones
        String searchStyle = "-fx-background-color: #1565c0; -fx-text-fill: white; -fx-background-radius: 5;";
        String searchHover = "-fx-background-color: #42a5f5; -fx-text-fill: white; -fx-background-radius: 5;";

        String addStyle = "-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;";
        String addHover = "-fx-background-color: #66bb6a; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;";

        String payStyle = "-fx-background-color: #e65100; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 14px; -fx-font-weight: bold;";
        String payHover = "-fx-background-color: #ff9800; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 14px; -fx-font-weight: bold;";

        String cancelStyle = "-fx-background-color: #424242; -fx-text-fill: #aaa; -fx-background-radius: 5;";
        String cancelHover = "-fx-background-color: #c62828; -fx-text-fill: white; -fx-background-radius: 5;";

        String returnStyle = "-fx-background-color: transparent; -fx-text-fill: #ffa726; -fx-border-color: #fb8c00; -fx-border-radius: 5;";
        String returnHover = "-fx-background-color: #fb8c00; -fx-text-fill: white; -fx-border-color: #fb8c00; -fx-border-radius: 5;";

        setupButtonHover(btnSearchCust, searchStyle, searchHover);
        setupButtonHover(btnSearchProd, searchStyle, searchHover);
        setupButtonHover(btnAdd, addStyle, addHover);
        setupButtonHover(btnPay, payStyle, payHover);
        setupButtonHover(btnCancel, cancelStyle, cancelHover);
        setupButtonHover(btnReturn, returnStyle, returnHover);
    }

    private void setupButtonHover(Button btn, String normal, String hover) {
        if(btn == null) return;
        btn.setCursor(Cursor.HAND);
        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> { btn.setStyle(hover); scaleButton(btn, 1.05); });
        btn.setOnMouseExited(e -> { btn.setStyle(normal); scaleButton(btn, 1.0); });
    }

    private void scaleButton(Button btn, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), btn);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }

    private void initializeUIElements() {
        total.setText("0.00");
        setSerial();
        lblSeller.setText(sellerName != null ? sellerName : "Desconocido");
        lblDate.setText(now.toString());

        quantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));
        quantity.setDisable(true); // Desactivado hasta que haya producto

        if (paymentMethod != null) {
            paymentMethod.setItems(FXCollections.observableArrayList("CASH", "CARD"));
            paymentMethod.setValue("CASH");
            cardDetailsContainer.setVisible(false);
            cardDetailsContainer.setManaged(false);
        }
    }

    private void configureAlerts() {
        configureAlert(alertCustomer, "Nuevo Cliente", "El cliente no existe", "¿Desea registrarlo?");
        configureAlert(alertProduct, "Nuevo Producto", "El producto no existe", "¿Desea registrarlo?");
    }

    private void configureTable() {
        configureTableColumns();
        tableSale.setItems(products);
        tableSale.setPlaceholder(new Label("El carrito está vacío"));
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

        // Formato moneda
        colPrice.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().price()).asObject());
        colPrice.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText((empty || price == null) ? null : String.format("$ %.2f", price));
            }
        });

        colTotal.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().total()).asObject());
        colTotal.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Double total, boolean empty) {
                super.updateItem(total, empty);
                setText((empty || total == null) ? null : String.format("$ %.2f", total));
                setStyle("-fx-text-fill: #a5d6a7; -fx-font-weight: bold;");
            }
        });
    }

    // --- ACCIONES DE BOTONES ---

    @FXML
    public void returnToMenu(ActionEvent event) {
        openNewStage(MANAGEMENT_VIEW_FXML, "Management");
        closeCurrentStage(btnReturn);
    }

    public void searchCustomer(ActionEvent actionEvent) {
        if(codCustomer.getText().isEmpty()) return;
        try {
            int customerId = Integer.parseInt(codCustomer.getText());
            customer = customerDAO.searchCustomer(customerId);

            if (customer != null) {
                customerName.setText(customer.name());
            } else {
                handleCustomerNotFound();
            }
        } catch (NumberFormatException e) {
            setAlert(Alert.AlertType.ERROR, "ID de cliente inválido");
        }
    }

    private void handleCustomerNotFound() {
        alertCustomer.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeAccept) openCustomerManagementView();
        });
    }

    private void openCustomerManagementView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource(CUSTOMER_VIEW_FXML));
            stage = new Stage();
            stage.setTitle("Gestión de Clientes");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchProduct(ActionEvent actionEvent) {
        if(codProduct.getText().isEmpty()) return;
        try {
            int productId = Integer.parseInt(codProduct.getText());
            Product product = productDAO.searchProduct(productId);

            if (product != null) {
                updateProductFields(product);
            } else {
                handleProductNotFound();
            }
        } catch (NumberFormatException e) {
            setAlert(Alert.AlertType.ERROR, "ID de producto inválido");
        }
    }

    private void updateProductFields(Product product) {
        productName.setText(product.name());
        stock.setText(String.valueOf(product.stock()));
        price.setText(String.valueOf(product.price()));

        // --- ACTIVACIÓN DEL SPINNER ---
        if (product.stock() > 0) {
            quantity.setDisable(false); // Lo activamos
            // Configuramos: Mínimo 1, Máximo = Stock, Valor Inicial = 1
            SpinnerValueFactory<Integer> valueFactory =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, product.stock(), 1);
            quantity.setValueFactory(valueFactory);
        } else {
            // Si no hay stock, lo dejamos desactivado y avisamos
            quantity.setDisable(true);
            quantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));
            MenuController.setAlert(Alert.AlertType.WARNING, "Producto Agotado (Stock 0)");
        }
    }

    private void handleProductNotFound() {
        alertProduct.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeAccept) openProductManagementView();
        });
    }

    private void openProductManagementView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource(PRODUCT_VIEW_FXML));
            stage = new Stage();
            stage.setTitle("Gestión de Productos");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addShoppingCart(ActionEvent actionEvent) {
        String errorMessage = validateInputs();
        if (errorMessage != null) {
            MenuController.setAlert(Alert.AlertType.ERROR, errorMessage);
            return;
        }

        ShoppingCart product = createShoppingCartObject();

        if (isProductAlreadyInCart(product)) {
            MenuController.setAlert(Alert.AlertType.WARNING, "Este producto ya está en el carrito");
            return;
        }

        addToCartAndUpdateTotal(product);
        // Limpiar inputs de producto para el siguiente
        codProduct.clear();
        productName.clear();
        price.clear();
        stock.clear();
        quantity.getValueFactory().setValue(0);
        codProduct.requestFocus(); // Focus listo para el siguiente
    }

    public void cancel(ActionEvent actionEvent) {
        if (products.isEmpty()) return;
        MenuController.cleanCells(codCustomer,codProduct,customerName,productName,price,stock);
        quantity.getValueFactory().setValue(0);
        products.clear();
        contProducts = 1;
        total.setText("0.00");
    }

    public void generateSale(ActionEvent actionEvent) {
        if (products.isEmpty()) {
            MenuController.setAlert(Alert.AlertType.WARNING, "El carrito está vacío");
            return;
        }

        // Usamos una variable interna para el total, más segura
        double totalAmount = products.stream().mapToDouble(ShoppingCart::total).sum();

        Payment payment = createPayment(totalAmount);
        if (payment == null) return;
        if (!payment.authorize()) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Pago no autorizado.\n" + payment.getDescription());
            return;
        }

        Sales.PaymentType paymentType = "CASH".equals(paymentMethod.getValue()) ? Sales.PaymentType.CASH : Sales.PaymentType.CARD;

        Sales sales = new Sales(
                customer.idCustomer(),
                idSeller,
                lblSerial.getText(),
                LocalDate.now(), // Usar fecha real
                totalAmount,
                Sales.State.ACTIVE,
                paymentType
        );

        if (saveSaleAndDetails(sales)) {
            productDAO.subtractStock(products);

            MenuController.setAlert(Alert.AlertType.INFORMATION, "¡Venta completada!\n" + payment.getDescription());

            // RESETEAR TODO
            cancel(null); // Reutilizamos el método de limpiar
            setSerial();
            updateReportsController();
        }
    }

    // ... (El resto de métodos auxiliares createSalesObject, createPayment, etc. se mantienen igual o se adaptan ligeramente) ...
    // Asegúrate de copiar el resto de tu lógica de validación aquí abajo.

    // NOTA: Métodos auxiliares necesarios
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
        double currentTotal = products.stream().mapToDouble(ShoppingCart::total).sum();
        total.setText(String.format("%.2f", currentTotal));
    }

    private String validateInputs() {
        if (productName.getText().isEmpty() || customerName.getText().isEmpty()) return "Faltan datos del cliente o producto.";
        if (quantity.getValue() == 0) return "La cantidad debe ser mayor a 0.";
        return null;
    }
    /*HAY QUE VALIDAR SI (CUANDO SE HACE UNA VENTA) */
    private void setSerial(){
        idSale = 1 + salesDAO.IdSale();
        lblSerial.setText(String.format("%04d", idSale));
    }

    private boolean saveSaleAndDetails(Sales sales) {
        boolean saleSaved = salesDAO.SaveSale(sales);
        boolean detailsSaved = salesDAO.SaveDetailsSale(products, idSale);
        return saleSaved && detailsSaved;
    }

    private Payment createPayment(double totalAmount) {
        String method = paymentMethod.getValue();
        if ("CASH".equals(method)) return new CashPayment(totalAmount);
        else if ("CARD".equals(method)) {
            String last4 = cardLast4.getText();
            if (last4 == null || last4.isBlank() || last4.length() != 4) {
                MenuController.setAlert(Alert.AlertType.ERROR, "Ingrese los últimos 4 dígitos válidos.");
                return null;
            }
            return new CardPayment(totalAmount, last4);
        }
        return null;
    }

    private void updateReportsController() {
        ReportsController.setSales(null);
        ReportsController.setPieChartData(null);
        ReportsController.setLineChartData(null);
    }

    public static void setSellerName(String sellerName) { GenerateSaleController.sellerName = sellerName; }
    public static void setIdSeller(int idSeller) { GenerateSaleController.idSeller = idSeller; }
}
