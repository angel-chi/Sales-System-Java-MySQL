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
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements CRUD<Product>{

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

    public Product searchProduct(String productName) {
        String sql = "SELECT * FROM product WHERE name = ?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productName);

            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return Product.fromResultSet(rs);
            }
        }catch (SQLException e){
            return null;
        }
    }

    @Override
    public boolean create(Product entity) {
        String sql = "INSERT INTO product (name,price,stock,state,garantia) values (?,?,?,?,?)";

        try (
                Connection conn = DBConnection.connection();
                ){

            if(conn == null) {
                return false;
            }

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, entity.name());
            pstmt.setDouble(2, entity.price());
            pstmt.setInt(3, entity.stock());
            pstmt.setString(4, entity.state().name());
            pstmt.setString(5, entity.garantia().getText());

            int rows_affected = pstmt.executeUpdate();

            pstmt.close();
            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Producto añadido correctamente");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error añadiendo el producto: ");
                return false;
            }
        }
        catch (NullPointerException e) {
            return false;
        }
        catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error añadiendo el producto: " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean update(Product entity) {
        String sql = "UPDATE product set price=?,stock=?,state=?,garantia=? where name=?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){



            pstmt.setDouble(1, entity.price());
            pstmt.setInt(2, entity.stock());
            pstmt.setString(3, entity.state().name());
            pstmt.setString(4, entity.garantia().getText());
            pstmt.setString(5, entity.name());


            int rows_affected = pstmt.executeUpdate();

            boolean result = false;
            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Producto actualizado correctamente");
                result = true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error actualizando el producto");
            }

            return result;
        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error actualizando el producto: " + e.getMessage());
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
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Producto eliminado correctamente");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error eliminando el producto");
                return false;
            }



        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "No puedes eliminar este producto debido a que tienes una venta que lo contiene");
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
            MenuController.setAlert(Alert.AlertType.ERROR, "Error configurando la tabla de productos" + e.getMessage());
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
            MenuController.setAlert(Alert.AlertType.ERROR, "Error buscando las ventas: " + e.getMessage());
        }
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM product";
        List<Product> list = new ArrayList<>();

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(Product.fromResultSet(rs));
            }

        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error cargando productos: " + e.getMessage());
        }

        return list;
    }
}
