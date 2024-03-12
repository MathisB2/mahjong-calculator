package ScoreService.HandRules;

import ScoreService.MahjongHand;
import ScoreService.SetRules.Suite;
import Settlement.AdditionScoreRule;

public class SuiteHand extends AdditionScoreRule<MahjongHand> {
    public SuiteHand(int points) {
        super(points);
    }

    @Override
    public boolean isVerified(MahjongHand hand) {
        Suite suiteRule = new Suite();
        int nbrSuite = 0;

        for(var set: hand.getSets()){
            if(suiteRule.isVerified(set)) nbrSuite += 1;
        }

        return nbrSuite >= 4;
    }
}
