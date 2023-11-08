package NetworkService;

import Signal.Event;
import Signal.Signal;
import org.java_websocket.WebSocket;


public class NetNamespace {
    private String name;
    private NetworkService network;
    Signal entries;
    NetNamespace(String name, NetworkService network){
        this.name = name;
        this.network = network;
        entries = new Signal();
    }
    public void fireAll(String message){
        this.network.fireAll(name + "/" + message);
    }

    public void fire(WebSocket user, String message){
        user.send(name+"/"+message);
    }

    public void on(NetworkCallBack callBack){
        entries.connect(new Event<Tuple<WebSocket, String>>() {
            @Override
            public void run(Tuple<WebSocket, String> info) {
                WebSocket user = info.obj1;
                String message = info.obj2;

                fire(user, callBack.run(user, message));
            }
        });
    }
}
