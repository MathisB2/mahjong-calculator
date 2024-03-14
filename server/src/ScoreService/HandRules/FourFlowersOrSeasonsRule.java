package ScoreService.HandRules;

import ScoreService.MahjongHand;
import Settlement.MultiplicationScoreRule;

public class FourFlowersOrSeasonsRule extends MultiplicationScoreRule<MahjongHand> {
    public FourFlowersOrSeasonsRule(int multiplier) {
        super(multiplier);
    }

    @Override
    public boolean isVerified(MahjongHand hand) {
        return hand.getSeasons().size() == 4 || hand.getFlowers().size() == 4;
    }
}
