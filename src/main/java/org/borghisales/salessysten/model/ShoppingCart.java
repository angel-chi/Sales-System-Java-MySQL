package org.borghisales.salessysten.model;

import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;

public record ShoppingCart(int nr, String cod, String product, int quantity, double price, double discount,double iva, double subtotal, double total){

    public static ShoppingCart fromResultSet(ResultSet rs) throws SQLException {
        int nr = rs.getInt("nr");
        String cod = rs.getString("cod");
        String product = rs.getString("product");
        int quantity = rs.getInt("quantity");
        double price = rs.getDouble("price");
        //nuevos atributos
        //double discount = rs.getDouble("discount");
        //double iva = rs.getDouble("iva");
        //double subtotal = rs.getDouble("subtotal");

        double total = rs.getDouble("total");
        double iva = 0.16;
        double discount = 0.0;
        double subtotal = quantity * price;

        return new ShoppingCart(nr, cod, product, quantity, Double.parseDouble(String.format("%.2f",price)),
                discount, iva, subtotal, Double.parseDouble(String.format("%.2f",total)));
    }



}
