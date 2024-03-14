package ScoreService.SetRules;

import ScoreService.MahjongSet;
import Settlement.AdditionScoreRule;
import Tiles.Tile;

import java.util.ArrayList;

public class Flush extends AdditionScoreRule<MahjongSet> {
    public Flush(int adder) {
        super(adder);
    }

    @Override
    public boolean isVerified(MahjongSet set) {
        ArrayList<Tile> tiles = set.getTiles();
        if (tiles.size() < 3) return false;

        Tile refferedTile = tiles.get(0);

        for (int i = 1; i < 3; ++i) {
            if (!refferedTile.equals(tiles.get(i))) return false;
        }

        return true;
    }

    public boolean isVerifiedWithType(MahjongSet set, Class<? extends Tile> tileClass){
        return isVerified(set) && set.getTiles().get(0).getClass() == tileClass;
    }
}
