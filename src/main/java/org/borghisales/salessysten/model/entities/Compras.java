package org.borghisales.salessysten.model.entities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public record Compras(int idCompra, int idProveedor, int idVendedor, LocalDate fechaCompra, double subtotal, EstadoCompra estado) {

    public enum EstadoCompra{CANCELADO, COMPLETADO;}
    /*
        idCompra` int NOT NULL AUTO_INCREMENT,
        `idProveedor` int DEFAULT NULL,
        `idSeller` int DEFAULT NULL,
        `subtotal` double NOT NULL,
        `stateCompra` enum('CANCELADO','COMPLETADO') DEFAULT 'ACTIVE',
        `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    * */
    public Compras(int idProveedor, int idVendedor, double monto, EstadoCompra estado) {
        this(0, idProveedor, idVendedor, LocalDate.now(), monto, estado);
    }

    public Compras(int idProveedor, int idVendedor, LocalDate fechaCompra, double subtotal, EstadoCompra estado){
        this(0, idProveedor, idVendedor, fechaCompra, subtotal, estado);
    }
    public static Compras fromResultSet(ResultSet rs) throws SQLException {
        int idCompra = rs.getInt("idCompra");
        int idProveedor = rs.getInt("idProveedor");
        int idVendedor = rs.getInt("idSeller");
        double subtotal = rs.getDouble("subtotal");
        LocalDate fecha = rs.getTimestamp("created_at").toLocalDateTime().toLocalDate();
        EstadoCompra state = EstadoCompra.valueOf(rs.getString("stateCompra"));
        return new Compras(idCompra, idProveedor, idVendedor, fecha, subtotal, state);
    }

}
