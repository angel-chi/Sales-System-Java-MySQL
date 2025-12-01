package org.borghisales.salessysten.model;

import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MenuController;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConnection {

    static Connection connection() {
        Properties properties = new Properties();

        // Intenta cargar desde resources/
        try (InputStream input = DBConnection.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                System.out.println(" No se encontró config.properties en resources/");
                return null;
            }

            properties.load(input);

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean verifyDuplicates(String name, String surname){
        String query = "SELECT * FROM employee_data WHERE NAME = ? AND SURNAME = ?";
        try (Connection conn = connection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name.strip());
            pstmt.setString(2, surname.strip());

            try (ResultSet rs = pstmt.executeQuery()) {
                return !rs.next();
            }

        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR,
                    "Error adding employee: " + e.getMessage());
            return false;
        }
    }
}
