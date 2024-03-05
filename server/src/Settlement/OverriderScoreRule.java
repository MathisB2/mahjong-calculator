package Settlement;

public class OverriderScoreRule<T> implements ScoreRule<T> {
    int score;
    IRule<T> rule;

    public OverriderScoreRule(int score, IRule<T> rule) {
        this.score = score;
        this.rule = rule;
    }

    @Override
    public boolean isVerified(T param) {
        return this.rule.isVerified(param);
    }

    @Override
    public int modifyScore(T param, int score) {
        if(!isVerified(param)) return score;
        return this.score;
    }
}
