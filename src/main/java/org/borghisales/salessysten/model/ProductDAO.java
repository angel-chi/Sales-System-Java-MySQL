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

public class ProductDAO extends AbstractBaseDAO<Product>{

    // Métodos genéricos.
    @Override
    protected String getEntityName() {
        return "Producto";
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO product (name,price,stock,state) values (?,?,?,?)";
    }

    @Override
    protected void setInsertParameters(PreparedStatement pstmt, Product entity) throws SQLException {
        pstmt.setString(1, entity.name());
        pstmt.setDouble(2, entity.price());
        pstmt.setInt(3, entity.stock());
        pstmt.setString(4, entity.state().name());
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE product set price=?,stock=?,state=? where name=?";
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, Product entity) throws SQLException {
        pstmt.setDouble(1, entity.price());
        pstmt.setInt(2, entity.stock());
        pstmt.setString(3, entity.state().name());
        pstmt.setString(4, entity.name());
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM product where name=?";
    }

    @Override
    protected String getSelectAllSQL() {
        return "SELECT * FROM product";
    }

    @Override
    protected Product fromResultSet(ResultSet rs) throws SQLException {
        return Product.fromResultSet(rs);
    }

    // Sobreescribiendo DELETE.
    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM product where name=?";

        try(Connection conn = DBConnection.connection()) {
            assert conn != null;
            try(PreparedStatement pstmt = conn.prepareStatement(sql))    {

                pstmt.setString(1,id);

                int rows_affected = pstmt.executeUpdate();

                if (rows_affected>0){
                    MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Producto eliminado correctamente!");
                    return true;
                } else {
                    MenuController.setAlert(Alert.AlertType.ERROR, "Error intentando eliminar el producto: ");
                    return false;
                }

            }
        } catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "No se puede eliminar un producto registrado en una venta.");
            return false;
        }
    }

    // Métodos específicos de Producto
    public void subtractStock(ObservableList<ShoppingCart> products){
        String sql = "UPDATE product SET stock = stock - ? WHERE idProduct = ?";

        try(Connection conn = DBConnection.connection()){

            for (ShoppingCart e:products) {
                assert conn != null;
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

        try(Connection conn = DBConnection.connection()) {
            assert conn != null;
            try(PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1,idProduct);

                try (ResultSet rs = pstmt.executeQuery()){
                    rs.next();
                    return Product.fromResultSet(rs);
                }

            }
        } catch (SQLException e){

            return null;
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

        try (Connection conn = DBConnection.connection()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)){

                pstmt.setInt(1, MainController.sellerLog.idSeller());

                try (ResultSet rs = pstmt.executeQuery()){
                    while (rs.next()){
                        pieChartData.add(new PieChart.Data(rs.getString("name"),rs.getInt("cant")));
                    }
                }

            }
        } catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al intentar buscar las ventas: " + e.getMessage());
        }

    }

}
