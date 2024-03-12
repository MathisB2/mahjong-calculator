package ScoreService.HandRules;

import ScoreService.MahjongHand;
import Settlement.AdditionScoreRule;
import Settlement.IRule;

public class WallHand extends AdditionScoreRule<MahjongHand> {
    public WallHand(int points) {
        super(points, hand ->  hand.containsSetting("wallHand"));
    }
}
