package ScoreService.SetRules;

import ScoreService.MahjongSet;
import Settlement.MultiplicationScoreRule;

public class HonorSet extends MultiplicationScoreRule<MahjongSet> {
    public HonorSet(int multiplier) {
        super(multiplier);
    }

    @Override
    public boolean isVerified(MahjongSet set) {
        var tiles = set.getTiles();
        for(var tile: tiles){
            if(!tile.isHonor()) return false;
        }

        return true;
    }
}
