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

public class CustomerController extends MenuController implements Initializable {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ObservableList<Customer.State> stateList = FXCollections.observableArrayList(Customer.State.ACTIVE, Customer.State.DISACTIVE);

    private ObservableList<Customer> customers;

    @FXML private TextField dni;
    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private ComboBox<Customer.State> cbState;

    // Botones
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private Button btnReturn;

    @FXML private TableView<Customer> tableCustomers;
    @FXML private TableColumn<Customer,Integer> colId;
    @FXML private TableColumn<Customer,String> colDni;
    @FXML private TableColumn<Customer,String> colName;
    @FXML private TableColumn<Customer,String> colAddress;
    @FXML private TableColumn<Customer,Customer.State> colState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeCustomerData();
        //Aplicando estilos y animaciones de botones
        UIEfectos.styleButtonAdd(btnAdd);
        UIEfectos.styleButtonUpdate(btnUpdate);
        UIEfectos.styleButtonDelete(btnDelete);
        UIEfectos.styleButtonGray(btnClear);
        UIEfectos.styleButtonReturn(btnReturn);
    }

    private void initializeCustomerData() {
        customers = FXCollections.observableArrayList();
        customerDAO.setTable(customers);
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

    private boolean validarEntradas() {
        // verifica que todos los campos esten llenos
        if (dni.getText().isEmpty() || name.getText().isEmpty() || address.getText().isEmpty()) {
            setAlert(Alert.AlertType.WARNING, "Todos los campos son obligatorios.");
            return false;
        }
        //identificador de 8 digitos
        if (!dni.getText().matches("\\d{8}")) {
            setAlert(Alert.AlertType.WARNING, "El DNI debe tener exactamente 8 números.");
            return false;
        }

        //Nombre: Solo letras y espacios
        if (!name.getText().matches("[a-zA-Z\\s]+")) {
            setAlert(Alert.AlertType.WARNING, "El nombre solo puede contener letras.");
            return false;
        }

        return true;
    }
    /*EVENTOS DE BOTONES*/
    @FXML
    public void addCustomer(ActionEvent actionEvent) {
        if (!validarEntradas()) return;

        Customer customer = new Customer(dni.getText(), name.getText(), address.getText(), cbState.getValue());
        if (customerDAO.create(customer)) {
            cleanCellsScreen(null); // Limpiar después de agregar
            initializeCustomerData(); // Recargar tabla
        }
    }

    @FXML
    public void updateCustomer(ActionEvent actionEvent) {
        if (!validarEntradas()) return;
        Customer customer = new Customer(dni.getText(), name.getText(), address.getText(), cbState.getValue());
        if (customerDAO.update(customer)) {
            cleanCellsScreen(null);
            initializeCustomerData();
        }
    }

    @FXML
    public void deleteCustomer(ActionEvent actionEvent) {
        if (dni.getText().isEmpty()) {
            setAlert(Alert.AlertType.WARNING, "Seleccione un cliente o escriba su DNI para eliminar.");
            return;
        }

        if (customerDAO.delete(dni.getText())){
            cleanCellsScreen(null);
            initializeCustomerData();
        }
    }

    @FXML
    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(dni, name, address);
        cbState.setValue(Customer.State.ACTIVE);
        tableCustomers.getSelectionModel().clearSelection();
    }

    @FXML
    public void returnToMenu(ActionEvent event) {
        openNewStage(MANAGEMENT_VIEW_FXML, "Management");
        closeCurrentStage(btnReturn);
    }

    private void setCells(Customer customer){
        name.setText(customer.name());
        dni.setText(customer.dni());
        address.setText(customer.address());
        cbState.setValue(customer.state());
    }
}