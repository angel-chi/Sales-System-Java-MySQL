package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public record Venta(int idVenta, int idCliente, int idVendedor, String numeroDeVenta, LocalDate fechaDeVenta, Double total, Estado estado, TipoPago tipo_pago, EntregaTicket entrega_ticket) {
    public enum Estado {ACTIVO, INACTIVO}
    public enum TipoPago {EFECTIVO, TARJETA}
    public enum EntregaTicket {IMPRESO, CORREO}

    public Venta(int idCliente, int idVendedor, String numeroDeVenta, LocalDate fechaDeVenta, Double total, Estado estado, TipoPago tipo_pago, EntregaTicket entrega_ticket) {
        this(0, idCliente, idVendedor, numeroDeVenta, fechaDeVenta, total, estado, tipo_pago, entrega_ticket);
    }

    public static Venta fromResultSet(ResultSet rs) throws SQLException {
        int idVenta = rs.getInt("idVenta");
        int idCliente = rs.getInt("idCliente");
        int idVendedor = rs.getInt("idVendedor");
        String numeroDeVenta = rs.getString("numeroDeVenta");
        LocalDate fechaDeVenta = rs.getDate("fechaDeVenta").toLocalDate();
        Double total = rs.getDouble("total");
        Estado estado = Estado.valueOf(rs.getString("estado"));
        TipoPago tipoPago = TipoPago.valueOf(rs.getString("tipo_pago"));
        EntregaTicket entregaTicket = EntregaTicket.valueOf(rs.getString("entrega_ticket"));
        return new Venta(idVenta, idCliente, idVendedor, numeroDeVenta, fechaDeVenta, total, estado, tipoPago, entregaTicket);
    }
}
