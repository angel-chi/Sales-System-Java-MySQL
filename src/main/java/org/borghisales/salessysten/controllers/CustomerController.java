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
import org.borghisales.salessysten.model.Cliente;
import org.borghisales.salessysten.model.ClienteDAO;


import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ObservableList<Cliente.Estado> stateList = FXCollections.observableArrayList(Cliente.Estado.ACTIVO, Cliente.Estado.INACTIVO);
    private static ObservableList<Cliente> clientes=null;

    @FXML
    private TextField dni;
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private ComboBox<Cliente.Estado> cbState;
    @FXML
    private TableView<Cliente> tableCustomers;
    @FXML
    private TableColumn<Cliente,Integer> colId;
    @FXML
    private TableColumn<Cliente,String> colDni;
    @FXML
    private TableColumn<Cliente,String> colName;
    @FXML
    private TableColumn<Cliente,String> colAddress;
    @FXML
    private TableColumn<Cliente,Cliente.Estado> colState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeCustomerData();
    }
    private void initializeCustomerData() {
        tableCustomers.getItems().clear();
        if (clientes == null) {
            clientes = FXCollections.observableArrayList();
            clienteDAO.setTable(clientes);
        }
        tableCustomers.setItems(clientes);
    }
    private void initializeTable() {
        tableCustomers.setOnMouseClicked(mouseEvent -> {
            if (!tableCustomers.getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                Cliente cliente = tableCustomers.getSelectionModel().getSelectedItem();
                setCells(cliente);
            }
        });

        colId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idCliente()).asObject());
        colName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().nombre()));
        colDni.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().correo()));
        colAddress.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().direccion()));
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().estado()));
    }
    private void initializeComboBox() {
        cbState.setValue(Cliente.Estado.ACTIVO);
        cbState.setItems(stateList);
    }
    @FXML
    public void addCustomer(ActionEvent actionEvent) {
        Cliente cliente = new Cliente(dni.getText(),name.getText(),address.getText(),cbState.getValue());
        if (clienteDAO.create(cliente)) {
            MenuController.cleanCells(dni, name, address);
            updateTable();
        }
    }
    @FXML
    public void updateCustomer(ActionEvent actionEvent) {
        Cliente cliente = new Cliente(dni.getText(),name.getText(),address.getText(),cbState.getValue());
        if (clienteDAO.update(cliente)) {
            MenuController.cleanCells(dni, name, address);
            updateTable();
        }
    }
    @FXML
    public void deleteCustomer(ActionEvent actionEvent) {
        if (clienteDAO.delete(dni.getText())){
            MenuController.cleanCells(dni,name,address);
            updateTable();
        }
    }
    @FXML
    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(dni,name,address);
    }
    private void setCells(Cliente cliente){
        name.setText(cliente.nombre());
        dni.setText(cliente.correo());
        address.setText(cliente.direccion());
        cbState.setValue(cliente.estado());
    }

    private void updateTable() {
        tableCustomers.getItems().clear();
        clienteDAO.setTable(clientes);
        tableCustomers.setItems(clientes);
    }

}
