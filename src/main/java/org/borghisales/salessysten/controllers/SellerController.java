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

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SellerController implements Initializable {
    private final SellerDAO sellerDAO = new SellerDAO();
    private final ObservableList<Seller.State> stateList = FXCollections.observableArrayList(Seller.State.ACTIVO, Seller.State.INACTIVO);
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
    private TableColumn<Seller, Integer> colId;
    @FXML
    private TableColumn<Seller, String> colDni;
    @FXML
    private TableColumn<Seller, String> colName;
    @FXML
    private TableColumn<Seller, String> colPhone;
    @FXML
    private TableColumn<Seller, Seller.State> colState;
    @FXML
    private TableColumn<Seller, Seller.Role> colRole;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeSellerData();
        initializeRoleComboBox();

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
        colRole.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().role()));
    }

    private void initializeComboBox() {
        cbState.setValue(Seller.State.ACTIVO);
        cbState.setItems(stateList);
    }

    private void initializeSellerData() {
        tableSellers.getItems().clear();
        if (sellers == null) {
            sellers = FXCollections.observableArrayList();
            sellerDAO.setTable(sellers);
        }
        tableSellers.setItems(sellers);
    }
    private void initializeRoleComboBox() {
        cbRole.setItems(FXCollections.observableArrayList(Seller.Role.NORMAL, Seller.Role.ADMIN));
        cbRole.setValue(Seller.Role.NORMAL);
    }

    public void addSeller(ActionEvent actionEvent) {
        Seller seller = new Seller(
                dni.getText(),
                name.getText(),
                phone.getText(),
                cbState.getValue(),
                cbRole.getValue(),
                user.getText()
        );

        if (sellerDAO.create(seller)) {
            MenuController.cleanCells(dni, name, phone, user);
            updateTable();
        }
    }

    public void updateSeller(ActionEvent actionEvent) {
        Seller selected = tableSellers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            MenuController.setAlert(Alert.AlertType.WARNING, "Seleccione un vendedor para actualizar");
            return;
        }

        Seller seller = new Seller(
                selected.idSeller(),
                dni.getText(),
                name.getText(),
                phone.getText(),
                cbState.getValue(),
                user.getText(),
                cbRole.getValue()
        );

        if (sellerDAO.update(seller)) {
            MenuController.cleanCells(dni, name, phone, user);
            updateTable();
        }
    }

    public void deleteSeller(ActionEvent actionEvent) {
        if (Objects.equals(dni.getText(), MainController.sellerLog.dni())) {
            MenuController.setAlert(Alert.AlertType.ERROR, "No se puede eliminar el vendedor actual");
            return;
        }
        if (sellerDAO.delete(dni.getText())) {
            MenuController.cleanCells(dni, name, phone, user);
            updateTable();
        }
    }

    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(dni, name, phone, user);
        cbState.setValue(Seller.State.ACTIVO);
        cbRole.setValue(Seller.Role.NORMAL);
        tableSellers.getSelectionModel().clearSelection();
    }

    private void setCells(Seller seller) {
        dni.setText(seller.dni());
        name.setText(seller.name());
        phone.setText(seller.phoneNumber());
        user.setText(seller.user());
        cbState.setValue(seller.state());
        cbRole.setValue(seller.role());
    }

    private void updateTable() {
        tableSellers.getItems().clear();
        sellerDAO.setTable(sellers);
        tableSellers.setItems(sellers);
    }
}