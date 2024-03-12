package Settlement;

public abstract class RuleSet<T> extends RulesContainer<T> implements ScoreRule<T> {
    @Override
    public int modifyScore(T param, int score) {
        if(!this.isVerified(param)) return score;

        int newScore = score;

        for(var rule : rules){
            newScore = rule.modifyScore(param, newScore);
        }

        return newScore;
    }
}