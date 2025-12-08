package org.borghisales.salessysten.model;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MenuController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractBaseDAO<T> implements CRUD<T> {

    protected abstract String getInsertSQL();
    protected abstract String getUpdateSQL();
    protected abstract String getDeleteSQL();
    protected abstract String getSelectAllSQL();

    protected abstract void setInsertParameters(PreparedStatement pstmt, T entity) throws SQLException;
    protected abstract void setUpdateParameters(PreparedStatement pstmt, T entity) throws SQLException;

    protected abstract T fromResultSet(ResultSet rs) throws SQLException;

    protected abstract String getEntityName();

    @Override
    public boolean create(T entity) {
        try (Connection conn = DBConnection.connection()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(getInsertSQL())) {

                setInsertParameters(pstmt, entity);

                int rows_affected = pstmt.executeUpdate();

                if (rows_affected > 0) {
                    MenuController.setAlert(Alert.AlertType.CONFIRMATION, getEntityName() + " agregado correctamente!");
                    return true;
                } else {
                    MenuController.setAlert(Alert.AlertType.ERROR, "Error añadiendo al " + getEntityName().toLowerCase());
                    return false;
                }

            }
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error añadiendo al " + getEntityName().toLowerCase() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(T entity) {
        try (Connection conn = DBConnection.connection()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(getUpdateSQL())) {

                setUpdateParameters(pstmt, entity);

                int rows_affected = pstmt.executeUpdate();

                if (rows_affected > 0) {
                    MenuController.setAlert(Alert.AlertType.CONFIRMATION, getEntityName() + " actualizado correctamente!");
                    return true;
                } else {
                    MenuController.setAlert(Alert.AlertType.ERROR, "Error actualizando al " + getEntityName().toLowerCase());
                    return false;
                }

            }
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error actualizando al " + getEntityName().toLowerCase() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try (Connection conn = DBConnection.connection()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement(getDeleteSQL())) {

                pstmt.setString(1, id);

                int rows_affected = pstmt.executeUpdate();

                if (rows_affected > 0) {
                    MenuController.setAlert(Alert.AlertType.CONFIRMATION, getEntityName() + " eliminado correctamente!");
                    return true;
                } else {
                    MenuController.setAlert(Alert.AlertType.ERROR, "Error eliminando al " + getEntityName().toLowerCase());
                    return false;
                }

            }
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error eliminando al " + getEntityName().toLowerCase() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public void setTable(ObservableList<T> list) {
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(getSelectAllSQL());
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(fromResultSet(rs));
            }

        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al configurar la tabla de " + getEntityName().toLowerCase() + " " + e.getMessage());
        }
    }
}
