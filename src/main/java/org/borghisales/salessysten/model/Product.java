package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Product(int idProduct, String name, double price, int stock, State state, double discountPercentage, int discountMinQuantity) {
    public enum State {ACTIVE(), DISACTIVE()}

    public Product(String name, double price, int stock, State state){
        this(0,name,price,stock,state, 0.0, 0);
    }

    public Product(String name, double price, int stock, State state, double discountPercentage, int discountMinQty) {
        this(0, name, price, stock, state, discountPercentage, discountMinQty);
    }


    public static Product fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("idProduct");
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        int stock = rs.getInt("stock");
        State state = Product.State.valueOf(rs.getString("state"));
        // Nuevos valores desde BD
        double discountPercentage = rs.getDouble("discount_percentage");
        int discountMinQuantity = rs.getInt("discount_min_qty");
        return new Product(id, name, price, stock, state, discountPercentage, discountMinQuantity);
    }



}
