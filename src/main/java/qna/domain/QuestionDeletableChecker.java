package qna.domain;

import java.util.Collection;
import java.util.List;

public class QuestionDeletableChecker {
    private Collection<DeleteCheckRule> checkingRules;

    public QuestionDeletableChecker(Collection<DeleteCheckRule> checkingRules) {
        this.checkingRules = checkingRules;
    }

    public boolean check(User writer, List<Answer> answers){
        return this.checkingRules.stream().anyMatch(rule -> rule.deletable(writer,answers));
    }
}
