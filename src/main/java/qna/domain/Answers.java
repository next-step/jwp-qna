package qna.domain;

import java.util.ArrayList;
import java.util.List;

import qna.CannotDeleteException;

public class Answers {
    private final List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> list = new ArrayList<>();
        for (Answer answer : answers) {
            list.add(answer.delete(loginUser));
        }
        return new DeleteHistories(list);
    }
}
