package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Seller(int idSeller, String dni, String name, String email, String phoneNumber, State state, String user, String password) implements User{
    // public enum State {ACTIVE(), DISACTIVE()}
    public Seller(String dni, String name, String email, String phoneNumber, State state, String user, String password) {
        this(0, dni, name, email, phoneNumber, state, user, password);
    }

    public static Seller fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("idSeller");
        String dni = rs.getString("dni");
        String name = rs.getString("name");
        String phoneNumber = rs.getString("phone_number");
        State state = State.valueOf(rs.getString("state"));
        String user = rs.getString("user");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new Seller(id, dni, name, email, phoneNumber, state, user, password);
    }
}

