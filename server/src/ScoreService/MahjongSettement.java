package ScoreService;

import Settement.*;
import Tiles.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MahjongSettement {
    Settement<MahjongSet> mahjongSetSettement;
    Settement<MahjongHand> mahjongHandSettement;
    MahjongSettement(){
        mahjongHandSettement = new Settement();
        mahjongSetSettement = new Settement();

        loadHandSettement();
        loadSetSettement();
    }
    private void loadHandSettement(){

    }
    private void loadSetSettement(){
        var flush = new AdditionScoreRule(2, (IRule<MahjongSet>) set -> {
            ArrayList<Tile> tiles = set.getTiles();
            if (tiles.size() < 3) return false;

            int size = tiles.size();
            tiles.sort((Tile t1, Tile t2) -> t1.value < t2.value ? 1 : 0);

            for (int i = 0; i < size - 1; ++i) {
                if (Math.abs(tiles.get(i).value - tiles.get(i + 1).value) != 1) {
                    return false;
                }
            }

            return true;
        });

        var suite = new AdditionScoreRule(1, (IRule<MahjongSet>) set -> {
            ArrayList<Tile> tiles = set.getTiles();
            if (tiles.size() < 3) return false;

            int result = 0;
            int size = tiles.size();

            for(int i = 0; i < size - 1; ++i){
                result += Math.abs(tiles.get(i).value - tiles.get(i+1).value);
            }

            return result == size-1;
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

        var hidden = new MultiplicationScoreRule(2, (IRule<MahjongSet>) set -> {
            return set.isHidden();
        });

        var flushSet = new RuleSet(flush);
        flushSet.insertRule(hidden);
        flushSet.insertRule(square);

        var suiteSet = new RuleSet(suite);
        suiteSet.insertRule(hidden);
        suiteSet.insertRule(square);

        mahjongSetSettement.insertRule(flushSet);
        mahjongSetSettement.insertRule(suiteSet);
    }

    Integer getScoreOf(MahjongHand hand){
        int score = mahjongHandSettement.getScoreOf(hand);

        for(var set : hand.getSets()){
            score += mahjongSetSettement.getScoreOf(set);
        }

        return score;
    }
}