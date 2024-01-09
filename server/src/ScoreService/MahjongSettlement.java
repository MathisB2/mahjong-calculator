package ScoreService;

import ScoreService.Rules.*;
import Settlement.*;

public class MahjongSettlement {
    Settlement<MahjongSet> mahjongSetSettlement;
    Settlement<MahjongHand> mahjongHandSettlement;
    MahjongSettlement(){
        mahjongHandSettlement = new Settlement();
        mahjongSetSettlement = new Settlement();

        loadHandSettlement();
        loadSetSettlement();
    }
    private void loadHandSettlement(){
        var mahjongRule = new Mahjong(20);

        mahjongHandSettlement.insertRule(mahjongRule);
    }
    private void loadSetSettlement(){
        var flush = new Flush(2);
        var suite = new Suite(0);

        var hidden = new Hidden(2);
        var square = new Square(2);

        var flushSet = new RuleSet(flush);
        flushSet.insertRule(hidden);
        flushSet.insertRule(square);

        var suiteSet = new RuleSet(suite);

        mahjongSetSettlement.insertRule(flushSet);
        mahjongSetSettlement.insertRule(suiteSet);
    }

    Integer getScoreOf(MahjongHand hand){
        int score = mahjongHandSettlement.getScoreOf(hand);

        for(var set : hand.getSets()){
            score += mahjongSetSettlement.getScoreOf(set);
        }

        return score;
    }
}