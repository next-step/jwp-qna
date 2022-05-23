package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Answers {
    private final List<Answer> answers;

    private Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public static Answers valueOf(List<Answer> answers) {
        return new Answers(answers);
    }

    public List<DeleteHistory> delete(User loginUser, LocalDateTime now) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser, now));
        }
        return deleteHistories;
    }
}
