package Settlement;

public abstract class MultiplicationScoreRule<T> implements ScoreRule<T> {
    int multiplier;
    public MultiplicationScoreRule(int multiplier){
        super();
        this.multiplier = multiplier;
    }

    @Override
    public int modifyScore(T param, int score) {
        if(!isVerified(param)) return score;
        return score * multiplier;
    }
}