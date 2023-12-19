package Settlement;

public class RuleSet<T> extends RulesContainer<T> implements ScoreRule<T> {
    private ScoreRule<T> mainRule;
    public RuleSet(ScoreRule<T> rule){
        this.mainRule = rule;
    }

    @Override
    public int modifyScore(T param, int score) {
        if(!mainRule.isVerified(param)) return score;

        int additionalScore = mainRule.modifyScore(param, 0);

        for(var rule : rules){
            additionalScore = rule.modifyScore(param, additionalScore);
        }

        return score + additionalScore;
    }

    @Override
    public boolean isVerified(T param) {
        return mainRule.isVerified(param);
    }
}