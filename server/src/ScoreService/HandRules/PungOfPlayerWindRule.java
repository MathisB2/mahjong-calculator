package ScoreService.HandRules;

import ScoreService.MahjongHand;
import ScoreService.SetRules.Flush;
import ScoreService.SetRules.WindSet;
import Settlement.MultiplicationScoreRule;

public class PungOfPlayerWindRule extends MultiplicationScoreRule<MahjongHand> {
    public PungOfPlayerWindRule(int multiplier) {
        super(multiplier);
    }

    @Override
    public boolean isVerified(MahjongHand hand) {
        String playerWind = hand.getPlayerWind().getWindDirection();
        Flush flush = new Flush(0);
        WindSet windSet = new WindSet();

        for(var set : hand.getSets()){
            if(flush.isVerified(set) && windSet.isWindSetOf(set, playerWind)) return true;
        }

        return false;
    }
}
