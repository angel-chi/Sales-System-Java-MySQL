package org.borghisales.salessysten.model;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MainController;
import org.borghisales.salessysten.controllers.MenuController;
import org.borghisales.salessysten.controllers.ReportsController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ProductDAO extends Validator<Product> implements CRUD<Product>{

    public void subtractStock(ObservableList<ShoppingCart> products){
        String sql = "UPDATE product SET stock = stock - ? WHERE idProduct = ?";

        try(Connection conn = DBConnection.connection()){

            for (ShoppingCart e:products) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setInt(1,e.quantity());
                    pstmt.setInt(2,Integer.parseInt(e.cod()));
                    pstmt.executeUpdate();
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Product searchProduct(int idProduct){
        String sql = "SELECT * FROM product WHERE idProduct = ?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,idProduct);

            try (ResultSet rs = pstmt.executeQuery()){
                rs.next();
                return Product.fromResultSet(rs);
            }

        }catch (SQLException e){

            return null;
        }
    }

    @Override
    public boolean create(Product entity) {
        if(!validate(entity))return false;
        String sql = "INSERT INTO product (name,price,stock,state) values (?,?,?,?)";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, entity.name());
            pstmt.setDouble(2, entity.price());
            pstmt.setInt(3, entity.stock());
            pstmt.setString(4, entity.state().name());

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Producto agregado correctamente");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error agregando producto: ");
                return false;
            }


        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error agregando producto: " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean update(Product entity) {
        if(!validate(entity))return false;
        String sql = "UPDATE product set price=?,stock=?,state=? where name=?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){



            pstmt.setDouble(1, entity.price());
            pstmt.setInt(2, entity.stock());
            pstmt.setString(3, entity.state().name());
            pstmt.setString(4, entity.name());


            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Producto actualizado correctamente");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error actualizando producto");
                return false;
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error actualizando producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM product where name=?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql))    {


            pstmt.setString(1,id);

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Product eliminado correctamente");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error elimando producto ");
                return false;
            }



        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "No puedes eiminar este producto porque ya tienes una venta con este.");
            return false;
        }
    }

    @Override
    public void setTable(ObservableList<Product> products) {
        String sql = "SELECT * FROM product";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            try (ResultSet rs = pstmt.executeQuery()){

                while (rs.next()){
                    Product product = Product.fromResultSet(rs);
                    products.add(product);
                }

            }
        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error estableciendo la tabla de vendedor: " + e.getMessage());
        }

    }
    public static void setPieChart(ObservableList<PieChart.Data> pieChartData) {
        String sql = """ 
                SELECT p.name, sum(s.quantity) as cant
                FROM product p
                INNER JOIN sales_details s
                USING (idProduct)
                WHERE s.idSales in (SELECT idSales FROM sales where idSeller=?)
                GROUP BY s.idProduct;
                """;

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, MainController.sellerLog.idSeller());

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    pieChartData.add(new PieChart.Data(rs.getString("name"),rs.getInt("cant")));
                }
            }


        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error buscando las ventas : " + e.getMessage());
        }



    }

    @Override
    protected boolean validate(Product entity){
        if(Objects.isNull(entity)){
            MenuController.setAlert(Alert.AlertType.ERROR, "El producto no puede estar vacio");
            return false;
        }
        if(entity.name() == null || entity.name().isEmpty()){
            MenuController.setAlert(Alert.AlertType.ERROR, "El nombre del producto no puede estar vacio");
            return false;
        }
        if( entity.price() <= 0){
            MenuController.setAlert(Alert.AlertType.ERROR, "Debes seleccionar un precio mayor a 0");
            return false;
        }
        if( entity.stock() < 0){
            MenuController.setAlert(Alert.AlertType.ERROR, "El stock del producto no puede ser negativo");
            return false;
        }
        if(entity.state() == null){
            MenuController.setAlert(Alert.AlertType.ERROR, "El estado del producto no puede estar vacio");
            return false;
        }
        return true;
    }

}
