package org.borghisales.salessysten.model.dao;

import org.borghisales.salessysten.model.CRUD;
import org.borghisales.salessysten.model.entities.*;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MenuController;
import org.borghisales.salessysten.model.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ComprasDAO implements CRUD<Compras> {
    // Create (Object object)
    // Update --
    // Delete (String id)
    // setTable(ObservableList<Object> products)
    /*
    idCompra INT NOT NULL AUTO_INCREMENT,
  idProveedor INT NOT NULL,
  idVendedor INT NOT NULL,
  subtotal DOUBLE NOT NULL,
  stateCompra ENUM('CANCELADO','COMPLETADO') DEFAULT 'COMPLETADO',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    * */

    @Override
    public boolean create(Compras entity) {
        String sql = "INSERT INTO compras (idProveedor, idVendedor, subtotal, stateCompra) values (?,?,?,?)";
        try (Connection conn = DBConnection.connection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, entity.idProveedor());
            pstmt.setInt(2, entity.idVendedor());
            pstmt.setDouble(3,entity.subtotal());
            pstmt.setString(4, entity.estado().name());
            int rows_affected = pstmt.executeUpdate();
            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Se ha realizado la compra");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Ha ocurrido un error al realizar la compra: ");
                return false;
            }
        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Ha ocurrido un error al realizar la compra: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean update(Compras compra) {
        String sql = "UPDATE compras SET idProveedor = ?, idVendedor = ?, subtotal = ?, stateCompra = ? WHERE idCompra = ?";
        try (Connection conn = DBConnection.connection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, compra.idProveedor());
            pstmt.setInt(2, compra.idVendedor());
            pstmt.setDouble(3, compra.subtotal());
            pstmt.setString(4, compra.estado().name());
            pstmt.setInt(5, compra.idCompra());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR,"Error al actualizar compra: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String user) {
        String sql = "DELETE FROM proveedor where name=?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql))    {


            pstmt.setString(1,user);

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Proveedor eliminado");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al intentar eliminar un proveedor: ");
                return false;
            }



        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al intentar eliminar el proveedor: " + e.getMessage());
            return false;
        }

    }

    @Override
    public void setTable(ObservableList<Compras> compras) {
        String sql = "SELECT * FROM compras";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Compras compra = Compras.fromResultSet(rs);
                compras.add(compra);
            }

        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error cargando compras: " + e.getMessage());
        }
    }


    // Complementos
    public int IdSale(){
        String sql = "SELECT MAX(idCompra) FROM compras";
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){
                    if (rs.getInt(1)==0)
                        return 1;

                    return rs.getInt(1);
                }
                return 1;
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al buscar la compra: " + e.getMessage());
            return 1;
        }
    }
    public boolean saveDetalles(ObservableList<ComprasDetalles> detalles, int idCompra) {
        String sql = "INSERT INTO compra_detalle (idCompra, idProduct, cantidad, precioCompra, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.connection()) {
            for (ComprasDetalles d : detalles) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, idCompra);
                    pstmt.setInt(2, d.idProducto());
                    pstmt.setInt(3, d.cantidad());
                    pstmt.setDouble(4, d.precioCompra());
                    pstmt.setDouble(5, d.subtotal());
                    pstmt.executeUpdate();
                }
            }
            return true;
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error en detalles de compra: " + e.getMessage());
            return false;
        }
    }
    public int createAndReturnId(Compras entity) {
        String sql = """
        INSERT INTO compras (idProveedor, idSeller, subtotal, stateCompra)
        VALUES (?,?,?,?)
        """;

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, entity.idProveedor());
            pstmt.setInt(2, entity.idVendedor());
            pstmt.setDouble(3, entity.subtotal());
            pstmt.setString(4, entity.estado().name());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                return -1;
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // ✅ ID REAL
                }
            }

        } catch (SQLException e) {
            MenuController.setAlert(
                    Alert.AlertType.ERROR,
                    "Error al crear la compra: " + e.getMessage()
            );
        }
        return -1;
    }

}
