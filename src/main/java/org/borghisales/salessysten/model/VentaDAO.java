package org.borghisales.salessysten.model;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MainController;
import org.borghisales.salessysten.controllers.MenuController;
import org.borghisales.salessysten.controllers.ReportsController;

import java.sql.*;


public class VentaDAO { // Cambiado de SalesDAO a VentaDAO

    public int IdVenta(){ // Cambiado de IdSale a IdVenta
        String sql = "SELECT MAX(idVenta) FROM ventas"; // Nombres de tabla y columna cambiados
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
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al buscar IdVenta: " + e.getMessage()); // Mensaje traducido
            return 1;
        }
    }
    public boolean SaveVenta(Venta venta){ // Cambiado de SaveSale a SaveVenta, y Sales a Venta
        String sql = "INSERT INTO ventas (idCliente,idVendedor,numeroDeVenta,fechaDeVenta,total,estado) values(?,?,?,?,?,?)"; // Nombres de tabla y columnas cambiados

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1,venta.idCliente()); // Nombre de método de entidad cambiado
            pstmt.setInt(2,venta.idVendedor()); // Nombre de método de entidad cambiado
            pstmt.setString(3,venta.numeroDeVenta()); // Nombre de método de entidad cambiado
            pstmt.setDate(4, Date.valueOf(venta.fechaDeVenta())); // Nombre de método de entidad cambiado
            pstmt.setDouble(5,venta.total()); // Nombre de método de entidad cambiado
            pstmt.setString(6,venta.estado().name());

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Venta guardada correctamente"); // Mensaje traducido
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al guardar venta: "); // Mensaje traducido
                return false;
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al guardar venta: " + e.getMessage()); // Mensaje traducido
            return false;
        }

    }

    public boolean SaveDetallesVenta(ObservableList<CarritoCompra> productos, int id){ // Corregido: ShoppingCart -> CarritoCompra

        try (Connection conn = DBConnection.connection()){

            for (CarritoCompra e:productos) { // Corregido: ShoppingCart -> CarritoCompra
                String sql = "INSERT INTO detalle_ventas (idVenta,idProducto,cantidad, precioVenta) values(?,?,?,?)"; // Nombres de tabla y columnas cambiados
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setInt(1, id);
                    pstmt.setInt(2, Integer.parseInt(e.codigo())); // Corregido: cod() -> codigo()
                    pstmt.setInt(3, e.cantidad()); // Corregido: quantity() -> cantidad()
                    pstmt.setDouble(4, e.precio()); // Corregido: price() -> precio()

                    pstmt.executeUpdate();

                }
            }
                return true;



        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al guardar detalles de venta: " + e.getMessage()); // Mensaje traducido
            return false;
        }

    }

    public void setTable(ObservableList<Venta> ventas) { // Tipo de lista y parámetro cambiado
        String sql = "SELECT * FROM ventas WHERE idVendedor = ?"; // Nombres de tabla y columna cambiados

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, MainController.vendedorLogeado.idVendedor()); // Corregido: sellerLog -> vendedorLogeado

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    Venta venta = Venta.fromResultSet(rs); // Tipo de objeto y nombre de variable cambiado
                    ventas.add(venta); // Nombre de lista cambiado
                }
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al buscar ventas: " + e.getMessage()); // Mensaje traducido
        }


    }

    public void setLineChart(XYChart.Series<String, Integer> lineChartData,int year,int month) {
        String sql = """ 
                SELECT day(fechaDeVenta) AS fechaDeVenta, COUNT(fechaDeVenta) AS ventasPorDia
                FROM ventas
                WHERE idVendedor=? AND year(fechaDeVenta) = ? AND month(fechaDeVenta)=?
                GROUP BY fechaDeVenta;
                """; // Nombres de tabla y columnas cambiados

        System.out.println("Buscado base de datos");


        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, MainController.vendedorLogeado.idVendedor()); // Corregido: sellerLog -> vendedorLogeado
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    XYChart.Data<String,Integer> data = new XYChart.Data<>(String.valueOf(rs.getInt("fechaDeVenta")),rs.getInt("ventasPorDia")); // Nombres de columna cambiados
                    lineChartData.getData().add(data);
                }
            }


            ReportsController.setCacheReporteGraficoLinea(year,month,lineChartData); // Corregido: setCacheReportLineChart -> setCacheReporteGraficoLinea



        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al buscar ventas: " + e.getMessage()); // Mensaje traducido
        }
    }

    public void setTableDetails(ObservableList<CarritoCompra> detallesProductos, int idVenta) { // Corregido: ShoppingCart -> CarritoCompra
        String sql = """ 
                SELECT ROW_NUMBER() OVER() AS nr, dv.idProducto AS cod, p.nombre AS producto, dv.cantidad, dv.precioVenta AS precio, ROUND(dv.cantidad * dv.precioVenta,2) AS total
                FROM detalle_ventas dv
                INNER JOIN productos p
                USING(idProducto)
                WHERE dv.idVenta = ?;
                """; // Nombres de tabla y columnas cambiados

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, idVenta); // Nombre de parámetro cambiado

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    CarritoCompra sc = CarritoCompra.fromResultSet(rs); // Corregido: ShoppingCart -> CarritoCompra
                    detallesProductos.add(sc); // Nombre de lista cambiado
                }
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al buscar detalles de ventas: " + e.getMessage()); // Mensaje traducido
        }

    }
}
