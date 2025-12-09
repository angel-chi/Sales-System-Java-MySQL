package org.borghisales.salessysten.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.borghisales.salessysten.model.AbstractBaseDAO;
import org.borghisales.salessysten.model.Product;
import org.borghisales.salessysten.model.ProductDAO;


public class ProductController extends AbstractCRUDController<Product> {

    private final ProductDAO productDAO = new ProductDAO();
    private final ObservableList<Product.State> stateList = FXCollections.observableArrayList(Product.State.ACTIVE, Product.State.DISACTIVE);

    @FXML private ComboBox<Product.State> cbState;
    @FXML private TextField name;
    @FXML private TextField price;
    @FXML private TextField stock;
    @FXML private TableView<Product> tableProducts;

    @FXML private TableColumn<Product,Integer> colId;
    @FXML private TableColumn<Product,String> colName;
    @FXML private TableColumn<Product,Double> colPrice;
    @FXML private TableColumn<Product,Integer> colStock;
    @FXML private TableColumn<Product,Product.State> colState;

    // Implementación de métodos abstractos del padre.
    @Override
    protected AbstractBaseDAO<Product> getDAO() {
        return productDAO;
    }

    @Override
    protected TableView<Product> getTableView() {
        return tableProducts;
    }

    @Override
    protected ComboBox<?> getComboBoxState() {
        return cbState;
    }

    @Override
    protected void setupComboBox() {
        cbState.setItems(stateList);
        cbState.setValue(Product.State.ACTIVE);
    }

    @Override
    protected void setupTableColumns() {
        colId.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().idProduct()).asObject());
        colName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().name()));
        colPrice.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().price()).asObject());
        colStock.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().stock()).asObject());
        colState.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().state()));
    }

    // Validaciones específicas para Product.
    // Verificando vacíos y formato numérico.
    @Override
    protected boolean validateSpecificFields() {
        if (campoVacio(name) || campoVacio(price) || campoVacio(stock)) {
            mostrarAdvertencia("Debes completar todos los campos.");
            return false;
        }
        try {
            Double.parseDouble(price.getText());
            Integer.parseInt(stock.getText());
        } catch (NumberFormatException e) {
            mostrarAdvertencia("El precio debe ser numérico (ej. 10.5) y el stock un entero.");
            return false;
        }
        return true;
    }

    @Override
    protected Product createEntityFromFields() {
        return new Product(
                name.getText(),
                Double.parseDouble(price.getText()),
                Integer.parseInt(stock.getText()),
                cbState.getValue()
        );
    }

    @Override
    protected void setFormFields(Product product) {
        name.setText(product.name());
        price.setText(String.valueOf(product.price()));
        stock.setText(String.valueOf(product.stock()));
        cbState.setValue(product.state());
    }

    @Override
    protected void clearFormFields() {
        MenuController.cleanCells(name, price, stock);
        cbState.setValue(Product.State.ACTIVE);
    }

    @Override
    protected String getEntityId() {
        return name.getText();
    }

    // Métodos puente FXML, por su friega FXML como mencioné en el otro Controller
    @FXML
    public void addProduct(ActionEvent actionEvent) {
        super.addAction(actionEvent);
    }

    @FXML
    public void updateProduct(ActionEvent actionEvent) {
        super.updateAction(actionEvent);
    }

    @FXML
    public void deleteProduct(ActionEvent actionEvent) {
        super.deleteAction(actionEvent);
    }

    @FXML
    public void cleanCellsScreen(ActionEvent actionEvent) {
        super.cleanAction(actionEvent);
    }
}
