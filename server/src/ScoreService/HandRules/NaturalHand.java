package ScoreService.HandRules;

import ScoreService.MahjongHand;
import Settlement.AdditionScoreRule;
import Settlement.MultiplicationScoreRule;

public class NaturalHand extends MultiplicationScoreRule<MahjongHand> {
    public NaturalHand(int multiplier) {
        super(multiplier);
    }

    @Override
    public boolean isVerified(MahjongHand hand) {
        return hand.containsSetting("wallHand");
    }
}
