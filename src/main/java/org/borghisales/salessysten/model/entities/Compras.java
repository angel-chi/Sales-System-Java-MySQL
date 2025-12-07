package org.borghisales.salessysten.model.entities;
import java.sql.ResultSet;
import java.sql.SQLException;

public record Compras(int idcompra, int idProveedor, int idVendedor, double subtotal, EstadoCompra estadoCompra) {

    public enum EstadoCompra{CANCELADO, COMPLETADO;}
    /*
        idCompra` int NOT NULL AUTO_INCREMENT,
        `idProveedor` int DEFAULT NULL,
        `idSeller` int DEFAULT NULL,
        `subtotal` double NOT NULL,
        `stateCompra` enum('CANCELADO','COMPLETADO') DEFAULT 'ACTIVE',
        `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    * */
    public Compras(int idProveedor, int idVendedor, double subtotal, EstadoCompra estadoCompra){
        this(0, idProveedor, idVendedor, subtotal, estadoCompra);
    }
    public static Compras fromResultSet(ResultSet rs) throws SQLException {
        int idProveedor = rs.getInt("idProveedor");
        int idVendedor = rs.getInt("idSeller");
        double subtotal = rs.getDouble("subtotal");
        EstadoCompra state = EstadoCompra.valueOf(rs.getString("estado"));
        return new Compras(idProveedor, idVendedor, subtotal, state);
    }

}
