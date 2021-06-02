package qna.domain;

import java.util.ArrayList;
import java.util.List;

import qna.CannotDeleteException;

public class DeleteHistories {
    private final List<DeleteHistory> deleteHistories = new ArrayList<>();

    public DeleteHistories(Question question, Answers answers, User loginUser) throws CannotDeleteException {
        this.deleteHistories.add(question.deleteByWriter(loginUser));
        this.deleteHistories.addAll(answers.deleteAllByWriter(loginUser));
    }

    public List<DeleteHistory> getDeleteHistories() {
        return new ArrayList<>(this.deleteHistories);
    }
}
