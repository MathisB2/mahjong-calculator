package ScoreService.HandRules;

import ScoreService.MahjongHand;
import Settlement.MultiplicationScoreRule;

public class HonorHand extends MultiplicationScoreRule<MahjongHand> {
    public HonorHand(int multiplier) {
        super(multiplier);
    }

    @Override
    public boolean isVerified(MahjongHand hand) {
        var sets = hand.getSets();

        for(var set : sets){
            var tiles = set.getTiles();

            for(var tile : tiles){
                if(!tile.isHonor()) return false;
            }
        }

        return true;
    }
}
