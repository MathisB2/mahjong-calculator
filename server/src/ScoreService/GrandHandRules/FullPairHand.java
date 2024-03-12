package ScoreService.GrandHandRules;

import ScoreService.MahjongHand;
import ScoreService.SetRules.Pair;
import Settlement.IRule;
import Settlement.OverriderScoreRule;

public class FullPairHand extends OverriderScoreRule<MahjongHand> {
    public FullPairHand() {
        super(400);
    }

    @Override
    public boolean isVerified(MahjongHand hand) {
        var sets = hand.getSets();
        if(sets.size() < 7) return false;
        Pair pairRule = new Pair();

        for(var set : sets){
            if(!pairRule.isVerified(set))return false;
        }

        return true;
    }
}
