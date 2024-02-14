package DataServices;

import NetworkService.NetNamespace;
import NetworkService.NetworkService;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class LogService {
    static private LogService service = null;
    private NetNamespace loginNet;
    private NetNamespace logoutNet;
    static public LogService get(){
        if(service == null) load();
        return service;
    }
    static public void load(){
        assert service == null : "LogService is already loaded";
        service = new LogService();
    }
    private LogService(){
        var network = NetworkService.getNetwork();
        var dataService = DataService.get();
        loginNet = network.getNameSpace("LoginNet");
        logoutNet = network.getNameSpace("logoutNet");

        loginNet.connect((WebSocket user, String message) -> {
            boolean success = true;
            JSONObject obj = new JSONObject(message);
            String password = obj.getString("password");
            String userName = obj.getString("userName");
            //TODO
            return String.valueOf(success);
        });

        logoutNet.connect((WebSocket user, String message) -> {
            boolean success = true;
            dataService.link(user, null);
            return String.valueOf(success);
        });
    }
}
