package org.borghisales.salessysten.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.borghisales.salessysten.model.dao.ProveedorDAO;
import org.borghisales.salessysten.model.entities.Proveedor;
import org.borghisales.salessysten.model.entities.State;

import java.net.URL;
import java.util.ResourceBundle;

public class ProveedorController  implements Initializable {
    // Valores importantes
    private final ProveedorDAO proveedorDAO = new ProveedorDAO();
    private final ObservableList<State> stateList = FXCollections.observableArrayList(State.ACTIVE, State.DISACTIVE);
    private static ObservableList<Proveedor> proveedors =null;
    // Atributos de clase
    @FXML private TextField name;
    @FXML private TextField email;
    @FXML private ComboBox<State> state;
    // Tabla
    @FXML private TableView<Proveedor> tableProveedor;
    // Columnas de tabla
    @FXML private TableColumn<Proveedor,Integer> colId;
    @FXML private TableColumn<Proveedor,String> colName;
    @FXML private TableColumn<Proveedor,String> colEmail;
    @FXML private TableColumn<Proveedor,State> colState;
    // Funciones de carga de interfaz
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeProductData();
        tableProveedor.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void initializeTable() {
        tableProveedor.setOnMouseClicked(mouseEvent -> {
            if (!tableProveedor.getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                Proveedor proveedor = tableProveedor.getSelectionModel().getSelectedItem();
                setCells(proveedor);
            }
        });

        colId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idProveedor()).asObject());
        colName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().name()));
        colEmail.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().email()));
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().state()));
    }

    private void initializeComboBox() {
        state.setValue(State.ACTIVE);
        state.setItems(stateList);
    }

    private void initializeProductData() {
        tableProveedor.getItems().clear();
        if (proveedors == null) {
            proveedors = FXCollections.observableArrayList();
            proveedorDAO.setTable(proveedors);
        }
        tableProveedor.setItems(proveedors);
    }
    // Funciones complementarias para la vista
    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(name,email);
    }
    private void setCells(Proveedor proveedor){
        name.setText(proveedor.name());
        email.setText(String.valueOf(proveedor.email()));
        state.setValue(proveedor.state());
    }
    private void updateTable() {
        tableProveedor.getItems().clear();
        proveedorDAO.setTable(proveedors);
        tableProveedor.setItems(proveedors);
    }
    // Funciones para CRUD
    public void addProduct(ActionEvent actionEvent) {
        Proveedor proveedor = new Proveedor(name.getText(),email.getText(), state.getValue());
        if (proveedorDAO.create(proveedor)) {
            MenuController.cleanCells(name,email);
            updateTable();
        }
    }
    public void updateProduct(ActionEvent actionEvent) {
        Proveedor proveedor = new Proveedor(name.getText(),email.getText(), state.getValue());
        if (proveedorDAO.update(proveedor)) {
            MenuController.cleanCells(name,email);
            updateTable();
        }
    }
    public void deleteProduct(ActionEvent actionEvent) {
        if (proveedorDAO.delete(name.getText())) {
            MenuController.cleanCells(name,email);
            updateTable();
        }
    }
}
