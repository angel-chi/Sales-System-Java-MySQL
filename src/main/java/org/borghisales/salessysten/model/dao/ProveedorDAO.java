package org.borghisales.salessysten.model.dao;

import org.borghisales.salessysten.model.CRUD;
import org.borghisales.salessysten.model.entities.Proveedor;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import java.sql.ResultSet;
import org.borghisales.salessysten.controllers.MenuController;
import org.borghisales.salessysten.model.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProveedorDAO implements CRUD<Proveedor> {
    // Create (Object object)
    // Update --
    // Delete (String id)
    // setTable(ObservableList<Object> products)

    @Override
    public boolean create(Proveedor entity) {
        String sql = "INSERT INTO proveedor (name,email,state) values (?,?,?)";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, entity.name());
            pstmt.setString(2, entity.email());
            pstmt.setString(3, entity.state().name());

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Seller added correctly");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error adding seller: ");
                return false;
            }


        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error adding seller: " + e.getMessage());
            return false;
        }

    }
    @Override
    public boolean update(Proveedor p) {
        String sql = "UPDATE proveedor SET name=?, email=?, state=? WHERE idProveedor=?";

        try (Connection conn = DBConnection.connection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.name());
            pstmt.setString(2, p.email());
            pstmt.setString(3, p.state().name());
            pstmt.setInt(4, p.idProveedor());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error actualizando proveedor: " + e.getMessage());
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
    public void setTable(ObservableList<Proveedor> proveedors){
        String sql = "SELECT * FROM proveedor";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            try (ResultSet rs = pstmt.executeQuery()){

                while (rs.next()){
                    Proveedor proveedor = Proveedor.fromResultSet(rs);
                    proveedors.add(proveedor);
                }

            }
        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error setting the table seller: " + e.getMessage());
        }
    }
    public Proveedor searchProveedor(int id) {
        String sql = "SELECT * FROM proveedor WHERE idProveedor=?";
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1,id);

            try (ResultSet rs = pstmt.executeQuery()){
                rs.next();
                return Proveedor.fromResultSet(rs);
            }

        }catch (SQLException e){

            return null;
        }
    }
}
