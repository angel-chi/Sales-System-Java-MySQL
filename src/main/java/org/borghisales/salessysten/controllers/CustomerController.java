package org.borghisales.salessysten.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.borghisales.salessysten.model.AbstractBaseDAO;
import org.borghisales.salessysten.model.Customer;
import org.borghisales.salessysten.model.CustomerDAO;

public class CustomerController extends AbstractCRUDController<Customer> {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ObservableList<Customer.State> stateList = FXCollections.observableArrayList(Customer.State.ACTIVE, Customer.State.DISACTIVE);

    @FXML private TextField DNI;
    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private ComboBox<Customer.State> cbState;
    @FXML private TableView<Customer> tableCustomers;

    // Columnas
    @FXML private TableColumn<Customer,Integer> colId;
    @FXML private TableColumn<Customer,String> colDni;
    @FXML private TableColumn<Customer,String> colName;
    @FXML private TableColumn<Customer,String> colAddress;
    @FXML private TableColumn<Customer,Customer.State> colState;

    // Implementación de métodos del padre.
    @Override
    protected AbstractBaseDAO<Customer> getDAO() {
        return customerDAO;
    }

    @Override
    protected TableView<Customer> getTableView() {
        return tableCustomers;
    }

    @Override
    protected ComboBox<?> getComboBoxState() {
        return cbState;
    }

    @Override
    protected void setupComboBox() {
        cbState.setItems(stateList);
        cbState.setValue(Customer.State.ACTIVE);
    }

    @Override
    protected void setupTableColumns() {
        colId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idCustomer()).asObject());
        colName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().name()));
        colDni.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().dni()));
        colAddress.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().address()));
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().state()));
    }

    @Override
    protected boolean validateSpecificFields() {
        if (campoVacio(DNI) || campoVacio(name) || campoVacio(address)) {
            mostrarAdvertencia("Debes de completar todos los campos antes de guardar.");
            return false;
        }
        return true;
    }

    @Override
    protected Customer createEntityFromFields() {
        return new Customer(DNI.getText(), name.getText(), address.getText(), cbState.getValue());
    }

    @Override
    protected void setFormFields(Customer customer) {
        name.setText(customer.name());
        DNI.setText(customer.dni());
        address.setText(customer.address());
        cbState.setValue(customer.state());
    }

    @Override
    protected void clearFormFields() {
        MenuController.cleanCells(DNI, name, address);
    }

    @Override
    protected String getEntityId() {
        return DNI.getText();
    }

    // En FXML se tienen que vincular los botones a los "Action" del padre.
    // Por si FXML no detecta dichos métodos directamente, se crearon puentes abajo:
    @FXML public void addCustomer(ActionEvent e) {
        super.addAction(e);
    }
    @FXML public void updateCustomer(ActionEvent e) {
        super.updateAction(e);
    }
    @FXML public void deleteAction(ActionEvent e) {
        super.deleteAction(e);
    }
    @FXML public void deleteCustomer(ActionEvent event) {
        super.deleteAction(event);
    }
    @FXML public void cleanCellsScreen(ActionEvent event) {
        super.cleanAction(event);
    }
}
