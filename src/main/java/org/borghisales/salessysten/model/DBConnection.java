package org.borghisales.salessysten.model;

import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MenuController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConnection {

    static Connection connection() throws SQLException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/main/java/org/borghisales/salessysten/model/config.properties")) {
            properties.load(input);
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            return DriverManager.getConnection(url, user, password);
        } catch (FileNotFoundException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "El archivo de configuración para la conexión con la base de datos no se ha encontrado.\nEl archivo debe de estar en la ruta\n\n\"src/main/java/org/borghisales/salessysten/model/config.properties\"");
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            // Manejar la excepción adecuadamente
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
            MenuController.setAlert(Alert.AlertType.ERROR,"Error añadiendo empleados: " + e.getMessage());
            return false;
        }

    }



}
