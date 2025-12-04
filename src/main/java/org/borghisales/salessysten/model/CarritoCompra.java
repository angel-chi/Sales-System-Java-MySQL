package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Nombre de clase y campos cambiados a español
public record CarritoCompra(int numero, String codigo, String producto, int cantidad, double precio, double total){
    public CarritoCompra(int numero, String codigo, String producto, int cantidad, double precio) {
        this(numero, codigo, producto, cantidad, precio, BigDecimal.valueOf(precio).multiply(BigDecimal.valueOf(cantidad)).setScale(2, RoundingMode.HALF_UP).doubleValue());
    }
    public static CarritoCompra fromResultSet(ResultSet rs) throws SQLException {
        // Los nombres de columna se mapean a los alias de las consultas SQL en VentaDAO.java
        int numero = rs.getInt("nr");
        String codigo = rs.getString("cod");
        String producto = rs.getString("producto");
        int cantidad = rs.getInt("cantidad");
        double precio = rs.getDouble("precio");
        double total = rs.getDouble("total");
        return new CarritoCompra(numero, codigo, producto, cantidad, precio, total);
    }
}
