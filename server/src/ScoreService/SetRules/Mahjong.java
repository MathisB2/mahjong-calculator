package ScoreService.SetRules;

import ScoreService.MahjongHand;
import Settlement.AdditionScoreRule;

public class Mahjong extends AdditionScoreRule<MahjongHand> {
    public Mahjong(int adder) {
        super(adder, hand -> {
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
        });
    }
}
