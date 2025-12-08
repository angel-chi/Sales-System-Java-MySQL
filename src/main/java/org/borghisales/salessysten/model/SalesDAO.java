package org.borghisales.salessysten.model;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MainController;
import org.borghisales.salessysten.controllers.MenuController;
import org.borghisales.salessysten.controllers.ReportsController;
import org.borghisales.salessysten.model.ProductDAO;
import java.awt.event.ActionEvent;
import java.sql.*;


public class SalesDAO {

    public int IdSale(){
        String sql = "SELECT MAX(idSales) FROM sales";
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){
                    if (rs.getInt(1)==0)
                        return 1;

                    return rs.getInt(1);
                }
                return 1;
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error buscando la ID-Venta: " + e.getMessage());
            return 1;
        }
    }
    public boolean SaveSale(Sales sale){
        String sql = "INSERT INTO sales (idCustomer,idSeller,numberSales,saleDate,amount,state) values(?,?,?,?,?,?)";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1,sale.idCustomer());
            pstmt.setInt(2,sale.idSeller());
            pstmt.setString(3,sale.numberSales());
            pstmt.setDate(4, Date.valueOf(sale.saleDate()));
            pstmt.setDouble(5,sale.amount());
            pstmt.setString(6,sale.state().name());

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Venta guardada correctamente");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error guardando la venta: ");
                return false;
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error guardando la venta: " + e.getMessage());
            return false;
        }

    }

    public boolean SaveDetailsSale(ObservableList<ShoppingCart> products, int id){

        try (Connection conn = DBConnection.connection()){

            for (ShoppingCart e:products) {
                String sql = "INSERT INTO sales_details (idSales,idProduct,quantity, priceSale) values(?,?,?,?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setInt(1, id);
                    pstmt.setInt(2, Integer.parseInt(e.cod()));
                    pstmt.setInt(3, e.quantity());
                    pstmt.setDouble(4, e.price());

                    pstmt.executeUpdate();

                }
            }
                return true;



        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error guardando los detalles de la venta: " + e.getMessage());
            return false;
        }

    }

    public void setTable(ObservableList<Sales> sales) {
        String sql = "SELECT * FROM sales WHERE idSeller = ?";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, MainController.sellerLog.idSeller());

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    Sales sale = Sales.fromResultSet(rs);
                    sales.add(sale);
                }
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error buscando ventas: " + e.getMessage());
        }


    }

    public void setLineChart(XYChart.Series<String, Integer> lineChartData,int year,int month) {
        String sql = """ 
                SELECT day(saleDate) as saleDate, count(saleDate) as salesPerDay
                FROM sales
                WHERE idSeller=? and year(saleDate) = ? and month(saleDate)=?
                AND state NOT IN ('CANCELADA', 'DEVUELTA')
                GROUP BY saleDate;
                """;

        System.out.println("Buscado base de datos");

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, MainController.sellerLog.idSeller());
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    XYChart.Data<String,Integer> data = new XYChart.Data<>(String.valueOf(rs.getInt("saleDate")),rs.getInt("salesPerDay"));
                    lineChartData.getData().add(data);
                }
            }


            ReportsController.setCacheReportLineChart(year,month,lineChartData);



        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error buscando ventas: " + e.getMessage());
        }
    }

    public void setTableDetails(ObservableList<ShoppingCart> productsDetails, int idSale) {
        String sql = """ 
                SELECT ROW_NUMBER() OVER() as nr, sd.idProduct as cod, p.name as product, sd.quantity, sd.priceSale as price, ROUND(sd.quantity *sd.priceSale,2) as total
                FROM sales_details sd
                INNER JOIN product p
                USING(idProduct)
                WHERE idSales = ?;
                """;

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, idSale);

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    ShoppingCart sc = ShoppingCart.fromResultSet(rs);
                    productsDetails.add(sc);
                }
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error buscando ventas: " + e.getMessage());
        }

    }

    public boolean desactivar(int idSales, Sales.State state) {
        String sql = "UPDATE sales SET state = ? WHERE idSales = ? AND state = 'ACTIVE'";

        try (Connection conn = DBConnection.connection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, state.name());
            stmt.setInt(2, idSales);
            int rows_affected = stmt.executeUpdate();

            if (rows_affected > 0) {
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Venta desactivada correctamente");
                return true;
            } else {
                MenuController.setAlert(Alert.AlertType.ERROR, "Esta venta ya esta desactivada");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void regresar(int idSales, Sales.State state, ObservableList<ShoppingCart> productsDetails) {
        boolean next = desactivar(idSales, state);
        if (next) {regresar(productsDetails, idSales);}
    }

    public void regresar(ObservableList<ShoppingCart> productsDetails, int idSale) {
        productsDetails.clear();
        String sql = """ 
                SELECT ROW_NUMBER() OVER() as nr, sd.idProduct as cod, p.name as product, sd.quantity, sd.priceSale as price, ROUND(sd.quantity *sd.priceSale,2) as total
                FROM sales_details sd
                INNER JOIN product p
                USING(idProduct)
                WHERE idSales = ?;
                """;

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, idSale);

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    ShoppingCart sc = ShoppingCart.fromResultSet(rs);
                    productsDetails.add(sc);
                }
                System.out.println(productsDetails);
                addStock(productsDetails);
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error buscando ventas: " + e.getMessage());
        }

    }

    public void addStock(ObservableList<ShoppingCart> products){
        String sql = "UPDATE product SET stock = stock + ? WHERE idProduct = ?";
        try(Connection conn = DBConnection.connection()){

            for (ShoppingCart e:products) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setInt(1,e.quantity());
                    pstmt.setInt(2,Integer.parseInt(e.cod()));
                    int rows_affected = pstmt.executeUpdate();

                    if (rows_affected>0){
                        System.out.println("productos regresados correctamente");
                    }else{
                        System.out.println("error regresando productos");
                    }
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
