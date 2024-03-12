package ScoreService;

import ScoreService.GrandHandRules.FullPairHand;
import ScoreService.HandRules.DealHand;
import ScoreService.HandRules.HiddenHand;
import ScoreService.HandRules.PureHand;
import ScoreService.HandRules.SuiteHand;
import ScoreService.SetRules.*;
import Settlement.*;

public class MahjongSettlement {
    Settlement<MahjongSet> mahjongSetSettlement;
    Settlement<MahjongHand> mahjongHandSettlement;
    Settlement<MahjongHand> grandHandSettlement;
    MahjongSettlement(){
        grandHandSettlement = new Settlement();
        mahjongHandSettlement = new Settlement();
        mahjongSetSettlement = new Settlement();

        loadSetSettlement();
        loadHandSettlement();
        loadGrandHandSettlement();
    }
    private void loadSetSettlement(){
        var flush = new Flush(2);
        var suite = new Suite();

        var hidden = new HiddenSet(2);
        var square = new Square(4);
        var honor = new HonorSet(2);

        var flushSet = new RuleSet(flush);
        flushSet.insertRule(hidden);
        flushSet.insertRule(square);
        flushSet.insertRule(honor);

        var suiteSet = new RuleSet(suite);

        mahjongSetSettlement.insertRule(flushSet);
        mahjongSetSettlement.insertRule(suiteSet);
    }

    private void loadHandSettlement(){
        var mahjongRule = new Mahjong(20);
        var mahjongSet = new RuleSet(mahjongRule);

        mahjongSet.insertRule(new SuiteHand(10));
        mahjongSet.insertRule(new HiddenHand(120));
        mahjongSet.insertRule(new DealHand(100));

        mahjongHandSettlement.insertRule(mahjongSet);
    }

    private void loadGrandHandSettlement(){
        var fullPairHandRuleSet = new RuleSet(new FullPairHand());
        fullPairHandRuleSet.insertRule(new PureHand(2));
        grandHandSettlement.insertRule(fullPairHandRuleSet);
    }
    Integer getScoreOf(MahjongHand hand){
        int score = grandHandSettlement.getScoreOf(hand);
        if(score > 0) return score;

        score = mahjongHandSettlement.getScoreOf(hand);
        for(var set : hand.getSets()){
            score += mahjongSetSettlement.getScoreOf(set);
        }

        return score;
    }
}