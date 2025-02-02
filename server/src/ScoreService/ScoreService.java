package ScoreService;

import NetworkService.*;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class ScoreService {
    private NetNamespace scoreNet;
    private MahjongSettlement settlement;
    static private ScoreService service = null;
    private ScoreService(){
        scoreNet = NetworkService.getNetwork().getNameSpace("ScoreNet");
        settlement = new MahjongSettlement();

        scoreNet.connect((WebSocket user, String message) -> {
            JSONObject jsonHand = new JSONObject(message);
            MahjongHand hand = new MahjongHand(jsonHand);

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
