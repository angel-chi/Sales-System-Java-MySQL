package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Product(int idProduct, String name, double price, int stock, State state) {
    public enum State {ACTIVE(), DISACTIVE()}

    public Product(String name, double price, int stock, State state){
        this(0,name,price,stock,state);
    }

    public static Product fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("idProducto");
        String name = rs.getString("Nombre");
        double price = rs.getDouble("Precio");
        int stock = rs.getInt("Existencias");
        State state = Product.State.valueOf(rs.getString("Estado"));
        return new Product(id, name, price, stock, state);
    }



}
