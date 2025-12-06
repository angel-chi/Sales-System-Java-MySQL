package org.borghisales.salessysten.controllers;

import com.sun.tools.javac.Main;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.collections.transformation.FilteredList;
import javafx.scene.layout.VBox;
import org.borghisales.salessysten.model.Customer;
import org.borghisales.salessysten.model.CustomerDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ObservableList<Customer.State> stateList = FXCollections.observableArrayList(Customer.State.ACTIVE, Customer.State.DISACTIVE);
    private static ObservableList<Customer> customers = null;
    private FilteredList<Customer> filteredData;        // Lista FILTRADA
    @FXML
    private TextField dni;
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private ComboBox<Customer.State> cbState;
    @FXML
    private TableView<Customer> tableCustomers;
    @FXML
    private TableColumn<Customer, Integer> colId;
    @FXML
    private TableColumn<Customer, String> colDni;
    @FXML
    private TableColumn<Customer, String> colName;
    @FXML
    private TableColumn<Customer, String> colAddress;
    @FXML
    private TableColumn<Customer, Customer.State> colState;
    @FXML
    private VBox searchContainer;
    @FXML
    private TextField txtSearch;
    @FXML
    private ToggleGroup searchOptions;

    @FXML
    private RadioButton rbId, rbNombre, rbDni;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeCustomerData();

        // CAMBIO: Aplicamos la configuración para poder deseleccionar los botones
        makeDeselectable(rbId);
        makeDeselectable(rbNombre);
        makeDeselectable(rbDni);

        rbNombre.setOnAction(e -> applyFilter(txtSearch.getText()));
        rbDni.setOnAction(e -> applyFilter(txtSearch.getText()));
        rbId.setOnAction(e -> applyFilter(txtSearch.getText()));
    }

    private void initializeCustomerData() {
        tableCustomers.getItems().clear();
        if (customers == null) {
            customers = FXCollections.observableArrayList();
            customerDAO.setTable(customers);
        }
        filteredData = new FilteredList<>(customers, p -> true);
        tableCustomers.setItems(filteredData);      //usamos la mascara sobre la original
    }

    private void initializeTable() {
        colId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idCustomer()).asObject());
        colName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().name()));
        colDni.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().dni()));
        colAddress.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().address()));
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().state()));

        tableCustomers.setOnMouseClicked(mouseEvent -> {
            if (!tableCustomers.getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                Customer customer = tableCustomers.getSelectionModel().getSelectedItem();
                setCells(customer);
            }
        });
    }

    private void initializeComboBox() {
        cbState.setValue(Customer.State.ACTIVE);
        cbState.setItems(stateList);
    }

    @FXML
    public void addCustomer(ActionEvent actionEvent) {
        Customer customer = new Customer(dni.getText(), name.getText(), address.getText(), cbState.getValue());
        if (customerDAO.create(customer)) {
            MenuController.cleanCells(dni, name, address);
            updateTable();
        }
    }

    @FXML
    public void updateCustomer(ActionEvent actionEvent) {
        Customer customer = new Customer(dni.getText(), name.getText(), address.getText(), cbState.getValue());
        if (customerDAO.update(customer)) {
            MenuController.cleanCells(dni, name, address);
            updateTable();
        }
    }

    @FXML
    public void deleteCustomer(ActionEvent actionEvent) {
        if (customerDAO.delete(dni.getText())) {
            MenuController.cleanCells(dni, name, address);
            updateTable();
        }
    }

    @FXML
    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(dni, name, address);
    }

    private void setCells(Customer customer) {
        name.setText(customer.name());
        dni.setText(customer.dni());
        address.setText(customer.address());
        cbState.setValue(customer.state());
    }

    private void updateTable() {
        tableCustomers.getItems().clear();
        customerDAO.setTable(customers);
        tableCustomers.setItems(customers);
    }

    @FXML
    private void searchCustomer() {

        boolean isVisible = searchContainer.isVisible();
        searchContainer.setVisible(!isVisible);
        searchContainer.setManaged(!isVisible);
        txtSearch.textProperty().addListener((n, un, txt) -> {
            applyFilter(txt);
        });


    }

    //  Nuevo metodo para permitir la deselección al hacer click
    private void makeDeselectable(RadioButton rb) {
        rb.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (rb.isSelected()) {
                searchOptions.selectToggle(null);
                // Consumimos el evento para evitar que JavaFX lo vuelva a seleccionar automáticamente
                event.consume();
            }
        });
    }

    private void applyFilter(String txt) {
        filteredData.setPredicate(customer -> {

            if (txt == null || txt.isEmpty()) {
                return true; // mostrar todos
            }

            String lowerCaseFilter = txt.toLowerCase();

            if (rbNombre.isSelected()) {
                return customer.name().toLowerCase().startsWith(lowerCaseFilter);
            } else if (rbDni.isSelected()) {
                return customer.dni().toLowerCase().startsWith(lowerCaseFilter);
            } else if (rbId.isSelected()) {
                return String.valueOf(customer.idCustomer()).startsWith(lowerCaseFilter);
            }

            // Si no hay ningún radio seleccionado, muestra todo
            return true;
        });
    }

}