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
import org.borghisales.salessysten.controllers.MenuController; // Asegúrate que este import sea correcto según tu proyecto
import org.borghisales.salessysten.model.Seller;
import org.borghisales.salessysten.model.SellerDAO;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerController implements Initializable {
    private final SellerDAO sellerDAO = new SellerDAO();
    private final ObservableList<Seller.State> stateList = FXCollections.observableArrayList(Seller.State.ACTIVE, Seller.State.DISACTIVE);
    private static ObservableList<Seller> sellers = null;
    private Seller seller; // Este guarda el vendedor original seleccionado en la tabla (evitamos a los usuarios malicisosos)

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeSellerData();
    }

    private void initializeTable() {
        tableSellers.setOnMouseClicked(mouseEvent -> {
            if (!tableSellers.getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                seller = tableSellers.getSelectionModel().getSelectedItem();
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
        if(dni.getText().isEmpty()){MenuController.setAlert(Alert.AlertType.WARNING, "Ingresa el ID"); return;}  //Evitarnos que se ingrese un vendedor sin usuario
        Seller seller = new Seller(dni.getText(),name.getText(),phone.getText(), cbState.getValue(),user.getText());
        if (sellerDAO.create(seller)) {
            MenuController.cleanCells(dni, name, phone, user);
            updateTable();
        }
    }

    public void updateSeller(ActionEvent actionEvent) {
        if (this.seller == null) {
            MenuController.setAlert(Alert.AlertType.WARNING, "Selecciona un vendedor primero haciendo doble clic en la tabla");
            return;
        }
        Seller datosNuevos = new Seller(dni.getText(), name.getText(), phone.getText(), cbState.getValue(), user.getText());
        verifyUpdate(this.seller, datosNuevos);
    }

    public void deleteSeller(ActionEvent actionEvent) {
        if (Objects.equals(dni.getText(), MainController.sellerLog.dni())){
            MenuController.setAlert(Alert.AlertType.ERROR,"No es posible eliminar el vendedor durante su sesión activa");
            return;
        }
        if (dni.getText() != null && !dni.getText().isEmpty()){
            verifyDelete(dni.getText());
        } else {
            sellerDAO.delete(dni.getText());
        }
    }

    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(dni,name,phone,user);
        this.seller = null;
    }

    private void setCells(Seller seller){
        dni.setText(seller.dni());
        name.setText(seller.name());
        phone.setText(seller.phoneNumber());
        user.setText(seller.user());
        cbState.setValue(seller.state());
    }

    private void updateTable(){
        tableSellers.getItems().clear();
        sellerDAO.setTable(sellers);
        tableSellers.setItems(sellers);
        this.seller = null;
    }

    private void verifyDelete(String dniSelected) {
        if (this.seller == null || !this.seller.dni().equals(dniSelected)) {
            for(Seller s : sellers) {
                if(s.dni().equals(dniSelected)) {
                    this.seller = s;
                    break;
                }
            }
            if (this.seller == null) {
                MenuController.setAlert(Alert.AlertType.ERROR, "Vendedor no encontrado.");
                return;
            }
        }

        if(this.seller.user() == null || this.seller.user().isEmpty()){
            sellerDAO.delete(dniSelected);
            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Vendedor eliminado (Sin contraseña)");
            MenuController.cleanCells(dni,name,phone,user);
            updateTable();
            return;
        }

        // Vendedor existe y tiene contraseña
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Advertencia de Seguridad");
        dialog.setHeaderText("Esta es una acción sensible.");
        dialog.setContentText("Contraseña del vendedor a eliminar para proceder:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(input -> {
            if (input.equals(this.seller.user())) {
                sellerDAO.delete(dniSelected);
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Vendedor eliminado");
                MenuController.cleanCells(dni,name,phone,user);
                updateTable();
            } else {
                MenuController.setAlert(Alert.AlertType.ERROR, "Contraseña INCORRECTA.");
            }
        });
    }

    private void verifyUpdate(Seller originalSeller, Seller newSellerData) {
        // Vendedor encontrado pero sin contraseña original
        if(originalSeller.user() == null || originalSeller.user().isEmpty()){
            sellerDAO.update(newSellerData);
            MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Vendedor Actualizado (Sin contraseña previa)");
            MenuController.cleanCells(dni,name,phone,user);
            updateTable();
            return;
        }

        // Vendedor existe y tiene contraseña
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Advertencia de Seguridad");
        dialog.setHeaderText("Esta es una acción sensible.");
        dialog.setContentText("Ingrese contraseña del vendedor "+ originalSeller.name() +" para guardar cambios:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(input -> {
            if (input.equals(originalSeller.user())) {
                sellerDAO.update(newSellerData);
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Vendedor Actualizado");
                MenuController.cleanCells(dni,name,phone,user);
                updateTable();
            } else {
                MenuController.setAlert(Alert.AlertType.ERROR, "Contraseña INCORRECTA. Cambios descartados.");
            }
        });
    }
}