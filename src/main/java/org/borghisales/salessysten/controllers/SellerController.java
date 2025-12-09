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
import org.borghisales.salessysten.model.Seller;
import org.borghisales.salessysten.model.SellerDAO;

import java.util.Objects;

public class SellerController extends AbstractCRUDController<Seller> {

    private final SellerDAO sellerDAO = new SellerDAO();
    private final ObservableList<Seller.State> stateList = FXCollections.observableArrayList(Seller.State.ACTIVE, Seller.State.DISACTIVE);

    @FXML private TextField dni;
    @FXML private TextField name;
    @FXML private TextField phone;
    @FXML private TextField user;
    @FXML private ComboBox<Seller.State> cbState;

    @FXML private TableView<Seller> tableSellers;
    @FXML private TableColumn<Seller,Integer> colId;
    @FXML private TableColumn<Seller,String> colDni;
    @FXML private TableColumn<Seller,String> colName;
    @FXML private TableColumn<Seller,String> colPhone;
    @FXML private TableColumn<Seller, Seller.State> colState;

    @Override
    protected AbstractBaseDAO<Seller> getDAO() { return sellerDAO; }

    @Override
    protected TableView<Seller> getTableView() { return tableSellers; }

    @Override
    protected ComboBox<?> getComboBoxState() { return cbState; }

    @Override
    protected void setupComboBox() {
        cbState.setItems(stateList);
        cbState.setValue(Seller.State.ACTIVE);
    }

    @Override
    protected void setupTableColumns() {
        colId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idSeller()).asObject());
        colDni.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().dni()));
        colName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().name()));
        colPhone.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().phoneNumber()));
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().state()));
    }

    @Override
    protected boolean validateSpecificFields() {
        if (campoVacio(dni)||campoVacio(name)||campoVacio(phone)||campoVacio(user)){
            mostrarAdvertencia("Debes de completar todos los campos.");
            return false;
        }
        return true;
    }

    @Override
    protected Seller createEntityFromFields() {
        return new Seller(dni.getText(), name.getText(), phone.getText(), cbState.getValue(), user.getText());
    }

    @Override
    protected void setFormFields(Seller seller) {
        dni.setText(seller.dni());
        name.setText(seller.name());
        phone.setText(seller.phoneNumber());
        user.setText(seller.user());
        cbState.setValue(seller.state());
    }

    @Override
    protected void clearFormFields() {
        MenuController.cleanCells(dni, name, phone, user);
    }

    @Override
    protected String getEntityId() {
        return dni.getText();
    }

    @Override
    public void deleteAction(ActionEvent event) {
        // Lógica única de SellerController
        if (Objects.equals(dni.getText(), MainController.sellerLog.dni())){
            MenuController.setAlert(Alert.AlertType.ERROR,"No se puede eliminar al vendedor actual");
            return;
        }
        // Si pasa la validación, llamamos a la lógica estándar del padre.
        super.deleteAction(event);
    }

    // Métodos puente FXML, para que no esté fregando el FXML
    @FXML
    public void addSeller(ActionEvent event) {
        super.addAction(event);
    }

    @FXML
    public void updateSeller(ActionEvent event) {
        super.updateAction(event);
    }

    @FXML
    public void deleteSeller(ActionEvent event) {
        // AGUAS!
        // Llamamos al método de ESTA clase porqu aquí es donde pusimos la protección para no borrar al usuario logueado.
        // Si llamamos a super.deleteAction(), podemos provocar que el vendedor se borre a sí mismo.
        this.deleteAction(event);
    }

    @FXML
    public void cleanCellsScreen(ActionEvent event) {
        super.cleanAction(event);
    }
}
