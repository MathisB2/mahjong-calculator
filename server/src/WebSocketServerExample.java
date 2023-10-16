import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WebSocketServerExample extends WebSocketServer {

    public WebSocketServerExample(int port) {
        super(port);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Nouvelle connexion : " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Connexion fermée : " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message reçu : " + message);
        broadcast(message); // Diffuse le message à tous les clients connectés
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    public static void main(String[] args) {
        int port = 8080;
        WebSocketServerExample server = new WebSocketServerExample(port);
        server.start();
        System.out.println("Serveur WebSocket en cours d'exécution sur le port " + port);
    }
}
