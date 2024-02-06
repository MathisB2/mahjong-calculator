import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

//import com.mongodb.client.*;
//import org.bson.Document;
import java.util.logging.*;


public class TestPdo {
    public static void main(String[] args) {


        PDO bd=new PDO();
        bd.run();

//        Connection conn =null;
//
//        try{
//            String url = "jdbc:sqlite:Z:\\Documents\\2A_SAE\\main\\server\\src\\BD\\test.db";
//            conn= DriverManager.getConnection(url);
//
//        }catch(SQLException e){
//            System.out.println(e.getMessage());
//        }finally {
//            try {
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (SQLException ex) {
//                System.out.println(ex.getMessage());
//            }
//        }



    }
}