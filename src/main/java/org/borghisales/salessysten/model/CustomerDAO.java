package org.borghisales.salessysten.model;

import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MenuController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO extends AbstractBaseDAO<Customer> {
    // Métodos repetitivos.
    @Override
    protected String getEntityName() { return "Cliente"; }

    @Override
    protected String getInsertSQL() {
        return "Insert into customer (dni,name,address,state) values(?,?,?,?)";
    }

    @Override
    protected void setInsertParameters(PreparedStatement pstmt, Customer entity) throws SQLException {
        pstmt.setString(1, entity.dni());
        pstmt.setString(2, entity.name());
        pstmt.setString(3, entity.address());
        pstmt.setString(4, entity.state().toString());
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE customer set name=?,address=?,state=? where dni=?";
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, Customer entity) throws SQLException {
        pstmt.setString(1, entity.name());
        pstmt.setString(2, entity.address());
        pstmt.setString(3, entity.state().name());
        pstmt.setString(4, entity.dni());
    }

    @Override
    protected String getDeleteSQL() {
        // Este no se usará porque sobrescribiremos delete, pero lo definimos por contrato.
        return "DELETE FROM customer WHERE dni=?";
    }

    @Override
    protected String getSelectAllSQL() {
        return "SELECT * FROM customer";
    }

    @Override
    protected Customer fromResultSet(ResultSet rs) throws SQLException {
        return Customer.fromResultSet(rs);
    }

    // Método de búsqueda específico
    public Customer searchCustomer(int dni){
        String sql = "SELECT * FROM customer WHERE dni=?";
        try (Connection conn = DBConnection.connection()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)){

                pstmt.setInt(1,dni);

                try (ResultSet rs = pstmt.executeQuery()){
                    rs.next();
                    return Customer.fromResultSet(rs);
                }

            }
        } catch (SQLException e){

            return null;
        }

    }

    // Lógica específica de método delete.
    @Override
    public boolean delete(String id) {
        String disableConstraintsSQL = "SET foreign_key_checks = 0;";
        String enableConstraintsSQL = "SET foreign_key_checks = 1;";
        String deleteCustomerSQL = "DELETE FROM customer WHERE dni=?";

        try(Connection conn = DBConnection.connection()) {
            assert conn != null;
            try (PreparedStatement disableConstraintsStmt = conn.prepareStatement(disableConstraintsSQL);
                PreparedStatement deleteCustomerStmt = conn.prepareStatement(deleteCustomerSQL);
                PreparedStatement enableConstraintsStmt = conn.prepareStatement(enableConstraintsSQL)) {

                // Disable foreign key restrictions
                disableConstraintsStmt.executeUpdate();

                // Delete the client
                deleteCustomerStmt.setString(1, id);
                int rowsAffected = deleteCustomerStmt.executeUpdate();

                // Reactivate foreign key constraints
                enableConstraintsStmt.executeUpdate();

                if (rowsAffected > 0) {
                    MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Cliente eliminado correctamente!");
                    return true;
                } else {
                    MenuController.setAlert(Alert.AlertType.ERROR, "Error intentando eliminar al cliente. ");
                    return false;
                }
            }
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error intentando eliminar al cliente: " + e.getMessage());
            return false;
        }
    }

}
