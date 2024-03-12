package ScoreService.HandRules;

import ScoreService.MahjongHand;
import Settlement.AdditionScoreRule;
import Settlement.IRule;

public class HiddenHand extends AdditionScoreRule<MahjongHand> {
    public HiddenHand(int points) {
        super(points, hand -> {
            for(var set : hand.getSets()){
                if(!set.isHidden()) return false;
            }

            return true;
        });
    }


}
