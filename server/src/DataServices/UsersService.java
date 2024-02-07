package DataServices;

import NetworkService.NetNamespace;
import NetworkService.NetworkService;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

import java.sql.*;

public class UsersService {
    static private UsersService service = null;
    private NetNamespace loginNet;
    private NetNamespace logoutNet;
    private NetNamespace islogedNet;
    static public UsersService get(){
        if(service == null) load();
        return service;
    }
    static public void load(){
        assert service == null : "LogService is already loaded";
        service = new UsersService();
    }
    private UsersService(){
        var network = NetworkService.getNetwork();
        var dataService = DataService.get();
        loginNet = network.getNameSpace("loginNet");
        logoutNet = network.getNameSpace("logoutNet");
        islogedNet = network.getNameSpace("logoutNet");

        loginNet.connect((WebSocket user, String message) -> {
            JSONObject obj = new JSONObject(message);
            String password = obj.getString("password");
            String username = obj.getString("username");

            if(!usersExists(username,password)){
                return String.valueOf(false);
            }

            UserData userdata = new UserData(username);
            dataService.link(user, userdata);
            return String.valueOf(true);
        });

        logoutNet.connect((WebSocket user, String message) -> {
            boolean success = true;
            dataService.link(user, null);
            return String.valueOf(success);
        });
    }

    private boolean usersExists(String username, String password){
        Connection conn = DataService.get().getConn();
        try{
            Statement stmt = conn.createStatement();

            String sql2 ="SELECT * FROM Users WHERE username='"+username+"' AND password='"+password+"';";

            ResultSet rs= stmt.executeQuery(sql2);

            return !rs.isBeforeFirst();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

}
