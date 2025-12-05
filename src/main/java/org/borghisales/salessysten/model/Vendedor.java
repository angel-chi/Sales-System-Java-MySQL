package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Vendedor extends Usuario {
    private final String identificacion;
    private final String telefono;
    private final String usuario;

    public Vendedor(int idVendedor, String identificacion, String nombre, String telefono, Estado estado, String usuario){
        super(idVendedor, nombre, estado);
        this.identificacion = identificacion;
        this.telefono = telefono;
        this.usuario = usuario;
    }

    public Vendedor(String identificacion, String nombre, String telefono, Estado estado, String usuario) {
        this(0, identificacion, nombre, telefono, estado, usuario);
    }

    //Método factory estático para usar desde VendedorDAO
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

    //Métodos getter
    public String getIdentificacion(){
        return identificacion;
    }

    public String getTelefono(){
        return telefono;
    }

    public String getUsuario(){
        return usuario;
    }
}
