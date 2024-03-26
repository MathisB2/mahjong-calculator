package ScoreService;

import ScoreService.GrandHandRules.FullPairHand;
import ScoreService.HandRules.*;
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
        var suite = new Suite(0);

        var hidden = new HiddenSet(2);
        var square = new Square(4);
        var honor = new HonorSet(2);

        flush.insertRule(hidden);
        flush.insertRule(square);
        flush.insertRule(honor);

        mahjongSetSettlement.insertRule(flush);
        mahjongSetSettlement.insertRule(suite);
    }

    private void loadHandSettlement(){
        var mahjongRule = new Mahjong(20);

        mahjongRule.insertRule(new SuiteHand(10));
        mahjongRule.insertRule(new HiddenHand(120));
        mahjongRule.insertRule(new DealHand(100));
        mahjongRule.insertRule(new NaturalMahjong(2));

        mahjongHandSettlement.insertRule(mahjongRule);
        mahjongHandSettlement.insertRule(new DragonPungRule(2));
        mahjongHandSettlement.insertRule(new HisFlowerOrSeasonRule(2));
        mahjongHandSettlement.insertRule(new FlowerRule(2));
        mahjongHandSettlement.insertRule(new PungOfGameWindRule(2));
        mahjongHandSettlement.insertRule(new PungOfPlayerWindRule(2));
        mahjongHandSettlement.insertRule(new PureHand(2));
        mahjongHandSettlement.insertRule(new HonorHand(2));

        mahjongHandSettlement.insertRule(new NaturalHand(4));
        mahjongHandSettlement.insertRule(new FourFlowersOrSeasonsRule(4));
    }

    private void loadGrandHandSettlement(){
        var fullPairHandRuleSet = new FullPairHand();
        fullPairHandRuleSet.insertRule(new PureHand(2));
        grandHandSettlement.insertRule(fullPairHandRuleSet);
    }
    Integer getScoreOf(MahjongHand hand){
        int score = grandHandSettlement.getScoreOf(hand);
        if(score > 0) return score;

        for(var set : hand.getSets()){
            score += mahjongSetSettlement.getScoreOf(set);
        }
        score = mahjongHandSettlement.modifyScore(hand, score);

        return score;
    }
}