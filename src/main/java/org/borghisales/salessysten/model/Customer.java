package org.borghisales.salessysten.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;

public record Customer(int idCustomer, String dni, String name, String address, State state, Membresia membresia) {
    public enum State {ACTIVA(), INACTIVA()}

    public enum Membresia {
        REGULAR("REGULAR", 0.0d), FRECUENTE("FRECUENTE", 0.1d), DESTACADO("DESTACADO", 0.25d);

        private final String text;
        private final Double discount;

        Membresia(String text, double discount) {
            this.text = text;
            this.discount = discount;
        }

        public static Membresia getFromText(String text) {
            for(Membresia membresia : Membresia.values()) {
                if( membresia.text.contentEquals(text) ) {
                    return membresia;
                }
            }
            return REGULAR;
        }

        public double getDiscount() {
            return discount;
        }

        public double applyDiscount(double price) {
            return BigDecimal.valueOf((1 - discount) * price).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }

        @Override
        public String toString() {
            return text;
        }
    };

    public Customer(String dni, String name, String address, State state, Membresia membresia){
        this(0,dni,name,address,state, membresia);
    }

    public static Customer fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("idCustomer");
        String dni = rs.getString("dni");
        String name = rs.getString("name");
        String address = rs.getString("address");
        State state = Customer.State.valueOf(rs.getString("state"));
        Membresia membresia = Membresia.getFromText(rs.getString("membresia"));
        return new Customer(id, dni, name, address, state, membresia);
    }
}
