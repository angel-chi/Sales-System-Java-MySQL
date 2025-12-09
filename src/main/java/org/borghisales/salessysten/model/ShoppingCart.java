package org.borghisales.salessysten.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;

public record ShoppingCart(int nr, String cod, String product, int quantity, double price, double total, double discount){
    public ShoppingCart(int nr, String cod, String product, int quantity, double price, double discount) {
        this(nr, cod, product, quantity, price, Double.parseDouble(String.format("%.2f",quantity*(price * (1 - discount)))), discount);
    }
    public static ShoppingCart fromResultSet(ResultSet rs) throws SQLException {
        int nr = rs.getInt("nr");
        String cod = rs.getString("cod");
        String product = rs.getString("product");
        int quantity = rs.getInt("quantity");
        double price = rs.getDouble("price");
        double total = rs.getDouble("total");
        double descuento = rs.getDouble("descuento");
        return new ShoppingCart(nr, cod, product, quantity, price, BigDecimal.valueOf(total * descuento).setScale(2, RoundingMode.HALF_UP).doubleValue(), descuento);
    }
}
