package NetworkService;
import org.java_websocket.WebSocket;

import java.io.IOException;

public interface NetworkCallBack {
    String run(WebSocket user, String message) throws Exception;
}
