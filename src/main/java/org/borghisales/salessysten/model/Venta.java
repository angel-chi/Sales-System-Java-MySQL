package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public record Venta(int idVenta, int idCliente, int idVendedor, String numeroDeVenta, LocalDate fechaDeVenta, Double total, Estado estado) {
    public enum Estado{ACTIVO,INACTIVO};

    public Venta(int idCliente, int idVendedor, String numeroDeVenta, LocalDate fechaDeVenta, Double total, Estado estado) {
        this(0, idCliente, idVendedor, numeroDeVenta, fechaDeVenta, total, estado);
    }

    public static Venta fromResultSet(ResultSet rs) throws SQLException {
        int idVenta = rs.getInt("idVenta");
        int idCliente = rs.getInt("idCliente");
        int idVendedor = rs.getInt("idVendedor");
        String numeroDeVenta = rs.getString("numeroDeVenta");
        LocalDate fechaDeVenta = rs.getDate("fechaDeVenta").toLocalDate();
        Double total = rs.getDouble("total");
        Estado estado = Estado.valueOf(rs.getString("estado"));
        return new Venta(idVenta, idCliente, idVendedor, numeroDeVenta, fechaDeVenta, total, estado);
    }
}
