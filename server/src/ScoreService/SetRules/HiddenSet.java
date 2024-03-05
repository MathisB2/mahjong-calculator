package ScoreService.SetRules;

import ScoreService.MahjongSet;
import Settlement.MultiplicationScoreRule;

public class HiddenSet extends MultiplicationScoreRule<MahjongSet> {
    public HiddenSet(int multiplier) {
        super(multiplier, set -> set.isHidden());
    }
}
