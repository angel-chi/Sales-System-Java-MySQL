package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public record Sales(int idSales, int idCustomer, int idSeller, String numberSales, LocalDate saleDate, Double amount,State state, Discount discount) {
    public enum State{ACTIVE,DISACTIVE}

    public Sales(int idCustomer, int idSeller, String numberSales, LocalDate saleDate, Double amount, State state, Discount discount) {
        this(0, idCustomer, idSeller, numberSales, saleDate, amount, state, discount);
    }

    public static Sales fromResultSet(ResultSet rs) throws SQLException {
        int idSales = rs.getInt("idSales");
        int idCustomer = rs.getInt("idCustomer");
        int idSeller = rs.getInt("idSeller");
        String numberSales = rs.getString("numberSales");
        LocalDate saleDate = rs.getDate("saleDate").toLocalDate();
        Double amount = rs.getDouble("amount");
        State state = State.valueOf(rs.getString("state"));
        Discount discount = Discount.valueOf(rs.getString("discount")); //Recibe el nombre guardado
                                                                                    // en la base de datos y lo convierte
                                                                                    //en objeto String
        return new Sales(idSales, idCustomer, idSeller, numberSales, saleDate, amount, state, discount);
    }
}