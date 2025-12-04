package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

// Nombre de clase y campos cambiados a español para coincidir con la base de datos
public record Producto(int idProducto, String nombre, double precio, int existencia, Estado estado) {

    // El enum debe coincidir con los valores de la base de datos: 'ACTIVO', 'INACTIVO'
    public enum Estado { ACTIVO, INACTIVO }

    public Producto(String nombre, double precio, int existencia, Estado estado){
        this(0, nombre, precio, existencia, estado);
    }

    public static Producto fromResultSet(ResultSet rs) throws SQLException {
        // Nombres de columna actualizados a español
        int id = rs.getInt("idProducto");
        String nombre = rs.getString("nombre");
        double precio = rs.getDouble("precio");
        int existencia = rs.getInt("existencia");
        Estado estado = Producto.Estado.valueOf(rs.getString("estado"));
        return new Producto(id, nombre, precio, existencia, estado);
    }
}
