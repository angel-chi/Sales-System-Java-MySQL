package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Seller(int idSeller, String dni, String name, String phoneNumber, State state, String user) {
    public enum State {ACTIVE(), DISACTIVE()}
    public Seller(String dni, String name, String phoneNumber, State state, String user) {
        this(0, dni, name, phoneNumber, state, user);
    }

    public static Seller fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("idVendedor");
        String dni = rs.getString("Identificación");
        String name = rs.getString("Nombre");
        String phoneNumber = rs.getString("Numero_teléfono");
        State state = Seller.State.valueOf(rs.getString("Estado"));
        String user = rs.getString("Usuario");
        return new Seller(id, dni, name, phoneNumber, state, user);
    }
}

