package ScoreService.SetRules;

import ScoreService.MahjongSet;
import Settlement.IRule;
import Tiles.WindTile;

public class WindSet implements IRule<MahjongSet> {
    @Override
    public boolean isVerified(MahjongSet set) {
        for(var tile : set.getTiles()){
            if(!(tile instanceof WindTile)) return false;
        }

        return set.size() >= 2;
    }

    public boolean isWindSetOf(MahjongSet set, String windDirection) {
        return isVerified(set) && ((WindTile) set.getTiles().get(0)).isDirection(windDirection);
    }
}
