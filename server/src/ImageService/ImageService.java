package ImageService;
import NetworkService.*;

import org.java_websocket.WebSocket;

public class ImageService {
    NetNamespace imageNet;
    ImageService(NetworkService networkService) {
        imageNet = networkService.newNameSpace("ImageNet");

        imageNet.connect((WebSocket user, String message) -> {
            return message;
        });
    }
}
