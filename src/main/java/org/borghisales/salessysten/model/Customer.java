package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Customer(int idCustomer, String dni, String name, String email, String address, State state)  implements User {
    // public enum State {ACTIVE(), DISACTIVE()}

    public Customer(String dni, String name, String email, String address, State state){
        this(0,dni,name,email,address,state);
    }

    public static Customer fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("idCustomer");
        String dni = rs.getString("dni");
        String name = rs.getString("name");
        String address = rs.getString("address");
        String email = rs.getString("email");
        State state = State.valueOf(rs.getString("state"));
        return new Customer(id, dni, name, email, address, state);
    }
}
