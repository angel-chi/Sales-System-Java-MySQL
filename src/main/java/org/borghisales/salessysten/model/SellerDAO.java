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

public class SellerDAO extends AbstractBaseDAO<Seller> {

    // Implementación de lo que se repite.
    @Override
    protected String getEntityName() {
        return "Vendedor";
    }

    @Override
    protected String getInsertSQL() {
        return "INSERT INTO seller (dni,name,phone_number,state,user) values (?,?,?,?,?)";
    }

    @Override
    protected void setInsertParameters(PreparedStatement pstmt, Seller entity) throws SQLException {
        pstmt.setString(1, entity.dni());
        pstmt.setString(2, entity.name());
        pstmt.setString(3, entity.phoneNumber());
        pstmt.setString(4, entity.state().name());
        pstmt.setString(5, entity.user());
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE seller set name=?,phone_number=?,state=?,user=? where dni=?";
    }

    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, Seller entity) throws SQLException {
        pstmt.setString(1, entity.name());
        pstmt.setString(2, entity.phoneNumber());
        pstmt.setString(3, entity.state().name());
        pstmt.setString(4, entity.user());
        pstmt.setString(5, entity.dni()); // El ID va al final en el UPDATE
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM seller where dni=?";
    }

    @Override
    protected String getSelectAllSQL() {
        return "SELECT * FROM seller";
    }

    @Override
    protected Seller fromResultSet(ResultSet rs) throws SQLException {
        return Seller.fromResultSet(rs);
    }

    // Las siguientes son métodos específicos del SellerDAO.
    public static boolean login(String dni,String user){

        if (dni ==null || user ==null || dni.isEmpty()||user.isEmpty() ){
            MenuController.setAlert(Alert.AlertType.ERROR,"Usuario o contraseña vacíos.");
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
                    GenerateSaleController.setIdVendedor(rs.getInt("idSeller"));

                    MainController.sellerLog = Seller.fromResultSet(rs);

                    return true;
                } else{
                    MenuController.setAlert(Alert.AlertType.ERROR, "Usuario no encontrado.") ;
                    return false;
                }
            }
        } catch (SQLException e){
            MenuController.setAlert(Alert.AlertType.ERROR, "Error intentando buscar al vendedor: " + e.getMessage());
            return false;
        }

    }
}
