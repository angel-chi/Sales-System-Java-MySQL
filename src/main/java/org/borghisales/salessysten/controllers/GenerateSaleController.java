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
import org.borghisales.salessysten.util.DiscountRate;
import org.borghisales.salessysten.util.ViewFiles;
import org.borghisales.salessysten.model.envio.Envio;
import org.borghisales.salessysten.model.envio.EnvioExpres;
import org.borghisales.salessysten.model.envio.EnvioEconomico;



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
    //cbox
    private final ObservableList<DiscountRate> discountList = FXCollections.observableArrayList(DiscountRate.CERO, DiscountRate.DIEZ,
            DiscountRate.QUINCE, DiscountRate.VEINTE, DiscountRate.CINCUENTA);

    private double finalPrice=0.0;

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
    //nuevos atributos
    @FXML
    private TextField subtotal;
    @FXML
    private TextField iva;
    @FXML
    private ComboBox<DiscountRate> cbDiscount;
    @FXML
    private TextField saving;
    //
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
    // nuevos atributos
    @FXML
    private TableColumn<ShoppingCart, Double> colSubtotal;
    @FXML
    private TableColumn<ShoppingCart, Double> colTotal;

    //static final para el IVA
    private static final double IVA_RATE = 0.16;

    @FXML
    private CheckBox chkEnvioExpres;

    @FXML
    private CheckBox checkBoxExpres;

    @FXML
    private Label labelTotal;

    @FXML
    private Label labelEnvio;

    @FXML
    private TextField amount;

    @FXML
    private TextField textFieldEnvio;

    private Sales ventaSeleccionada;

       private double getShippingCost() {
        return chkEnvioExpres.isSelected() ? 150.0 : 80.0;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeUIElements();
        configureAlerts();
        configureTable();
    }

    private void initializeUIElements() {
        total.setText("0.0");
        saving.setText("0.0");
        subtotal.setText("0.0");
        iva.setText("0.0");
        // inicializar costo de envío según el checkbox
        textFieldEnvio.setText(String.format("%.2f", getShippingCost()));

        // si cambia el checkbox, actualizamos el costo de envío en el textfield
        chkEnvioExpres.selectedProperty().addListener((obs, oldV, newV) -> {
            textFieldEnvio.setText(String.format("%.2f", getShippingCost()));
        });

        setSerial();
        seller.setText(sellerName);
        date.setText(String.valueOf(now));
        initializeComboBox();

    }



    private void initializeComboBox() {
        cbDiscount.setValue(DiscountRate.CERO);
        cbDiscount.setItems(discountList);
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
        //nuevas columnas en la tabla
        colSubtotal.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().subtotal()).asObject());
        colTotal.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().total()).asObject());
       //nuevas columnas a la tabla


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
        //utilizamos la clase Tab Controller
        TabController.openNewTab(CUSTOMER_VIEW_FXML);
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
        TabController.openNewTab(ViewFiles.PRODUCT_VIEW_FXML);
    }

     public void cancel(ActionEvent actionEvent) {
        if (products.isEmpty()) return;

        MenuController.cleanCells(codCustomer,codProduct,customerName,productName,price,stock);
        quantity.getValueFactory().setValue(null);
        tableSale.getItems().clear();
        products.clear();

        // reset envío
        textFieldEnvio.setText(String.format("%.2f", getShippingCost()));

        MenuController.setAlert(Alert.AlertType.INFORMATION,"Venta cancelada");
        total.setText("0.0");
    }

    public void generateSale(ActionEvent actionEvent) {
        if (products.isEmpty()) {
            MenuController.setAlert(Alert.AlertType.WARNING, "No hay productos en el carrito.");
            return;
        }

        try {
            // total sin envío (el que ya tienes)
            double totalSinEnvio = Double.parseDouble(total.getText().replace(",", "."));


            // costo de envío según el checkbox
            double shippingCost = getShippingCost();

            // mostrar envío al usuario
            textFieldEnvio.setText(String.format("%.2f", shippingCost));

            // total final con envío
            double totalConEnvio = totalSinEnvio + shippingCost;

            // mostrar total final
            total.setText(String.format("%.2f", totalConEnvio));

            // crear objeto Sales con total FINAL
            Sales sale = createSalesObject();

            this.ventaSeleccionada = sale;

            // guardar en BD
            if (saveSaleAndDetails(sale)) {
                productDAO.subtractStock(products);
                cleanFieldsAndTable();
                setSerial();
                total.setText("0.0");
                products.clear();
                updateReportsController();
                MenuController.setAlert(Alert.AlertType.INFORMATION, "Venta generada correctamente.");
            } else {
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al guardar la venta o los detalles.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            MenuController.setAlert(Alert.AlertType.ERROR,
                    "Ocurrió un error al generar la venta: " + e.getMessage());
        }
    }

    // modificar para crear objeto de sales
    private Sales createSalesObject() {

        //idCustomer, idSeller, numberSales, saleDate, subtotal, state, total, ivaRate, saving)
        return new Sales(customer.idCustomer(), idSeller, serial.getText(),
                LocalDate.parse(date.getText()), Double.parseDouble(subtotal.getText()),Sales.State.ACTIVE, Double.parseDouble(total.getText()),
                Double.parseDouble(iva.getText()), Double.parseDouble(saving.getText()) );

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
        subtotal.setText("0.0");
        saving.setText("0.0");
        iva.setText("0.0");
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

        addToCartAndUpdateTotals(product);
    }

    private ShoppingCart createShoppingCartObject() {
        return new ShoppingCart(contProducts++, codProduct.getText(),
                productName.getText(), quantity.getValue(),
                Double.parseDouble(price.getText()), CalcSaving(),CalcSubtotal()*IVA_RATE, CalcSubtotal(), CalcTotal());
    }

    private double CalcSubtotal (){
        double subtotal = quantity.getValue() * Double.parseDouble(price.getText());
        return Round(subtotal);
    }
    private double CalcTotal (){
        double subtotal = CalcSubtotal();
        double subtotalIva = subtotal * (1 + IVA_RATE);
        double discountAmount = subtotalIva * getDiscountRate();
        double total = subtotalIva - discountAmount;
        return Round(total);
    }
    private double CalcSaving (){
        double subtotal = CalcSubtotal();
        double subtotalIva = subtotal * (1 + IVA_RATE);
        double discountAmount = subtotalIva * getDiscountRate();
        return discountAmount;
    }
    private double Round(double num){
        String roundStr = String.format("%.2f",num);
        double round = Double.parseDouble(roundStr);
        return round;
    }

    private boolean isProductAlreadyInCart(ShoppingCart product) {
        return products.stream().anyMatch(e -> Objects.equals(e.cod(), product.cod()));
    }

    private void addToCartAndUpdateTotals(ShoppingCart product) {
        products.add(product);
        tableSale.setItems(products);
        // actualizar totales
        double currentSubTotal = Double.parseDouble(subtotal.getText()) + product.subtotal();
        subtotal.setText(String.format("%.2f", currentSubTotal));
        double currentTotal = Double.parseDouble(total.getText()) + product.total();

        total.setText(String.format("%.2f", currentTotal )); //campo total de todas las ventas
        iva.setText(String.format("%.2f", currentSubTotal * IVA_RATE));
        saving.setText(String.format("%.2f", CalcSaving()));
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

    //nuevo metodo para el valor del descuento
    private double getDiscountRate(){
        DiscountRate dis = cbDiscount.getValue();
        return dis.getRate();
    }
     
    private Envio obtenerEnvio() {
        if (chkEnvioExpres.isSelected()) {
            return new EnvioExpres();
        }
        return new EnvioEconomico();
    }

    private void calcularTotalVenta() {
        Envio envio;
        if (checkBoxExpres.isSelected()) {
            envio = new EnvioExpres();
        } else {
            envio = new EnvioEconomico();
        }

        VentaConEnvio ventaConEnvio = new VentaConEnvio(ventaSeleccionada, envio);

        labelEnvio.setText(String.format("Costo de envío: $%.2f", envio.calcularCosto()));

        labelTotal.setText(String.format("Total: $%.2f", ventaConEnvio.calcularTotal()));
    }

    public void setVentaSeleccionada(Sales venta) {
        this.ventaSeleccionada = venta;

        // Inicializar valores
        actualizarMontos();

        // Escuchar cambios en el CheckBox para actualizar dinámicamente
        chkEnvioExpres.selectedProperty().addListener((observable, oldValue, newValue) -> actualizarMontos());
    }

    private void actualizarMontos() {
        if (ventaSeleccionada == null) return;

        Envio envio = chkEnvioExpres.isSelected() ? new EnvioExpres() : new EnvioEconomico();
        VentaConEnvio ventaConEnvio = new VentaConEnvio(ventaSeleccionada, envio);

        // Mostrar monto del envío en el TextField
        textFieldEnvio.setText(String.format("%.2f", envio.calcularCosto()));

        // Mostrar total venta + envío
        total.setText(String.format("%.2f", ventaConEnvio.calcularTotal()));
    }

}
