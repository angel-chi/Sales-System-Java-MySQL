package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Customer(int idCustomer, String dni, String name, String address, State state, String email) {
    public enum State {ACTIVO(), INACTIVO()}

    public Customer(String dni, String name, String address, State state, String email){
        this(0,dni,name,address,state, email);
    }

    public static Customer fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("idCustomer");
        String dni = rs.getString("dni");
        String name = rs.getString("name");
        String address = rs.getString("address");
        State state = Customer.State.valueOf(rs.getString("state"));
        String email = rs.getString("correo");
        return new Customer(id, dni, name, address, state, email);
    }
}
