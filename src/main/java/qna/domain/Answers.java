package qna.domain;

import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Answers {

    private List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void checkDeleteAuth(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.checkDeleteAuth(loginUser);
        }
    }

    public List<DeleteHistory> deleteAndGetDeleteHistories() {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter()));
        }
        return deleteHistories;
    }
}
