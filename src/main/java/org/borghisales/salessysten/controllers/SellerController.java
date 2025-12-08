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
import org.borghisales.salessysten.model.entities.Nivel;
import org.borghisales.salessysten.model.entities.Seller;
import org.borghisales.salessysten.model.dao.SellerDAO;
import org.borghisales.salessysten.model.entities.State;
import org.borghisales.salessysten.model.entities.Nivel;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SellerController implements Initializable {
    private final SellerDAO sellerDAO = new SellerDAO();
    private final ObservableList<State> stateList = FXCollections.observableArrayList(State.ACTIVE, State.DISACTIVE);
    private static ObservableList<Seller> sellers = null;

    @FXML
    private TextField dni;
    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private TextField phone;
    @FXML
    private TextField user;
    @FXML
    private TextField password;
    @FXML
    private ComboBox<Nivel> nivel;
    @FXML
    private ComboBox<State> cbState;
    @FXML
    private TableView<Seller> tableSellers;
    @FXML
    private TableColumn<Seller,Integer> colId;
    @FXML
    private TableColumn<Seller,String> colDni;
    @FXML
    private TableColumn<Seller,String> colName;
    @FXML
    private TableColumn<Seller,String> colEmail;
    @FXML
    private TableColumn<Seller,String> colPhone;
    @FXML
    private TableColumn<Seller, State> colState;
    @FXML
    private TableColumn<Seller, Nivel> colNivel;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeSellerData();
        tableSellers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
        colEmail.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().email()));
        colPhone.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().phoneNumber()));
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().state()));
        colNivel.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().nivel()));
    }

    private void initializeComboBox() {
        cbState.setValue(State.ACTIVE);
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


    public void addSeller(ActionEvent actionEvent){
        if (dni.getText().isEmpty() || name.getText().isEmpty() || phone.getText().isEmpty() || user.getText().isEmpty() || password.getText().isEmpty()) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Debe llenar los datos po weon");
            ;
        }
        else {
            Seller seller = new Seller(dni.getText(), name.getText(), email.getText(), phone.getText(), cbState.getValue(), nivel.getValue(), user.getText(), password.getText());
            if (sellerDAO.create(seller)) {
                MenuController.cleanCells(dni, name, phone, user, password);
                updateTable();
            }
        }
    }
    public void updateSeller(ActionEvent actionEvent) {
        if (dni.getText().isEmpty() || user.getText().isEmpty() || password.getText().isEmpty()) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Los campos DNI, Usuario o contraseña no pueden ser vacios");
            ;
        }
        else {
            Seller seller = new Seller(dni.getText(), name.getText(), email.getText(), phone.getText(), (State) cbState.getValue(), (Nivel) nivel.getValue(),user.getText(), password.getText());
            if (sellerDAO.update(seller)) {
                MenuController.cleanCells(dni, name, phone, user, password);
                updateTable();
            }
        }
    }

    public void deleteSeller(ActionEvent actionEvent) {
        if (Objects.equals(dni.getText(), MainController.sellerLog.dni())){
            MenuController.setAlert(Alert.AlertType.ERROR,"Cannot delete the current seller");
            return;
        }
        if (sellerDAO.delete(user.getText())){
            MenuController.cleanCells(dni,name,email,phone,user);
            updateTable();
        }
    }

    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(dni,name,phone,user);
    }

    private void setCells(Seller seller){
        dni.setText(seller.dni());
        name.setText(seller.name());
        email.setText(seller.email());
        phone.setText(seller.phoneNumber());
        user.setText(seller.user());
        password.setText(seller.password());
        cbState.setValue(seller.state());
        nivel.setValue(seller.nivel());
    }

    private void updateTable(){
        tableSellers.getItems().clear();
        sellerDAO.setTable(sellers);
        tableSellers.setItems(sellers);
    }


}
