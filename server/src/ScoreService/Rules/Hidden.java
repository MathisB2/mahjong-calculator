package ScoreService.Rules;

import ScoreService.MahjongSet;
import Settlement.MultiplicationScoreRule;

public class Hidden extends MultiplicationScoreRule<MahjongSet> {
    public Hidden(int multiplier) {
        super(multiplier, set -> set.isHidden());
    }
}
