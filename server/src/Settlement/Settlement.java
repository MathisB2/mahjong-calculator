package Settlement;

public class Settlement<T> extends RulesContainer<T> {
    public Integer getScoreOf(T param){
       return this.modifyScore(param, 0);
    }

    public Integer modifyScore(T param, Integer score){
        for(ScoreRule<T> rule: rules){
            score = rule.modifyScore(param, score);
        }

        return score;
    }
}