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

public class SellerDAO implements CRUD<Seller> {
    @Override
    public boolean create(Seller entity) {
        String sql = "INSERT INTO seller (dni,name,phone_number,state,user) values (?,?,?,?,?)";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, entity.dni());
            pstmt.setString(2, entity.name());
            pstmt.setString(3, entity.phoneNumber());
            pstmt.setString(4, entity.state().name());
            pstmt.setString(5, entity.user());

            int rows_affected = pstmt.executeUpdate();

            return rows_affected > 0;


        }catch (SQLException e){
            e.printStackTrace();
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
            return rows_affected > 0;


        }catch (SQLException e){
            e.printStackTrace();
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
            return rows_affected>0;



        }catch (SQLException e){
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }


    public static Seller login(String dni,String user){

        String query = "SELECT * from seller where dni = ? and user = ?";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(query)  ){

            pstmt.setString(1,dni);
            pstmt.setString(2,user);

            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){

                    return Seller.fromResultSet(rs);

                }else{
                    return null;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }
}
