package ScoreService.SetRules;

import ScoreService.MahjongSet;
import Settlement.MultiplicationScoreRule;
import Tiles.Tile;

import java.util.ArrayList;

public class HiddenSet extends MultiplicationScoreRule<MahjongSet> {
    public HiddenSet(int multiplier) {
        super(multiplier);
    }
    public boolean isVerified(MahjongSet set) {
      return set.isHidden();
    }
}
