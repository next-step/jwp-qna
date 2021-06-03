package qna.domain;

import qna.CannotDeleteException;

import java.util.Collections;
import java.util.List;

public class Answers {

    private List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void validateOwner(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.validateSameOwnerInAnswer(loginUser);
        }
    }

    public List<Answer> answers() {
        return Collections.unmodifiableList(answers);
    }

    public DeleteHistories answersDeleteHistories() {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            deleteHistories.addDeleteHistory(answer.answerDeleteHistory());
        }
        return deleteHistories;
    }
}
