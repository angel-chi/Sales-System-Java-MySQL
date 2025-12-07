package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Customer(int idCustomer, String dni, String name, String address, State state, String number) {
    public enum State {ACTIVE(), DISACTIVE()}

    public Customer(String dni, String name, String address, State state, String number){
        this(0,dni,name,address,state,number);
    }


    //Convierte un registro de la base de datos en un objeto
    public static Customer fromResultSet(ResultSet rs) throws SQLException { //Un resultSet es una consulta en la base de datos
        int id = rs.getInt("idCustomer");
        String dni = rs.getString("dni");
        String name = rs.getString("name");
        String address = rs.getString("address");
        State state = Customer.State.valueOf(rs.getString("state"));
        String number = rs.getString("number");
        return new Customer(id, dni, name, address, state, number);
    }
}
