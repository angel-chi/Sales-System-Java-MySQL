package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public record SalesDetails(int idSalesDetails, int idSales, int idProduct, int quantity, double priceSale) {
    public SalesDetails(int idSales, int idProduct, int quantity, double priceSale) {
        this(0, idSales, idProduct, quantity, priceSale);
    }

}
