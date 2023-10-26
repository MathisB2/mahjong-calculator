import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Main {

    public static void main(String[] args) throws Exception {
        // Créer le serveur sur le port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Définir le gestionnaire de requêtes pour répondre aux demandes
        server.createContext("/", new MyHandler());

        // Démarrer le serveur
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            // Réponse au client
            String response = "Bonjour, c'est la réponse du serveur!";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
