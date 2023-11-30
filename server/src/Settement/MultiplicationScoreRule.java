package Settement;

public class MultiplicationScoreRule<T> implements ScoreRule<T> {
    IRule<T> rule;
    int multiplier;
    public MultiplicationScoreRule(int multiplier, IRule<T> rule){
        this.multiplier = multiplier;
        this.rule = rule;
    }
    @Override
    public boolean isVerified(T param) {
        return rule.isVerified(param);
    }

    @Override
    public int modifyScore(T param, int score) {
        if(!isVerified(param)) return score;
        return score + multiplier;
    }
}
