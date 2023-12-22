package ScoreService;

import NetworkService.*;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class ScoreService {
    NetNamespace scoreNet;
    MahjongSettlement settlement;
    public ScoreService(NetworkService networkService){
        scoreNet = networkService.newNameSpace("ScoreNet");
        settlement = new MahjongSettlement();

        scoreNet.connect((WebSocket user, String message) -> {
            JSONObject jsonHand = new JSONObject(message);
            MahjongHand hand = new MahjongHand(jsonHand);

            return settlement.getScoreOf(hand).toString();
        });
    }
}
