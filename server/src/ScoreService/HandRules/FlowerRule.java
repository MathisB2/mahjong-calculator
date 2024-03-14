package ScoreService.HandRules;

import ScoreService.MahjongHand;
import Settlement.AdditionScoreRule;

public class FlowerRule extends AdditionScoreRule<MahjongHand> {
    public FlowerRule(int pointsPerFlowers) {
        super(pointsPerFlowers);
    }

    @Override
    public boolean isVerified(MahjongHand hand) {
        return this.getNbrFlowersIn(hand) > 0;
    }

    @Override
    public int modifyScore(MahjongHand hand, int score) {
        return score + this.adder * this.getNbrFlowersIn(hand);
    }

    private int getNbrFlowersIn(MahjongHand hand){
        return hand.getFlowers().size();
    }
}
