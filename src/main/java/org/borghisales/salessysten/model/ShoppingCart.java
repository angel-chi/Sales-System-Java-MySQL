package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public record ShoppingCart(int nr, String cod, String product, int quantity, double price, double total){

    // correcion ya no convierte en string el precio y evita error en otras maquinas
    public ShoppingCart(int nr, String cod, String product, int quantity, double price) {
        this(nr, cod, product, quantity, price, quantity * price);
    }

    public static ShoppingCart fromResultSet(ResultSet rs) throws SQLException {
        int nr = rs.getInt("nr");
        String cod = rs.getString("cod");
        String product = rs.getString("product");
        int quantity = rs.getInt("quantity");
        double price = rs.getDouble("price");
        double total = rs.getDouble("total");
        return new ShoppingCart(nr, cod, product, quantity, price, total);
    }
}
