package ScoreService;

import NetworkService.*;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class ScoreService {
    NetNamespace scoreNet;
    MahjongSettement settement;
    ScoreService(NetworkService networkService){
        scoreNet = networkService.newNameSpace("ScoreNet");
        settement = new MahjongSettement();

        scoreNet.connect((WebSocket user, String message) ->{
            JSONObject jsonHand = new JSONObject(message);
            MahjongHand hand = new MahjongHand(jsonHand);

            return settement.getScoreOf(hand).toString();
        });
    }
}
