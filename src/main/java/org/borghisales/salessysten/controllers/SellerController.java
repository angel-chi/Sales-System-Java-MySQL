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
import org.borghisales.salessysten.model.Seller;
import org.borghisales.salessysten.model.SellerDAO;
import org.borghisales.salessysten.model.CRUD;
import org.borghisales.salessysten.model.IValidable;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SellerController implements Initializable, IValidable{
    //Usando la abstraccion
    private final CRUD<Seller> sellerDAO = new SellerDAO();
    private final ObservableList<Seller.State> stateList = FXCollections.observableArrayList(Seller.State.ACTIVE, Seller.State.DISACTIVE);
    private final ObservableList<Seller.Role> roleList = FXCollections.observableArrayList(Seller.Role.MANAGER, Seller.Role.SELLER);
    private static ObservableList<Seller> sellers = null;

    @FXML
    private TextField dni;
    @FXML
    private TextField name;
    @FXML
    private TextField phone;
    @FXML
    private TextField user;
    @FXML
    private ComboBox<Seller.State> cbState;
    @FXML
    private ComboBox<Seller.Role> cbRole;
    @FXML
    private TableView<Seller> tableSellers;
    @FXML
    private TableColumn<Seller,Integer> colId;
    @FXML
    private TableColumn<Seller,String> colDni;
    @FXML
    private TableColumn<Seller,String> colName;
    @FXML
    private TableColumn<Seller,String> colPhone;
    @FXML
    private TableColumn<Seller, Seller.State> colState;
    @FXML
    private TableColumn<Seller, Seller.Role> colRole;


    @Override
    public String validarCampos() {
        String dniText = dni.getText();
        String nameText = name.getText();
        String phoneText = phone.getText();
        String userText = user.getText();
        if (IValidable.esCampoVacio(dniText)) {
            return "El campo DNI del vendedor es obligatorio.";
        }
        if (IValidable.esCampoVacio(nameText)) {
            return "El campo Nombre del vendedor es obligatorio.";
        }
        if (IValidable.esCampoVacio(phoneText)) {
            return "El campo Teléfono del vendedor es obligatorio.";
        }
        if (IValidable.esCampoVacio(userText)) {
            return "El campo Usuario del vendedor es obligatorio.";
        }

        return null; // La validación fue exitosa
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeSellerData();
    }

    private void initializeTable() {
        tableSellers.setOnMouseClicked(mouseEvent -> {
            if (!tableSellers.getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                Seller seller = tableSellers.getSelectionModel().getSelectedItem();
                setCells(seller);
            }
        });

        colId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idSeller()).asObject());
        colDni.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().dni()));
        colName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().name()));
        colPhone.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().phoneNumber()));
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().state()));
    }

    private void initializeComboBox() {
        cbState.setValue(Seller.State.ACTIVE);
        cbState.setItems(stateList);
        cbRole.setValue(Seller.Role.SELLER);
        cbRole.setItems(roleList);
    }

    private void initializeSellerData() {
        tableSellers.getItems().clear();
        if (sellers == null) {
            sellers = FXCollections.observableArrayList();
            sellerDAO.setTable(sellers);
        }
        tableSellers.setItems(sellers);
    }


    public void addSeller(ActionEvent actionEvent){
        String errorMessage = validarCampos();
        if (errorMessage != null) {
            MenuController.setAlert(Alert.AlertType.WARNING, errorMessage);
            return; // Detiene la ejecución si hay errores
        }
        Seller seller = new Seller(dni.getText(),name.getText(),phone.getText(), cbState.getValue(),user.getText(), cbRole.getValue());
        if (sellerDAO.create(seller)) {
            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Vendedor añadido con éxito");
            MenuController.cleanCells(dni, name, phone, user);
            updateTable();
        } else {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error añadiendo vendedor: el usuario ya existe");
        }
    }
    public void updateSeller(ActionEvent actionEvent) {
        String errorMessage = validarCampos();

        if (errorMessage != null) {
            MenuController.setAlert(Alert.AlertType.WARNING, errorMessage);
            return; // Detiene la ejecución si hay errores
        }
        Seller seller = new Seller(dni.getText(),name.getText(),phone.getText(),(Seller.State) cbState.getValue(),user.getText(), cbRole.getValue());
        if (sellerDAO.update(seller)) {
            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Vendedor actualizado con éxito");
            MenuController.cleanCells(dni, name, phone, user);
            updateTable();
        } else {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error actualizando vendedor: Usuario no encontrado o error de datos");
        }
    }

    public void deleteSeller(ActionEvent actionEvent) {
        if (Objects.equals(dni.getText(), MainController.sellerLog.dni())){
            MenuController.setAlert(Alert.AlertType.ERROR,"No se puede eliminar el vendedor actual");
            return;
        }
        if (sellerDAO.delete(dni.getText())){
            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Vendedor eliminado con éxito");
            MenuController.cleanCells(dni,name,phone,user);
            updateTable();
        }
    }

    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(dni,name,phone,user);
    }

    private void setCells(Seller seller){
        dni.setText(seller.dni());
        name.setText(seller.name());
        phone.setText(seller.phoneNumber());
        user.setText(seller.user());
        cbState.setValue(seller.state());
        cbRole.setValue(seller.role());
    }

    private void updateTable(){
        tableSellers.getItems().clear();
        sellerDAO.setTable(sellers);
        tableSellers.setItems(sellers);
    }


}
