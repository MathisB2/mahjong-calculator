package NetworkService;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class NetworkConfiguration {
    static private NetworkConfiguration config = null;
    private int port;
    private NetworkConfiguration(JSONObject config) throws JSONException {
        port = config.getInt("port");
    }
    static public NetworkConfiguration get(){
        if(config == null) load();
        return config;
    }
    static public void load(){
        assert config == null : "NetworkConfig is already loaded";
        try{
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/NetworkService/networkconfig.json")));
            config = new NetworkConfiguration(new JSONObject(jsonContent));
        }catch (Exception e){
            System.err.println("fail to load configuration, check if nerworkconfig.json file is set \n error: "+e.getMessage());
        }
    }
    public int getPort() {
        return port;
    }
}
