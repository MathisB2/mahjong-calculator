package Settement;

public interface ScoreRule<T> extends IRule<T>{
    int modifyScore(T param, int score);
}
