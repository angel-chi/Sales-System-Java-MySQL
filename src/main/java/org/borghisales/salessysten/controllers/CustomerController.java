package org.borghisales.salessysten.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.borghisales.salessysten.model.Customer;
import org.borghisales.salessysten.model.CustomerDAO;
import org.borghisales.salessysten.model.IValidable;
import org.borghisales.salessysten.model.CRUD; //usa la interfaz

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable, IValidable  {
    // Depender de la abstraccion (CRUD)
    private final CRUD<Customer> customerDAO = new CustomerDAO();
    private final ObservableList<Customer.State> stateList = FXCollections.observableArrayList(Customer.State.ACTIVE, Customer.State.DISACTIVE);
    private static ObservableList<Customer> customers=null;

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
    private TableColumn<Customer,Integer> colId;
    @FXML
    private TableColumn<Customer,String> colDni;
    @FXML
    private TableColumn<Customer,String> colName;
    @FXML
    private TableColumn<Customer,String> colAddress;
    @FXML
    private TableColumn<Customer,Customer.State> colState;

    @Override
    public String validarCampos() {
        String dniText = dni.getText();
        String nameText = name.getText();
        String addressText = address.getText();
        if (IValidable.esCampoVacio(dniText)) {
            return "El campo usuario del cliente es obligatorio.";
        }
        if (IValidable.esCampoVacio(nameText)) {
            return "El campo Nombre del cliente es obligatorio.";
        }
        if (IValidable.esCampoVacio(addressText)) {
            return "El campo Dirección del cliente es obligatorio.";
        }

        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeCustomerData();
    }
    private void initializeCustomerData() {
        tableCustomers.getItems().clear();
        if (customers == null) {
            customers = FXCollections.observableArrayList();
            customerDAO.setTable(customers);
        }
        tableCustomers.setItems(customers);
    }
    private void initializeTable() {
        tableCustomers.setOnMouseClicked(mouseEvent -> {
            if (!tableCustomers.getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                Customer customer = tableCustomers.getSelectionModel().getSelectedItem();
                setCells(customer);
            }
        });

        colId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idCustomer()).asObject());
        colName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().name()));
        colDni.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().dni()));
        colAddress.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().address()));
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().state()));
    }
    private void initializeComboBox() {
        cbState.setValue(Customer.State.ACTIVE);
        cbState.setItems(stateList);
    }
    @FXML
    public void addCustomer(ActionEvent actionEvent) {
        String errorMessage = validarCampos();
        if (errorMessage != null) {
            MenuController.setAlert(Alert.AlertType.WARNING, errorMessage);
            return; // Detiene la ejecución si hay errores
        }
        Customer customer = new Customer(dni.getText(),name.getText(),address.getText(),cbState.getValue());
        if (customerDAO.create(customer)) {
            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Cliente añadido con éxito");
            MenuController.cleanCells(dni, name, address);
            updateTable();
        } else{
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al añadir cliente: Por favor checar el usuario o la información");
        }
    }
    @FXML
    public void updateCustomer(ActionEvent actionEvent) {
        String errorMessage = validarCampos();
        if (errorMessage != null) {
            MenuController.setAlert(Alert.AlertType.WARNING, errorMessage);
            return; // Detiene la ejecución si hay errores
        }
        Customer customer = new Customer(dni.getText(),name.getText(),address.getText(),cbState.getValue());
        if (customerDAO.update(customer)) {
            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Cliente actualizado con éxito");
            MenuController.cleanCells(dni, name, address);
            updateTable();
        } else {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al actualizar cliente. No se encontro el usuario o error de información");
        }
    }
    @FXML
    public void deleteCustomer(ActionEvent actionEvent) {
        if (customerDAO.delete(dni.getText())){
            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Cliente eliminado con éxito");
            MenuController.cleanCells(dni,name,address);
            updateTable();
        } else {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al borrar cliente. Puede que no tenga ventas asociadas o el usuario es incorrecto");
        }
    }
    @FXML
    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(dni,name,address);
    }
    private void setCells(Customer customer){
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

}
