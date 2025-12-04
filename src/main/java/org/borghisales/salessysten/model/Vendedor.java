package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

// Nombre de clase y campos cambiados a español para coincidir con la base de datos
public record Vendedor(int idVendedor, String identificacion, String nombre, String telefono, Estado estado, String usuario) {
    // El enum debe coincidir con los valores de la base de datos: 'ACTIVO', 'INACTIVO'
    public enum Estado { ACTIVO, INACTIVO }

    public Vendedor(String identificacion, String nombre, String telefono, Estado estado, String usuario) {
        this(0, identificacion, nombre, telefono, estado, usuario);
    }

    public static Vendedor fromResultSet(ResultSet rs) throws SQLException {
        // Nombres de columna actualizados a español
        int id = rs.getInt("idVendedor");
        String identificacion = rs.getString("identificacion");
        String nombre = rs.getString("nombre");
        String telefono = rs.getString("telefono"); // En la BD es 'telefono'
        Estado estado = Vendedor.Estado.valueOf(rs.getString("estado"));
        String usuario = rs.getString("usuario");
        return new Vendedor(id, identificacion, nombre, telefono, estado, usuario);
    }
}
