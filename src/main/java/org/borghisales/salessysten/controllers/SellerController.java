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

public class SellerController extends MenuController implements Initializable {

    private final SellerDAO sellerDAO = new SellerDAO();
    private final ObservableList<Seller.State> stateList = FXCollections.observableArrayList(Seller.State.ACTIVE, Seller.State.DISACTIVE);

    // Cambiado a no estático para evitar problemas de refresco
    private ObservableList<Seller> sellers;

    // Campos de Texto
    @FXML private TextField dni;
    @FXML private TextField name;
    @FXML private TextField phone;
    @FXML private TextField user;
    @FXML private ComboBox<Seller.State> cbState;

    // Botones (Agregados para vincular con FXML y aplicar efectos)
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private Button btnReturn;

    // Tabla
    @FXML private TableView<Seller> tableSellers;
    @FXML private TableColumn<Seller,Integer> colId;
    @FXML private TableColumn<Seller,String> colDni;
    @FXML private TableColumn<Seller,String> colName;
    @FXML private TableColumn<Seller,String> colPhone;
    @FXML private TableColumn<Seller, Seller.State> colState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeComboBox();
        initializeSellerData();

        // Implementación de UIEfectos (Estilos y Animaciones)
        UIEfectos.styleButtonAdd(btnAdd);
        UIEfectos.styleButtonUpdate(btnUpdate);
        UIEfectos.styleButtonDelete(btnDelete);
        UIEfectos.styleButtonGray(btnClear);
        UIEfectos.styleButtonReturn(btnReturn);
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
    }

    private void initializeSellerData() {
        // Inicializamos la lista local
        sellers = FXCollections.observableArrayList();
        sellerDAO.setTable(sellers);
        tableSellers.setItems(sellers);
    }

    // --- ACCIONES CRUD (Lógica original) ---

    public void addSeller(ActionEvent actionEvent){
        Seller seller = new Seller(dni.getText(), name.getText(), phone.getText(), cbState.getValue(), user.getText());
        if (sellerDAO.create(seller)) {
            cleanCellsScreen(null); // Usamos cleanCellsScreen para limpiar
            initializeSellerData(); // Recargamos tabla
        }
    }

    public void updateSeller(ActionEvent actionEvent) {
        Seller seller = new Seller(dni.getText(), name.getText(), phone.getText(), cbState.getValue(), user.getText());
        if (sellerDAO.update(seller)) {
            cleanCellsScreen(null);
            initializeSellerData();
        }
    }

    public void deleteSeller(ActionEvent actionEvent) {
        if (Objects.equals(dni.getText(), MainController.sellerLog.dni())){
            setAlert(Alert.AlertType.ERROR,"Cannot delete the current seller");
            return;
        }
        if (sellerDAO.delete(dni.getText())){
            cleanCellsScreen(null);
            initializeSellerData();
        }
    }

    public void cleanCellsScreen(ActionEvent actionEvent) {
        MenuController.cleanCells(dni, name, phone, user);
        cbState.setValue(Seller.State.ACTIVE);
        tableSellers.getSelectionModel().clearSelection();
    }

    @FXML
    public void returnToMenu(ActionEvent event) {
        openNewStage(MANAGEMENT_VIEW_FXML, "Management");
        closeCurrentStage(btnReturn);
    }


    private void setCells(Seller seller){
        dni.setText(seller.dni());
        name.setText(seller.name());
        phone.setText(seller.phoneNumber());
        user.setText(seller.user());
        cbState.setValue(seller.state());
    }
}