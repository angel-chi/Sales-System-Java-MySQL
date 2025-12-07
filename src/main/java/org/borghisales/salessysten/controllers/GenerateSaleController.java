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

    private int contProductos =1; // contProducts -> contProductos
    private ObservableList<CarritoCompra> productosEnCarrito; // products -> productosEnCarrito, ShoppingCart -> CarritoCompra

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
    private final ConfiguracionDAO configuracionDAO = new ConfiguracionDAO();
    private double ivaPercentage = 0.0;

    private Cliente cliente; // Customer -> Cliente
    private final ProductoDAO productoDAO = new ProductoDAO(); // ProductDAO -> ProductoDAO

    @FXML
    private Label productoMasVendido; // bestSellingProduct -> productoMasVendido
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
    private TextField date;
    @FXML
    private TextField subtotalField;
    @FXML
    private TextField ivaField;
    @FXML
    private Label ivaLabel;
    @FXML
    private TextField totalFinalField;

    @FXML
    private TextField campoCodigoDescuento;

    private final CodigoDescuentoDAO codigoDescuentoDAO = new CodigoDescuentoDAO();
    private CodigoDescuento descuentoActual = null;

    @FXML
    private ComboBox<Venta.TipoPago> cbTipoPago;
    @FXML
    private ComboBox<Venta.EntregaTicket> cbEntregaTicket;

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
        inicializarElementosUI();
        configurarAlertas();
        configureTable();
        showBestSellingProduct();
    }

    private void inicializarElementosUI() {
        // Inicializar campos de totales
        subtotalField.setText(String.format(Locale.US, "%.2f", 0.0));
        ivaField.setText(String.format(Locale.US, "%.2f", 0.0));
        totalFinalField.setText(String.format(Locale.US, "%.2f", 0.0));

        // Cargar valor del IVA desde la BD
        try {
            String ivaStr = configuracionDAO.getValor("IVA");
            if (ivaStr != null) {
                this.ivaPercentage = Double.parseDouble(ivaStr);
            }
        } catch (NumberFormatException | NullPointerException e) {
            this.ivaPercentage = 0.0; // Valor por defecto en caso de error
            MenuController.setAlert(Alert.AlertType.ERROR, "No se pudo cargar el valor del IVA desde la configuración. Se usará 0%.");
        }
        ivaLabel.setText(String.format("IVA (%.0f%%):", ivaPercentage * 100));


        setSerial();
        vendedor.setText(nombreVendedor); // seller -> vendedor, sellerName -> nombreVendedor
        date.setText(String.valueOf(now));
        cbTipoPago.setItems(FXCollections.observableArrayList(Venta.TipoPago.values()));
        cbTipoPago.setValue(Venta.TipoPago.EFECTIVO);
        cbEntregaTicket.setItems(FXCollections.observableArrayList(Venta.EntregaTicket.values()));
        cbEntregaTicket.setValue(Venta.EntregaTicket.IMPRESO);
    }

    private void configurarAlertas() {
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
            setAlert(Alert.AlertType.CONFIRMATION, "Cliente encontrado: " + cliente.getNombre()); // cliente.name() -> cliente.nombre()
            nombreCliente.setText(cliente.getNombre()); // customerName -> nombreCliente
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
        try {
            int productoId = Integer.parseInt(codProducto.getText()); // productId -> productoId, codProduct -> codProducto
            Producto producto = productoDAO.searchProducto(productoId); // Product -> Producto, productDAO -> productoDAO, searchProduct -> searchProducto

            if (producto != null) {
                if (producto.existencia() <= 0) {
                    setAlert(Alert.AlertType.WARNING, "El producto '" + producto.nombre() + "' no tiene existencias.");
                    nombreProducto.clear();
                    existencia.clear();
                    price.clear();
                    return;
                }
                updateProductFields(producto);
            } else {
                handleProductNotFound();
            }
        } catch (NumberFormatException e) {
            setAlert(Alert.AlertType.ERROR, "El código del producto no es un número válido.");
        }
    }

    private void updateProductFields(Producto producto) { // Product -> Producto
        setAlert(Alert.AlertType.CONFIRMATION, "Producto encontrado: " + producto.nombre()); // product.name() -> producto.nombre()
        nombreProducto.setText(producto.nombre()); // productName -> nombreProducto
        existencia.setText(String.valueOf(producto.existencia())); // stock -> existencia, product.stock() -> producto.existencia()
        price.setText(String.valueOf(producto.precio())); // product.price() -> producto.precio()

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, producto.existencia(), 1); // product.stock() -> producto.existencia()
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
        productosEnCarrito.clear(); // Clear the list as well
        descuentoActual = null; // Reset discount
        campoCodigoDescuento.clear(); // Clear discount field
        recalcularTotales(); // Recalculate totals after clearing the cart
        MenuController.setAlert(Alert.AlertType.INFORMATION,"Venta Cancelada");
    }

    public void generateSale(ActionEvent actionEvent) {
        if (productosEnCarrito.isEmpty()) {
            MenuController.setAlert(Alert.AlertType.WARNING, "El carrito de compras está vacío.");
            return;
        }

        if (cliente == null) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Por favor, busque y seleccione un cliente válido.");
            return;
        }

        Venta venta = createSalesObject();

        Integer newVentaId = ventaDAO.SaveVenta(venta);

        if (newVentaId != null) {
            boolean detailsSaved = ventaDAO.SaveDetallesVenta(productosEnCarrito, newVentaId);

            if (detailsSaved) {
                productoDAO.subtractStock(productosEnCarrito);
                cleanFieldsAndTable();
                setSerial();
                totalFinalField.setText("0.0"); // Update to totalFinalField
                productosEnCarrito.clear();
                descuentoActual = null;
                campoCodigoDescuento.clear();
                updateReportsController();
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "¡Venta generada con éxito!");
            } else {
                // Considerar implementar un rollback de la transacción aquí en una futura versión.
                MenuController.setAlert(Alert.AlertType.ERROR, "Error: La venta fue creada (ID: " + newVentaId + ") pero falló al guardar los detalles de los productos. Contacte a soporte.");
            }
        } else {
             MenuController.setAlert(Alert.AlertType.ERROR, "Error: No se pudo registrar la venta en la base de datos.");
        }
    }

    private Venta createSalesObject() { // Sales -> Venta
        //Se reemplaza la coma por punto decimal
        String totalText = totalFinalField.getText().trim().replace(',', '.'); // Use totalFinalField
        //Se convierte a double
        double totalValue = Double.parseDouble(totalText);
        Venta.TipoPago tipoPago = cbTipoPago.getValue();
        Venta.EntregaTicket entregaTicket = cbEntregaTicket.getValue();
        return new Venta(cliente.getId(), idVendedor, serial.getText(), // Customer -> Cliente, idCustomer() -> idCliente(), idSeller -> idVendedor
                LocalDate.parse(date.getText()), totalValue,
                Venta.Estado.ACTIVO, tipoPago, entregaTicket); // Sales.State.ACTIVE -> Venta.Estado.ACTIVO
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
        try {
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

            productosEnCarrito.add(producto);
            tableSale.refresh();
            recalcularTotales();
        } catch (NumberFormatException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "El precio o la cantidad tienen un formato inválido. Por favor, busque el producto de nuevo.");
        } catch (Exception e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Ocurrió un error inesperado al agregar el producto: " + e.getMessage());
        }
    }

    private CarritoCompra createShoppingCartObject() { // ShoppingCart -> CarritoCompra
        return new CarritoCompra(contProductos++, codProducto.getText(), // contProducts -> contProductos, codProduct -> codProducto
                nombreProducto.getText(), quantity.getValue(), // productName -> nombreProducto
                Double.parseDouble(price.getText()));
    }

    private boolean isProductAlreadyInCart(CarritoCompra producto) { // ShoppingCart -> CarritoCompra, product -> producto
        return productosEnCarrito.stream().anyMatch(e -> Objects.equals(e.codigo(), producto.codigo())); // products -> productosEnCarrito, cod() -> codigo()
    }

    private void recalcularTotales() {
        double subtotal = 0.0;
        for (CarritoCompra item : productosEnCarrito) {
            subtotal += item.total();
        }

        if (descuentoActual != null) {
            double montoDescuento = subtotal * descuentoActual.getPorcentaje();
            subtotal -= montoDescuento;
        }

        subtotalField.setText(String.format(Locale.US, "%.2f", subtotal));

        double ivaAmount = subtotal * ivaPercentage;
        ivaField.setText(String.format(Locale.US, "%.2f", ivaAmount));

        double finalTotal = subtotal + ivaAmount;
        totalFinalField.setText(String.format(Locale.US, "%.2f", finalTotal));

        // Actualizar la etiqueta del IVA por si el porcentaje cambia dinámicamente en el futuro
        ivaLabel.setText(String.format("IVA (%.0f%%):", ivaPercentage * 100));
    }


    @FXML
    public void aplicarDescuento(ActionEvent actionEvent) {
        String codigo = campoCodigoDescuento.getText();
        if (codigo.isEmpty()) {
            MenuController.setAlert(Alert.AlertType.WARNING, "Por favor, introduzca un código de descuento.");
            return;
        }

        descuentoActual = codigoDescuentoDAO.buscarPorCodigo(codigo);

        if (descuentoActual != null) {
            MenuController.setAlert(Alert.AlertType.INFORMATION, "Descuento aplicado: " + descuentoActual.getPorcentaje() * 100 + "%");
            recalcularTotales();
        } else {
            MenuController.setAlert(Alert.AlertType.ERROR, "El código de descuento no es válido o ha expirado.");
        }
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

    private void showBestSellingProduct() {
        String resultado = productoDAO.getBestSellingProduct(MainController.vendedorLogeado.getId());

        if(resultado == null){
            productoMasVendido.setText("Sin ventas registradas");
        }else{
            productoMasVendido.setText(resultado);
        }
    }

    @FXML
    public void backToMenu(ActionEvent actionEvent) {
        openNewStage(MANAGEMENT_VIEW_FXML, "Management");
        closeCurrentStage(serial);
    }
}
