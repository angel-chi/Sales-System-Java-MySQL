package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Customer(int idCustomer, String dni, String name, String address, State state) {
    public enum State {ACTIVE(), DISACTIVE()}

    public Customer(String dni, String name, String address, State state){
        this(0,dni,name,address,state);
    }

    public static Customer fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("idCliente");
        String dni = rs.getString("documnto de identidad");
        String name = rs.getString("nombre");
        String address = rs.getString("direccion");
        State state = Customer.State.valueOf(rs.getString("sestado"));
        return new Customer(id, dni, name, address, state);
    }
}
