package ScoreService.HandRules;

import ScoreService.MahjongHand;
import Settlement.AdditionScoreRule;
import Settlement.MultiplicationScoreRule;

public class NaturalMahjong extends AdditionScoreRule<MahjongHand> {
    NaturalHand naturalRule;
    public NaturalMahjong(int points) {
        super(points);
        naturalRule = new NaturalHand(0);
    }

    @Override
    public boolean isVerified(MahjongHand hand) {
        return this.naturalRule.isVerified(hand);
    }
}
