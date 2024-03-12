package ScoreService.HandRules;

import ScoreService.MahjongHand;
import Settlement.IRule;
import Settlement.MultiplicationScoreRule;

public class PureHand extends MultiplicationScoreRule<MahjongHand> {
    public PureHand(int multiplier) {
        super(multiplier);
    }

    @Override
    public boolean isVerified(MahjongHand hand) {
        var sets = hand.getSets();
        if(sets.size() == 0) return false;
        var tileListOfSet = sets.get(0).getTiles();
        if(tileListOfSet.size() == 0) return false;
        var refTileType = tileListOfSet.get(0).getClass();

        for(var set : sets){
            for(var tile : set.getTiles()){
                if(tile.getClass() !=refTileType) return false;
            }
        }

        return true;
    }
}
