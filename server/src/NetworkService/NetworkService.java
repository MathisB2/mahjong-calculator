package NetworkService;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class NetworkService extends WebSocketServer {
    static HashMap<Integer,NetworkService> services = new HashMap();
    private final Set<WebSocket> conns;
    private final HashMap<String, NetNamespace> namespaces;
    private NetworkService(int port){
        super(new InetSocketAddress(port));
        conns = new HashSet<>();
        namespaces = new HashMap<>();
    }

    static public NetworkService getNetworkOnPort(int port){
        if (services.containsKey(port)) return services.get(port);
        NetworkService service = new NetworkService(port);
        services.put(port, service);
        service.start();
        return service;
    }

    public NetNamespace newNameSpace(String name){
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
        conns.add(conn);
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conns.remove(conn);

    }

    @Override
    public void onMessage(WebSocket conn, String messageInfo) {
        System.out.println(messageInfo);
        String[] splitedMessage = messageInfo.split("/");
        String name = splitedMessage[0];
        String message = splitedMessage[1];
        if (!namespaces.containsKey(name)){ conn.send("error/"+name+" does not exist"); return;}

        NetNamespace netNamespace = namespaces.get(name);
        try {
            netNamespace.entries.fire(new Tuple(conn, message));
        }catch (Exception e){
            conn.send("error/"+e.getMessage());
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        if (conn != null) {
            conns.remove(conn);
        }
    }
}