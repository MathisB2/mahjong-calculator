package Settement;

import java.util.ArrayList;

public abstract class RulesContainer<T> {
    protected ArrayList<ScoreRule<T>> rules;

    RulesContainer(){
        rules = new ArrayList();
    }
    public void insertRule(ScoreRule<T> rule){
        assert !isRuleIn(rule) : "rule already inserted";
        rules.add(rule);
    }
    public void removeRule(ScoreRule<T> rule){
        assert isRuleIn(rule) : "rule does not exist";
        rules.remove(rule);
    }

    public boolean isRuleIn(ScoreRule<T> rule){
        for(var storedRule : rules){
            if(storedRule == rule) return true;
        }

        return false;
    }
}
