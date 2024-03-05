package ScoreService.SetRules;

import ScoreService.MahjongSet;
import Settlement.AdditionScoreRule;
import Tiles.Tile;

import java.util.ArrayList;

public class Suite extends AdditionScoreRule<MahjongSet> {
    public Suite() {
        super(0, set -> {
            ArrayList<Tile> tiles = set.getTiles();
            if (tiles.size() < 3) return false;

            int size = tiles.size();
            tiles.sort((Tile t1, Tile t2) -> t1.value > t2.value ? 1 : -1);

            for (int i = 0; i < size - 1; ++i) {
                if (Math.abs(tiles.get(i).value - tiles.get(i + 1).value) != 1) {
                    return false;
                }
            }

            return true;
        });
    }
}
