package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public record Sales(int idSales, int idCustomer, int idSeller, String numberSales, LocalDate saleDate, Double subtotal,
                    State state, double total, double iva, double discount) {
    public enum State{ACTIVE,DISACTIVE};

    public Sales(int idCustomer, int idSeller, String numberSales, LocalDate saleDate, Double subtotal, State state,
                 double total, double iva, double discount) {
        this(0, idCustomer, idSeller, numberSales, saleDate, subtotal, state, total, iva, discount);
    }

    public static Sales fromResultSet(ResultSet rs) throws SQLException {
        int idSales = rs.getInt("idSales");
        int idCustomer = rs.getInt("idCustomer");
        int idSeller = rs.getInt("idSeller");
        String numberSales = rs.getString("numberSales");
        LocalDate saleDate = rs.getDate("saleDate").toLocalDate();
        Double subtotal = rs.getDouble("subtotal");
        State state = State.valueOf(rs.getString("state"));
        //nuevos atributos
        double total = rs.getDouble("amount");
        double iva = rs.getDouble("iva");
        double discount = rs.getDouble("discount");
        //
        return new Sales(idSales, idCustomer, idSeller, numberSales, saleDate, subtotal, state, total, iva, discount);
    }
}