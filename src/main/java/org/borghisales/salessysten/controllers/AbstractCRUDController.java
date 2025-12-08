package org.borghisales.salessysten.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import org.borghisales.salessysten.model.AbstractBaseDAO;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractCRUDController<T> implements Initializable, validacionEntrada {
    // Métodos abstractos
    protected abstract AbstractBaseDAO<T> getDAO();

    protected abstract TableView<T> getTableView();

    protected abstract ComboBox<?> getComboBoxState(); // Puede ser Customer.State, Seller.State...

    protected abstract T createEntityFromFields();

    protected abstract void setFormFields(T entity);

    protected abstract void clearFormFields();

    protected abstract boolean validateSpecificFields();

    protected abstract String getEntityId();

    protected ObservableList<T> dataList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        setupTableEvents();
        setupComboBox();
        loadData();
    }

    private void setupTableEvents() {
        getTableView().setOnMouseClicked(mouseEvent -> {
            if (!getTableView().getSelectionModel().isEmpty() && mouseEvent.getClickCount() == 2) {
                T entity = getTableView().getSelectionModel().getSelectedItem();
                setFormFields(entity);
            }
        });
    }

    protected void loadData() {
        getTableView().getItems().clear();
        if (dataList == null) {
            dataList = FXCollections.observableArrayList();
            getDAO().setTable(dataList);
        }
        getTableView().setItems(dataList);
    }

    protected void refreshTable() {
        getTableView().getItems().clear();
        getDAO().setTable(dataList);
        getTableView().setItems(dataList);
    }

    // Acciones CRUD Genéricas | @FMXL

    @FXML
    public void addAction(ActionEvent event) {
        if (!validateSpecificFields()) return; // Validaciones de campos vacíos delegadas al hijo.

        if (getComboBoxState().getValue() == null) {
            mostrarAdvertencia("Debes seleccionar un estado.");
            return;
        }

        T entity = createEntityFromFields();
        if (getDAO().create(entity)) {
            clearFormFields();
            refreshTable();
        }
    }

    @FXML
    public void updateAction(ActionEvent event) {
        T entity = createEntityFromFields();
        if (getDAO().update(entity)) {
            clearFormFields();
            refreshTable();
        }
    }

    @FXML
    public void deleteAction(ActionEvent event) {
        if (getDAO().delete(getEntityId())) {
            clearFormFields();
            refreshTable();
        }
    }

    @FXML
    public void cleanAction(ActionEvent event) {
        clearFormFields();
    }

    // Método auxiliar para configurar el combo en el hijo.
    protected abstract void setupComboBox();

    // Método auxiliar para configurar columnas en el hijo.
    protected abstract void setupTableColumns();

}
