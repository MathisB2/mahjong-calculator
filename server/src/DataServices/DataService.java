package DataServices;

import NetworkService.NetworkService;

import org.java_websocket.WebSocket;

import java.sql.*;
import java.util.HashMap;



public class DataService {
    private Connection conn;
    String url="jdbc:sqlite:src/DataServices/datastore.db";
    private HashMap<WebSocket, UserData> userDataHashMap = new HashMap<>();
    private HashMap<WebSocket, UserData> floatingConnections = new HashMap<>();
    static private DataService service = null;
    static public DataService get(){
        if(service == null) load();

        return service;
    }

    static public void load(){
        assert service == null : "DataService is already loaded";

        service = new DataService();
    }

    private DataService(){
        connect();

        var network = NetworkService.getNetwork();

        network.onEnter.connect(user -> {
            userDataHashMap.put(user, null);
        });

        network.onLeave.connect(user -> {
            if(!userDataHashMap.containsKey(user)) return;
            userDataHashMap.remove(user);
        });
    }
    private void connect(){
        try{
            conn = DriverManager.getConnection(url);
            System.out.println("success db link");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
    private void disconnect(){
        try{
            if(conn!=null){
                conn.close();
                System.out.println("db close");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void link(WebSocket user, UserData data){
        var network = NetworkService.getNetwork();
        assert network.connections().contains(user) : user + " is not connected to the server";

        userDataHashMap.replace(user, data);
    }

    public UserData getDataOf(WebSocket user){
        assert hasData(user) : user + " has no data";
        return userDataHashMap.get(user);
    }

    public boolean hasData(WebSocket user){
        return userDataHashMap.containsKey(user) && userDataHashMap.get(user) != null;
    }

    public Connection getConn(){
        return conn;
    }

    public void addUser(String username, String password){
        String sql = "INSERT INTO users (username, password) VALUES ('"+username+"','"+password+"');";

        try{
            Statement stmt = conn.createStatement();

            stmt.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println(e);
        }
    }
}
