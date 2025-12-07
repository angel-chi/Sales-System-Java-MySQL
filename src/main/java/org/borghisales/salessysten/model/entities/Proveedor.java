package org.borghisales.salessysten.model.entities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public record Proveedor(int idProveedor, String name, String email, State state) {
    public Proveedor(String name, String email, State state){
        this(0, name, email, state);
    }
    public static Proveedor fromResultSet(ResultSet rs) throws SQLException {
        int idProveedor = rs.getInt("idProveedor");
        String name = rs.getString("name");
        String email = rs.getString("email");
        State state = State.valueOf(rs.getString("state"));
        return new Proveedor(idProveedor, name, email, state);
    }
}
