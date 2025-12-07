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


import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable,validacionEntrada {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ObservableList<Customer.State> stateList = FXCollections.observableArrayList(Customer.State.ACTIVE, Customer.State.DISACTIVE);
    private static ObservableList<Customer> customers=null;

    @FXML
    private TextField DNI;
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
        if (campoVacio(DNI) || campoVacio(name) || campoVacio(address)) {
            mostrarAdvertencia("Debes de completar todos los campos antes de guardar.");
            //FALTABA RETURN para no guardar al cliente
            return;
        }
        if (cbState.getValue() == null) {
            mostrarAdvertencia("Debes seleccionar un estado.");
            return;
        }
            Customer customer = new Customer(DNI.getText(), name.getText(), address.getText(), cbState.getValue());
            if (customerDAO.create(customer)) {
                MenuController.cleanCells(DNI, name, address);
                updateTable();
            }
        }

    @FXML
    public void updateCustomer(ActionEvent actionEvent) {
        Customer customer = new Customer(DNI.getText(),name.getText(),address.getText(),cbState.getValue());
        if (customerDAO.update(customer)) {
            MenuController.cleanCells(DNI, name, address);
            updateTable();
        }
    }
    @FXML
    public void deleteCustomer(ActionEvent actionEvent) {
        if (customerDAO.delete(DNI.getText())){
            MenuController.cleanCells(DNI,name,address);
            updateTable();
        }
    }
    @FXML
    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(DNI,name,address);
    }
    private void setCells(Customer customer){
        name.setText(customer.name());
        DNI.setText(customer.dni());
        address.setText(customer.address());
        cbState.setValue(customer.state());
    }

    private void updateTable() {
        tableCustomers.getItems().clear();
        customerDAO.setTable(customers);
        tableCustomers.setItems(customers);
    }

}
