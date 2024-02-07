//import com.mongodb.client.*;
//import org.bson.Document;
import java.sql.*;


public class PDO {
    //MongoClient mongoClient;
    void run(){
        Connection conn=null;

        try{
            String url="jdbc:sqlite:src/BD/bd.db";
            //String url2="jdbc:mysql://iutbg-lamp.univ-lyon1.fr:3306";
            //String url="jdbc:sqlite:/home/iutbgdin/Documents/sae/BD/server/src/BD/test.db";

            conn = DriverManager.getConnection(url);
//            conn = DriverManager.getConnection(url2,"p2204906","12204906");

//            ResultSet rs = conn.getMetaData().getTables(null, null, null, null);
//            while (rs.next()) {
//                System.out.println(rs.getString("TABLE_NAME"));
//            }

            Statement stmt =conn.createStatement();
//
            String sql = "CREATE TABLE Users " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " username VARCHAR(255), " +
                    " password VARCHAR(255))";
            String sql4="DROP TABLE SCORE;";
            String sql1 =" INSERT INTO Users (username,password) VALUES ('test','test')";
            String query="select * FROM Users WHERE username='tesjyt';";
//            String sql2 ="SELECT * FROM Users WHERE username='"+username+"' AND password='"+password+"';";
//            String sql = "DROP TABLE Users";
//
            String sql2 = "CREATE TABLE Score " +
                    "(id INTEGER not NULL, " +
                    " score INTEGER, " +
                    " date DATE, " +
                    " FOREIGN KEY ( id ) REFERENCES Users(id) )";
//
            ResultSet rs= stmt.executeQuery(query);
            rs.next();
            System.out.println("id : "+rs.getInt("id"));
            System.out.println("username : "+rs.getString("username"));
//            if (!rs.isBeforeFirst() ) {
//                System.out.println("No data");
//            }else{
//                System.out.println("data");
//            }

//            stmt.executeUpdate(sql2);




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
