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
    private static ObservableList<ComprasDetalles> detalles = FXCollections.observableArrayList();
    private Proveedor proveedor;
    private final LocalDate now = LocalDate.now();
    // Necesarios para vista
    private Scene scene = null;
    private Stage stage;
    // Configuraciones
    private final Alert alertCustomer = new Alert(Alert.AlertType.WARNING);
    private final Alert alertProduct = new Alert(Alert.AlertType.WARNING);
    private final ButtonType buttonTypeAccept = new ButtonType("YES");
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
    @FXML private TableColumn<ComprasDetalles,Integer> colIdProveedor;
    @FXML private TableColumn<ComprasDetalles,Integer> colCantidad;
    @FXML private TableColumn<ComprasDetalles, Double> colPrecio;
    @FXML private TableColumn<ComprasDetalles,Double> colSubtotal;

    // Configuraciones basicas
    private Compras crearCompra() {
        double subtotal = detalles.stream().mapToDouble(ComprasDetalles::subtotal).sum();
        return new Compras(proveedor.idProveedor(),MainController.sellerLog.idSeller(),subtotal,Compras.EstadoCompra.COMPLETADO);
    }
    public void generarCompra(ActionEvent e) {
        if (detalles.isEmpty()) return;
        Compras compra = crearCompra();
        if (comprasDAO.create(compra)) {
            int idCompra = comprasDAO.IdSale();
            comprasDAO.saveDetalles(detalles, idCompra);
            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Compra registrada");
            detalles.clear();
            tableCompra.getItems().clear();
        }
    }


    // Complementos
    public void searchProveedor(ActionEvent e) {
        proveedor = proveedorDAO.searchProveedor(Integer.parseInt(idProveedor.getText()));
    }

    public void searchProduct(ActionEvent actionEvent) {
        int productId = Integer.parseInt(idProducto.getText());
        // return new productDAO.searchProduct(productId);
    }

    /// //// ---------------------------
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeUIElements();
        configureAlerts();
        configureTable();
    }

    private void initializeUIElements() {
        total.setText("0.0");
        tableCompra.setItems(detalles);
    }

    private void configureAlerts() {
        configureAlert(alertCustomer, "New Proveedor", "El proveedor no existe", "¿Quiers agregar uno nuveo?");
        configureAlert(alertProduct, "New Product", "El producto no existe", "¿Quiers agregar uno nuveo?");
    }

    private void configureTable() {
        configureTableColumns();
        tableCompra.getItems().clear();
        detalles = FXCollections.observableArrayList();
    }


    private void configureAlert(Alert alert, String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getButtonTypes().setAll(buttonTypeAccept, buttonTypeCancel);
    }

    private void configureTableColumns() {

        colIdProducto.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().idProducto()).asObject());
        colCantidad.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().cantidad()).asObject());
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
        stage.setTitle("Manage Customer");
        stage.setScene(scene);
        stage.show();
    }


}
