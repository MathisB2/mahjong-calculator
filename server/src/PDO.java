import com.mongodb.client.*;
import org.bson.Document;

public class PDO {
    MongoClient mongoClient;
    void run(){
        try{
            setConnection();

            MongoIterable<String> databaseNames=mongoClient.listDatabaseNames();
            System.out.println("test");

            for(String databaseName : databaseNames){
                System.out.println("Database : "+databaseName);
            }

            System.out.println("test2");

            MongoDatabase database= mongoClient.getDatabase("dbTest");
            MongoCollection<Document> collection = database.getCollection("users");

            Document doc1 = new Document("nom","Doe").append("prenom","John").append("age",37);
            Document doc2 = new Document("nom","Smith").append("prenom","Jane").append("age",25);

            collection.insertOne(doc1);
            collection.insertOne(doc2);

            
            for(String databaseName : databaseNames){
                System.out.println("Database : "+databaseName);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    void setConnection(){
        mongoClient= MongoClients.create("mongodb://localhost:27017");
    }
}
