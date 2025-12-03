package org.borghisales.salessysten.model;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MenuController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO implements CRUD<Customer> {

    public Customer searchCustomer(int dni){
        String sql = "SELECT * FROM customer WHERE dni=?";
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1,dni);

            try (ResultSet rs = pstmt.executeQuery()){
                rs.next();
                return Customer.fromResultSet(rs);
            }

        }catch (SQLException e){

            return null;
        }

    }
    @Override
    public boolean create(Customer entity) {
        String sql = "Insert into customer (dni,name,address,state) values(?,?,?,?)";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, entity.dni());
            pstmt.setString(2, entity.name());
            pstmt.setString(3, entity.address());
            pstmt.setString(4, entity.state().toString());

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Cliente añadido correctamente");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al agregar cliente: ");
                return false;
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al agregar cliente: " + e.getMessage());
            return false;
        }
    }



    @Override
    public boolean update(Customer entity) {
        String sql = "UPDATE customer set name=?,address=?,state=? where dni=?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, entity.name());
            pstmt.setString(2, entity.address());
            pstmt.setString(3, entity.state().name());
            pstmt.setString(4, entity.dni());


            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Cliente actualizado correctamente");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al actualizar cliente");
                return false;
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String disableConstraintsSQL = "SET foreign_key_checks = 0;";
        String enableConstraintsSQL = "SET foreign_key_checks = 1;";
        String deleteCustomerSQL = "DELETE FROM customer WHERE dni=?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement disableConstraintsStmt = conn.prepareStatement(disableConstraintsSQL);
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
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Cliente eliminado correctamente");
                return true;
            } else {
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al eliminar cliente: ");
                return false;
            }
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }


    @Override
    public void setTable(ObservableList<Customer> customers) {
        String sql = "SELECT * FROM customer";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            try (ResultSet rs = pstmt.executeQuery()){

                while (rs.next()){
                    Customer customer = Customer.fromResultSet(rs);
                    customers.add(customer);
                }

            }
        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al establecer la tabla de clientes: " + e.getMessage());
        }

    }

    public Customer searchById(int id) {
        String query = "SELECT * FROM customer WHERE idCustomer = ?";

        try (Connection connection = DBConnection.connection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Customer.fromResultSet(rs);
            } else {
                return null; // No encontrado
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }






}
