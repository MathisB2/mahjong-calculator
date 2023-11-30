package NetworkService;

import Signal.*;
import org.java_websocket.WebSocket;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;


public class NetNamespace {
    private String name;
    private NetworkService network;
    public Signal entries;
    NetNamespace(String name, NetworkService network){
        this.name = name;
        this.network = network;
        entries = new Signal();
    }
    public Connection connect(NetworkCallBack callBack){
        return entries.connect(new Event<Tuple<WebSocket, JSONObject>>() {
            @Override
            public void run(Tuple<WebSocket, JSONObject> info) throws Exception {
                System.out.println(info.obj1);
                WebSocket user = info.obj1;
                String message = info.obj2.getString("message");

                System.out.println(info.obj1);
                sendBack(user, callBack.run(user, message), info.obj2.getString("callId"));
            }
        });
    }
    public void send(WebSocket user, String message) throws JSONException {
        this.sendClient(user, message, "none");
    }

    public void sendBack(WebSocket user, String message, String callId) throws JSONException {
        this.sendClient(user, message, callId);
    }

    private void sendClient(WebSocket user, String message, String callId) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("messageId", this.generateUUID());
        json.put("callId", callId);

        float limitLength = 30000;
        double nbrSplit = Math.ceil(message.length()/limitLength);

        for(int i = 0; i < nbrSplit; ++i) {
            String pack = message.substring(i*(int)limitLength, Math.min((i+1) * (int)limitLength, message.length()));
            json.put("ended", i+1 == nbrSplit);
            json.put("message", pack);

            user.send(json.toString());
        }
    }
    private String generateUUID() {
        return UUID.randomUUID().toString();
    };
}
