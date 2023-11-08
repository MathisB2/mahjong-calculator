import NetworkService.*;
import org.java_websocket.WebSocket;

public class Main {
    public static void main(String[] args) {
        NetworkService service = NetworkService.getNetworkOnPort(8080);
        NetNamespace addition = service.newNameSpace("addition");
        NetNamespace multi = service.newNameSpace("multi");

        addition.on(new NetworkCallBack() {
            @Override
            public String run(WebSocket user, String message) {
                String[] split = message.split(",");
                int nbr1 = Integer.parseInt(split[0]);
                int nbr2 = Integer.parseInt(split[1]);
                return String.valueOf(nbr1 + nbr2);
            }
        });

        multi.on(new NetworkCallBack() {
            @Override
            public String run(WebSocket user, String message) {
                String[] split = message.split(",");
                int nbr1 = Integer.parseInt(split[0]);
                int nbr2 = Integer.parseInt(split[1]);
                return String.valueOf(nbr1 * nbr2);
            }
        });
    }

}