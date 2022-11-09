package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class Answers {
    private final List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public DeleteHistories delete(User loginUser) {
        List<DeleteHistory> list = new ArrayList<>();
        for (Answer answer : answers) {
            list.add(answer.delete(loginUser));
        }
        return new DeleteHistories(list);
    }
}
