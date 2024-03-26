package ScoreService.HandRules;

import ScoreService.MahjongHand;
import ScoreService.SetRules.Flush;
import Settlement.MultiplicationScoreRule;
import Tiles.DragonTile;

public class DragonPungRule extends MultiplicationScoreRule<MahjongHand> {
    Flush flush;
    public DragonPungRule(int multiplier) {
        super(multiplier);
        flush = new Flush(0);
    }

    @Override
    public boolean isVerified(MahjongHand hand) {
        for(var set : hand.getSets()){
            if(!flush.isVerifiedWithType(set, DragonTile.class)) return false;
        }
        return true;
    }
}
