package ScoreService.HandRules;

import ScoreService.MahjongHand;
import ScoreService.SetRules.Flush;
import ScoreService.SetRules.Pair;
import ScoreService.SetRules.Suite;
import Settlement.AdditionScoreRule;

public class Mahjong extends AdditionScoreRule<MahjongHand> {
    public Mahjong(int adder) {
        super(adder);
    }

    @Override
    public boolean isVerified(MahjongHand hand) {
        var flush = new Flush(0);
        var suite = new Suite();
        var pair = new Pair();
        boolean canBePair = true;

        for(var set : hand.getSets()){
            boolean isPair = canBePair && pair.isVerified(set);

            if(isPair) canBePair = false;
            if(!(flush.isVerified(set) || suite.isVerified(set) || isPair)) return false;
        }

        return true;
    }
}
