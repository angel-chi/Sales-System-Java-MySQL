package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Cliente(int idCliente, String correo, String nombre, String direccion, Estado estado) {
    
    // El enum debe coincidir exactamente con los valores ENUM de la base de datos
    public enum Estado { ACTIVO, INACTIVO }

    public Cliente(String correo, String nombre, String direccion, Estado estado){
        this(0, correo, nombre, direccion, estado);
    }

    public static Cliente fromResultSet(ResultSet rs) throws SQLException {
        // Aquí usamos los nombres exactos de las columnas de la tabla 'clientes'
        int id = rs.getInt("idCliente");
        String correo = rs.getString("correo"); // Cambiado de dni a correo
        String nombre = rs.getString("nombre");
        String direccion = rs.getString("direccion");
        
        // Convertimos el texto de la base de datos ("ACTIVO"/"INACTIVO") al Enum
        Estado estado = Cliente.Estado.valueOf(rs.getString("estado"));
        
        return new Cliente(id, correo, nombre, direccion, estado);
    }
}