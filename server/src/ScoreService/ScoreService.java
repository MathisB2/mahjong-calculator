package ScoreService;

import NetworkService.*;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

import java.security.Provider;

public class ScoreService {
    private NetNamespace scoreNet;
    private MahjongSettlement settlement;
    static private ScoreService service = null;
    private ScoreService(){
        scoreNet = NetworkService.getNetwork().newNameSpace("ScoreNet");
        settlement = new MahjongSettlement();

        scoreNet.connect((WebSocket user, String message) -> {
            JSONObject jsonHand = new JSONObject(message);
            MahjongHand hand = new MahjongHand(jsonHand);

            System.out.println(message);
            return settlement.getScoreOf(hand).toString();
        });
    }
    static public ScoreService get(){
        if(service == null) load();

        return service;
    }
    static public void load(){
        assert service == null : "service is already loaded";

        service = new ScoreService();
    }
}
