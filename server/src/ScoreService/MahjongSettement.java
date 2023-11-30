package ScoreService;

import Settement.*;
import Tiles.*;

import java.util.ArrayList;

public class MahjongSettement {
    Settement<MahjongSet> mahjongSetSettement;
    Settement<MahjongHand> mahjongHandSettement;
    MahjongSettement(){
        loadHandSettement();
        loadSetSettement();
    }
    private void loadHandSettement(){
        mahjongHandSettement = new Settement();
    }
    private void loadSetSettement(){
        mahjongSetSettement = new Settement();
        var flush = new AdditionScoreRule(2, (IRule<MahjongSet>) set -> {
            ArrayList<Tile> tiles = set.getTiles();
            if (tiles.size() < 3) return false;

            Tile refferedTile = tiles.get(0);

            for (int i = 1; i < 3; ++i) {
                if (tiles.get(i).getClass() != refferedTile.getClass()) return false;
            }

            return true;
        });
        var square = new MultiplicationScoreRule(2, (IRule<MahjongSet>) set -> {
            ArrayList<Tile> tiles = set.getTiles();
            if (tiles.size() != 4) return false;

            Tile refferedTile = tiles.get(0);

            for (int i = 1; i < 4; ++i) {
                if (tiles.get(i).getClass() != refferedTile.getClass()) return false;
            }

            return true;
        });

        var flushSet = new RuleSet(flush);
        flushSet.insertRule(square);

        mahjongSetSettement.insertRule(flush);
    }

    Integer getScoreOf(MahjongHand hand){
        int score = mahjongHandSettement.getScoreOf(hand);

        for(var set : hand.getSets()){
            score += mahjongSetSettement.getScoreOf(set);
        }

        return score;
    }
}