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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SellerDAO extends Validator<Seller> implements CRUD<Seller>{
    @Override
    public boolean create(Seller entity) {

        if(!validate(entity)) return false;

        String sql = "INSERT INTO seller (dni,name,phone_number,state,user) values (?,?,?,?,?)";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, entity.dni());
            pstmt.setString(2, entity.name());
            pstmt.setString(3, entity.phoneNumber());
            pstmt.setString(4, entity.state().name());
            pstmt.setString(5, entity.user());

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Vendedor correctamente agregado");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al agregar al vendedor: ");
                return false;
            }


        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al agregar al vendedor: " + e.getMessage());
            return false;
        }

    }


    @Override
    public boolean update(Seller entity) {
        String sql = "UPDATE seller set name=?,phone_number=?,state=?,user=? where dni=?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){


            pstmt.setString(1, entity.name());
            pstmt.setString(2, entity.phoneNumber());
            pstmt.setString(3, entity.state().name());
            pstmt.setString(4, entity.user());
            pstmt.setString(5, entity.dni());

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Vendedor actualizado correctamente");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error al actualizar al vendedor");
                return false;
            }

        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error al actualizar al vendedor: " + e.getMessage());
            return false;
        }

    }

    @Override
    public boolean delete(String id) {

        String sql = "DELETE FROM seller where dni=?";

        try(Connection conn = DBConnection.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql))    {


            pstmt.setString(1,id);

            int rows_affected = pstmt.executeUpdate();

            if (rows_affected>0){
                MenuController.setAlert(Alert.AlertType.CONFIRMATION, "Vendedor correctamente elminado");
                return true;
            }else{
                MenuController.setAlert(Alert.AlertType.ERROR, "Error eliminado al vendedor: ");
                return false;
            }



        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error eliminado al vendedor: " + e.getMessage());
            return false;
        }

    }

    @Override
    public void setTable(ObservableList<Seller> sellers){
        String sql = "SELECT * FROM seller";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            try (ResultSet rs = pstmt.executeQuery()){

                while (rs.next()){
                    Seller seller = Seller.fromResultSet(rs);
                    sellers.add(seller);
                }

            }
        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error sestableciendo la tabla de vendedor: " + e.getMessage());
        }
    }


    public static boolean login(String dni,String user){

        if (dni.isEmpty()||user.isEmpty()){
            MenuController.setAlert(Alert.AlertType.ERROR,"Usuario o contraseña vacios");
            return false;
        }

        String query = "SELECT * from seller where dni = ? and user = ?";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(query)  ){

            pstmt.setString(1,dni);
            pstmt.setString(2,user);

            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){

                    GenerateSaleController.setSellerName(rs.getString("name"));
                    GenerateSaleController.setIdSeller(rs.getInt("idSeller"));

                    MainController.sellerLog = Seller.fromResultSet(rs);

                    return true;
                }else{
                    MenuController.setAlert(Alert.AlertType.ERROR, "usuario no encontrado") ;
                    return false;
                }
            }
        }catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error buscando al vendedor: " + e.getMessage());
            return false;
        }

    }

    @Override
    protected boolean validate(Seller entity) {
        if(Objects.isNull(entity)){
            MenuController.setAlert(Alert.AlertType.ERROR, "El vendedor no puede estar vacio");
            return false;
        }

        if(entity.dni() == null || entity.dni().isEmpty()){
            MenuController.setAlert(Alert.AlertType.ERROR, "El dni del vendedor no puede estar vacio");
            return false;
        }
        if (entity.name() == null || entity.name().isEmpty()){
            MenuController.setAlert(Alert.AlertType.ERROR, "El nombre del vendedor no puede estar vacio");
            return false;
        }
        if(entity.phoneNumber() == null || entity.phoneNumber().isEmpty()){
            MenuController.setAlert(Alert.AlertType.ERROR, "El numero de telefono del vendedor no puede estar vacio");
            return false;
        }
        if(entity.user() == null || entity.user().isEmpty()){
            MenuController.setAlert(Alert.AlertType.ERROR, "El usuario del vendedor no puede estar vacio");
            return false;
        }
        if(entity.state()==null){
            MenuController.setAlert(Alert.AlertType.ERROR, "El estado del vendedor no puede estar vacio");
            return false;
        }
        return true;
    }
}
