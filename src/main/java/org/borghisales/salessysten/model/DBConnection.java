package org.borghisales.salessysten.model;

import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MenuController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConnection {

    public static Connection connection() throws SQLException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/main/java/org/borghisales/salessysten/model/config.properties")) {
            properties.load(input);
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            return DriverManager.getConnection(url, user, password);
        } catch (IOException e) {
            e.printStackTrace();
            MenuController.setAlert(Alert.AlertType.ERROR, "No se ha encontrado el archivo de configuración para la base de datos");
            return null;
        }
    }

    private static boolean verifyDuplicates(String name, String surname){
        String query = "SELECT* FROM employee_data WHERE NAME = ? AND SURNAME = ?";
        try (Connection conn = connection();
             PreparedStatement pstmt = conn.prepareStatement(query) ){

            pstmt.setString(1,name.strip());
            pstmt.setString(2,surname.strip());

            try (ResultSet rs = pstmt.executeQuery()){
                return !(rs.next());
            }

        }catch(SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR,"Error al agregar el empleado (Este ya existe): " + e.getMessage());
            return false;
        }

    }
    private static boolean verifyBeforeDeletingSeller(String idVendedor){
        // Esto evitara problemas de logica al intentar eliminar alguna entidad que ya teng aun historial
        String query = "Select S.idCustomer from Sales join S sales, C customer";
        return false;
    }
    private static boolean verifyBeforeDeletingProduct(String idProducto){
        String query = "Select * from ";
        return false;
    }
    private static boolean verifyBeforeDeletingCustomer(String idCliente){
        String query = "Select * from ";
        return false;
    }



}
