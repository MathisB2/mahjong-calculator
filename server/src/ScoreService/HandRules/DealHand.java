package ScoreService.HandRules;

import ScoreService.MahjongHand;
import Settlement.AdditionScoreRule;

public class DealHand extends AdditionScoreRule<MahjongHand> {
    public DealHand(int points) {
        super(points, hand -> hand.containsSetting("dealHand"));
    }
}
