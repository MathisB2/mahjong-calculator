package NetworkService;

import Signal.Signal;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class NetworkService extends WebSocketServer {
    static HashMap<Integer,NetworkService> services = new HashMap();
    private final Set<WebSocket> conns;
    private final HashMap<String, String> queue;
    private final HashMap<String, NetNamespace> namespaces;
    public Signal<WebSocket> onEnter;
    public Signal<WebSocket> onLeave;
    private NetworkService(int port){
        super(new InetSocketAddress(port));
        conns = new HashSet<>();
        queue = new HashMap<>();
        namespaces = new HashMap<>();

        onEnter = new Signal();
        onLeave = new Signal();
    }

    static public NetworkService getNetwork() {
        NetworkConfiguration config = NetworkConfiguration.get();

        return getNetworkOnPort(config.getPort());
    }

    static public NetworkService getNetworkOnPort(int port){
        if (services.containsKey(port)) return services.get(port);
        NetworkService service = new NetworkService(port);
        services.put(port, service);
        service.start();
        return service;
    }

    public NetNamespace getNameSpace(String name){
        if (namespaces.containsKey(name)) return namespaces.get(name);
        NetNamespace namespace = new NetNamespace(name, this);
        namespaces.put(name, namespace);
        return namespace;
    }

    void fireAll(String message){
        for (WebSocket conn: conns) {
            conn.send(message);
        }
    }
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        conns.add(conn);
        onEnter.fireSafely(conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conns.remove(conn);
        onLeave.fireSafely(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String messageInfo) {
        try{
            JSONObject clientCall = new JSONObject(messageInfo);
            String messageId = clientCall.getString("messageId");

            boolean ended = clientCall.getBoolean("ended");
            this.addToQueue(messageId, clientCall.getString("message"));

            if(!ended) return;

            try {
                processMessage(conn, clientCall);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                clientCall.put("error", true);
                clientCall.put("message", e.getMessage());
                conn.send(clientCall.toString());
            }
        }catch(Exception e){
            System.err.println(e.getMessage());
            conn.send("{'name': 'FatalError', 'message':"+ e.getMessage() + "}");
        }
    }

    private void processMessage(WebSocket conn, JSONObject clientCall) throws JSONException {
        String name = clientCall.getString("name");
        String messageId = clientCall.getString("messageId");
        assert namespaces.containsKey(name) : "namespace "+name+" is not defined";
        NetNamespace netNamespace = namespaces.get(name);
        clientCall.put("message", this.getToQueue(messageId));

        Thread th = new Thread(() -> {
            try{
                netNamespace.entries.fireAsync(new Tuple(conn, clientCall));
            }catch(Exception e){}
        });

        th.setDaemon(true);
        th.start();
    }

    private void addToQueue(String messageId, String message){
        String result = this.queue.getOrDefault(messageId, "");
        this.queue.put(messageId, result+message);
    }

    private String getToQueue(String messageId){
        String result = this.queue.get(messageId);
        this.queue.remove(messageId);

        return result;

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        if (conn != null) {
            conns.remove(conn);
        }
    }
}