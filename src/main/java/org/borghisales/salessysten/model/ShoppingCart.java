package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public record ShoppingCart(int nr, String cod, String product, int quantity, double price, double total){
    public ShoppingCart(int nr, String cod, String product, int quantity, double price) {
        this(nr, cod, product, quantity, price, Math.round(quantity * price * 100.0) / 100.0);
    }//se redondea a 2 decimales a fuerza bruta para no tener error con las , y poder añadir productos
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
