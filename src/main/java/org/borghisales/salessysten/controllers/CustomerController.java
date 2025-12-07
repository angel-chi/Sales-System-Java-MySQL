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

public class CustomerController implements Initializable {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ObservableList<Customer.State> stateList = FXCollections.observableArrayList(Customer.State.ACTIVE, Customer.State.DISACTIVE);
    private static ObservableList<Customer> customers=null;

    // customerDAO: Se encarga de acceder a los datos.
    // stateList: Contiene los posibles estados de un cliente.
    // customers: Contiene a todos los objetos Customer de la tabla.

    // Variables de la interfaz de usuario:
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

    @Override // Métodos para configurar la Vista
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable(); // Configura tabla y columnas
        initializeComboBox(); // Configura menú desplegable de estado
        initializeCustomerData(); // Carga datos iniciales de los clientes
    }

    // Carga customer a TableView a través de customerDAO.
    private void initializeCustomerData() {
        tableCustomers.getItems().clear();
        if (customers == null) {
            customers = FXCollections.observableArrayList();
            customerDAO.setTable(customers);
        }
        tableCustomers.setItems(customers);
    }

    // Genera el "Doble click para editar" y asigna los datos a las columnas.
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

    // ComboBox activo. Rellena ComboBox con la lista de estados.
    private void initializeComboBox() {
        cbState.setValue(Customer.State.ACTIVE);
        cbState.setItems(stateList);
    }
    @FXML // Agrega un cliente nuevo.
    public void addCustomer(ActionEvent actionEvent) {
        Customer customer = new Customer(DNI.getText(),name.getText(),address.getText(),cbState.getValue());
        if (customerDAO.create(customer)) {
            MenuController.cleanCells(DNI, name, address);
            updateTable();
        }
    }
    @FXML // Actualiza la información de un cliente.
    public void updateCustomer(ActionEvent actionEvent) {
        Customer customer = new Customer(DNI.getText(),name.getText(),address.getText(),cbState.getValue());
        if (customerDAO.update(customer)) {
            MenuController.cleanCells(DNI, name, address);
            updateTable();
        }
    }
    @FXML // Elimina un cliente.
    public void deleteCustomer(ActionEvent actionEvent) {
        if (customerDAO.delete(DNI.getText())){
            MenuController.cleanCells(DNI,name,address);
            updateTable();
        }
    }
    @FXML // Limpia el texto de los campos de texto disponibles.
    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(DNI,name,address);
    }
    // Rellena campos de texto con la información de un cliente.
    private void setCells(Customer customer){
        name.setText(customer.name());
        DNI.setText(customer.dni());
        address.setText(customer.address());
        cbState.setValue(customer.state());
    }
    // "Refresca" la lista de clientes, después de haber hecho un cambio.
    private void updateTable() {
        tableCustomers.getItems().clear();
        customerDAO.setTable(customers);
        tableCustomers.setItems(customers);
    }

}
