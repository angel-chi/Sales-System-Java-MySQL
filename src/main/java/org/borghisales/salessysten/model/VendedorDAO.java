package org.borghisales.salessysten.model;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.borghisales.salessysten.controllers.GenerateSaleController;
import org.borghisales.salessysten.controllers.MainController;
import org.borghisales.salessysten.controllers.MenuController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class VendedorDAO implements CRUD<Vendedor> { // Cambiado de SellerDAO a VendedorDAO, y Seller a Vendedor
    @Override
    public boolean create(Vendedor entity) { // Tipo de entidad cambiado
        String sql = "INSERT INTO vendedores (identificacion,nombre,telefono,estado,usuario) values (?,?,?,?,?)"; // Nombres de tabla y columnas cambiados

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, entity.identificacion()); // Nombre de método de entidad cambiado
            pstmt.setString(2, entity.nombre()); // Nombre de método de entidad cambiado
            pstmt.setString(3, entity.telefono()); // Nombre de método de entidad cambiado
            pstmt.setString(4, entity.estado().name());
            pstmt.setString(5, entity.usuario()); // Nombre de método de entidad cambiado

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Vendedor agregado correctamente"); // Mensaje traducido
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al agregar vendedor: "); // Mensaje traducido
                return false;
            }


        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al agregar vendedor: " + e.getMessage()); // Mensaje traducido
            return false;
        }

    }


    @Override
    public boolean update(Vendedor entity) { // Tipo de entidad cambiado
        String sql = "UPDATE vendedores SET nombre=?,telefono=?,estado=?,usuario=? WHERE identificacion=?"; // Nombres de tabla y columnas cambiados, la lógica de WHERE se mantiene con 'identificacion'

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){


            pstmt.setString(1, entity.nombre()); // Nombre de método de entidad cambiado
            pstmt.setString(2, entity.telefono()); // Nombre de método de entidad cambiado
            pstmt.setString(3, entity.estado().name());
            pstmt.setString(4, entity.usuario()); // Nombre de método de entidad cambiado
            pstmt.setString(5, entity.identificacion()); // Nombre de método de entidad cambiado

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Vendedor actualizado correctamente"); // Mensaje traducido
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al actualizar vendedor "); // Mensaje traducido
                return false;
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al actualizar vendedor: " + e.getMessage()); // Mensaje traducido
            return false;
        }

    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM vendedores WHERE identificacion=?"; // Nombre de tabla y columna cambiados, la lógica de WHERE se mantiene con 'identificacion'

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql))    {


            pstmt.setString(1,id);

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Vendedor eliminado correctamente"); // Mensaje traducido
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al eliminar vendedor: "); // Mensaje traducido
                return false;
            }



        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al eliminar vendedor: " + e.getMessage()); // Mensaje traducido
            return false;
        }

    }

    @Override
    public void setTable(ObservableList<Vendedor> vendedores){ // Tipo de lista y parámetro cambiado
        String sql = "SELECT * FROM vendedores"; // Nombre de tabla cambiado

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            try (ResultSet rs = pstmt.executeQuery()){

                while (rs.next()){
                    Vendedor vendedor = Vendedor.fromResultSet(rs); // Tipo de objeto y nombre de variable cambiado
                    vendedores.add(vendedor); // Nombre de lista cambiado
                }

            }
        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al cargar la tabla de vendedores: " + e.getMessage()); // Mensaje traducido
        }
    }


    public static boolean login(String identificacion,String usuario){ // Nombres de parámetros y método cambiados

        if (identificacion == null || usuario == null || identificacion.isEmpty() || usuario.isEmpty() ){ // Nombres de parámetros cambiados
            MenuController.setAlert(Alert.AlertType.ERROR,"Usuario o contraseña vacíos"); // Mensaje traducido
            return false;
        }

        String query = "SELECT * FROM vendedores WHERE identificacion = ? AND usuario = ?"; // Nombres de tabla y columnas cambiados

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(query)  ){

            pstmt.setString(1,identificacion); // Nombre de parámetro cambiado
            pstmt.setString(2,usuario); // Nombre de parámetro cambiado

            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){

                    GenerateSaleController.setNombreVendedor(rs.getString("nombre")); // setSellerName -> setNombreVendedor
                    GenerateSaleController.setIdVendedor(rs.getInt("idVendedor")); // setIdSeller -> setIdVendedor

                    MainController.vendedorLogeado = Vendedor.fromResultSet(rs); // sellerLog -> vendedorLogeado

                    return true;
                }else{
                    MenuController.setAlert(Alert.AlertType.ERROR, "Vendedor no encontrado") ; // Mensaje traducido
                    return false;
                }
            }
        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al buscar vendedor: " + e.getMessage()); // Mensaje traducido
            return false;
        }

    }
}
