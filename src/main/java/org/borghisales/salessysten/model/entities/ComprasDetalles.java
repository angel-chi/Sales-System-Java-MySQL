package org.borghisales.salessysten.model.entities;

public record ComprasDetalles(int idCompraDetalle, int idCompra, int idProducto, int cantidad, double precioCompra, double subtotal) {
    public ComprasDetalles(int idCompra, int idProducto, int cantidad, double precioCompra, double subtotal){
        this(0, idCompra, idProducto, cantidad, precioCompra, (double) cantidad*precioCompra);
    }
}
