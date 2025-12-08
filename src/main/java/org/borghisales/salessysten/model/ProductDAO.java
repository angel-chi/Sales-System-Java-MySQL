package org.borghisales.salessysten.model;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MainController;
import org.borghisales.salessysten.controllers.MenuController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        // Nota: Idealmente aquí también haríamos un JOIN si necesitas la marca en la búsqueda individual
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

    // metodo para obtener marcas
    public void getBrands(ObservableList<Brand> list) {
        String sql = "SELECT idBrand, name FROM brand ORDER BY name ASC";
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while (rs.next()){
                list.add(new Brand(rs.getInt("idBrand"), rs.getString("name")));
            }
        } catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error loading brands: " + e.getMessage());
        }
    }
    // ----------------------------------------

    @Override
    public boolean create(Product entity) {
        // Incluye idBrand
        String sql = "INSERT INTO product (name, idBrand, price, stock, state) values (?,?,?,?,?)";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, entity.name());
            pstmt.setInt(2, entity.idBrand()); // Nuevo campo de marca gg
            pstmt.setDouble(3, entity.price());
            pstmt.setInt(4, entity.stock());
            pstmt.setString(5, entity.state().name());

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Product added correctly");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error adding product: ");
                return false;
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error adding product: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Product entity) {
        // incluye idBrand
        String sql = "UPDATE product set price=?, stock=?, state=?, idBrand=? where name=?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setDouble(1, entity.price());
            pstmt.setInt(2, entity.stock());
            pstmt.setString(3, entity.state().name());
            pstmt.setInt(4, entity.idBrand()); // brand
            pstmt.setString(5, entity.name());

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Product updated correctly");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error updating product");
                return false;
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error updating product: " + e.getMessage());
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
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Product deleted correctly");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error deleting product: ");
                return false;
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "You cannot delete this product because you already have a sale with it");
            return false;
        }
    }

    @Override
    public void setTable(ObservableList<Product> products) {
        // usa JOIN para traer el nombre de la marca
        String sql = """
                SELECT p.idProduct, p.name, p.idBrand, p.price, p.stock, p.state, b.name as brand_name 
                FROM product p 
                INNER JOIN brand b ON p.idBrand = b.idBrand
                """;

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            try (ResultSet rs = pstmt.executeQuery()){

                while (rs.next()){
                    Product product = Product.fromResultSet(rs);
                    products.add(product);
                }

            }
        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error setting the table seller: " + e.getMessage());
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
            MenuController.setAlert(Alert.AlertType.ERROR, "Error searching sales : " + e.getMessage());
        }
    }
}