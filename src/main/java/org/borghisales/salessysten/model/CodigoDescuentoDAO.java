package org.borghisales.salessysten.model;

import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MenuController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CodigoDescuentoDAO {

    public CodigoDescuento buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM codigos_descuento WHERE codigo = ? AND estado = 'ACTIVO' AND (fecha_expiracion IS NULL OR fecha_expiracion >= ?)";
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);
            pstmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new CodigoDescuento(
                            rs.getInt("idCodigoDescuento"),
                            rs.getString("codigo"),
                            rs.getDouble("porcentaje"),
                            CodigoDescuento.Estado.valueOf(rs.getString("estado")),
                            rs.getDate("fecha_expiracion") != null ? rs.getDate("fecha_expiracion").toLocalDate() : null
                    );
                }
            }
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al buscar código de descuento: " + e.getMessage());
        }
        return null;
    }

    public boolean guardarCodigo(CodigoDescuento codigoDescuento) {
        String sql = "INSERT INTO codigos_descuento (codigo, porcentaje, estado, fecha_expiracion) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigoDescuento.getCodigo());
            pstmt.setDouble(2, codigoDescuento.getPorcentaje());
            pstmt.setString(3, codigoDescuento.getEstado().toString());
            pstmt.setDate(4, codigoDescuento.getFechaExpiracion() != null ? java.sql.Date.valueOf(codigoDescuento.getFechaExpiracion()) : null);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al guardar código de descuento: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarCodigo(CodigoDescuento codigoDescuento) {
        String sql = "UPDATE codigos_descuento SET codigo = ?, porcentaje = ?, estado = ?, fecha_expiracion = ? WHERE idCodigoDescuento = ?";
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigoDescuento.getCodigo());
            pstmt.setDouble(2, codigoDescuento.getPorcentaje());
            pstmt.setString(3, codigoDescuento.getEstado().toString());
            pstmt.setDate(4, codigoDescuento.getFechaExpiracion() != null ? java.sql.Date.valueOf(codigoDescuento.getFechaExpiracion()) : null);
            pstmt.setInt(5, codigoDescuento.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al actualizar código de descuento: " + e.getMessage());
            return false;
        }
    }

    public java.util.List<CodigoDescuento> obtenerTodosLosCodigos() {
        java.util.List<CodigoDescuento> codigos = new java.util.ArrayList<>();
        String sql = "SELECT * FROM codigos_descuento";
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                codigos.add(new CodigoDescuento(
                        rs.getInt("idCodigoDescuento"),
                        rs.getString("codigo"),
                        rs.getDouble("porcentaje"),
                        CodigoDescuento.Estado.valueOf(rs.getString("estado")),
                        rs.getDate("fecha_expiracion") != null ? rs.getDate("fecha_expiracion").toLocalDate() : null
                ));
            }
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al obtener todos los códigos de descuento: " + e.getMessage());
        }
        return codigos;
    }

    public boolean eliminarCodigo(int id) {
        String sql = "DELETE FROM codigos_descuento WHERE idCodigoDescuento = ?";
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al eliminar código de descuento: " + e.getMessage());
            return false;
        }
    }
}
