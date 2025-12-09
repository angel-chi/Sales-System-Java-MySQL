package org.borghisales.salessysten.model;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.MenuController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO {

    public Product searchProduct(int idProduct) {
        String sql = """
                SELECT p.idProduct, p.name, p.idBrand, b.name as brand_name, p.price, p.stock, p.state
                FROM product p
                INNER JOIN brand b ON p.idBrand = b.idBrand
                WHERE p.idProduct = ? AND p.state = 'ACTIVE'
                """;

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProduct);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Product.fromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al buscar producto: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void getBrands(ObservableList<Brand> list) {
        String sql = "SELECT * FROM brand ORDER BY name ASC";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            list.clear();
            while(rs.next()){
                list.add(new Brand(rs.getInt("idBrand"), rs.getString("name")));
            }

        } catch(SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error cargando marcas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setTable(ObservableList<Product> products) {
        String sql = """
            SELECT p.idProduct, p.name, p.idBrand, b.name as brand_name, p.price, p.stock, p.state
            FROM product p
            INNER JOIN brand b ON p.idBrand = b.idBrand
            ORDER BY p.idProduct ASC
            """;
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                products.add(Product.fromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resta el stock al realizar una venta.
     */
    public void subtractStock(ObservableList<ShoppingCart> products) {
        String sql = "UPDATE product SET stock = stock - ? WHERE idProduct = ?";
        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (ShoppingCart product : products) {
                pstmt.setInt(1, product.quantity());
                // Asegúrate que product.cod() sea numérico o manéjalo con try-catch si es String
                try {
                    pstmt.setInt(2, Integer.parseInt(product.cod()));
                    pstmt.executeUpdate();
                } catch (NumberFormatException e) {
                    System.err.println("Error restando stock, código inválido: " + product.cod());
                }
            }
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error actualizando stock: " + e.getMessage());
        }
    }

    // --- MÉTODOS CRUD BÁSICOS ---

    public boolean create(Product product) {
        String sql = "INSERT INTO product (name, idBrand, price, stock, state) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.connection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.name());
            pstmt.setInt(2, product.idBrand());
            pstmt.setDouble(3, product.price());
            pstmt.setInt(4, product.stock());
            pstmt.setString(5, product.state().toString());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error creando producto: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Product product) {
        String sql = "UPDATE product SET name=?, idBrand=?, price=?, stock=?, state=? WHERE idProduct=?";
        try (Connection conn = DBConnection.connection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.name());
            pstmt.setInt(2, product.idBrand());
            pstmt.setDouble(3, product.price());
            pstmt.setInt(4, product.stock());
            pstmt.setString(5, product.state().toString());
            pstmt.setInt(6, product.idProduct());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error actualizando producto: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String name) {
        String sql = "DELETE FROM product WHERE name = ?";
        try (Connection conn = DBConnection.connection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            MenuController.setAlert(Alert.AlertType.ERROR, "Error eliminando producto: " + e.getMessage());
            return false;
        }
    }
}