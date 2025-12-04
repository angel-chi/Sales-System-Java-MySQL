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
import org.apache.commons.collections4.splitmap.AbstractIterableGetMapDecorator;
import org.borghisales.salessysten.model.*; // Importación general para nuevas clases

//Para que se tome siempre los números con punto decimal.
import java.util.Locale;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class GenerateSaleController extends MenuController implements Initializable {

    private final VentaDAO ventaDAO = new VentaDAO(); // SalesDAO -> VentaDAO
    private int idVenta; // idSale -> idVenta

    private static int contProductos =1; // contProducts -> contProductos
    private static ObservableList<CarritoCompra> productosEnCarrito; // products -> productosEnCarrito, ShoppingCart -> CarritoCompra

    private static String nombreVendedor; // sellerName -> nombreVendedor
    private static int idVendedor; // idSeller -> idVendedor

    private final LocalDate now = LocalDate.now();


    private Scene scene = null;
    private Stage stage;

    private final Alert alertCliente = new Alert(Alert.AlertType.WARNING); // alertCustomer -> alertCliente
    private final Alert alertProducto = new Alert(Alert.AlertType.WARNING); // alertProduct -> alertProducto
    private final ButtonType buttonTypeAccept = new ButtonType("SÍ");
    private final ButtonType buttonTypeCancel = new ButtonType("NO");


    private final ClienteDAO clienteDAO = new ClienteDAO(); // CustomerDAO -> ClienteDAO

    private Cliente cliente; // Customer -> Cliente
    private final ProductoDAO productoDAO = new ProductoDAO(); // ProductDAO -> ProductoDAO

    @FXML
    private TextField serial;
    @FXML
    private TextField codCliente; // codCustomer -> codCliente
    @FXML
    private TextField codProducto; // codProduct -> codProducto
    @FXML
    private TextField price;
    @FXML
    private TextField nombreCliente; // customerName -> nombreCliente
    @FXML
    private TextField nombreProducto; // productName -> nombreProducto
    @FXML
    private TextField existencia; // stock -> existencia
    @FXML
    private TextField vendedor; // seller -> vendedor
    @FXML
    private TextField total;
    @FXML
    private TextField date;

    @FXML
    private Spinner<Integer> quantity;
    @FXML
    private TableView<CarritoCompra> tableSale; // ShoppingCart -> CarritoCompra
    @FXML
    private TableColumn<CarritoCompra,Integer> colNro; // ShoppingCart -> CarritoCompra
    @FXML
    private TableColumn<CarritoCompra,String> colCod; // ShoppingCart -> CarritoCompra
    @FXML
    private TableColumn<CarritoCompra,String> colProduct; // ShoppingCart -> CarritoCompra
    @FXML
    private TableColumn<CarritoCompra, Integer> colQuantity; // ShoppingCart -> CarritoCompra
    @FXML
    private TableColumn<CarritoCompra, Double> colPrice; // ShoppingCart -> CarritoCompra
    @FXML
    private TableColumn<CarritoCompra,Double> colTotal;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeUIElements();
        configureAlerts();
        configureTable();
    }

    private void initializeUIElements() {
        //Se setea con decimal
        total.setText(String.format(Locale.US, "%.2f", 0.0));
        setSerial();
        vendedor.setText(nombreVendedor); // seller -> vendedor, sellerName -> nombreVendedor
        date.setText(String.valueOf(now));
    }

    private void configureAlerts() {
        configureAlert(alertCliente, "Nuevo cliente", "El cliente no existe", "¿Quieres añadirlo?"); // alertCustomer -> alertCliente, mensajes traducidos
        configureAlert(alertProducto, "Nuevo producto", "El producto no existe", "¿Quieres añadirlo?"); // alertProduct -> alertProducto, mensajes traducidos
    }

    private void configureTable() {
        configureTableColumns();
        tableSale.getItems().clear();
        productosEnCarrito = FXCollections.observableArrayList(); // products -> productosEnCarrito
    }


    private void configureAlert(Alert alert, String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getButtonTypes().setAll(buttonTypeAccept, buttonTypeCancel);
    }

    private void configureTableColumns() {
        colNro.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().numero()).asObject()); // nr -> numero
        colCod.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().codigo())); // cod -> codigo
        colProduct.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().producto())); // product -> producto
        colQuantity.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().cantidad()).asObject()); // quantity -> cantidad
        colPrice.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().precio()).asObject()); // price -> precio
        colTotal.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().total()).asObject());
    }

    public void searchCustomer(ActionEvent actionEvent) {
        cliente = clienteDAO.searchCliente(codCliente.getText()); // customer -> cliente, customerDAO -> clienteDAO, searchCustomer -> searchCliente

        if (cliente != null) {
            setAlert(Alert.AlertType.CONFIRMATION, "Cliente encontrado: " + cliente.nombre()); // cliente.name() -> cliente.nombre()
            nombreCliente.setText(cliente.nombre()); // customerName -> nombreCliente
        } else {
            handleCustomerNotFound();
        }
    }

    private void handleCustomerNotFound() {
        alertCliente.showAndWait().ifPresent(buttonType -> { // alertCustomer -> alertCliente
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
        stage.setTitle("Gestionar Cliente"); // Título traducido
        stage.setScene(scene);
        stage.show();
    }


    public void searchProduct(ActionEvent actionEvent) {
        int productoId = Integer.parseInt(codProducto.getText()); // productId -> productoId, codProduct -> codProducto
        Producto producto = productoDAO.searchProducto(productoId); // Product -> Producto, productDAO -> productoDAO, searchProduct -> searchProducto

        if (producto != null) {
            updateProductFields(producto);
        } else {
            handleProductNotFound();
        }
    }

    private void updateProductFields(Producto producto) { // Product -> Producto
        setAlert(Alert.AlertType.CONFIRMATION, "Producto encontrado: " + producto.nombre()); // product.name() -> producto.nombre()
        nombreProducto.setText(producto.nombre()); // productName -> nombreProducto
        existencia.setText(String.valueOf(producto.existencia())); // stock -> existencia, product.stock() -> producto.existencia()
        price.setText(String.valueOf(producto.precio())); // product.price() -> producto.precio()

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, producto.existencia(), 0); // product.stock() -> producto.existencia()
        quantity.setValueFactory(valueFactory);
    }

    private void handleProductNotFound() {
        alertProducto.showAndWait().ifPresent(buttonType -> { // alertProduct -> alertProducto
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
        stage.setTitle("Gestionar Producto"); // Título traducido
        stage.setScene(scene);
        stage.show();
    }


    public void cancel(ActionEvent actionEvent) {
        if (productosEnCarrito.isEmpty())return; // products -> productosEnCarrito
        MenuController.cleanCells(codCliente,codProducto,nombreCliente,nombreProducto,price,existencia); // codCustomer -> codCliente, codProduct -> codProducto, customerName -> nombreCliente, productName -> nombreProducto, stock -> existencia
        quantity.getValueFactory().setValue(null);
        tableSale.getItems().clear();
        MenuController.setAlert(Alert.AlertType.INFORMATION,"Venta Cancelada");
        //En vez del total.clear() mejor se deja la variable en 0.0 para que no tengamos problemas con contenido vacío
        total.setText(String.format(Locale.US, "%.2f", 0.0));
    }

    public void generateSale(ActionEvent actionEvent) {
        if (productosEnCarrito.isEmpty()) { // products -> productosEnCarrito
            return;
        }

        Venta venta = createSalesObject(); // Sales -> Venta

        if (saveSaleAndDetails(venta)) { // sales -> venta
            productoDAO.subtractStock(productosEnCarrito); // productDAO -> productoDAO, products -> productosEnCarrito
            cleanFieldsAndTable();
            setSerial();
            total.setText("0.0");
            productosEnCarrito.clear(); // products -> productosEnCarrito
            updateReportsController();
        }
    }

    private Venta createSalesObject() { // Sales -> Venta
        //Se reemplaza la coma por punto decimal
        String totalText = total.getText().trim().replace(',', '.');
        //Se convierte a double
        double totalValue = Double.parseDouble(totalText);
        return new Venta(cliente.idCliente(), idVendedor, serial.getText(), // Customer -> Cliente, idCustomer() -> idCliente(), idSeller -> idVendedor
                LocalDate.parse(date.getText()), totalValue,
                Venta.Estado.ACTIVO); // Sales.State.ACTIVE -> Venta.Estado.ACTIVO
    }

    private boolean saveSaleAndDetails(Venta venta) { // Sales -> Venta
        boolean saleSaved = ventaDAO.SaveVenta(venta); // salesDAO -> ventaDAO, SaveSale -> SaveVenta
        boolean detailsSaved = ventaDAO.SaveDetallesVenta(productosEnCarrito, idVenta); // salesDAO -> ventaDAO, SaveDetailsSale -> SaveDetallesVenta, products -> productosEnCarrito, idSale -> idVenta
        return saleSaved && detailsSaved;
    }

    private void cleanFieldsAndTable() {
        MenuController.cleanCells(codCliente, codProducto, nombreCliente, nombreProducto, price, existencia); // codCustomer -> codCliente, codProduct -> codProducto, customerName -> nombreCliente, productName -> nombreProducto, stock -> existencia
        quantity.getValueFactory().setValue(null);
        tableSale.getItems().clear();
    }

    private void updateReportsController() {
        ReportsController.setVentas(null);
        ReportsController.setDatosGraficoCircular(null);
        ReportsController.setDatosGraficoLinea(null);
        ReportsController.eliminarCacheGraficoLinea(now.getYear(), now.getMonth().getValue());
    }


    public static void setNombreVendedor(String nombreVendedor) { // sellerName -> nombreVendedor
        GenerateSaleController.nombreVendedor = nombreVendedor;
    }

    public static void setIdVendedor(int idVendedor) { // idSeller -> idVendedor
        GenerateSaleController.idVendedor = idVendedor;
    }

    public void addShoppingCart(ActionEvent actionEvent) {
        String errorMessage = validateInputs();

        if (errorMessage != null) {
            MenuController.setAlert(Alert.AlertType.ERROR, errorMessage);
            return;
        }

        CarritoCompra producto = createShoppingCartObject(); // ShoppingCart -> CarritoCompra

        if (isProductAlreadyInCart(producto)) { // product -> producto
            MenuController.setAlert(Alert.AlertType.ERROR, "Este producto ya está en tu carrito de compras"); // Mensaje traducido
            return;
        }

        addToCartAndUpdateTotal(producto); // product -> producto
    }

    private CarritoCompra createShoppingCartObject() { // ShoppingCart -> CarritoCompra
        return new CarritoCompra(contProductos++, codProducto.getText(), // contProducts -> contProductos, codProduct -> codProducto
                nombreProducto.getText(), quantity.getValue(), // productName -> nombreProducto
                Double.parseDouble(price.getText()));
    }

    private boolean isProductAlreadyInCart(CarritoCompra producto) { // ShoppingCart -> CarritoCompra, product -> producto
        return productosEnCarrito.stream().anyMatch(e -> Objects.equals(e.codigo(), producto.codigo())); // products -> productosEnCarrito, cod() -> codigo()
    }

    private void addToCartAndUpdateTotal(CarritoCompra producto) { // ShoppingCart -> CarritoCompra, product -> producto
        productosEnCarrito.add(producto); // products -> productosEnCarrito
        tableSale.setItems(productosEnCarrito); // products -> productosEnCarrito
        //Para asegurar se reemplaza una coma por punto decimal
        String totalText = total.getText().trim().replace(',','.');
        double currentTotal = Double.parseDouble(totalText) + producto.total(); // product.total() -> producto.total()
        //Siempre escribimos usando Locale.US
        total.setText(String.format(Locale.US, "%.2f", currentTotal));
    }


    private String validateInputs() {
        if (nombreProducto.getText().isEmpty() || nombreCliente.getText().isEmpty()) { // productName -> nombreProducto, customerName -> nombreCliente
            return "Falta nombre del cliente o producto"; // Mensaje traducido
        } else if (quantity.getValue() == 0) {
            return "La cantidad no puede ser 0"; // Mensaje traducido
        }
        return null;
    }

    private void setSerial(){
        idVenta = 1+ventaDAO.IdVenta(); // idSale -> idVenta, salesDAO -> ventaDAO, IdSale() -> IdVenta()
        String formattedId= String.format("%04d", idVenta); // idSale -> idVenta
        serial.setText(formattedId);
    }


}
