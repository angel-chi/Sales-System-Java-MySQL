package org.borghisales.salessysten.model;

// Nombre de clase y campos cambiados a español para coincidir con la base de datos
public record DetalleVenta(int idDetalleVenta, int idVenta, int idProducto, int cantidad, double precioVenta) {
    public DetalleVenta(int idVenta, int idProducto, int cantidad, double precioVenta) {
        this(0, idVenta, idProducto, cantidad, precioVenta);
    }
}
