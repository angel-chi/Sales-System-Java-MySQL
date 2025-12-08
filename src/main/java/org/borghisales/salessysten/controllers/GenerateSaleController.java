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
    private final ButtonType buttonTypeAccept = new ButtonType("SÍ");
    private final ButtonType buttonTypeCancel = new ButtonType("NO");


    private final CustomerDAO customerDAO = new CustomerDAO(); //TODOS LOS QUE TIENEN DAO SE CONECTAN A LA BASE DE DATOS

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
    private ComboBox<Discount> cbDiscount;
    @FXML
    private TextField total;
    @FXML
    private TextField totalDiscount;
    @FXML
    private TextField percentageDiscount;
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
        initializeComboBox();
        configureAlerts();
        configureTable();
    }

    private void initializeUIElements() {
        total.setText("0.0");
        setSerial();
        seller.setText(sellerName);
        date.setText(String.valueOf(now));
        totalDiscount.setText("0.0");
        percentageDiscount.setText("0%");
    } //***

    //Inicializa la lista de descuentos
    private void initializeComboBox(){
        cbDiscount.setItems(FXCollections.observableArrayList(Discount.values()));
        cbDiscount.setValue(Discount.NONE);
        //Cada vez que se presione el boton
        cbDiscount.setOnAction(actionEvent -> {
            updatePercentageDiscount();
            recalculateTotals();
        });
    }
    //Actualiza el descuento cada vez que el seller seleccione
    private void updatePercentageDiscount(){
        Discount selectedDiscount = cbDiscount.getValue();

        if(selectedDiscount== null || selectedDiscount == Discount.NONE) {
            percentageDiscount.setText("0.0%");
        }else{
            double percentage = selectedDiscount.getPercentage() * 100;
            percentageDiscount.setText(String.format("%.0f%%", percentage));
        }
    }

    private void configureAlerts() {
        configureAlert(alertCustomer, "Nuevo cliente", "El cliente no existe");
        configureAlert(alertProduct, "Nuevo producto", "El producto no existe");
    }

    private void configureTable() {
        configureTableColumns();
        tableSale.getItems().clear();
        products = FXCollections.observableArrayList();
    }


    private void configureAlert(Alert alert, String title, String header) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText("¿Deseas agregarlo?");
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
            setAlert(Alert.AlertType.CONFIRMATION, "Cliente encontrado: " + customer.name()); //Te dice si encontro
                                                                                                    // un customer con esa matricula
            customerName.setText(customer.name());
        } else { //En caso de no encontrarlo
            handleCustomerNotFound(); //La tachita no hace nada
        }
    }

    //Funcion que abre una pestaña en caso de no encontrar un customer
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
        stage.setTitle("Administrar cliente");
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
        stage.setTitle("Administrar producto");
        stage.setScene(scene);
        stage.show();
    }

    //CANCELA LA VENTA
    public void cancel(ActionEvent actionEvent) {
        if (products.isEmpty()){ //Agrega alerta al botón cancelar //CORRECCIÓN ERROR
            MenuController.setAlert(Alert.AlertType.INFORMATION, "No se ha ingresado ningún dato a la compra que se pueda cancelar");
            return;
        }
        MenuController.cleanCells(codCustomer,codProduct,customerName,productName,price,stock); //limpia la tabla
        quantity.getValueFactory().setValue(null);
        tableSale.getItems().clear();
        MenuController.setAlert(Alert.AlertType.INFORMATION,"Venta cancelada");
        total.clear();
        totalDiscount.clear();
        cbDiscount.setValue(Discount.NONE);

    }

    //GENERA VENTA //***
    public void generateSale(ActionEvent actionEvent) {
        if (products.isEmpty()) { //Agregar alerta al botón generar venta //CORRECCIÓN ERROR
            MenuController.setAlert(Alert.AlertType.INFORMATION, "No se puede generar una venta sin ningún producto en el carrito de compras");
            return;
        }

        Sales sales = createSalesObject();  //crea un objeto sales sin pasarle parametros

        if (saveSaleAndDetails(sales)) { //si se concreta la venta
            productDAO.subtractStock(products); //quita productos del stock
            cleanFieldsAndTable(); //se eliminanlos objetos de la tabla
            setSerial();
            total.setText("0.0"); //se reinicia el total a cero
            totalDiscount.setText("0.0");
            products.clear();
            updateReportsController(); //Se actualiza la base de datos
        }
    }

    //Crea un objeto "ventas" (constructor)
    private Sales createSalesObject() {
        return new Sales(customer.idCustomer(), idSeller, serial.getText(),
                LocalDate.parse(date.getText()), Double.parseDouble(total.getText()),
                Sales.State.ACTIVE, cbDiscount.getValue());
    }

    //guarda las ventas, regresa true or false
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

        ShoppingCart product = createShoppingCartObject(); //Crea objeto en el carrito

        if (isProductAlreadyInCart(product)) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Este producto ya se encuentra en tu carrito de compras");
            return;
        }

        addToCartAndUpdateTotalWithDiscount(product); //***
    }

    private ShoppingCart createShoppingCartObject() {
        return new ShoppingCart(contProducts++, codProduct.getText(),
                productName.getText(), quantity.getValue(),
                Double.parseDouble(price.getText()));
    }

    private boolean isProductAlreadyInCart(ShoppingCart product) {
        return products.stream().anyMatch(e -> Objects.equals(e.cod(), product.cod()));
    }

    /*private void addToCartAndUpdateTotal(ShoppingCart product) {
        products.add(product);
        tableSale.setItems(products);
        double currentTotal = Double.parseDouble(total.getText()) + product.total();
        total.setText(String.format("%.2f", currentTotal));
    }*/

    //Suma de precios 1 //mostrar datos con descuento
    private void addToCartAndUpdateTotalWithDiscount(ShoppingCart product) {
        products.add(product);
        tableSale.setItems(products);
        recalculateTotals();
    }

    //Calcula precios con y sin descuentos
    private void recalculateTotals() {
        double totalSinDescuento = 0;
        double totalConDescuento = 0;
        double diferenciaDescuento = 0;

        Discount discount = cbDiscount.getValue();
        double percent = discount.getPercentage();

        for (ShoppingCart product : products) {
            double subtotal = product.total();
            totalSinDescuento += subtotal;
            totalConDescuento += subtotal - (subtotal * percent);
            diferenciaDescuento = totalSinDescuento - totalConDescuento; //***

        }

        totalDiscount.setText(String.format("%.2f", totalSinDescuento)); //Sin Descuento (subtotal)
        total.setText(String.format("%.2f", totalConDescuento)); //(total)
    }



    /* private String validateInputs() {
        if (productName.getText().isEmpty() || customerName.getText().isEmpty()) {
            return "Missing customer name or product name.";
        } else if (quantity.getValue() == 0) {
            return "Quantity can't be 0.";
        }
        return null;
    } */

    private String validateInputs() {
        if (productName.getText().isEmpty() || customerName.getText().isEmpty()) {
            return "No se ingresó el nombre del cliente o el nombre del producto.";
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
