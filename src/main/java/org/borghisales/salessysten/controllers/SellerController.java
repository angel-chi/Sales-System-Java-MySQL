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
import org.borghisales.salessysten.model.Vendedor; // Seller -> Vendedor
import org.borghisales.salessysten.model.VendedorDAO; // SellerDAO -> VendedorDAO

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SellerController implements Initializable {
    private final VendedorDAO vendedorDAO = new VendedorDAO(); // SellerDAO -> VendedorDAO
    private final ObservableList<Vendedor.Estado> stateList = FXCollections.observableArrayList(Vendedor.Estado.ACTIVO, Vendedor.Estado.INACTIVO); // Seller.State -> Vendedor.Estado
    private static ObservableList<Vendedor> vendedores = null; // Seller -> Vendedor, sellers -> vendedores

    @FXML
    private TextField identificacion; // dni -> identificacion
    @FXML
    private TextField nombre; // name -> nombre
    @FXML
    private TextField telefono; // phone -> telefono
    @FXML
    private TextField usuario; // user -> usuario
    @FXML
    private ComboBox<Vendedor.Estado> cbState; // Seller.State -> Vendedor.Estado
    @FXML
    private TableView<Vendedor> tableSellers; // Seller -> Vendedor
    @FXML
    private TableColumn<Vendedor,Integer> colId; // Seller -> Vendedor
    @FXML
    private TableColumn<Vendedor,String> colDni; // Seller -> Vendedor
    @FXML
    private TableColumn<Vendedor,String> colName; // Seller -> Vendedor
    @FXML
    private TableColumn<Vendedor,String> colPhone; // Seller -> Vendedor
    @FXML
    private TableColumn<Vendedor, Vendedor.Estado> colState; // Seller -> Vendedor, Seller.State -> Vendedor.Estado




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeSellerData();
    }

    private void initializeTable() {
        tableSellers.setOnMouseClicked(mouseEvent -> {
            if (!tableSellers.getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                Vendedor vendedor = tableSellers.getSelectionModel().getSelectedItem(); // Seller -> Vendedor
                setCells(vendedor);
            }
        });

        colId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idVendedor()).asObject()); // idSeller() -> idVendedor()
        colDni.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().identificacion())); // dni() -> identificacion()
        colName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().nombre())); // name() -> nombre()
        colPhone.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().telefono())); // phoneNumber() -> telefono()
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().estado())); // state() -> estado()
    }

    private void initializeComboBox() {
        cbState.setValue(Vendedor.Estado.ACTIVO); // Seller.State.ACTIVE -> Vendedor.Estado.ACTIVO
        cbState.setItems(stateList);
    }

    private void initializeSellerData() {
        tableSellers.getItems().clear();
        if (vendedores == null) { // sellers -> vendedores
            vendedores = FXCollections.observableArrayList(); // sellers -> vendedores
            vendedorDAO.setTable(vendedores); // sellerDAO -> vendedorDAO, sellers -> vendedores
        }
        tableSellers.setItems(vendedores); // sellers -> vendedores
    }


    public void addSeller(ActionEvent actionEvent){
        Vendedor vendedor = new Vendedor(identificacion.getText(),nombre.getText(),telefono.getText(), cbState.getValue(),usuario.getText()); // Seller -> Vendedor, dni -> identificacion, name -> nombre, phone -> telefono, user -> usuario
        if (vendedorDAO.create(vendedor)) { // sellerDAO -> vendedorDAO
            MenuController.cleanCells(identificacion, nombre, telefono, usuario); // dni -> identificacion, name -> nombre, phone -> telefono, user -> usuario
            updateTable();
        }
    }
    public void updateSeller(ActionEvent actionEvent) {
        Vendedor vendedor = new Vendedor(identificacion.getText(),nombre.getText(),telefono.getText(), cbState.getValue(),usuario.getText()); // Seller -> Vendedor, dni -> identificacion, name -> nombre, phone -> telefono, user -> usuario
        if (vendedorDAO.update(vendedor)) { // sellerDAO -> vendedorDAO
            MenuController.cleanCells(identificacion, nombre, telefono, usuario); // dni -> identificacion, name -> nombre, phone -> telefono, user -> usuario
            updateTable();
        }
    }

    public void deleteSeller(ActionEvent actionEvent) {
        if (Objects.equals(identificacion.getText(), MainController.vendedorLogeado.identificacion())){ // dni -> identificacion, sellerLog -> vendedorLogeado, dni() -> identificacion()
            MenuController.setAlert(Alert.AlertType.ERROR,"No se puede eliminar el vendedor actual"); // Mensaje traducido
            return;
        }
        if (vendedorDAO.delete(identificacion.getText())){ // sellerDAO -> vendedorDAO, dni -> identificacion
            MenuController.cleanCells(identificacion,nombre,telefono,usuario); // dni -> identificacion, name -> nombre, phone -> telefono, user -> usuario
            updateTable();
        }
    }

    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(identificacion,nombre,telefono,usuario); // dni -> identificacion, name -> nombre, phone -> telefono, user -> usuario
    }

    private void setCells(Vendedor vendedor){ // Seller -> Vendedor
        identificacion.setText(vendedor.identificacion()); // dni -> identificacion
        nombre.setText(vendedor.nombre()); // name -> nombre
        telefono.setText(vendedor.telefono()); // phoneNumber -> telefono
        usuario.setText(vendedor.usuario()); // user -> usuario
        cbState.setValue(vendedor.estado()); // state -> estado
    }

    private void updateTable(){
        tableSellers.getItems().clear();
        vendedorDAO.setTable(vendedores); // sellerDAO -> vendedorDAO, sellers -> vendedores
        tableSellers.setItems(vendedores); // sellers -> vendedores
    }


}
