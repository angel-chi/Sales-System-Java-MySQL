package org.borghisales.salessysten.model;

import org.borghisales.salessysten.controllers.MenuController;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfiguracionDAO {

    public String getValor(String clave) {
        String sql = "SELECT valor FROM configuracion WHERE clave = ?";
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, clave);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("valor");
                }
            }
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al obtener configuración: " + e.getMessage());
        }
        return null; // Devuelve null si no se encuentra la clave o hay un error
    }

    public boolean updateValor(String clave, String valor) {
        // Esta consulta intenta actualizar, y si la fila no existe, la inserta.
        // Es una forma más robusta que hacer un UPDATE y luego un INSERT por separado.
        String sql = "INSERT INTO configuracion (clave, valor) VALUES (?, ?) ON DUPLICATE KEY UPDATE valor = ?";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, clave);
            pstmt.setString(2, valor);
            pstmt.setString(3, valor); // Para la parte de UPDATE

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al actualizar configuración: " + e.getMessage());
            return false;
        }
    }
}
