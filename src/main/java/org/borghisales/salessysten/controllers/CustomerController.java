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
import org.borghisales.salessysten.model.entities.Customer;
import org.borghisales.salessysten.model.dao.CustomerDAO;
import org.borghisales.salessysten.model.entities.State;


import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ObservableList<State> stateList = FXCollections.observableArrayList(State.ACTIVE, State.DISACTIVE);
    private static ObservableList<Customer> customers=null;

    @FXML
    private TextField dni;
    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private TextField address;
    @FXML
    private ComboBox<State> cbState;
    @FXML
    private TableView<Customer> tableCustomers;
    @FXML
    private TableColumn<Customer,Integer> colId;
    @FXML
    private TableColumn<Customer,String> colDni;
    @FXML
    private TableColumn<Customer,String> colName;
    @FXML
    private TableColumn<Customer,String> colEmail;
    @FXML
    private TableColumn<Customer,String> colAddress;
    @FXML
    private TableColumn<Customer,State> colState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeCustomerData();
        tableCustomers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
        colEmail.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().email()));
        colDni.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().dni()));
        colAddress.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().address()));
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().state()));
    }
    private void initializeComboBox() {
        cbState.setValue(State.ACTIVE);
        cbState.setItems(stateList);
    }
    @FXML
    public void addCustomer(ActionEvent actionEvent) {
        Customer customer = new Customer(dni.getText(),name.getText(), email.getText(),address.getText(),cbState.getValue());
        if (customerDAO.create(customer)) {
            MenuController.cleanCells(dni, name, email, address);
            updateTable();
        }
    }
    @FXML
    public void updateCustomer(ActionEvent actionEvent) {
        Customer customer = new Customer(dni.getText(),name.getText(), email.getText(), address.getText(),cbState.getValue());
        if (customerDAO.update(customer)) {
            MenuController.cleanCells(dni, name, email, address);
            updateTable();
        }
    }
    @FXML
    public void deleteCustomer(ActionEvent actionEvent) {
        if (customerDAO.delete(dni.getText())){
            MenuController.cleanCells(dni,name,address);
            updateTable();
        }
    }
    @FXML
    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(dni,name,address);
    }
    private void setCells(Customer customer){
        name.setText(customer.name());
        dni.setText(customer.dni());
        email.setText(customer.email());
        address.setText(customer.address());
        cbState.setValue(customer.state());
    }

    private void updateTable() {
        tableCustomers.getItems().clear();
        customerDAO.setTable(customers);
        tableCustomers.setItems(customers);
    }

}
