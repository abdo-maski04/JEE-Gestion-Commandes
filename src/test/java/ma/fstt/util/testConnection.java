package ma.fstt.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class testConnection {

    public static void main(String[] args){
        String sql="select 1";
        try{
            Connection connection=DBConnection.getConnection();
            PreparedStatement ps=connection.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                System.out.println("Connection reussie");
            }
        }catch(Exception e){
            System.out.println("connection echouée avec bdd "+e.getMessage());
        }
    }
}
