package org.borghisales.salessysten.model.entities;

public record SalesDetails(int idSalesDetails, int idSales, int idProduct, int quantity, double priceSale, double subtotal) {
    public SalesDetails(int idSales, int idProduct, int quantity, double priceSale, double subtotal) {
        this(0, idSales, idProduct, quantity, priceSale, subtotal);
    }
}
