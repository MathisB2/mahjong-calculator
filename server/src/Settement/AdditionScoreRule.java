package Settement;

public class AdditionScoreRule<T> implements ScoreRule<T> {
    IRule<T> rule;
    int adder;
    public AdditionScoreRule(int adder, IRule<T> rule){
        this.adder = adder;
        this.rule = rule;
    }
    @Override
    public boolean isVerified(T param) {
        return rule.isVerified(param);
    }

    @Override
    public int modifyScore(T param, int score) {
        if(!isVerified(param)) return score;
        return score + adder;
    }
}
