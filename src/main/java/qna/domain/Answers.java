package qna.domain;

import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.List;

public class Answers {

    private List<Answer> answers;

    private Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public static Answers create(List<Answer> answers) {
        return new Answers(answers);
    }

    public DeleteHistories delete(User loginUser, LocalDateTime deletedDt) throws CannotDeleteException {
        DeleteHistories deleteHistories = DeleteHistories.create();

        for (Answer answer : answers) {
            deleteHistories.addHistory(answer.delete(loginUser, deletedDt));
        }

        return deleteHistories;
    }
}
