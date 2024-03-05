package ScoreService.SetRules;

import ScoreService.MahjongSet;
import Settlement.MultiplicationScoreRule;
import Tiles.Tile;

import java.util.ArrayList;

public class Square extends MultiplicationScoreRule<MahjongSet> {
    public Square(int multiplier) {
        super(multiplier, set -> {
            ArrayList<Tile> tiles = set.getTiles();
            if (tiles.size() != 4) return false;
            Tile refferedTile = tiles.get(0);

            for (int i = 1; i < 4; ++i) {
                if (!refferedTile.equals(tiles.get(i))) return false;
            }

            return true;
        });
    }
}
