package org.borghisales.salessysten.model;

public record SalesDetails(int idSalesDetails, int idSales, int idProduct, int quantity, double priceSale, double discount) {
    public SalesDetails(int idSales, int idProduct, int quantity, double priceSale, double discount) {
        this(0, idSales, idProduct, quantity, priceSale, discount);
    }
}
