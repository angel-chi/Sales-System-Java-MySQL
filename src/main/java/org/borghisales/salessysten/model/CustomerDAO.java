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
                if (rs.next()) {
                    return Customer.fromResultSet(rs);
                }
                return null;
            }

        }catch (SQLException e){
            e.printStackTrace();
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
            //s: solo devuelve resultado
            return rows_affected > 0;



        }catch (SQLException e){
            e.printStackTrace();
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

            return rows_affected > 0;


        }catch (SQLException e){
            e.printStackTrace();
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

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }

    }
}
