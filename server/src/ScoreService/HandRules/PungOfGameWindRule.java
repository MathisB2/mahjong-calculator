package ScoreService.HandRules;

import ScoreService.MahjongHand;
import ScoreService.SetRules.Flush;
import ScoreService.SetRules.WindSet;
import Settlement.MultiplicationScoreRule;
import Tiles.Tile;
import Tiles.WindTile;

public class PungOfGameWindRule extends MultiplicationScoreRule<MahjongHand> {
    public PungOfGameWindRule(int multiplier) {
        super(multiplier);
    }

    @Override
    public boolean isVerified(MahjongHand hand) {
        String gameWind = hand.getGameWind().getWindDirection();
        Flush flush = new Flush(0);
        WindSet windSet = new WindSet();

        for(var set : hand.getSets()){
            if(flush.isVerified(set) && windSet.isWindSetOf(set, gameWind)) return true;
        }

        return false;
    }
}
