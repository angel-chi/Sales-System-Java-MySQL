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

public class ProductoDAO implements CRUD<Producto>{ // Cambiado de ProductDAO a ProductoDAO, y Product a Producto

    public void subtractStock(ObservableList<CarritoCompra> productosDelCarrito){
        String sql = "UPDATE productos SET existencia = existencia - ? WHERE idProducto = ?";

        try(Connection conn = DBConnection.connection()){
            for (CarritoCompra e:productosDelCarrito) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setInt(1,e.cantidad());
                    pstmt.setInt(2,Integer.parseInt(e.codigo()));
                    pstmt.executeUpdate();
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Producto searchProducto(int idProducto){
        String sql = "SELECT * FROM productos WHERE idProducto = ?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,idProducto);

            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    return Producto.fromResultSet(rs);
                } else {
                    return null;
                }
            }

        }catch (SQLException e){

            return null;
        }
    }

    @Override
    public boolean create(Producto entity) {
        String sql = "INSERT INTO productos (nombre,precio,existencia,estado) values (?,?,?,?)";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, entity.nombre());
            pstmt.setDouble(2, entity.precio());
            pstmt.setInt(3, entity.existencia());
            pstmt.setString(4, entity.estado().name());

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Producto agregado correctamente");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al agregar producto: ");
                return false;
            }


        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al agregar producto: " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean update(Producto entity) {
        String sql = "UPDATE productos SET precio=?,existencia=?,estado=? WHERE nombre=?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setDouble(1, entity.precio());
            pstmt.setInt(2, entity.existencia());
            pstmt.setString(3, entity.estado().name());
            pstmt.setString(4, entity.nombre());


            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Producto actualizado correctamente");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al actualizar producto");
                return false;
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM productos WHERE nombre=?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql))    {


            pstmt.setString(1,id);

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Producto eliminado correctamente");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al eliminar producto: ");
                return false;
            }



        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "No se puede eliminar este producto porque ya tiene una venta asociada");
            return false;
        }
    }

    @Override
    public void setTable(ObservableList<Producto> productos) {
        String sql = "SELECT * FROM productos";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            try (ResultSet rs = pstmt.executeQuery()){

                while (rs.next()){
                    Producto producto = Producto.fromResultSet(rs);
                    productos.add(producto);
                }

            }
        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al cargar la tabla de productos: " + e.getMessage());
        }

    }
    public static void setPieChart(ObservableList<PieChart.Data> pieChartData) {
        String sql = """ 
                SELECT p.nombre, sum(dv.cantidad) as cant
                FROM productos p
                INNER JOIN detalle_ventas dv
                USING (idProducto)
                WHERE dv.idVenta IN (SELECT idVenta FROM ventas WHERE idVendedor=?)
                GROUP BY dv.idProducto, p.nombre;
                """;

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, MainController.vendedorLogeado.idVendedor());

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    pieChartData.add(new PieChart.Data(rs.getString("nombre"),rs.getInt("cant")));
                }
            }


        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al buscar ventas: " + e.getMessage());
        }
    }

}
