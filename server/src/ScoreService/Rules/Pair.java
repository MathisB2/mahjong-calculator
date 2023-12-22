package ScoreService.Rules;

import ScoreService.MahjongSet;
import Settlement.IRule;
import Tiles.Tile;

import java.util.ArrayList;

public class Pair implements IRule<MahjongSet> {
    @Override
    public boolean isVerified(MahjongSet set) {
        ArrayList<Tile> tiles = set.getTiles();
        if (tiles.size() != 2) return false;

        return tiles.get(0).equals(tiles.get(1));
    }
}
