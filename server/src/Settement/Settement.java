package Settement;

public class Settement<T> extends RulesContainer<T> {
    public Integer getScoreOf(T param){
        int score = 0;
        for(ScoreRule<T> rule: rules){
            score = rule.modifyScore(param, score);
        }

        return score;
    }
}
