import java.net.InetSocketAddress;

public class MainClass {

    public static void main(String[] args) {
        int port = 8080;
        WebSocketServerExample server = new WebSocketServerExample(new InetSocketAddress(port));
        server.start();
        System.out.println("Serveur WebSocket en cours d'exécution sur le port " + port);
    }
}
