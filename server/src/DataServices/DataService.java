package DataServices;

import NetworkService.NetworkService;
import org.java_websocket.WebSocket;

import java.util.HashMap;

public class DataService {
    private HashMap<WebSocket, UserData> userDataHashMap = new HashMap<>();
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
        var network = NetworkService.getNetwork();

        network.onEnter.connect(user -> {
            userDataHashMap.put(user, null);
        });

        network.onLeave.connect(user -> {
            if(!userDataHashMap.containsKey(user)) return;
            userDataHashMap.remove(user);
        });
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
}
