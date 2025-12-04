package org.borghisales.salessysten.model;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MenuController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO implements CRUD<Cliente> {

    // Cambiado para buscar por correo que es único
    public Cliente searchCliente(String correo){
        String sql = "SELECT * FROM clientes WHERE correo = ?";
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, correo);

            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    return Cliente.fromResultSet(rs);
                }
            }

        }catch (SQLException e){
            // Manejar la excepción, por ejemplo, logueándola
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean create(Cliente entity) {
        // Corregido para usar la tabla 'clientes' y las columnas correctas
        String sql = "INSERT INTO clientes (correo, nombre, direccion, estado) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, entity.correo());
            pstmt.setString(2, entity.nombre());
            pstmt.setString(3, entity.direccion());
            pstmt.setString(4, entity.estado().toString());

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected > 0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Cliente agregado correctamente");
                return true;
            } else {
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al agregar cliente");
                return false;
            }

        } catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al agregar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Cliente entity) {
        // Corregido para usar 'clientes' y las columnas correctas, usando idCliente como PK
        String sql = "UPDATE clientes SET nombre = ?, direccion = ?, estado = ?, correo = ? WHERE idCliente = ?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, entity.nombre());
            pstmt.setString(2, entity.direccion());
            pstmt.setString(3, entity.estado().name());
            pstmt.setString(4, entity.correo());
            pstmt.setInt(5, entity.idCliente()); // Usando idCliente para el WHERE

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected > 0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Cliente actualizado correctamente");
                return true;
            } else {
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al actualizar cliente");
                return false;
            }

        } catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        // El id debería ser el idCliente. Se asume que el 'id' que llega es el idCliente en formato String.
        String sql = "DELETE FROM clientes WHERE idCliente = ?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(id));
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Cliente eliminado correctamente");
                return true;
            } else {
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al eliminar cliente");
                return false;
            }
        } catch (SQLException | NumberFormatException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }


    @Override
    public void setTable(ObservableList<Cliente> clientes) {
        String sql = "SELECT * FROM clientes";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            try (ResultSet rs = pstmt.executeQuery()){

                while (rs.next()){
                    Cliente cliente = Cliente.fromResultSet(rs);
                    clientes.add(cliente);
                }

            }
        } catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al cargar la tabla de clientes: " + e.getMessage());
        }
    }
}
