package ScoreService.HandRules;

import ScoreService.MahjongHand;
import Settlement.AdditionScoreRule;
import Settlement.IRule;

public class HiddenHand extends AdditionScoreRule<MahjongHand> {
    public HiddenHand(int adder) {
        super(adder, hand -> {
            for(var set : hand.getSets()){
                if(!set.isHidden()) return false;
            }

            return true;
        });
    }


}
