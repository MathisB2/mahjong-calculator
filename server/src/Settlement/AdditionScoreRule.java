package Settlement;

public abstract class AdditionScoreRule<T> extends RuleSet<T> implements ScoreRule<T> {
    int adder;
    public AdditionScoreRule(int adder){
        this.adder = adder;
    }

    @Override
    public int modifyScore(T param, int score) {
        if(!isVerified(param)) return score;
        return score + super.modifyScore(param, adder);
    }
}
