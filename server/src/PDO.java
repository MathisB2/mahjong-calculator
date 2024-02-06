//import com.mongodb.client.*;
//import org.bson.Document;
import java.sql.*;


public class PDO {
    //MongoClient mongoClient;
    void run(){
        Connection conn=null;

        System.out.println("test");
        try{
            System.out.println("test2");
            //Class.forName("org.sqlite.JDBC");
            System.out.println("test3");
            String url="jdbc:sqlite:/home/iutbgdin/Documents/sae/BD/server/src/BD/test.db";
            conn = DriverManager.getConnection(url);

            ResultSet rs = conn.getMetaData().getTables(null, null, null, null);
            while (rs.next()) {
                System.out.println(rs.getString("TABLE_NAME"));
            }
        }catch (SQLException e){
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }finally{
            try{
                if(conn!=null){
                    conn.close();
                    System.out.println("db close");
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }


//        try{
//            setConnection();
//
//            MongoIterable<String> databaseNames=mongoClient.listDatabaseNames();
//            System.out.println("test");
//
//            for(String databaseName : databaseNames){
//                System.out.println("Database : "+databaseName);
//            }
//
//            System.out.println("test2");
//
//            MongoDatabase database= mongoClient.getDatabase("dbTest");
//            MongoCollection<Document> collection = database.getCollection("users");
//
//            Document doc1 = new Document("nom","Doe").append("prenom","John").append("age",37);
//            Document doc2 = new Document("nom","Smith").append("prenom","Jane").append("age",25);
//
//            collection.insertOne(doc1);
//            collection.insertOne(doc2);
//
//
//            for(String databaseName : databaseNames){
//                System.out.println("Database : "+databaseName);
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }
    void setConnection(){
        //mongoClient= MongoClients.create("mongodb://localhost:27017");
    }
}
