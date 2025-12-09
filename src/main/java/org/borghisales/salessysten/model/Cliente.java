package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Cliente extends Usuario {

    private final String correo;
    private final String direccion;

    public Cliente(int idCliente, String correo, String nombre, String direccion, Estado estado) {
        super(idCliente, nombre, estado);
        this.correo = correo;
        this.direccion = direccion;
    }

    public Cliente(String correo, String nombre, String direccion, Estado estado){
        this(0, correo, nombre, direccion, estado);
    }

    //Método factory estático para usar desde ClienteDAO
    public static Cliente fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("idCliente");
        String nombre = rs.getString("nombre");
        String correo = rs.getString("correo");
        String direccion = rs.getString("direccion");
        Estado estado = Estado.valueOf(rs.getString("estado"));

        // Asegurar el orden correcto de parámetros: (id, correo, nombre, direccion, estado)
        return new Cliente(id, correo, nombre, direccion, estado);
    }

    public String getCorreo() {
        return correo;
    }

    public String getDireccion() {
        return direccion;
    }
}