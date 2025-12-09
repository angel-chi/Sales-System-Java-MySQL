package org.borghisales.salessysten.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Product(int idProduct, String name, double price, int stock, State state, Garantia garantia) {
    public enum State {ACTIVO(), INACTIVO()}

    public enum Garantia {
        SIN_GARANTIA("SIN GARANTIA"), MES_1("1 MES"), MESES_3("3 MESES"), MESES_6("6 MESES"), ANO_1("1 AÑO"), ANO_2("2 AÑOS");

        final private String text;

        Garantia(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public static Garantia getFromString(String str) {
          return switch(str) {
              case "1 MES" -> MES_1;
              case "3 MESES" -> MESES_3;
              case "6 MESES" -> MESES_6;
              case "1 AÑO" -> ANO_1;
              case "2 AÑOS" -> ANO_2;
              default -> SIN_GARANTIA;
          };
        };

        @Override
        public String toString() {
            return text;
        }
    };

    public Product(String name, double price, int stock, State state, Garantia garantia){
        this(0,name,price,stock,state, garantia);
    }

    public static Product fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("idProduct");
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        int stock = rs.getInt("stock");
        State state = Product.State.valueOf(rs.getString("state"));
        Garantia garantia = Garantia.getFromString(rs.getString("garantia"));
        return new Product(id, name, price, stock, state, garantia);
    }

}
