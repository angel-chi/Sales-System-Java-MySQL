package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

// Se agregan idBrand y brandName
public record Product(int idProduct, String name, int idBrand, String brandName, double price, int stock, State state) {
    public enum State {ACTIVE(), DISACTIVE()}

    // se modificaron los constructores
    public Product(String name, int idBrand, String brandName, double price, int stock, State state){
        this(0, name, idBrand, brandName, price, stock, state);
    }

    public static Product fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("idProduct");
        String name = rs.getString("name");
        // Recuperamos datos de la marca gracias al JOIN en el DAO
        int idBrand = rs.getInt("idBrand");
        String brandName = rs.getString("brand_name");
        double price = rs.getDouble("price");
        int stock = rs.getInt("stock");
        State state = Product.State.valueOf(rs.getString("state"));

        return new Product(id, name, idBrand, brandName, price, stock, state);
    }
}