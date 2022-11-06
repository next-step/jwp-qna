package qna.domain;

import qna.CannotDeleteException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuestionDeletableChecker {
    private Collection<DeleteCheckRule> checkingRules;

    public QuestionDeletableChecker(Collection<DeleteCheckRule> checkingRules) {
        this.checkingRules = checkingRules;
    }

    public boolean isDeletable(User writer, List<Answer> answers) throws CannotDeleteException {
        Collection<Boolean> results = new ArrayList<>();
        for (DeleteCheckRule rule : this.checkingRules) {
            results.add(rule.deletable(writer, answers));
        }
        return results.stream().anyMatch(result -> result.equals(true));
    }
}
