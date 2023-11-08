package NetworkService;
import org.java_websocket.WebSocket;

public interface NetworkCallBack {
    String run(WebSocket user, String message);
}
