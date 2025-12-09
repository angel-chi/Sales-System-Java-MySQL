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

    //Método para veriricar si el vendedor logeado es administrador
    private boolean isAdmin() {
        return MainController.vendedorLogeado != null && MainController.vendedorLogeado.getRol() == Vendedor.Rol.ADMINISTRADOR;
    }

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

        // No permite crear productos si el vendedor logeado no es administrador
        if (!isAdmin()) {
            return false;
        }

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

        //Si no es admin no permite actualizar productos
        if (!isAdmin()) {
            return false;
        }

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

        //Si no es admin no permite eliminar productos
        if (!isAdmin()) {
            return false;
        }

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

        //Si no es admin no permite cargar la tabla de productos
        if (!isAdmin()) {
            productos.clear();
            return;
        }

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

            pstmt.setInt(1, MainController.vendedorLogeado.getId());

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    pieChartData.add(new PieChart.Data(rs.getString("nombre"),rs.getInt("cant")));
                }
            }


        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al buscar ventas: " + e.getMessage());
        }
    }

    //Método para obtener el producto más vendido de un vendedor
    public String getBestSellingProduct(int idVendedor) {
        //Text Block para el query SQL
        String sql = """
            SELECT p.nombre, SUM(dv.cantidad) AS cant
            FROM productos p
            INNER JOIN detalle_ventas dv
            USING (idProducto)
            WHERE dv.idVenta IN (SELECT idVenta FROM ventas WHERE idVendedor = ?)
            GROUP BY dv.idProducto, p.nombre
            ORDER BY cant DESC
            LIMIT 1
            """;

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, MainController.vendedorLogeado.getId());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    //Se obtiene el nombre y la cantidad vendida del producto
                    String nombre = rs.getString("nombre");
                    int cantidad = rs.getInt("cant");
                    return nombre + " (" + cantidad + " unidades)";
                } else {
                    //En caso de no tener ventas resgistradas
                    return null;
                }
            }

        } catch (SQLException e) {
            return null;
        }
    }

}
