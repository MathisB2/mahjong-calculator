package Settlement;

public abstract class OverriderScoreRule<T> extends RuleSet<T> implements ScoreRule<T> {
    int score;

    public OverriderScoreRule(int score) {
        this.score = score;
    }

    @Override
    public int modifyScore(T param, int score) {
        if(!isVerified(param)) return score;
        return super.modifyScore(param, this.score);
    }
}
