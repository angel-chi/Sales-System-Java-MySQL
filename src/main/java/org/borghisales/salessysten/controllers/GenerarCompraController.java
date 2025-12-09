package org.borghisales.salessysten.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.borghisales.salessysten.model.dao.ComprasDAO;
import org.borghisales.salessysten.model.dao.ProductDAO;
import org.borghisales.salessysten.model.dao.ProveedorDAO;
import org.borghisales.salessysten.model.entities.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class GenerarCompraController extends MenuController implements Initializable {
    // Controladores
    private final ComprasDAO comprasDAO = new ComprasDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final ProveedorDAO proveedorDAO = new ProveedorDAO();
    // Atributos principales
    private static final ObservableList<ComprasDetalles> detalles = FXCollections.observableArrayList();
    private Proveedor proveedor;
    private Seller vendedorLogueado = MainController.sellerLog;
    private final LocalDate now = LocalDate.now();
    // Necesarios para vista
    private Scene scene = null;
    private Stage stage;
    // Configuraciones
    private final Alert alertCustomer = new Alert(Alert.AlertType.WARNING);
    private final Alert alertProduct = new Alert(Alert.AlertType.WARNING);
    private final ButtonType buttonTypeAccept = new ButtonType("SI");
    private final ButtonType buttonTypeCancel = new ButtonType("NO");
    // Campos para agregar nuevos
    @FXML private TextField idProducto;
    @FXML private TextField idProveedor;
    @FXML private Spinner<Integer> cantidad;
    @FXML private TextField precioCompra;
    @FXML private TextField total;
    // Propiedades de la tabla
    @FXML private TableView<ComprasDetalles> tableCompra;
    // Columnas de la tabla
    @FXML private TableColumn<ComprasDetalles,Integer> colIdProducto;
    @FXML private TableColumn<Proveedor,Integer> colIdProveedor;
    @FXML private TableColumn<ComprasDetalles,Integer> colCantidad;
    @FXML private TableColumn<ComprasDetalles, Double> colPrecio;
    @FXML private TableColumn<ComprasDetalles,Double> colSubtotal;
   @FXML private TableColumn<Seller,Integer> colIdVendedor;
    //Agregué estos campos faltantes
    @FXML private TextField nombreProveedor;
    @FXML private TextField nombreProducto;
    @FXML private TextField stockActual;
    @FXML private TextField vendedor;

    @FXML private TextField date;

    // Configuraciones basicas
    private Compras crearCompra(int id) {
        double subtotal = detalles.stream().mapToDouble(ComprasDetalles::subtotal).sum();
        return new Compras(proveedor.idProveedor(),id,subtotal,Compras.EstadoCompra.COMPLETADO);
    }

    @FXML
    public void generarCompra(ActionEvent e) {
        if (detalles.isEmpty()) {
            MenuController.setAlert(Alert.AlertType.WARNING, "No hay productos en la compra");
            return;
        }

        if (proveedor == null) {
            MenuController.setAlert(Alert.AlertType.WARNING, "Debes seleccionar un proveedor");
            return;
        }

        Compras compra = crearCompra(vendedorLogueado.idSeller());
        // Usar el método que devuelve el id generado (suponiendo que lo agregaste en ComprasDAO)
        int idCompra = comprasDAO.createAndReturnId(compra);

        if (idCompra > 0) {
            comprasDAO.saveDetalles(detalles, idCompra);
            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Compra registrada exitosamente");
            // Limpiar
            cancel(e);
        } else {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al registrar la compra");
        }
    }


    // Complementos
    @FXML
    public void searchProveedor(ActionEvent e) {
        try {
            int proveedorId = Integer.parseInt(idProveedor.getText());
            proveedor = proveedorDAO.searchProveedor(proveedorId);

            if (proveedor != null) {
                nombreProveedor.setText(proveedor.name());
            } else {
                nombreProveedor.clear();
                MenuController.setAlert(Alert.AlertType.WARNING, "Proveedor no encontrado");
            }
        } catch (NumberFormatException ex) {
            MenuController.setAlert(Alert.AlertType.ERROR, "ID de proveedor inválido");
        }
    }

    @FXML
    public void searchProduct(ActionEvent actionEvent) {
        try {
            int productId = Integer.parseInt(idProducto.getText());
            Product product = productDAO.searchProduct(productId);

            if (product != null) {
                nombreProducto.setText(product.name());
                stockActual.setText(String.valueOf(product.stock()));
                precioCompra.setText(String.valueOf(product.price()));
            } else {
                nombreProducto.clear();
                stockActual.clear();
                precioCompra.clear();
                MenuController.setAlert(Alert.AlertType.WARNING, "Producto no encontrado");
            }
        } catch (NumberFormatException ex) {
            MenuController.setAlert(Alert.AlertType.ERROR, "ID de producto inválido");
        }
    }

    @FXML
    public void addShoppingCart(ActionEvent e) {
        try {
            // Validar que haya proveedor seleccionado
            if (proveedor == null) {
                MenuController.setAlert(Alert.AlertType.WARNING, "Primero busca un proveedor");
                return;
            }

            // Validar que haya producto
            if (idProducto.getText().isEmpty() || nombreProducto.getText().isEmpty()) {
                MenuController.setAlert(Alert.AlertType.WARNING, "Primero busca un producto");
                return;
            }

            // Obtener valores
            int productoId = Integer.parseInt(idProducto.getText());
            int cantidadCompra = cantidad.getValue();
            double precio = Double.parseDouble(precioCompra.getText());
            double subtotal = cantidadCompra * precio;

            // Crear detalle de compra
            ComprasDetalles detalle = new ComprasDetalles(
                    0, // idCompra se asigna después
                    productoId,
                    proveedor.idProveedor(),
                    cantidadCompra,
                    precio,
                    subtotal
            );

            // Agregar a la lista
            detalles.add(detalle);

            // Actualizar total
            actualizarTotal();

            // Limpiar campos
            limpiarCamposProducto();

        } catch (NumberFormatException ex) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error en los datos ingresados");
        }
    }

    @FXML
    public void cancel(ActionEvent e) {
        // Limpiar tabla
        detalles.clear();
        tableCompra.getItems().clear();

        // Limpiar campos
        idProveedor.clear();
        nombreProveedor.clear();
        idProducto.clear();
        nombreProducto.clear();
        precioCompra.clear();
        stockActual.clear();
        total.setText("0.0");
        cantidad.getValueFactory().setValue(1);

        proveedor = null;
        // Mensaje
        MenuController.setAlert(Alert.AlertType.INFORMATION, "Compra cancelada");
    }

    private void actualizarTotal() {
        double totalCompra = detalles.stream().mapToDouble(ComprasDetalles::subtotal).sum();
        total.setText(String.format("%.2f", totalCompra));
    }

    private void limpiarCamposProducto() {
        idProducto.clear();
        nombreProducto.clear();
        precioCompra.clear();
        stockActual.clear();
        cantidad.getValueFactory().setValue(1);
    }

    /// //// ---------------------------
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeUIElements();
        configureAlerts();
        configureTable();
        tableCompra.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void initializeUIElements() {
        total.setText("0.0");
        tableCompra.setItems(detalles);

        // Mostrar nombre del vendedor y su ID en el campo vendedor
        if (MainController.sellerLog != null) {
            vendedor.setText(String.format("%s (ID:%d)", MainController.sellerLog.name(), MainController.sellerLog.idSeller()));
        } else {
            vendedor.setText("Desconocido");
        }

        date.setText(LocalDate.now().toString());
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1);
        cantidad.setValueFactory(valueFactory);
    }

    private void configureAlerts() {
        configureAlert(alertCustomer, "Nuevo Proveedor", "El proveedor no existe", "¿Quieres agregar uno nuevo?");
        configureAlert(alertProduct, "Nuevo Producto", "El producto no existe", "¿Quieres agregar uno nuevo?");
    }

    private void configureTable() {
        configureTableColumns();
        tableCompra.getItems().clear();
        // NO reasignar 'detalles' para no romper la referencia con la tabla
        // detalles = FXCollections.observableArrayList(); <-- eliminado
    }


    private void configureAlert(Alert alert, String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getButtonTypes().setAll(buttonTypeAccept, buttonTypeCancel);
    }

    private void configureTableColumns() {

        // Mostrar idProducto
        colIdProducto.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().idProducto()).asObject());
        colCantidad.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().cantidad()).asObject());
        colIdProveedor.setCellValueFactory(c ->
                new SimpleIntegerProperty(
                        proveedor != null ? proveedor.idProveedor() : 0
                ).asObject()
        );
        colIdVendedor.setCellValueFactory(c ->
                new SimpleIntegerProperty(
                        vendedorLogueado != null ? vendedorLogueado.idSeller() : 0
                ).asObject()
        );
        colPrecio.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().precioCompra()).asObject());
        colSubtotal.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().subtotal()).asObject());
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

}
