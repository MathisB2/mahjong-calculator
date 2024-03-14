package ScoreService.HandRules;

import ScoreService.MahjongHand;
import Settlement.MultiplicationScoreRule;
import Tiles.WindTile;

public class HisFlowerOrSeasonRule extends MultiplicationScoreRule<MahjongHand> {
    public HisFlowerOrSeasonRule(int multiplier) {
        super(multiplier);
    }

    @Override
    public boolean isVerified(MahjongHand hand) {
        WindTile playerWind = hand.getPlayerWind();
        var bonuses = hand.getBonuses();
        String windDirection = playerWind.getWindDirection();

        for(var bonus : bonuses){
            if(bonus.getWindDirection().equals(windDirection)) return true;
        }

        return false;
    }
}
