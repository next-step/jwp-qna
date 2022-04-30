package qna.domain;

import qna.exception.CannotDeleteException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Answers {
    private final List<Answer> answers;

    public Answers() {
        this.answers = new ArrayList<>();
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> add(Answer newAnswer) {
        this.answers.add(newAnswer);
        return answers;
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }

    public void validateDelete(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.validateDelete(loginUser);
        }
    }

    public void deleteAll(User loginUser, List<DeleteHistory> deleteHistories) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.delete(loginUser, deleteHistories);
        }
    }

}
